package com.estafet.bench.yordan.nalbantov.task1.camel.processors;

import com.estafet.bankx.accounts.api.AccountServiceApi;
import com.estafet.bankx.model.Account;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by Yordan Nalbantov.
 */
public class FakeDataProcessor implements Processor {

    private static Account[] data = {
            new Account("BG66 ESTF 0616 0000 0000 01", "Ivan Ivanov", 100.0, "BGN"),
            new Account("BG66 ESTF 0616 0000 0000 02", "Dimitar Iliev", 100.0, "BGN"),
            new Account("BG66 ESTF 0616 0000 0000 03", "Ivan Miltenov", 100.0, "BGN"),
    };

    AccountServiceApi accountEnricherService;

    @Override
    public void process(Exchange exchange) throws Exception {
        Object body = exchange.getIn().getBody();
        if (body instanceof Account) {
            Account entity = (Account) body;
            for (Account fakeEntity : data) {
                if (fakeEntity.getIban().equals(entity.getIban())) {

                    entity.setName(fakeEntity.getName());
                    entity.setBalance(fakeEntity.getBalance());
                    entity.setCurrency(fakeEntity.getCurrency());

                    break;
                }
            }
        }
    }

    public void setAccountEnricherService(AccountServiceApi accountEnricherService) {
        this.accountEnricherService = accountEnricherService;
    }
}