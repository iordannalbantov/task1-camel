package com.estafet.bankx.camel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Created by estafet on 08/06/16
 */
public class IbanTest {

    private static final String SERVICE_URI = "http://127.0.0.1:20616/estafet/iban/report";

    private class Payload {
        public String[] ibans = {
                "BG66 ESTF 0616 0000 0000 01",
                "BG66 ESTF 0616 0000 0000 02",
                "BG66 ESTF 0616 0000 0000 03"
        };
    }

    //    @Test
//    public void reports() {
//        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();) {
//            HttpPost postRequest = new HttpPost(SERVICE_URI);
//            Payload payload = new Payload();
//
//            Gson gson = new Gson();
//            String json = gson.toJson(payload);
//            HttpEntity entity = new ByteArrayEntity(json.getBytes(StandardCharsets.UTF_8));
//            postRequest.setEntity(entity);
//
//            HttpResponse response = httpClient.execute(postRequest);
//            String result = EntityUtils.toString(response.getEntity());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
