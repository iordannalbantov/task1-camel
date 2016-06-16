package com.estafet.bankx.camel.processors;

import com.estafet.bankx.accounts.api.AccountServiceApi;
import com.estafet.bankx.model.Account;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by Yordan Nalbantov.
 */
public class FakeDataProcessor implements Processor {

    private AccountServiceApi accountEnricherService;

    @Override
    public void process(Exchange exchange) throws Exception {

        Object body = exchange.getIn().getBody();
        if (body instanceof Account) {
            Account entity = (Account) body;

            if (accountEnricherService != null) {
                Account fakeEntity = accountEnricherService.getAccountByIban(entity.getIban());
                if (fakeEntity != null) {
                    entity.setName(fakeEntity.getName());
                    entity.setBalance(fakeEntity.getBalance());
                    entity.setCurrency(fakeEntity.getCurrency());
                }
            }
        }
    }

    public void setAccountEnricherService(AccountServiceApi accountEnricherService) {
        this.accountEnricherService = accountEnricherService;
    }
}