package com.estafet.bankx.camel.processor;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by estafet.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        BankXServiceRouteBuilderTest.class,
        BankXSchedulesRouteBuilderTest.class,
        BankXReportsRouteBuilderTest.class
})
public class BankXTestSuite {
}
