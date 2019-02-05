package com.snake.drivers.main;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.snake.drivers.util.HttpRequest;
import com.snake.drivers.util.Security;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class SeleniumClient {


    private static volatile Properties property;
    private JsonObject result;
    private Properties properties;


    public static Properties getProperties(){
        synchronized (SeleniumClient.class){
            if (property == null){
                synchronized (SeleniumClient.class){
                    property = new Properties();
                }
            }
        }
        return property;
    }


    SeleniumClient(String url) {
        try {
            this.properties = SeleniumClient.getProperties();
            InputStream is = new FileInputStream(url);
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    void execute() {
        HttpRequest t = new HttpRequest();
        if (t.Get(properties.getProperty("host")).get("status").getAsString().equals("200")){
            System.out.println("测试成功");
            result = t.Post(properties.getProperty("host"), buildParam());
        }
    }

    public JsonObject getResult() {
        return result;
    }

    private Map<String, String> buildParam() {
        Map<String, String> map = new HashMap<>();
        map.put("clientIp", properties.getProperty("ip"));
        map.put("uuid", properties.getProperty("key"));
        map.put("user", properties.getProperty("user"));
        map.put("key", Security.getSha1(properties.getProperty("key"), properties.getProperty("user")));
        return map;
    }

}
