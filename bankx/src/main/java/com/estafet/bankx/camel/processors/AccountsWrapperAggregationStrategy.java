package com.estafet.bankx.camel.processors;

import com.estafet.bankx.camel.pojo.other.AccountsReportWrapper;
import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

/**
 * Created by Yordan Nalbantov.
 */
public class AccountsWrapperAggregationStrategy implements AggregationStrategy {

    @Override
    public Exchange aggregate(Exchange original, Exchange resource) {
        if (resource.getIn().getBody() instanceof AccountsReportWrapper) {
            AccountsReportWrapper entity = (AccountsReportWrapper) resource.getIn().getBody();
            AccountsReportWrapper result;
            if (original == null) {
                result = new AccountsReportWrapper();
                result.getAccounts().addAll(entity.getAccounts());
                resource.getIn().setBody(result);
                return resource;
            } else {
                //noinspection unchecked
                result = original.getIn().getBody(AccountsReportWrapper.class);
                result.getAccounts().addAll(entity.getAccounts());
                return original;
            }
        }
        return null;
    }
}
