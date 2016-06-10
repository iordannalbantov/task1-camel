package com.estafet.bench.yordan.nalbantov.task1.camel.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by estafet.
 */
public class AccountsWrapper implements Serializable {

    private List<IbanSingleReportEntity> accounts = new ArrayList<>();

    public AccountsWrapper() {
    }

    public List<IbanSingleReportEntity> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<IbanSingleReportEntity> accounts) {
        this.accounts = accounts;
    }
}
