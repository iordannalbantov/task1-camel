package com.estafet.bankx.dao.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Yordan Nalbantov.
 */
@Entity
@Cacheable
@Table(name = "account")
public class Account  {

    @Id
    @Column(name = "iban")
    private String iban;
    @Column(name = "name")
    private String name;
    @Column(name = "balance")
    private Double balance;
    @Column(name = "changed")
    private Boolean changed;

    public Account() {
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

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Boolean getChanged() {
        return changed;
    }

    public void setChanged(Boolean changed) {
        this.changed = changed;
    }
}
