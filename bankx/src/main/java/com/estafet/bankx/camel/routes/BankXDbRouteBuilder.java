package com.estafet.bankx.camel.routes;

import com.estafet.bankx.camel.base.camel.BaseBankXRouteBuilder;
import com.estafet.bankx.camel.pojo.AccountPayload;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by Yordan Nalbantov.
 */
public class BankXDbRouteBuilder extends BaseBankXRouteBuilder {

    @Override
    public void configure() throws Exception {
        // REST micro-service accepting JSON accounts data.
        from("jetty:{{bankx.endpoint.db.baseUrl}}?httpMethodRestrict=POST&continuationTimeout=5000").routeId("jettyDbInsert")
                .onException(Exception.class)
                .handled(true)
                .transform(constant("Something went wrong."))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpServletResponse.SC_INTERNAL_SERVER_ERROR))
                .end()
                .unmarshal().json(JsonLibrary.Jackson, AccountPayload.class)
                .processRef("dbMergeProcessor");
    }
}