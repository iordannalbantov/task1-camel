package com.estafet.bench.yordan.nalbantov.task1.camel.model;

import java.io.Serializable;

/**
 * Created by estafet on 09/06/16
 */
public class IbanSingleReportEntity implements Serializable {

    private String iban;
    private String name;
    private double balance;
    private String currency;

    public IbanSingleReportEntity() {
    }

    public IbanSingleReportEntity(String iban) {
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
