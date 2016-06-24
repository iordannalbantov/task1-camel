package com.estafet.bankx.camel.pojo;

import java.io.Serializable;

/**
 * Created by Yordan Nalbantov.
 */
public class Transaction implements Serializable {

    private String iban;
    private Double amount;

    public Transaction() {
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
