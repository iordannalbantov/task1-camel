package com.estafet.bankx.camel.pojo;

import com.estafet.bankx.camel.base.pojo.GenericPayloadWrapper;

/**
 * Created by Yordan Nalbantov.
 */
public class TransactionPayload extends GenericPayloadWrapper<Transaction> {

    @Override
    public Transaction newInstance() {
        return new Transaction();
    }
}
