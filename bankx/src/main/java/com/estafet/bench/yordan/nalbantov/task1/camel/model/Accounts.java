package com.estafet.bench.yordan.nalbantov.task1.camel.model;

import java.io.Serializable;

/**
 * Created by Yordan Nalbantov
 */
public class Accounts implements Serializable {

    private String iban;
    private String name;
    private double balance;
    private String currency;

    public Accounts() {
    }

    public Accounts(String iban, String name, double balance, String currency) {
        this.iban = iban;
        this.name = name;
        this.balance = balance;
        this.currency = currency;
    }

    public Accounts(String iban) {
        this.iban = iban;
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
