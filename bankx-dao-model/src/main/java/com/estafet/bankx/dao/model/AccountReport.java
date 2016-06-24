package com.estafet.bankx.dao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by Yordan Nalbantov.
 */
@Entity
@Table(name = "account_report")
public class AccountReport implements Serializable {

    @Id
    @Column(name = "stamp")
    private String timestamp;

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
