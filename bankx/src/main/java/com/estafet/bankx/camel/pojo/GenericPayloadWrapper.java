package com.estafet.bankx.camel.pojo;

/**
 * Created by Yordan Nalbantov.
 */
public abstract class GenericPayloadWrapper<T> implements PayloadWrapper<T> {

    private T data;

    public GenericPayloadWrapper() {
        this.data = newInstance();
    }

    public abstract T newInstance();

    @Override
    public T getData() {
        return data;
    }

    @Override
    public void setData(T data) {
        this.data = data;
    }
}
