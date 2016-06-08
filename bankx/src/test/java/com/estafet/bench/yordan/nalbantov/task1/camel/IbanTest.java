package com.estafet.bench.yordan.nalbantov.task1.camel;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by estafet on 08/06/16.
 */
public class IbanTest {

    @Test
    public void reports() {

        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost postRequest = new HttpPost("jetty:http://localhost:20616/estafet/iban/report");
            httpClient.execute(postRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
