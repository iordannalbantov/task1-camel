package com.estafet.bench.yordan.nalbantov.task1.camel.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by estafet.
 */
public class AccountsWrapper implements Serializable {

    private List<Accounts> accounts = new ArrayList<>();

    public AccountsWrapper() {
    }

    public List<Accounts> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Accounts> accounts) {
        this.accounts = accounts;
    }
}
