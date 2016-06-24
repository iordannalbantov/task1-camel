package com.estafet.bankx.camel.processors;

import com.estafet.bankx.dao.api.AccountService;
import com.estafet.bankx.dao.model.Account;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by Yordan Nalbantov.
 */
public class DbMergeProcessor implements Processor {

    private AccountService accountService;

    @Override
    public void process(Exchange exchange) throws Exception {
        Account account = exchange.getIn().getBody(Account.class);

        if (account != null) {
            account.setChanged(true);
            accountService.mergeAccount(account);
        }
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}