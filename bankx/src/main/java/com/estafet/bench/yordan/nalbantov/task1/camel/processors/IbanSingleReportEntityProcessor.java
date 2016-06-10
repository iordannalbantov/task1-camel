package com.estafet.bench.yordan.nalbantov.task1.camel.processors;

import com.estafet.bench.yordan.nalbantov.task1.camel.model.IbanSingleReportEntity;
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
        IbanSingleReportEntity entity = new IbanSingleReportEntity(iban);
        exchange.getIn().setBody(entity);
    }
}