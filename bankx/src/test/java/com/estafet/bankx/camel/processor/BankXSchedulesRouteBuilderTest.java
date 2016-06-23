package com.estafet.bankx.camel.processor;

import com.estafet.bankx.camel.base.BankXServerTestSupport;
import com.estafet.bankx.camel.routes.BankXSchedulesRouteBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.junit.Test;

/**
 * Created by Yordan Nalbantov.
 */
public class BankXSchedulesRouteBuilderTest extends BankXServerTestSupport {

    @Test
    public void testRouteQuartz() throws Exception {

        // Lookup roots.

        RouteDefinition routeDefinition = context.getRouteDefinition("dummySchedule");

        // Prepare test data.

        // Prepare test scenario.

        MockEndpoint mockResult = getMockEndpoint("mock:dummyScheduleResult");
        mockResult.expectedMessageCount(2);

        routeDefinition
                .adviceWith(context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        weaveById("dummyScheduleResult").replace().to("mock:dummyScheduleResult");
                    }
                });

        // Challenge the route.

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
        return new BankXSchedulesRouteBuilder();
    }
}
