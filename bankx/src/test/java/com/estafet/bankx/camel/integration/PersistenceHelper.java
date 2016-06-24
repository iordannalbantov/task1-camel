package com.estafet.bankx.camel.integration;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Yordan Nalbantov.
 */
public class PersistenceHelper {

    private static final Logger logger = Logger.getLogger("ServicesTest");

    public static void transaction(EntityManager entityManager, Transacted transacted) {
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();

            transacted.action(entityManager);

            entityManager.flush();

            transaction.commit();
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            transaction.rollback();
        }

        entityManager.clear();
    }
}
