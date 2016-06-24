package com.estafet.bankx.camel.processors;

import com.estafet.bankx.dao.api.AccountService;
import com.estafet.bankx.dao.model.Account;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by Yordan Nalbantov.
 */
public class FakeDataProcessor implements Processor {

    private AccountService accountService;

    @Override
    public void process(Exchange exchange) throws Exception {

        Object body = exchange.getIn().getBody();
        if (body instanceof Account) {
            Account entity = (Account) body;

            if (accountService != null) {
                Account account = accountService.get(entity.getIban());
                if (account != null) {
                    entity.setName(account.getName());
                    entity.setBalance(account.getBalance());
                    entity.setCurrency(account.getCurrency());
                }
            }
        }
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}