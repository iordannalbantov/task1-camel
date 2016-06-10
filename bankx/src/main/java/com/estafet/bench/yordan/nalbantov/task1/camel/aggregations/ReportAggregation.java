package com.estafet.bench.yordan.nalbantov.task1.camel.aggregations;

import com.estafet.bench.yordan.nalbantov.task1.camel.model.AccountsWrapper;
import com.estafet.bench.yordan.nalbantov.task1.camel.model.IbanSingleReportEntity;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.ArrayList;

/**
 * Created by Yordan Nalbantov.
 */
public class ReportAggregation implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        if (newExchange.getIn().getBody() instanceof IbanSingleReportEntity) {
            IbanSingleReportEntity entity = (IbanSingleReportEntity) newExchange.getIn().getBody();
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
