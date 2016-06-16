package com.estafet.bankx.camel.routes;

import com.estafet.bankx.camel.processors.AccountsWrapperAggregationStrategy;
import com.estafet.bankx.model.AccountWrapper;
import com.estafet.bankx.model.IbanWrapper;
import com.estafet.bankx.camel.processors.AccountsEnricherAggregationStrategy;
import com.estafet.bankx.camel.processors.ReportAggregation;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * Created by Yordan Nalbantov.
 */
public class BankXRouteBuilder extends RouteBuilder {

    // TODO: Response body of the JSON continues to be com.estafet.bankx.model.IbanWrapper.

    // Sory, but no way to set aggregation strategy by bean ref to aggregate.
    private AggregationStrategy reportAggregation = new ReportAggregation();
    private AggregationStrategy accountsEnricherAggregationStrategy = new AccountsEnricherAggregationStrategy();
    private AggregationStrategy accountsWrapperAggregationStrategy = new AccountsWrapperAggregationStrategy();

    @Override
    public void configure() throws Exception {

        // Using IP instead of localhost, as it causes a log message.
        // Using not default continuation timeout of 5000, as it defaults to 30000 and generates a log info message.
        // routeId is the correct way of setting route id. The id method sets component´s id.
        // Jetty restricted to accept POST requests only. 404 - method not allowed is returned otherwise.
        from("jetty:{{bankx.endpoint.entry.url}}?httpMethodRestrict=POST&continuationTimeout=5000").routeId("entry")
                .onException(Exception.class)
                .handled(true)
                .transform(constant("Something went wrong."))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                .end()
                .unmarshal().json(JsonLibrary.Jackson, IbanWrapper.class)
                // Header is set before the splitting to ensure that it will be the same nevertheless splitting on large data may span milliseconds.
                // The format is such that could be used directly to form the final file name.
                .setHeader("IbanTimestampOfRequest", simple("${date:now:yyyy MM dd HH mm ss SSS}"))
                // Split the message object into IBANs, based on the collection from the bean.
                .split(simple("${body.getIbans()}"))
                // Send the IBANs to the message queue.
                .to(ExchangePattern.InOnly, "activemq:queue:ibanReport").setBody(constant(""));

        // Populate beans with fake data, later on used to enrich the original beans.
        from("direct:data").routeId("data").processRef("fakeDataProcessor");

        // Route with id "processing" from the message queue.
        from("activemq:queue:ibanReport").routeId("processing")
                // Replace the body with a new instance of the bean.
                .processRef("ibanSingleReportEntityProcessor")
                // Populate the other three fields of the bean.
                .enrich("direct:data", accountsEnricherAggregationStrategy)
                // Aggregate all incoming messages by the header "IbanTimestampOfRequest".
                .aggregate(header("IbanTimestampOfRequest"), reportAggregation)
                .log(LoggingLevel.INFO, "Aggregated")
                // Wait 2 seconds to aggregate all messages.
                .completionTimeout(2000)
                // Marshall back to JSON. Can not prettify the output with Camel 2.15.1.
                .marshal().json(JsonLibrary.Jackson)
                .to("sftp://{{bankx.endpoint.output.host}}:22/{{bankx.endpoint.output.dir}}?username={{bankx.endpoint.output.username}}&knownHostsFile={{bankx.endpoint.output.knownHostsFile}}&privateKeyFile={{bankx.endpoint.output.privateKeyFile}}&connectTimeout=20000&fileName=${header.IbanTimestampOfRequest}.txt").id("output");

        from("sftp://{{bankx.endpoint.output.host}}:22/{{bankx.endpoint.output.dir}}?username={{bankx.endpoint.output.username}}&knownHostsFile={{bankx.endpoint.output.knownHostsFile}}&privateKeyFile={{bankx.endpoint.output.privateKeyFile}}&connectTimeout=20000&delay=60000").routeId("scan")
                .filter(header(Exchange.FILE_NAME).endsWith(".txt"))
                .unmarshal().json(JsonLibrary.Jackson, AccountWrapper.class)
                .processRef("accountsReportProcessor")
                .aggregate(simple("${in.header.CamelFileName.substring(0, 10)}"), accountsWrapperAggregationStrategy)
                .completionTimeout(2000)
                .setHeader(Exchange.XSLT_FILE_NAME, simple("/u01/data/iban/reports/${date:now:yyyyMMdd}.csv"))
                .to("xslt:com/estafet/bankx/camel/xslt/AccountsCSV.xsl?output=file")
                .log(LoggingLevel.INFO, "File $simple{in.header.CamelFileName} received with body: $simple{in.body}");

        from("quartz2://dummy/schedule?cron={{bankx.endpoint.dummySchedule.cron}}&fireNow=true").routeId("dummySchedule")
                .log(LoggingLevel.INFO, "Cron route executed.");
    }
}
