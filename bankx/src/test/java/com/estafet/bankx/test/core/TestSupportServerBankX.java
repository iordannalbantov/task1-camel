package com.estafet.bankx.test.core;

import com.estafet.bankx.camel.processors.*;
import com.estafet.bankx.dao.api.AccountService;
import com.estafet.bankx.dao.model.Account;
import org.apache.camel.impl.JndiRegistry;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Yordan Nalbantov.
 */
public abstract class TestSupportServerBankX extends TestSupportServer {

    private static Map<String, Account> database = new LinkedHashMap<>();

    static {
        Account account;

        account = new Account("BG66 ESTF 0616 0000 0000 01", "Ivan Ivanov", 100.0, "BGN", false);
        database.put(account.getIban(), account);
        account = new Account("BG66 ESTF 0616 0000 0000 02", "Dimitar Iliev", 100.0, "BGN", false);
        database.put(account.getIban(), account);
        account = new Account("BG66 ESTF 0616 0000 0000 03", "Ivan Miltenov", 100.0, "BGN", false);
        database.put(account.getIban(), account);
    }

    public TestSupportServerBankX() {
    }

    @Override
    protected JndiRegistry createRegistry() throws Exception {
        JndiRegistry registry = super.createRegistry();

        AccountService accountService = mock(AccountService.class);

        for (Map.Entry<String, Account> entry : database.entrySet()) {
            when(accountService.get(entry.getKey())).thenReturn(entry.getValue());
        }

        registry.bind("accountEnricherService", accountService);
        registry.bind("reportAggregationStrategy", new ReportAggregationStrategy());
        registry.bind("accountsReportProcessor", new AccountsReportProcessor());
        registry.bind("fakeDataProcessor", new FakeDataProcessor());
        registry.bind("ibanSingleReportEntityProcessor", new IbanSingleReportEntityProcessor());
        registry.bind("prepareTransformationProcessor", new PrepareTransformationProcessor());
        registry.bind("testProcessor", new TestProcessor());
        registry.bind("accountsWrapperAggregationStrategy", new AccountsWrapperAggregationStrategy());

        // Manually wiring the services.
        ((FakeDataProcessor) registry.lookupByName("fakeDataProcessor")).setAccountService((AccountService) registry.lookup("accountEnricherService"));

        return registry;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }
}