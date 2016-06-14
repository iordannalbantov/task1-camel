package com.estafet.bench.yordan.nalbantov.task1.camel.processor;

import com.estafet.bench.yordan.nalbantov.task1.camel.model.Account;
import org.apache.camel.ExchangePattern;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Test;

import java.util.logging.Logger;

/**
 * Created by estafet.
 */
public class RouteDirectDataTest extends CamelBlueprintTestSupport {

//    @Override
//    protected RouteBuilder createRouteBuilder() throws Exception {
//        return new RouteBuilder() {
//            @Override
//            public void configure() throws Exception {
//                from("direct:data").routeId("data").process(fakeDataProcessor).to("mock:result");
//            }
//        };
//    }

    @Test
    public void testRouteDirectData() throws Exception {

        // Prepare test scenario.

        // Challenge the route.

        template.sendBody("direct:data", getTestAccount());
        Object obj = template.sendBody("direct:data", ExchangePattern.InOut, getTestAccount());

        // Assert results.

        assertEquals(getExpectedAccount(), obj);
    }

    private Account getTestAccount() {
        Account account = new Account();
        account.setIban("BG66 ESTF 0616 0000 0000 01");
        return account;
    }

    private Account getExpectedAccount() {
        Account expectedAccount = getTestAccount();
        expectedAccount.setName("Ivan Ivanov");
        expectedAccount.setBalance(100.0);
        expectedAccount.setCurrency("BGN");
        return expectedAccount;
    }

    private static String BLUEPRINT_DESCRIPTOR = "OSGI-INF/blueprint/activemq.xml," +
            "OSGI-INF/blueprint/beans.xml," +
            "OSGI-INF/blueprint/context.xml," +
            "OSGI-INF/blueprint/idempotent.xml," +
            "OSGI-INF/blueprint/quartz.xml," +
            "OSGI-INF/blueprint/routes.xml";

    @Override
    protected String getBlueprintDescriptor() {
        return BLUEPRINT_DESCRIPTOR;
    }
}
