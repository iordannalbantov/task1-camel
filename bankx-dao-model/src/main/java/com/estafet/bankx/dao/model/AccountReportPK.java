package com.estafet.bankx.dao.model;

import java.io.Serializable;

/**
 * Created by Yordan Nalbantov.
 */
public class AccountReportPK implements Serializable {

    private String timestamp;
    private String iban;

    public AccountReportPK() {
    }

    public int hashCode() {
        return (timestamp + iban).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AccountReportPK) {
            AccountReportPK account = (AccountReportPK) obj;
            boolean equalChanged = Utils.equality(timestamp, account.timestamp);
            boolean equalIban = Utils.equality(iban, account.iban);
            return equalIban && equalChanged;
        }
        return false;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
