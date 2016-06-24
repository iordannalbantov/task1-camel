package com.estafet.bankx.camel.processors;

import com.estafet.bankx.camel.pojo.AccountPayload;
import com.estafet.bankx.dao.api.AccountService;
import com.estafet.bankx.dao.model.Account;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.List;

/**
 * Created by Yordan Nalbantov.
 */
public class DbChangedAccountsProcessor implements Processor {

    private AccountService accountService;

    @Override
    public void process(Exchange exchange) throws Exception {
        List<Account> accounts = accountService.changed();
        accountService.same(accounts);
        AccountPayload accountPayload = new AccountPayload();
        accountPayload.setData(accounts);

        exchange.getIn().setBody(accountPayload);
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}