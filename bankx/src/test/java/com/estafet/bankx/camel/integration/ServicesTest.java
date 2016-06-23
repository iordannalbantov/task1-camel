package com.estafet.bankx.camel.integration;

import com.estafet.bankx.camel.Utils;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import org.junit.Test;

import javax.servlet.http.HttpServletResponse;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by Yordan Nalbantov.
 */
public class ServicesTest {

    private static final long WAIT_TIMEOUT = 5000L;

    @Test
    public void testJettyDbInsertServiceEndpoint() throws Exception {

//        Properties properties = new Properties();
//        InputStream inputStream = ServicesTest.class.getResourceAsStream("integration.properties");
//        properties.load(inputStream);

        String jettyDbInsertServiceEndpointUrl = "http://127.0.0.1:20616/estafet/iban/db/insert";

        // Prepare test data.

        String challenge = Utils.resource("payload//integration//db//insert//challenge.json");

        // Prepare test scenario.

        // Challenge the route.

        Response response = given()
                .contentType(ContentType.JSON)
                .body(challenge)
                .when()
                .post(jettyDbInsertServiceEndpointUrl)
                .then()
                .assertThat()
                .statusCode(HttpServletResponse.SC_OK)
                .assertThat()
                .body(equalTo(""))
                .extract().response();

        Thread.sleep(WAIT_TIMEOUT);

        // Assert results.


    }
}
