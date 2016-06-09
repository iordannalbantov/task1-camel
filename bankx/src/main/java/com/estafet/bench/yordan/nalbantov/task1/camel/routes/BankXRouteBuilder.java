package com.estafet.bench.yordan.nalbantov.task1.camel.routes;

import org.apache.camel.builder.RouteBuilder;

import java.time.LocalDateTime;

/**
 * Created by estafet on 08/06/16
 */
public class BankXRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        LocalDateTime now = LocalDateTime.now();

        from("jetty:http://localhost:20616/estafet/iban/report?httpClient.method=POST").id("entry")
                .split(body()).setHeader("IbanTimestampOfRequest", constant(now))
                .to("activemq:queue:ibanReport");

        from("activemq:queue:ibanReport").id("processing")
                .to("file:///u01/data/iban/reports");

//        from("activemq:queue:ibanReport").id("processing")
//                .process(exchange -> {
//                    String iban = exchange.getIn().getBody(String.class);
//                    IbanSingleReportEntity entity = new IbanSingleReportEntity(iban);
//                    exchange.getIn().setBody(entity);
//                })
//                .enrich("direct:data", (original, resource) -> {
//                    IbanSingleReportEntity originalBody = original.getIn().getBody(IbanSingleReportEntity.class);
//
//                    originalBody.setName("Yordan Nalbantov");
//                    originalBody.setBalance(100.0);
//                    originalBody.setCurrency("BGN");
//
//                    original.getIn().setBody(originalBody);
//                    return original;
//                })
//                .to("file:///u01/data/iban/reports");
    }
}
