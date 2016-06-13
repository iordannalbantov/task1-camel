package com.estafet.bench.yordan.nalbantov.task1.camel.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Processor used for debugging purposes, to get into the route`s context while debugging it.
 * It is not a logger!
 *
 * Created by Yordan Nalbantov.
 */
public class TestProcessor implements Processor {

    private static Logger logger = Logger.getLogger(FakeDataProcessor.class.getSimpleName());

    @Override
    public void process(Exchange exchange) throws Exception {
//        logger.log(Level.INFO, "Message Processed ");
         throw new Exception();
    }
}
