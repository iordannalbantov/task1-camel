package com.estafet.bankx.accounts.api;

import com.estafet.bankx.model.Account;

/**
 * Created by Yordan Nalbantov.
 */
public interface AccountServiceApi {

    Account getAccountByIban(String iban);
}
