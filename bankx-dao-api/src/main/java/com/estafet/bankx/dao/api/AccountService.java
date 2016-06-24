package com.estafet.bankx.dao.api;

import com.estafet.bankx.dao.model.Account;

import java.util.List;

/**
 * Created by Yordan Nalbantov.
 */
public interface AccountService {

    void merge(Account account);

    boolean transaction(String iban, Double amount);

    List<Account> changed();

    void same(List<Account> accounts);
}
