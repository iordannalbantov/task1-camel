package com.estafet.bankx.camel.pojo;

/**
 * Created by Yordan Nalbantov.
 */
public class TransactionPayload extends GenericPayloadWrapper<Transaction> {

    @Override
    public Transaction newInstance() {
        return new Transaction();
    }
}
