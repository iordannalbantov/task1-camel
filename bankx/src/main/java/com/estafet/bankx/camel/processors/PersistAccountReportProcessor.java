package com.estafet.bankx.camel.processors;

import com.estafet.bankx.dao.api.AccountService;
import com.estafet.bankx.dao.model.AccountReport;
import com.estafet.bankx.camel.pojo.other.AccountsReportWrapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by Yordan Nalbantov.
 */
public class PersistAccountReportProcessor implements Processor {

    private AccountService accountService;

    @Override
    public void process(Exchange exchange) throws Exception {
        AccountsReportWrapper wrapper = (AccountsReportWrapper) exchange.getIn().getBody();

        for (AccountReport accountReport : wrapper.getAccounts()) {
            accountService.persistAccountReport(accountReport);
        }
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
}
