package com.estafet.bench.yordan.nalbantov.task1.camel.aggregations;

import com.estafet.bench.yordan.nalbantov.task1.camel.model.IbanSingleReportEntity;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * Created by estafet.
 */
public class IbanSingleReportEntityAggregation implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange original, Exchange resource) {
        IbanSingleReportEntity originalBody = original.getIn().getBody(IbanSingleReportEntity.class);
        IbanSingleReportEntity resourceBody = resource.getIn().getBody(IbanSingleReportEntity.class);

        originalBody.setName(resourceBody.getName());
        originalBody.setBalance(resourceBody.getBalance());
        originalBody.setCurrency(resourceBody.getCurrency());

        original.getIn().setBody(originalBody);
        return original;
    }
}
