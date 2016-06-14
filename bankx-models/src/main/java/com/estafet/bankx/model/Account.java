package com.estafet.bankx.model;

import java.io.Serializable;

/**
 * Created by Yordan Nalbantov
 */
public class Account implements Serializable {

    private String iban;
    private String name;
    private double balance;
    private String currency;

    public Account() {
    }

    public Account(Account account) {
        this.iban = account.iban;
        this.name = account.name;
        this.balance = account.balance;
        this.currency = account.currency;
    }

    public Account(String iban, String name, double balance, String currency) {
        this.iban = iban;
        this.name = name;
        this.balance = balance;
        this.currency = currency;
    }

    public Account(String iban) {
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Account) {
            Account account = (Account) obj;
            boolean isEqualIban = (this.iban == null && account.getIban() == null) || (this.iban != null && this.iban.equals(((Account) obj).getIban()));
            boolean isEqualName = (this.name == null && account.getName() == null) || (this.name != null && this.name.equals(((Account) obj).getName()));
            boolean isEqualBalance = (this.balance == ((Account) obj).getBalance());
            boolean isEqualCurrency = (this.currency == null && account.getCurrency() == null) || (this.currency != null && this.currency.equals(((Account) obj).getCurrency()));

            return isEqualIban && isEqualName && isEqualBalance && isEqualCurrency;
        }
        return false;
    }
}
