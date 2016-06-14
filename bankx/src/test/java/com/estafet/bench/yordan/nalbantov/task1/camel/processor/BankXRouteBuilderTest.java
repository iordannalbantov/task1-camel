package com.estafet.bench.yordan.nalbantov.task1.camel.processor;

import org.apache.camel.CamelContext;
import org.apache.camel.component.properties.PropertiesComponent;

/**
 * Created by estafet.
 */
public class BankXRouteBuilderTest  {

//    private FakeDataProcessor fakeDataProcessor = new FakeDataProcessor();
//
//    @Override
//    protected RouteBuilder createRouteBuilder() throws Exception {
//        return new RouteBuilder() {
//            @Override
//            public void configure() throws Exception {
//                from("direct:data").routeId("data").process(fakeDataProcessor).to("mock:result");
//            }
//        };
//    }
//
//    @Test
//    public void testRouteDirectData() throws Exception {
//
//        // Prepare test data.
//
//        Account account = new Account();
//        account.setIban("BG66 ESTF 0616 0000 0000 01");
//
//        Account expectedAccount = new Account();
//        expectedAccount.setIban("BG66 ESTF 0616 0000 0000 01");
//        expectedAccount.setName("Ivan Ivanov");
//        expectedAccount.setBalance(100.0);
//        expectedAccount.setCurrency("BGN");
//
//        // Execute the test.
//
//        directEndpoint.expectedMessageCount(1);
//        directEndpoint.expectedBodiesReceived(expectedAccount);
//
//        template.sendBody("direct:data", account);
////        template1.sendBody(account);
//
//        directEndpoint.assertIsSatisfied();
//    }

//    @Override
//    protected RouteBuilder createRouteBuilder() throws Exception {
//        // return super.createRouteBuilder();
//        return new BankXRouteBuilder();
//    }

//    @Override
//    protected CamelContext createCamelContext() throws Exception {
//        // Jenerally this is not needed for this example but I wold like to remember to test using properties as route configuration.
//        CamelContext context = super.createCamelContext();
//
//        PropertiesComponent properties = context.getComponent("properties", PropertiesComponent.class);
//        properties.setLocation("classpath:etc/test.properties");
//
//        // TODO: Properties is a good way to setup this mini Frankenstein. Figure it out hot to setup one in the actual execution environment. Hint: CHM use is via {{some_property}}.
//
//        return context;
//    }
//
//
//    @Override
//    public boolean isUseAdviceWith() {
//        return true;
//    }

//    @EndpointInject(uri = "mock:result")
//    private MockEndpoint directEndpoint;
//
//    @Produce(uri = "direct:data")
//    private ProducerTemplate template1;



//    @Test
//    public void testPositiveRouteExecution() throws Exception {
//        MockEndpoint mockEndpoint = getMockEndpoint("mock:entry");
//        mockEndpoint.expectedMessageCount(1);
//
//        RouteDefinition route = context.getRouteDefinition("entry");
//
//        route.adviceWith(context, new AdviceWithRouteBuilder() {
//            @Override
//            public void configure() throws Exception {
//                replaceFromWith("mock:entry");
//
////                interceptSendToEndpoint("jetty:http:*")
////                        .skipSendToOriginalEndpoint()
////                        .to(mockEndpoint.getEndpointUri())
////                        .process(new Processor() {
////                            @Override
////                            public void process(Exchange exchange) throws Exception {
////                                exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);
////                            }
////                        });
//            }
//        });
//
//        String body = Utils.resource("payload\\validRequest.json");
//        Map<String, Object> headers = new HashMap<>();
//
//        consumer.start();
//
//        template.sendBodyAndHeader("mock:entry", ExchangePattern.OutOnly, body, headers);
//
//        assertMockEndpointsSatisfied();
//
//        // TODO: Verify mockEndpoint here.
//        // List<Exchange> list = mock.getReceivedExchanges();
//
//        context.stop();
//    }
}
