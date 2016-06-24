package com.estafet.bankx.dao.api;

import com.estafet.bankx.dao.model.Account;
import com.estafet.bankx.dao.model.AccountReport;

import java.util.List;

/**
 * Created by Yordan Nalbantov.
 */
public interface AccountService {

    /**
     * Persists the provided account.
     * @param account The account to be persisted.
     */
    void mergeAccount(Account account);

    /**
     * Merges the data provided into the database.
     *
     * @param accountReport The AccountReport provided.
     */
    void persistAccountReport(AccountReport accountReport);

    /**
     * Gets the account with the provided IBAN or null if not found.
     * @param iban The IBAN of the account of interest.
     * @return The account for the provided IBAN.
     */
    Account get(String iban);

    /**
     * Given an iban and amount changes the balance with the given number.
     *
     * @param iban   The iban to lockup the account with.
     * @param amount The amount to add to the account's balance. It could be an negative number or zero.
     * @return Returns false when there is no account for this iban.
     */
    boolean transaction(String iban, Double amount);

    /**
     * Selects from the database currently unchanged accounts.
     * @return The result set.
     */
    List<Account> changed();

    /**
     * The method merges the accounts provided, but beforehand marks them as unchanged.
     * @param accounts The accounts provided to be marked as unchanged.
     */
    void same(List<Account> accounts);
}
