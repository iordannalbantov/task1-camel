package com.estafet.bankx.test.unit;

import com.estafet.bankx.test.core.Resource;
import com.estafet.bankx.test.core.TestSupportServerBankX;
import com.estafet.bankx.camel.routes.BankXSchedulesRouteBuilder;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Yordan Nalbantov.
 */
public class SchedulesRouteBuilderTestServerBankX extends TestSupportServerBankX {

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();

        Resource.baseURI = null;
    }

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
