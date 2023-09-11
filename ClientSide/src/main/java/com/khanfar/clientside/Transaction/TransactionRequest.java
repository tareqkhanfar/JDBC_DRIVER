package com.khanfar.clientside.Transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TransactionRequest {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();


    public static String StartEndTransactionViaHttp(String url , String token , TransactionEnum transactionEnum) throws IOException {
        HttpPost httpPost ;
              String json = "{\"token\": \"" + token + "\"}";

       
        httpPost = new HttpPost(url +"/transaction/"+transactionEnum);

        System.out.println("URL : " + url +"/transaction/"+transactionEnum);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json; charset=UTF-8");
        
        StringEntity stringEntity = new StringEntity(json);
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {

            if (response.getStatusLine().getStatusCode() != 200) {

                String content = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

                throw new IOException("Unexpected HTTP response status: " + response.getStatusLine().getStatusCode() + " Error : " + content);
            }

            return EntityUtils.toString(response.getEntity());
        } finally {
            response.close();
        }
    }


    public static boolean TransactionHttp(String url,  String token ,  TransactionEnum status) throws IOException {

        String tokenTransaction = URLEncoder.encode(token, String.valueOf(StandardCharsets.UTF_8));

        String json = "{\"token\": \"" + tokenTransaction + "\"}";

        HttpPost httpPost = new HttpPost(url + "/transaction/" + status);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json; charset=UTF-8");

        StringEntity stringEntity = new StringEntity(json, StandardCharsets.UTF_8);
        httpPost.setEntity(stringEntity);


        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {

            if (response.getStatusLine().getStatusCode() != 200) {

                String content = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

                throw new IOException("Unexpected HTTP response status: " + response.getStatusLine().getStatusCode() + " Error : " + content);
            }

            return EntityUtils.toString(response.getEntity()) != null ;
        } finally {
            response.close();
        }

    }
}
