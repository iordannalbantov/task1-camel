package com.estafet.bankx.camel.processors;

import com.estafet.bankx.camel.pojo.Transaction;
import com.estafet.bankx.dao.api.AccountService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by Yordan Nalbantov.
 */
public class DbTransactionProcessor implements Processor {

    private AccountService accountService;

    @Override
    public void process(Exchange exchange) throws Exception {
        Transaction transaction = exchange.getIn().getBody(Transaction.class);

        boolean success = false;
        if (transaction != null) {
            success = accountService.transaction(transaction.getIban(), transaction.getAmount());
        }

        exchange.getOut().setBody("");
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
