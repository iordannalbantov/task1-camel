package com.estafet.bankx.dao.impl;

import com.estafet.bankx.dao.api.AccountService;
import com.estafet.bankx.dao.model.Account;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by estafet.
 */
public class AccountServiceImpl implements AccountService {

    @PersistenceContext
    protected EntityManager entityManager;

    public AccountServiceImpl() {
    }

    @Override
    public void merge(Account account) {
       Account mergedAccount = entityManager.merge(account);
    }

    @Override
    public Account lockup(String iban) {
        return entityManager.find(Account.class, iban);
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
