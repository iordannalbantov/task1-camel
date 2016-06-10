package com.estafet.bench.yordan.nalbantov.task1.camel.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.language.Bean;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by estafet.
 */
public class LoggerProcessor implements Processor {

    private static Logger logger = Logger.getLogger(IbanSingleReportEntityProcessor.class.getSimpleName());

    @Override
    public void process(Exchange exchange) throws Exception {
        if (exchange.getIn() != null && exchange.getIn().getBody() != null) {
            logger.log(Level.INFO, "Message Processed with body: %n {0} %n", exchange.getIn().getBody().toString());
        } else {
            logger.log(Level.INFO, "Message Processed with body: null");
        }
    }
}
