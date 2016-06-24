package com.estafet.bankx.camel.pojo;

/**
 * Created by Yordan Nalbantov.
 */
public interface PayloadWrapper<T> {

    T getData();

    void setData(T data);
}
