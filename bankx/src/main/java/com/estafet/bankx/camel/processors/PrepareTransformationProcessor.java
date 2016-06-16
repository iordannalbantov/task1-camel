package com.estafet.bankx.camel.processors;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.SimpleBuilder;

import java.util.Random;

/**
 * Created by estafet.
 */
public class PrepareTransformationProcessor implements Processor {

    private Random randomGenerator = new Random();

    @Override
    public void process(Exchange exchange) throws Exception {
        SimpleBuilder expression = SimpleBuilder.simple("{{bankx.endpoint.directEntry.destination}}/${date:now:yyyyMMdd}_" +
                Integer.toString(randomGenerator.nextInt(9999999) + 1) + ".csv");
        String fileName = expression.evaluate(exchange, String.class);
        exchange.getIn().setHeader(Exchange.XSLT_FILE_NAME, fileName);
    }
}
