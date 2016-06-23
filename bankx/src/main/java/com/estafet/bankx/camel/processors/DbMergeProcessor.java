package com.estafet.bankx.camel.processors;

import com.estafet.bankx.camel.pojo.AccountPayload;
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
        AccountPayload accountPayload = exchange.getIn().getBody(AccountPayload.class);

        if (accountPayload != null) {
            for (Account account : accountPayload.getData()) {
                account.setChanged(true);
                accountService.merge(account);
            }
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