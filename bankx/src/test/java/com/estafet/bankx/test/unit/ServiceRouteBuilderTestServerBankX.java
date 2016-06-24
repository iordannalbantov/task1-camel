package com.estafet.bankx.test.unit;

import com.estafet.bankx.test.core.Resource;
import com.estafet.bankx.test.core.TestSupportServerBankX;
import com.estafet.bankx.camel.routes.BankXServiceRouteBuilder;
import com.estafet.bankx.model.Account;
import com.estafet.bankx.model.IbanWrapper;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.FromDefinition;
import org.apache.camel.model.RouteDefinition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static com.jayway.restassured.RestAssured.given;
import static org.apache.camel.language.simple.SimpleLanguage.simple;
import static org.hamcrest.Matchers.equalTo;

/**
 * Tests the easiest of the routes - the `direct:data` route.
 * It accepts Accounts and tests their bodies. As the route ends with a producer we are actually testing the message
 * body against a baseline object.
 * <p>
 * Created by Yordan Nalbantov.
 */
public class ServiceRouteBuilderTestServerBankX extends TestSupportServerBankX {

    /**
     * The default assert period in Camel is 10 secconds.
     */
    private static final int ASSERT_PERIOD = 10_000;
    private static final long WAIT_TIMEOUT = 5000L;

    // TODO: Mock accountEnricherService with mockito.
    // TODO: Test negative cases and alternative scenarios.

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        Resource.baseURI = null;

        // At this point, prepare the Ftp for the unit test if needed.
    }

    @Override
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    public void testRouteJettyEntity() throws Exception {

        // Lookup roots.

        RouteDefinition routeDefinition = context.getRouteDefinition("entry");

        List<FromDefinition> fromDefinitions = routeDefinition.getInputs();
        FromDefinition definition = fromDefinitions.get(0);
        String jettyEndpointURI = definition.getEndpointUri();
        jettyEndpointURI = jettyEndpointURI.substring("jetty:".length(), jettyEndpointURI.length());

        // Prepare test data.

        String challenge = Resource.resource("payload//route//direct//entry//challenge.json");

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
                        interceptSendToEndpoint("direct:entry")
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

        RouteDefinition routeDefinition = context.getRouteDefinition("directEntry");

        // Prepare test data.

        String challenge = Resource.resource("payload//route//direct//entry//challenge.json");
        IbanWrapper challengeIbanWrapper = Resource.jsonFromString(challenge, IbanWrapper.class);

        // Prepare test scenario.

        MockEndpoint mockResult = getMockEndpoint("mock:entry");
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
                replaceFromWith("direct:entry");
                weaveById("result").replace().to("mock:entry");
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

        Account challenge = Resource.json("payload//route//direct//data//challenge.json", Account.class);
        Account expected = Resource.json("payload//route//direct//data//expected.json", Account.class);

        Object result = template.sendBody("direct:data", ExchangePattern.InOut, challenge);

        // Assert results.
        assertEquals("The enriched result does not equal the expected.", expected, result);
    }

    @Test
    public void testRouteProcessing() throws Exception {

        // Lookup roots.

        RouteDefinition routeDefinition = context.getRouteDefinition("processing");

        // Prepare test data.

        String challenge = "BG66 ESTF 0616 0000 0000 01";
//        IbanWrapper challengeIbanWrapper = Resource.jsonFromString(challenge, IbanWrapper.class);

        // Prepare test scenario.

        MockEndpoint mockResult = getMockEndpoint("mock:result");
        mockResult.expectedMessageCount(1);
        mockResult.setAssertPeriod(ASSERT_PERIOD);

        routeDefinition.adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() throws Exception {
                replaceFromWith("direct:processing");
                weaveById("output").replace().to("mock:result");
            }
        });

        // Challenge the route.
        Object result = template.sendBodyAndHeader("direct:processing", ExchangePattern.InOut, challenge,
                "IbanTimestampOfRequest", simple("${date:now:yyyy MM dd HH mm ss SSS}"));

        // Assert results.

        assertMockEndpointsSatisfied();
    }

    /**
     * Creating the BankX`s route builder.
     *
     * @return The actual RouteBuilder instance.
     * @throws Exception On any setup or configuration problem.
     */
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new BankXServiceRouteBuilder();
    }
}