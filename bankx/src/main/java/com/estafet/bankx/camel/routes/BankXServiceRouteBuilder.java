package com.estafet.bankx.camel.routes;

import com.estafet.bankx.camel.base.camel.BaseBankXRouteBuilder;
import com.estafet.bankx.dao.model.other.IbanWrapper;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Yordan Nalbantov.
 */
public class BankXServiceRouteBuilder extends BaseBankXRouteBuilder {

    @Override
    public void configure() throws Exception {

        // Sorry, but there is no way to set aggregation strategy by bean ref. This is the best
        AggregationStrategy reportAggregationStrategy = lookupByName("reportAggregationStrategy");
        AggregationStrategy accountsEnricherAggregationStrategy = lookupByName("accountsEnricherAggregationStrategy");

        // Using IP instead of localhost, as it causes a log message.
        // Using not default continuation timeout of 5000, as it defaults to 30000 and generates a log info message.
        // routeId is the correct way of setting route id. The id method sets component´s id.
        // Jetty restricted to accept POST requests only. 404 - method not allowed is returned otherwise.
        from("jetty:{{bankx.endpoint.entry.url}}?httpMethodRestrict=POST&continuationTimeout=5000").routeId("entry")
                .onException(Exception.class)
                .handled(true)
                .transform(constant("Something went wrong."))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpServletResponse.SC_INTERNAL_SERVER_ERROR))
                .end()
                // It seems that jetty endpoint is assuming that there will not be any modification of the body.
                // Therefore, it is best not to modify the HttpMessage instance in the route.
                // If the message object is replaced with an message of another class, it leads to calling toString
                // method on the new object, at the pint when the response to the caller is rendered.
                // In the last case it is impossible to return meaningful message to the caller.
                .to(ExchangePattern.InOnly, "direct:entry").setBody(constant(""));

        from("direct:entry").routeId("directEntry")
                .unmarshal().json(JsonLibrary.Jackson, IbanWrapper.class)
                // Header is set before the splitting to ensure that it will be the same nevertheless splitting on large data may span milliseconds.
                // The format is such that could be used directly to form the final file name.
                .setHeader("IbanTimestampOfRequest", simple("${date:now:yyyy MM dd HH mm ss SSS}"))
                // Split the message object into IBANs, based on the collection from the bean.
                .split(simple("${body.getIbans()}"))
                // Send the IBANs to the message queue.
                .to("activemq:queue:ibanReport").id("result");

        // Populate beans with fake data, later on used to enrich the original beans.
        from("direct:data").routeId("data").processRef("fakeDataProcessor");

        // Route with id "processing" from the message queue.
        from("activemq:queue:ibanReport").routeId("processing")
                // Replace the body with a new instance of the bean.
                .processRef("ibanSingleReportEntityProcessor")
                // Populate the other three fields of the bean.
                .enrich("direct:data", accountsEnricherAggregationStrategy)
                // Aggregate all incoming messages by the header "IbanTimestampOfRequest".
                .aggregate(header("IbanTimestampOfRequest"), reportAggregationStrategy)
                .log(LoggingLevel.INFO, "Aggregated")
                // Wait 2 seconds to aggregate all messages.
                .completionTimeout(2000)
                // Marshall back to JSON. Can not prettify the output with Camel 2.15.1.
                .marshal().json(JsonLibrary.Jackson)
                .to("sftp://{{bankx.endpoint.output.host}}:{{bankx.endpoint.output.port}}/{{bankx.endpoint.output.dir}}?username={{bankx.endpoint.output.username}}&knownHostsFile={{bankx.endpoint.output.knownHostsFile}}&privateKeyFile={{bankx.endpoint.output.privateKeyFile}}&connectTimeout=20000&fileName=${header.IbanTimestampOfRequest}.txt").id("output");
    }
}
