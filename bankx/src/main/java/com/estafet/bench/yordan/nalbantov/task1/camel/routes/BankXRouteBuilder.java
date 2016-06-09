package com.estafet.bench.yordan.nalbantov.task1.camel.routes;

import com.estafet.bench.yordan.nalbantov.task1.camel.model.IbanSingleReportEntity;
import org.apache.camel.builder.RouteBuilder;

import java.time.LocalDateTime;

/**
 * Created by estafet on 08/06/16
 */
public class BankXRouteBuilder extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        LocalDateTime now = LocalDateTime.now();

        from("jetty:http://localhost:20616/estafet/iban/report?httpClient.method=POST").routeId("entry")
                .split(body()).setHeader("IbanTimestampOfRequest", /* to string */ constant(now))
                .to("activemq:queue:ibanReport");

//        from("activemq:queue:ibanReport").id("processing")
//                .to("file:///u01/data/iban/reports");

        from("direct:data").process(exchange -> {
            IbanSingleReportEntity entity = new IbanSingleReportEntity(null);

            entity.setName("Yordan Nalbantov");
            entity.setBalance(100.0);
            entity.setCurrency("BGN");

            exchange.getIn().setBody(entity);
        });

        from("activemq:queue:ibanReport").routeId("processing")
                .process(exchange -> {
                    String iban = exchange.getIn().getBody(String.class);
                    IbanSingleReportEntity entity = new IbanSingleReportEntity(iban);
                    exchange.getIn().setBody(entity);
                })
                .enrich("direct:data", (original, resource) -> {
                    IbanSingleReportEntity originalBody = original.getIn().getBody(IbanSingleReportEntity.class);
                    IbanSingleReportEntity resourceBody = resource.getIn().getBody(IbanSingleReportEntity.class);

                    originalBody.setName(resourceBody.getName());
                    originalBody.setBalance(resourceBody.getBalance());
                    originalBody.setCurrency(resourceBody.getCurrency());

                    original.getIn().setBody(originalBody);
                    return original;
                })
                .to("file:///u01/data/iban/reports");
    }
}
