package com.estafet.bankx.dao.impl;

import com.estafet.bankx.dao.api.AccountService;
import com.estafet.bankx.dao.model.Account;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by Yordan Nalbantov.
 */
public class AccountServiceImpl implements AccountService {

    /**
     * The EntityManager wired to this service via the modifier.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * A default constructor as required by the Java beans specification.
     */
    public AccountServiceImpl() {
    }

    /**
     * Merges the data provided into the database.
     *
     * @param account The Account data provided.
     */
    @Override
    public void merge(Account account) {
        // The attached instance captured in the result variable for future use.
        Account mergedAccount = entityManager.merge(account);
    }

    @Override
    public Account get(String iban) {
        return Account.get(entityManager, iban);
    }

    /**
     * Given an iban and amount changes the balance with the given number.
     *
     * @param iban   The iban to lockup the account with.
     * @param amount The amount to add to the account's balance. It could be an negative number or zero.
     * @return Returns false when there is no account for this iban.
     */
    @Override
    public boolean transaction(String iban, Double amount) {
        Account account = entityManager.find(Account.class, iban);
        if (account != null) {
            double balance = 0;
            if (account.getBalance() == null) {
                balance = account.getBalance();
            }
            double amountToAdd = 0;
            if (amount != null) {
                amountToAdd = amount;
            }
            account.setBalance(balance + amountToAdd);
        }
        return false;
    }

    @Override
    public List<Account> changed() {
        return Account.changed(entityManager);
    }

    @Override
    public void same(List<Account> accounts) {
        for (Account account : accounts) {
            account.setChanged(false);
            entityManager.merge(account);
        }
    }

    /**
     * An accessor for the EntityManager. The usage scenario requires only the modifier but the beans specification
     * requires both, so it is good practice to do it that way.
     *
     * @return The EntityManager instance provided to this service.
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * The modifier for the EntityManager dependency.
     *
     * @param entityManager The new EntityManager provided for this service.
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
