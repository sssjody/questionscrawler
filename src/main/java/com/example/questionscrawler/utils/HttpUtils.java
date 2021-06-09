package com.example.questionscrawler.utils;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public class HttpUtils {

    private static PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

    public static String doGetHtml(String url){
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        HttpGet request = new HttpGet(url);
        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(request);
            if(response.getStatusLine().getStatusCode()==200){
                return EntityUtils.toString(response.getEntity(),"gbk");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
