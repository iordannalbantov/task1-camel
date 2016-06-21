package com.estafet.bankx.camel.routes;

import com.estafet.bankx.camel.routes.base.BaseBankXRouteBuilder;
import com.estafet.bankx.model.AccountWrapper;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.apache.camel.processor.idempotent.MemoryIdempotentRepository;

/**
 * Created by Yordan Nalbantov.
 */
public class BankXReportsRouteBuilder extends BaseBankXRouteBuilder {

    @Override
    public void configure() throws Exception {
        AggregationStrategy accountsWrapperAggregationStrategy = lookupByName("accountsWrapperAggregationStrategy");

        from("sftp://{{bankx.endpoint.output.host}}:{{bankx.endpoint.output.port}}/{{bankx.endpoint.output.dir}}?username={{bankx.endpoint.output.username}}&knownHostsFile={{bankx.endpoint.output.knownHostsFile}}&privateKeyFile={{bankx.endpoint.output.privateKeyFile}}&connectTimeout=20000&delay=60000").routeId("scan")
                .filter(header(Exchange.FILE_NAME).endsWith(".txt"))
                .idempotentConsumer(header(Exchange.FILE_NAME), MemoryIdempotentRepository.memoryIdempotentRepository(200))
                // Log the content of the files.
                .log(LoggingLevel.INFO, "File $simple{in.header.CamelFileName} received with body: $simple{in.body}")
                // Unmarshal them for future use.
                .unmarshal().json(JsonLibrary.Jackson, AccountWrapper.class)
                // Replace the AccountWrapper with AccountReportWrapper.
                .processRef("accountsReportProcessor")
                // Aggregate daily data. N.B. Reading should be synchronized to execute once a day.
                // For the test it is not.
                .aggregate(simple("${in.header.CamelFileName.substring(0, 10)}"), accountsWrapperAggregationStrategy)
                .completionTimeout(2000)
                // Set output filename and transform it. We could use sftp for writing again,
                // but for this test it is pointless. Also, we must use processor to populate the file name because
                // it must be dynamic, to avoid some problems that will occur when because of the fact that the route
                // is called once per a minute, but not daily.
                .processRef("prepareTransformationProcessor")
                .to("xslt:com/estafet/bankx/camel/xslt/AccountsCSV.xsl?output=file").id("scanResult");
    }
}