package com.estafet.bench.yordan.nalbantov.task1.camel.processors;

import com.estafet.bench.yordan.nalbantov.task1.camel.model.AccountsWrapper;
import com.estafet.bench.yordan.nalbantov.task1.camel.model.Accounts;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * Created by Yordan Nalbantov.
 */
public class ReportAggregation implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (newExchange.getIn().getBody() instanceof Accounts) {
            Accounts entity = (Accounts) newExchange.getIn().getBody();
            AccountsWrapper result;
            if (oldExchange == null) {
                result = new AccountsWrapper();
                result.getAccounts().add(entity);
                newExchange.getIn().setBody(result);
                return newExchange;
            } else {
                //noinspection unchecked
                result = oldExchange.getIn().getBody(AccountsWrapper.class);
                result.getAccounts().add(entity);
                return oldExchange;
            }
        }
        return null;
    }
}
