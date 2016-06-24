package com.estafet.bankx.dao.model.other;


import com.estafet.bankx.dao.model.Account;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yordan Nalbantov.
 */
@XmlRootElement
public class AccountsReportWrapper implements Serializable {
    private String timestamp;
    private List<AccountReport> accounts = new ArrayList<>();

    public AccountsReportWrapper() {
    }

    public AccountsReportWrapper(String timestamp, AccountWrapper accountWrapper) {
        this.timestamp = timestamp;
        if (accountWrapper != null) {
            for (Account account : accountWrapper.getAccounts()) {
                accounts.add(new AccountReport(timestamp, account) );
            }
        }
    }

    @XmlElement()
    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<AccountReport> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountReport> accounts) {
        this.accounts = accounts;
    }
}
