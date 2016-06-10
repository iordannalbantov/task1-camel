package com.estafet.bench.yordan.nalbantov.task1.camel.routes;

import com.estafet.bench.yordan.nalbantov.task1.camel.aggregations.IbanSingleReportEntityAggregation;
import com.estafet.bench.yordan.nalbantov.task1.camel.model.IbanSingleReportEntity;
import com.estafet.bench.yordan.nalbantov.task1.camel.processors.FakeDataProcessor;
import com.estafet.bench.yordan.nalbantov.task1.camel.processors.IbanSingleReportEntityProcessor;
import com.estafet.bench.yordan.nalbantov.task1.camel.processors.LoggerProcessor;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by estafet on 08/06/16
 */
public class BankXRouteBuilder extends RouteBuilder {

    private static Logger logger = Logger.getLogger(BankXRouteBuilder.class.getSimpleName());

    private LoggerProcessor loggerProcessor;

    private FakeDataProcessor fakeDataProcessor;

    private IbanSingleReportEntityProcessor ibanSingleReportEntityProcessor;

    private IbanSingleReportEntityAggregation ibanSingleReportEntityAggregation;

    @Override
    public void configure() throws Exception {

        // fileName=${date:now:dd-MM-yyyy}
        // 2016 06 07 19 41 07 100

        // Using IP instead of localhost, as it causes a log message.
        // Using not default continuation timeout of 5000, as it defaults to 30000 and generates a log info message.
        // routeId is the correct way of setting route id. The id method sets component´s id.
        // Jetty restricted to accept POST requests only. 404 - method not allowed is returned otherwise.
        from("jetty:http://127.0.0.1:20616/estafet/iban/report?httpMethodRestrict=POST&continuationTimeout=5000").routeId("entry")
                .unmarshal().json(JsonLibrary.Gson)
                // Header is set before the splitting to ensure that it will be the same nevertheless splitting on large data may span milliseconds.
                .setHeader("IbanTimestampOfRequest", simple("${date:now:yyyy MM dd HH mm ss SSS}"))
                .split().method("splitters", "splitIbansLinkedHashMapToStrings")
                .to("activemq:queue:ibanReport");

        from("direct:data").routeId("data").process(fakeDataProcessor);

        from("activemq:queue:ibanReport").routeId("processing")
                .process(ibanSingleReportEntityProcessor)
                .enrich("direct:data", ibanSingleReportEntityAggregation)
                .to("file:///u01/data/iban/reports");
    }

    public void setLoggerProcessor(LoggerProcessor loggerProcessor) {
        this.loggerProcessor = loggerProcessor;
    }

    public void setFakeDataProcessor(FakeDataProcessor fakeDataProcessor) {
        this.fakeDataProcessor = fakeDataProcessor;
    }

    public void setIbanSingleReportEntityProcessor(IbanSingleReportEntityProcessor ibanSingleReportEntityProcessor) {
        this.ibanSingleReportEntityProcessor = ibanSingleReportEntityProcessor;
    }

    public void setIbanSingleReportEntityAggregation(IbanSingleReportEntityAggregation ibanSingleReportEntityAggregation) {
        this.ibanSingleReportEntityAggregation = ibanSingleReportEntityAggregation;
    }
}
