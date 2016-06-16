package com.estafet.bankx.model;

/**
 * Created by Yordan Nalbantov.
 */
public class AccountReport extends Account {

    private String timestamp;

    public AccountReport() {
    }

    public AccountReport(String timestamp, Account account) {
        super(account);
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
