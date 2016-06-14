package com.estafet.bankx.accounts.impl;

import com.estafet.bankx.accounts.api.AccountServiceApi;
import com.estafet.bankx.model.Account;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Yordan Nalbantov.
 */
public class AccountsServiceImpl implements AccountServiceApi {

    private static Map<String, Account> database = new LinkedHashMap<>();

    static {
        Account account;

        account = new Account("BG66 ESTF 0616 0000 0000 01", "Ivan Ivanov", 100.0, "BGN");
        database.put(account.getIban(), account);
        account = new Account("BG66 ESTF 0616 0000 0000 02", "Dimitar Iliev", 100.0, "BGN");
        database.put(account.getIban(), account);
        account = new Account("BG66 ESTF 0616 0000 0000 03", "Ivan Miltenov", 100.0, "BGN");
        database.put(account.getIban(), account);
    }

    @Override
    public Account getAccountByIban(String iban) {
        return iban != null ? database.get(iban) : null;
    }
}
