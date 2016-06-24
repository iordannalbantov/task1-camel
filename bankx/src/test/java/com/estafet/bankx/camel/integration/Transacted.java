package com.estafet.bankx.camel.integration;

import javax.persistence.EntityManager;

/**
 * Created by Yordan Nalbantov.
 */
public abstract class Transacted {

    public abstract void action(EntityManager entityManager);
}
