package com.estafet.bankx.dao.api;

import com.estafet.bankx.dao.model.Account;

/**
 * Created by Yordan Nalbantov.
 */
public interface AccountService {

    void merge(Account account);

    boolean transaction(String iban, Double amount);
}
