package com.estafet.bankx.camel.routes;

import com.estafet.bankx.camel.base.camel.BaseBankXRouteBuilder;
import com.estafet.bankx.camel.pojo.AccountPayload;
import com.estafet.bankx.camel.pojo.ReportRequestPayload;
import com.estafet.bankx.camel.pojo.TransactionPayload;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.model.dataformat.JsonLibrary;

import javax.servlet.http.HttpServletResponse;

/**
 * Implementation of all BankX persistent micro-services.
 * Underlaying technology is Camel routes and OSGi wired persistent services.
 * <p>
 * Created by Yordan Nalbantov.
 */
public class BankXDbRouteBuilder extends BaseBankXRouteBuilder {

    /**
     * Configure Camel routes. Implemented as 'subroutines' as it is cleaner code.
     *
     * @throws Exception Throws Exception as the abstract method from the superclass requires. We are not rising such
     *                   implicitly but explicit rising might occur.
     */
    @Override
    public void configure() throws Exception {
        configureRouteJettyInsertAccounts();
        configureRouteJettyUpdateAmounts();
        configureRouteCronReport();
    }

    /**
     * REST micro-service accepting JSON accounts data and storing it into the database.
     * Implemented with Jetty endpoint, exception handling, un-marshalling the payload and processing it to the database.
     */
    private void configureRouteJettyInsertAccounts() {
        from("jetty:{{bankx.endpoint.db.baseUrl}}/insertAccounts?httpMethodRestrict=POST&continuationTimeout=5000").routeId("routeJettyInsertAccounts")
                .onException(Exception.class)
                //@formatter:off
                    .handled(true)
                    .transform(constant("Something went wrong.")).log(LoggingLevel.INFO, "${exception.message}\\n${exception.stacktrace}")
                    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpServletResponse.SC_INTERNAL_SERVER_ERROR))
                //@formatter:on
                .end()
                .unmarshal().json(JsonLibrary.Jackson, AccountPayload.class)
                // The service lets the wire tap continue processing the message asynchronously.
                // The caller is assured with HttpServletResponse.SC_OK and empty HTTP response body that the message is
                // received and will be processed.
                .wireTap("direct:insertAccounts")
                .transform().constant("");

        // Asynchronously process the AccountPayload.
        // Basically it is part of the async part of the main route, therefore not separated in its own method.
        from("direct:insertAccounts")
                .split(simple("${body.getData()}"))
                // Persisting the splitted Accounts in parallel, while the caller thread still waits for all
                // sub-messages to be fully processed.
                .parallelProcessing()
                .processRef("dbMergeProcessor");
    }

    /**
     * REST micro-service accepting JSON data comprising of iban and transaction amount and processing it to the database.
     */
    private void configureRouteJettyUpdateAmounts() {
        from("jetty:{{bankx.endpoint.db.baseUrl}}/updateAmounts?httpMethodRestrict=POST&continuationTimeout=5000").routeId("routeJettyUpdateAmounts")
                .onException(Exception.class)
                //@formatter:off
                    .handled(true)
                    .transform(constant("Something went wrong.")).log(LoggingLevel.INFO, "${exception.message}\\n${exception.stacktrace}")
                    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpServletResponse.SC_INTERNAL_SERVER_ERROR))
                //@formatter:on
                .end()
                .unmarshal().json(JsonLibrary.Jackson, TransactionPayload.class)
                .split(simple("${body.getData()}"))
                .processRef("dbTransactionProcessor");
    }

    private void configureRouteCronReport() {
        from("quartz2://report?cron={{bankx.cron.report}}&fireNow=false").routeId("routeCronReport")
                .processRef("dbChangedAccountsProcessor");
//                .marshal().json(JsonLibrary.Jackson, ReportRequestPayload.class)
//                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
//                .to("http:{{bankx.routeCronReport.toUrl}}").id("routeCronReportHttp");


//        from("jetty:{{bankx.endpoint.entry.url}}?httpMethodRestrict=POST&continuationTimeout=5000").routeId("entry")
//                .onException(Exception.class)
//                .handled(true)
//                .transform(constant("Something went wrong."))
//                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpServletResponse.SC_INTERNAL_SERVER_ERROR))
//                .end()
//                .to(ExchangePattern.InOnly, "direct:entry").setBody(constant(""));
//
//        from("direct:entry").routeId("directEntry")
//                .unmarshal().json(JsonLibrary.Jackson, IbanWrapper.class)
//                .setHeader("IbanTimestampOfRequest", simple("${date:now:yyyy MM dd HH mm ss SSS}"))
//                .split(simple("${body.getIbans()}"))
//                .to("activemq:queue:ibanReport").id("result");
    }
}