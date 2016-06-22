package com.estafet.bankx.camel.pojo;

import com.estafet.bankx.camel.base.pojo.GenericPayloadWrapper;
import com.estafet.bankx.dao.model.Account;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Yordan Nalbantov.
 */
public class AccountPayload extends GenericPayloadWrapper<Collection<Account>> {

    public AccountPayload() {
        super();
    }

    @Override
    public Collection<Account> newInstance() {
        return new ArrayList<>();
    }
}
