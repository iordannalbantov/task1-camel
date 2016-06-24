package com.estafet.bankx.test.unit;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by estafet.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        ServiceRouteBuilderTestServerBankX.class,
        SchedulesRouteBuilderTestServerBankX.class,
        ReportsRouteBuilderTestServerBankX.class
})
public class BankXTestSuite {
}
