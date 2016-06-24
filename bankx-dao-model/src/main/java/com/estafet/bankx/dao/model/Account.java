package com.estafet.bankx.dao.model;

import javax.persistence.*;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Known problem:
 * We do not provide locking mechanism for this experimental entity but as it is, we will get the data into the mess.
 * The user could read an account with intent to change its balance. Meanwhile a transaction could read the very same
 * account. The first transaction changes the account's balance. The second transaction changes the reporting flag
 * and as a side effect overrides the balance of the account with the old data.
 * <p>
 * Created by Yordan Nalbantov.
 */
@Entity
@Table(name = "account")
@NamedQueries({
        @NamedQuery(name = "Account.allListed",
                query = "select new com.estafet.bankx.dao.model.Account(o.iban, o.name, o.balance, o.changed) o " +
                        "from Account o where o.iban in :ibans")
})
public class Account {

    @Id
    @Column(name = "iban")
    private String iban;

    @Column(name = "name")
    private String name;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "changed")
    private Boolean changed;

    public Account() {
        this.changed = false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Account) {
            Account account = (Account) obj;
            boolean equalIban = equality(iban, account.getIban());
            boolean equalName = equality(name, account.getName());
            boolean equalBalance = equality(balance, account.getBalance());
            boolean equalChanged = equality(changed, account.getChanged());
            return equalIban && equalName && equalBalance && equalChanged;
        }
        return false;
    }

    /**
     * Support for testing that primitive classes are equal but wrap the additional null-notnull logic.
     *
     * @param a   The first value.
     * @param b   The second value.
     * @param <T> A generic to offload to the compiler the class-comparison logic.
     * @return True if both are equal or null.
     */
    public static <T> boolean equality(T a, T b) {
        return ((a == null) && (b == null)) || ((a != null) && a.equals(b));
    }

    /**
     * A constructor to use with JPA named queries.
     *
     * @param iban    The IBAN of the account.
     * @param name    The name of the account owner.
     * @param balance The amount of money for this account.
     * @param changed Is the account changed or not. It is used for reporting purposes.
     */
    public Account(String iban, String name, Double balance, Boolean changed) {
        this.iban = iban;
        this.name = name;
        this.balance = balance;
        this.changed = changed;
    }

    /**
     * Returns fresh accounts as detached objects read directly from the database.
     *
     * @param entityManager An EntityManager instance provided.
     * @param ibans         The IBANs of interest.
     * @return A list of detached objects for this IBANs.
     */
    public static List<Account> allListed(EntityManager entityManager, Collection<String> ibans) {
        TypedQuery<Account> query = entityManager.createNamedQuery("Account.allListed", Account.class);
        query.setParameter("ibans", ibans);
        return query.getResultList();
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Boolean getChanged() {
        return changed;
    }

    public void setChanged(Boolean changed) {
        this.changed = changed;
    }
}