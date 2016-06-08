package com.estafet.bench.yordan.nalbantov.task1.camel;

import org.apache.camel.builder.RouteBuilder;

/**
 * Created by estafet on 08/06/16.
 */
public class BankXRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("jetty:http://localhost:20616/estafet/iban/report?httpClient.method=POST").id("entry")
                .to("file:///u01/data/iban/reports");
    }
}
