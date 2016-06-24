package com.estafet.bankx.dao.api;

import com.estafet.bankx.dao.model.Account;
import com.estafet.bankx.dao.model.AccountReport;

import java.util.List;

/**
 * Created by Yordan Nalbantov.
 */
public interface AccountService {

    void persist(Account account);

    void persist(AccountReport accountReport);

    Account get(String iban);

    boolean transaction(String iban, Double amount);

    List<Account> changed();

    void same(List<Account> accounts);
}
