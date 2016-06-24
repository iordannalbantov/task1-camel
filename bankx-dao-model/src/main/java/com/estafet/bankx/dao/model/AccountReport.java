package com.estafet.bankx.dao.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Yordan Nalbantov.
 */
@IdClass(AccountReportPK.class)
@Entity
@Table(name = "account_report")
public class AccountReport implements Serializable {

    @Id
    @Column(name = "stamp")
    private String timestamp;

    @Id
    @Column(name = "iban")
    private String iban;

    @Column(name = "name")
    private String name;

    @Column(name = "currency")
    private String currency;

    @Column(name = "balance")
    private Double balance;

    public AccountReport() {
    }

    public AccountReport(String timestamp, Account account) {
        this.iban = account.getIban();
        this.name = account.getName();
        this.currency = account.getCurrency();
        this.balance = account.getBalance();

        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AccountReport) {
            AccountReport account = (AccountReport) obj;
            boolean equalTimestamp = Utils.equality(timestamp, account.timestamp);
            boolean equalIban = Utils.equality(iban, account.iban);
            boolean equalName = Utils.equality(name, account.name);
            boolean equalBalance = Utils.equality(balance, balance);
            boolean equalCurrency = Utils.equality(currency, account.currency);
            return equalTimestamp && equalIban && equalName && equalBalance && equalCurrency;
        }
        return false;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
