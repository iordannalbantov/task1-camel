package com.estafet.bankx.camel.routes;

import com.estafet.bankx.camel.base.camel.BaseBankXRouteBuilder;
import org.apache.camel.LoggingLevel;

/**
 * Created by estafet.
 */
public class BankXSchedulesRouteBuilder extends BaseBankXRouteBuilder {

    @Override
    public void configure() throws Exception {
        from("quartz2://dummy/schedule?cron={{bankx.endpoint.dummySchedule.cron}}&fireNow=true").routeId("dummySchedule")
                .log(LoggingLevel.INFO, "Cron route executed.").id("dummyScheduleResult");
    }
}
