package com.snake.drivers.util;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpRequest {
    private CookieStore cookieStore = new BasicCookieStore();
    private RequestConfig config = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
    private CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(config).setDefaultCookieStore(cookieStore).build();
    private String XSRF;


    public JsonObject Post(String url, Map<String, String> map) {
        JsonObject p = null;
        try {
            HttpPost httpPost = new HttpPost(url);

            List<NameValuePair> list = new ArrayList<>();

            for (Map.Entry<String, String> elem : map.entrySet()) {
                list.add(new BasicNameValuePair(elem.getKey(), elem.getValue()));
            }

            httpPost.addHeader("X-Xsrftoken", XSRF);

            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
                httpPost.setEntity(entity);
            }
            CloseableHttpResponse execute = httpClient.execute(httpPost);
            p = new JsonParser().parse(EntityUtils.toString(execute.getEntity())).getAsJsonObject();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }


    public JsonObject Get(String url) {
        JsonObject p = null;
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse execute;
        try {
            execute = httpClient.execute(httpGet);
            p = new JsonParser().parse(EntityUtils.toString(execute.getEntity())).getAsJsonObject();
            XSRF = p.get("message").getAsString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }

    public void upload(String url,String fileName){

        HttpPost post = new HttpPost(url);
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        File file = new File(fileName);

        builder.addBinaryBody("file",file);

        HttpEntity entity = builder.build();

        post.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(post);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200){
                System.out.println("succeed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
                if (response != null)
                    response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}