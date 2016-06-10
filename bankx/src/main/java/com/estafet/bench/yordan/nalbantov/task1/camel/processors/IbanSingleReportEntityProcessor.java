package com.estafet.bench.yordan.nalbantov.task1.camel.processors;

import com.estafet.bench.yordan.nalbantov.task1.camel.model.IbanSingleReportEntity;
import org.apache.camel.BeanInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.logging.Logger;

/**
 * Created by estafet on 10/06/16.
 */
public class IbanSingleReportEntityProcessor implements Processor {

    private static Logger logger = Logger.getLogger(IbanSingleReportEntityProcessor.class.getSimpleName());

    @Override
    public void process(Exchange exchange) throws Exception {
        IbanSingleReportEntity entity = new IbanSingleReportEntity(null);

        entity.setName("Yordan Nalbantov");
        entity.setBalance(100.0);
        entity.setCurrency("BGN");

        exchange.getIn().setBody(entity);
    }
}
