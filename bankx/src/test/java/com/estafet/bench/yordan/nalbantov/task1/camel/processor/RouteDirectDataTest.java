package com.estafet.bench.yordan.nalbantov.task1.camel.processor;

import com.estafet.bench.yordan.nalbantov.task1.camel.Utils;
import com.estafet.bench.yordan.nalbantov.task1.camel.model.Account;
import com.estafet.bench.yordan.nalbantov.task1.camel.processors.FakeDataProcessor;
import com.estafet.bench.yordan.nalbantov.task1.camel.routes.BankXRouteBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelBeanPostProcessor;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.io.IOException;

/**
 * Tests the easiest of the routes - the `direct:data` route.
 * It accepts Accounts and tests their bodies. As the route ends with a producer we are actually testing the message
 * body against a baseline object.
 * <p>
 * Created by Yordan Nalbantov.
 */
public class RouteDirectDataTest extends CamelTestSupport {

    // TODO: Switch to CamelTestSupport as CamelBlueprintSupport class have disadvantages.
    // TODO: Read test account and expected account from `json` file.
    // TODO: Check the implementation against the specification.

    private FakeDataProcessor fakeDataProcessor = new FakeDataProcessor();

    @Test
    public void testRouteDirectData() throws Exception {

        // Prepare test scenario.

        // Challenge the route.

        template.sendBody("direct:data", getTestAccount());
        Object obj = template.sendBody("direct:data", ExchangePattern.InOut, getTestAccount());

        // Assert results.

        assertEquals(getExpectedAccount(), obj);
    }

    private static final String TEST_DATA_BASE_URI = "payload//route//direct//data//";

    private Account getTestAccount() throws IOException {
        return Utils.json(TEST_DATA_BASE_URI + "challenge.json", Account.class);
    }

    private Account getExpectedAccount() throws IOException {
        return Utils.json(TEST_DATA_BASE_URI + "expected.json", Account.class);
    }

//    private static String BLUEPRINT_DESCRIPTOR = "OSGI-INF/blueprint/activemq.xml," +
//            "OSGI-INF/blueprint/beans.xml," +
//            "OSGI-INF/blueprint/context.xml," +
//            "OSGI-INF/blueprint/idempotent.xml," +
//            "OSGI-INF/blueprint/quartz.xml," +
//            "OSGI-INF/blueprint/routes.xml";
//
//    @Override
//    protected String getBlueprintDescriptor() {
//        return BLUEPRINT_DESCRIPTOR;
//    }


    @Override
    public void setUp() throws Exception {
        super.setUp();

        new DefaultCamelBeanPostProcessor(context).postProcessBeforeInitialization(fakeDataProcessor, "fakeDataProcessor");
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry registry = super.createRegistry();

        registry.bind("fakeDataProcessor", fakeDataProcessor);

        return registry;
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

        // TODO: Properties is a good way to setup this mini Frankenstein. Figure it out hot to setup one in the actual execution environment. Hint: CHM use is via {{some_property}}.

        return context;
    }
}
