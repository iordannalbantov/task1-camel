package com.estafet.bankx.camel.routes.base;

import org.apache.camel.builder.RouteBuilder;

/**
 * Created by estafet.
 */
public abstract class BaseBankXRouteBuilder extends RouteBuilder {

    @Override
    public abstract void configure() throws Exception;

    @SuppressWarnings("unchecked")
    protected <T> T lookupByName(String name) {
        return (T) getContext().getRegistry().lookupByName(name);
    }
}