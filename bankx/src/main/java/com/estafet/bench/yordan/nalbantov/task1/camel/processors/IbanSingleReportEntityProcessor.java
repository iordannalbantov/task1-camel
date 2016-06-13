package com.estafet.bench.yordan.nalbantov.task1.camel.processors;

import com.estafet.bench.yordan.nalbantov.task1.camel.model.Accounts;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Yordan Nalbantov.
 */
public class IbanSingleReportEntityProcessor implements Processor {

    private static Logger logger = Logger.getLogger(FakeDataProcessor.class.getSimpleName());

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.log(Level.INFO, "IbanTimestampOfRequest = {0}", exchange.getIn().getHeader("IbanTimestampOfRequest"));
        String iban = exchange.getIn().getBody(String.class);
        Accounts entity = new Accounts(iban);
        exchange.getIn().setBody(entity);
    }
}