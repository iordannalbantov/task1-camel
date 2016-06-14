package com.estafet.bench.yordan.nalbantov.task1.camel.processors;

import com.estafet.bench.yordan.nalbantov.task1.camel.model.Account;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * Created by estafet.
 */
public class AccountsEnricherAggregationStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange original, Exchange resource) {
        Account originalBody = original.getIn().getBody(Account.class);
        Account resourceBody = resource.getIn().getBody(Account.class);

        originalBody.setName(resourceBody.getName());
        originalBody.setBalance(resourceBody.getBalance());
        originalBody.setCurrency(resourceBody.getCurrency());

        original.getIn().setBody(originalBody);
        return original;
    }
}
