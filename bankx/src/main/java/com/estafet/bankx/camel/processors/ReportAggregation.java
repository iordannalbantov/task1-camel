package com.estafet.bankx.camel.processors;

import com.estafet.bankx.model.Account;
import com.estafet.bankx.model.AccountWrapper;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * Created by Yordan Nalbantov.
 */
public class ReportAggregation implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (newExchange.getIn().getBody() instanceof Account) {
            Account entity = (Account) newExchange.getIn().getBody();
            AccountWrapper result;
            if (oldExchange == null) {
                result = new AccountWrapper();
                result.getAccounts().add(entity);
                newExchange.getIn().setBody(result);
                return newExchange;
            } else {
                //noinspection unchecked
                result = oldExchange.getIn().getBody(AccountWrapper.class);
                result.getAccounts().add(entity);
                return oldExchange;
            }
        }
        return null;
    }
}
