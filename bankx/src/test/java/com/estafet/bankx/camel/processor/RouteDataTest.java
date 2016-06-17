package com.estafet.bankx.camel.processor;

import com.estafet.bankx.accounts.api.AccountServiceApi;
import com.estafet.bankx.accounts.impl.AccountsServiceImpl;
import com.estafet.bankx.camel.Utils;
import com.estafet.bankx.camel.processors.*;
import com.estafet.bankx.camel.routes.BankXRouteBuilder;
import com.estafet.bankx.model.Account;
import com.estafet.bankx.model.IbanWrapper;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelBeanPostProcessor;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.model.FromDefinition;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Tests the easiest of the routes - the `direct:data` route.
 * It accepts Accounts and tests their bodies. As the route ends with a producer we are actually testing the message
 * body against a baseline object.
 * <p>
 * Created by Yordan Nalbantov.
 */
public class RouteDataTest extends CamelTestSupport {
    /**
     * The default assert period in Camel is 10 secconds.
     */
    private static final int ASSERT_PERIOD = 10_000;
    private static final long WAIT_TIMEOUT = 5000L;

    // TODO: Check the implementation against the specification.
    // TODO: The test is not running properly after the bankx-modles OSGI module introduction.
    // TODO: Mock accountEnricherService.
    // TODO: Test negative cases and alternative scenarios.

    private final AccountServiceApi accountEnricherService = new AccountsServiceImpl();

    private final AccountsReportProcessor accountsReportProcessor = new AccountsReportProcessor();
    private final FakeDataProcessor fakeDataProcessor = new FakeDataProcessor();
    private final IbanSingleReportEntityProcessor ibanSingleReportEntityProcessor = new IbanSingleReportEntityProcessor();
    private final PrepareTransformationProcessor prepareTransformationProcessor = new PrepareTransformationProcessor();
    private final TestProcessor testProcessor = new TestProcessor();

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry registry = super.createRegistry();

        registry.bind("accountEnricherService", accountEnricherService);
        registry.bind("accountsReportProcessor", accountsReportProcessor);

        fakeDataProcessor.setAccountEnricherService(accountEnricherService);
        registry.bind("fakeDataProcessor", fakeDataProcessor);
        registry.bind("ibanSingleReportEntityProcessor", ibanSingleReportEntityProcessor);
        registry.bind("prepareTransformationProcessor", prepareTransformationProcessor);
        registry.bind("testProcessor", testProcessor);

        return registry;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        DefaultCamelBeanPostProcessor postProcessor = new DefaultCamelBeanPostProcessor(context);

        postProcessor.postProcessBeforeInitialization(accountEnricherService, "accountEnricherService");

        postProcessor.postProcessBeforeInitialization(accountsReportProcessor, "accountsReportProcessor");
        postProcessor.postProcessBeforeInitialization(fakeDataProcessor, "fakeDataProcessor");
        postProcessor.postProcessBeforeInitialization(ibanSingleReportEntityProcessor, "ibanSingleReportEntityProcessor");
        postProcessor.postProcessBeforeInitialization(prepareTransformationProcessor, "prepareTransformationProcessor");
        postProcessor.postProcessBeforeInitialization(testProcessor, "testProcessor");
    }

    @Test
    public void testRouteJettyEntity() throws Exception {

        // Lookup roots.

        RouteDefinition routeDefinition = context.getRouteDefinition(BankXRouteBuilder.ROUTE_JETTY_ENTRY_ID);

        List<FromDefinition> fromDefinitions = routeDefinition.getInputs();
        FromDefinition definition = fromDefinitions.get(0);
        String jettyEndpointURI = definition.getEndpointUri();
        jettyEndpointURI = jettyEndpointURI.substring("jetty:".length(), jettyEndpointURI.length());

        // Prepare test data.

        String challenge = Utils.resource("payload//route//direct//entry//challenge.json");

        // Prepare test scenario.

        MockEndpoint mockResult = getMockEndpoint("mock:result");

        mockResult.expectedMessageCount(1);
        mockResult.setAssertPeriod(ASSERT_PERIOD);
        List<String> expectedBodies = new ArrayList<>();
        expectedBodies.add(challenge);
        mockResult.expectedBodiesReceived(expectedBodies);

        routeDefinition
                .adviceWith(context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        interceptSendToEndpoint(BankXRouteBuilder.ROUTE_DIRECT_ENTRY)
                                .skipSendToOriginalEndpoint()
                                .to("mock:result");
                    }
                });

        // Challenge the route.

        Response response = given()
                .contentType(ContentType.JSON)
                .body(challenge)
                .when()
                .post(jettyEndpointURI)
                .then()
                .assertThat()
                .statusCode(HttpServletResponse.SC_OK)
                .assertThat()
                .body(equalTo(""))
                .extract().response();

        Thread.sleep(WAIT_TIMEOUT);

        // Assert results.

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testRouteDirectEntry() throws Exception {

        // Lookup roots.

        RouteDefinition routeDefinition = context.getRouteDefinition(BankXRouteBuilder.ROUTE_DIRECT_ENTRY_ID);

        // Prepare test data.

        String challenge = Utils.resource("payload//route//direct//entry//challenge.json");
        IbanWrapper challengeIbanWrapper = Utils.jsonFromString(challenge, IbanWrapper.class);

        // Prepare test scenario.

        MockEndpoint mockResult = getMockEndpoint("mock:result");
        // Test setting the header value.
        mockResult.expectedMessagesMatches(mockResult.allMessages().simple("${in.header.IbanTimestampOfRequest.length}").isEqualTo(23));
        // Test the splitting. It seams that there is no way to assure that exactly N messages have arrived.
        mockResult.expectedMessageCount(3);
        mockResult.setAssertPeriod(ASSERT_PERIOD);
        // Test the IBAN values.
        mockResult.expectedBodiesReceived(challengeIbanWrapper.getIbans());

        routeDefinition.adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                weaveById("result").replace().to(mockResult);
                replaceFromWith("direct:entry");
            }
        });

        // Challenge the route.

        Object result = template.sendBody("direct:entry", ExchangePattern.InOut, challenge);

        // Assert results.

        assertMockEndpointsSatisfied();
    }

    @Test
    public void testRouteDirectData() throws Exception {
        // Prepare test scenario.

        // Challenge the route.

        Account challenge = Utils.json("payload//route//direct//data//challenge.json", Account.class);
        Account expected = Utils.json("payload//route//direct//data//expected.json", Account.class);

        Object result = template.sendBody(BankXRouteBuilder.ROUTE_DIRECT_DATA, ExchangePattern.InOut, challenge);

        // Assert results.
        assertEquals("The enriched result does not equal the expected.", expected, result);
    }

    /**
     * Creating the BankX`s route builder.
     *
     * @return The actual RouteBuilder instance.
     * @throws Exception On any setup or configuration problem.
     */
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new BankXRouteBuilder();
    }

    /**
     * Creating Camel Context with `test` properties.
     *
     * @return The newly created Camel context.
     * @throws Exception On any setup and configuration problem.
     */
    @Override
    protected CamelContext createCamelContext() throws Exception {
        // Generally this is not needed for this example but I wold like to remember to test using properties as route configuration.
        CamelContext context = super.createCamelContext();

        PropertiesComponent properties = context.getComponent("properties", PropertiesComponent.class);
        properties.setLocation("classpath:etc/test.properties");

        return context;
    }
}