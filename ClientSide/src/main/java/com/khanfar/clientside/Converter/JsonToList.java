package com.khanfar.clientside.Converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import com.khanfar.clientside.Exception.NoMorePagesToFetchException;
import com.khanfar.clientside.Request.Request;

import java.io.IOException;

import com.khanfar.clientside.Request.Response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class JsonToList {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final CloseableHttpClient httpClient = HttpClients.createDefault();


    public synchronized  Response convertJsonToList(String jsonString) throws JsonProcessingException {
        List<LinkedHashMap<String, Object>> data = mapper.readValue(jsonString, new TypeReference<List<LinkedHashMap<String, Object>>>() {
        });


        List<LinkedHashMap<String , Object>>metaData = new LinkedList<>();
        int counter = 0 ;
        for (LinkedHashMap<String , Object > objectMap : data ) {
            if (objectMap.containsKey("ColumnName")){
                metaData.add(objectMap);
                counter++;
            }
        }
       for (int i = 0 ; i <counter ; i++) {
           data.remove(metaData.get(i)) ;
       }


        for (LinkedHashMap<String, Object> map : data) {
           List<String> keys = new LinkedList<>(map.keySet());
            for (String key : keys) {
                Object value = map.remove(key);
                map.put(key.trim().toUpperCase(), value);
            }
        }

        return new Response(data , metaData);
    }



    public  synchronized LinkedHashMap<String , Object> convertJsonToHashMap (String jsonString) {
        try {
            LinkedHashMap<String, Object> data = mapper.readValue(jsonString, new TypeReference<LinkedHashMap<String, Object>>() {
            });
            return data;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public  synchronized String sendRequestAndGetResponse(String url,String token , STATUS status, String query ) throws IOException {
        System.out.println("NOT PAGING : ");
// note that the flag this mean to enable a paging or not paging , if flag ==1 : paging and  if equal = 0 that mean no paging ;
        String encodedQuery = URLEncoder.encode(query, String.valueOf(StandardCharsets.UTF_8));
        String json = "{\"query\": \"" + encodedQuery + "\", \"token\": \"" + token + "\"}";



        HttpPost httpPost = new HttpPost(url + "/" + status);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json; charset=UTF-8");

        StringEntity stringEntity = new StringEntity(json, StandardCharsets.UTF_8);
        httpPost.setEntity(stringEntity);


        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {

            if (response.getStatusLine().getStatusCode() != 200) {

                String content = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

                throw new IOException("Unexpected HTTP response status: " + response.getStatusLine().getStatusCode() + " Error : " + content + "\n query : " + query);
            }

            return EntityUtils.toString(response.getEntity());
        } finally {
            response.close();
        }
    }


    public  synchronized String retriveDataWithPaging(String url, String token ,  STATUS status, String query ) throws IOException {

// note that the flag this mean to enable a paging or not paging , if flag ==1 : paging and  if equal = 0 that mean no paging ;

        String encodedQuery = URLEncoder.encode(query, String.valueOf(StandardCharsets.UTF_8));

        String json = "{\n" +
                "  \"query\": \"" + encodedQuery + "\",\n" +
                "  \"pageNumber\": " + SharedPage.getPageNumber() + ",\n" +
                "  \"pageSize\": " + SharedPage.getPageSize() + ",\n" +
                "  \"token\": \"" + token + "\"\n" +
                "}\n";



        HttpPost httpPost = new HttpPost(url + "/" + status);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");


        StringEntity stringEntity = new StringEntity(json, StandardCharsets.UTF_8);
        httpPost.setEntity(stringEntity);

        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {

            if (response.getStatusLine().getStatusCode() != 200) {
                if (response.getStatusLine().getStatusCode() == 408){ // 408 myOwnStatus Code
                    String content = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                    throw new NoMorePagesToFetchException(content);
                }

                String content = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
                throw new IOException("Unexpected HTTP response status: " + response.getStatusLine().getStatusCode() + " Error : " +content+ "\n query : " + query);
            }

            return EntityUtils.toString(response.getEntity());
        } finally {
            response.close();
        }
    }






    public synchronized String sendRequestAndGetResponse(String url, String username, String password) throws IOException {
        Request request = new Request(username, password);

        String query = mapper.writeValueAsString(request);



        HttpPost httpPost = new HttpPost(url + "/" + STATUS.AUTHENTICATE);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        StringEntity stringEntity = new StringEntity(query);

        httpPost.setEntity(stringEntity);

        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {
            if (response.getStatusLine().getStatusCode() != 200) {
                String content = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

                throw new IOException("NOT AUTHUNTIECTAE !!  : Unexpected HTTP response status: " + response.getStatusLine().getStatusCode() + "  Error : " +content+ "\n query : " + query);
            }


            return EntityUtils.toString(response.getEntity());
        } finally {
            response.close();
        }

    }

    public synchronized void sendRequestt(String url ,String testName) throws IOException {
        String query = mapper.writeValueAsString(testName);



        HttpPost httpPost = new HttpPost(url + "/test");
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "text/plain");

        StringEntity stringEntity = new StringEntity(query);

        httpPost.setEntity(stringEntity);

        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new IOException("Unexpected HTTP response status: " + response.getStatusLine().getStatusCode() + "  Error : " + response.getEntity()+ "\n query : " + query);
            }
        } finally {
            response.close();
        }

    }
}
