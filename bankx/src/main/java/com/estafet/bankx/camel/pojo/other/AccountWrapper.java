package com.estafet.bankx.camel.pojo.other;

import com.estafet.bankx.dao.model.Account;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yordan Nalbantov.
 */
@XmlRootElement
public class AccountWrapper implements Serializable {

    private List<Account> accounts = new ArrayList<>();

    public AccountWrapper() {
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
