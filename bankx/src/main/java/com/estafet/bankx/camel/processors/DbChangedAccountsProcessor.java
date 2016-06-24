package com.estafet.bankx.camel.processors;

import com.estafet.bankx.camel.pojo.AccountPayload;
import com.estafet.bankx.camel.pojo.ReportRequestPayload;
import com.estafet.bankx.dao.api.AccountService;
import com.estafet.bankx.dao.model.Account;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.util.ArrayList;
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

        List<String> ibans = new ArrayList<>(accounts.size());
        for (Account account : accounts) {
            ibans.add(account.getIban());
        }

        ReportRequestPayload accountPayload = new ReportRequestPayload();
        accountPayload.setData(ibans);

        exchange.getIn().setBody(accountPayload);
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}