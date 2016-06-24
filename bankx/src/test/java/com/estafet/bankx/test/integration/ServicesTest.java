package com.estafet.bankx.test.integration;

import com.estafet.bankx.dao.model.Utils;
import com.estafet.bankx.test.core.Resource;
import com.estafet.bankx.camel.pojo.AccountPayload;
import com.estafet.bankx.dao.model.Account;
import com.estafet.bankx.test.core.Transacted;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.estafet.bankx.test.core.Transacted.transaction;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by Yordan Nalbantov.
 */
public class ServicesTest {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    @BeforeClass
    public static void initEntityManager() throws SQLException {
        entityManagerFactory = Persistence.createEntityManagerFactory("bankx");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterClass
    public static void closeEntityManager() throws SQLException {
        entityManager.close();
        entityManagerFactory.close();
    }

    @Before
    public void setUp() throws Exception {
        Resource.baseURI = "integration//";
        RestAssured.baseURI = "http://127.0.0.1:20616/estafet/iban/db/";
        entityManager.clear();
    }

    private static final long WAIT_TIMEOUT = 5000L;

    private static final String JETTY_INSERT_ACCOUNTS_ENDPOINT_URL = "insertAccounts";

    /**
     * Twice challenging the route in order to detect if data is not changing for some reason.
     * Otherwise we could easily miss such a scenario when the past data state is left all the same from the previously
     * ran integration tests.
     * It is not possible to test only the changed flag this way.
     *
     * @throws Exception
     */
    @Test
    public void testRouteJettyInsertAccounts() throws Exception {
        testRouteJettyInsertAccountsChallenge("challenge-01.json", "expected-01.json");
        testRouteJettyInsertAccountsChallenge("challenge-02.json", "expected-02.json");
    }

    private void testRouteJettyInsertAccountsChallenge(String resource, String expectations)
            throws java.io.IOException, InterruptedException {

        String challenge = Resource.resource(resource);
        final AccountPayload accountPayload = Resource.jsonFromString(challenge, AccountPayload.class);
        final List<String> ibans = new ArrayList<>(accountPayload.getData().size());
        for (Account account : accountPayload.getData()) {
            ibans.add(account.getIban());
        }

        String expected = Resource.resource(expectations);
        final AccountPayload expectedAccountPayload = Resource.jsonFromString(expected, AccountPayload.class);

        Response response = given()
                //@formatter:off
                .contentType(ContentType.JSON)
                .body(challenge)
                .when()
                    .post(JETTY_INSERT_ACCOUNTS_ENDPOINT_URL)
                .then()
                    .assertThat()
                        .statusCode(HttpServletResponse.SC_OK)
                    .assertThat()
                        .body(equalTo(""))
                //@formatter:on
                .extract().response();

        Thread.sleep(WAIT_TIMEOUT);

        transaction(entityManager, new Transacted() {
            @Override
            public void action(EntityManager entityManager) {
                List<Account> accounts = Account.allListed(entityManager, ibans);
                for (Account account : expectedAccountPayload.getData()) {
                    for (Account dbState : accounts) {
                        if (Utils.equality(dbState.getIban(), account.getIban())) {
                            Assert.assertEquals(account, dbState);
                        }
                    }
                }
            }
        });
    }

    /**
     * Here we are testing the route with invalid JSON file and expecting to get the proper error response.
     */
    @Test
    public void challengeRouteJettyInsertAccountsWithJibberish() {

        String challenge = Resource.resource("jibberish.txt");

        Response response = given()
                //@formatter:off
                .contentType(ContentType.JSON)
                .body(challenge)
                .when()
                    .post(JETTY_INSERT_ACCOUNTS_ENDPOINT_URL)
                .then()
                    .assertThat()
                        .statusCode(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
                    .assertThat()
                        .body(equalTo("Something went wrong."))
                //@formatter:on
                .extract().response();
    }
}
