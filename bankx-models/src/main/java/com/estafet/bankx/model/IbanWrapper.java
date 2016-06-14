package com.estafet.bankx.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yordan Nalbantov.
 */
public class IbanWrapper implements Serializable {

    private List<String> ibans = new ArrayList<>();

    public IbanWrapper() {
    }

    public List<String> getIbans() {
        return ibans;
    }

    public void setIbans(List<String> ibans) {
        this.ibans = ibans;
    }
}
