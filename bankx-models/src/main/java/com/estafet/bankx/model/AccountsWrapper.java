package com.estafet.bankx.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by estafet.
 */
public class AccountsWrapper implements Serializable {

    private List<Account> accounts = new ArrayList<>();

    public AccountsWrapper() {
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
