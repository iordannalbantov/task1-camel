package com.estafet.bench.yordan.nalbantov.task1.camel.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Yordan Nalbantov.
 */
public class LoggerProcessor implements Processor {

    private static Logger logger = Logger.getLogger(FakeDataProcessor.class.getSimpleName());

    @Override
    public void process(Exchange exchange) throws Exception {
        logger.log(Level.INFO, "Message Processed ");
    }
}
