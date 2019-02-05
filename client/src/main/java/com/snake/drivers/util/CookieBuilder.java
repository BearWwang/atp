package com.snake.drivers.util;

import org.apache.http.client.CookieStore;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.Date;

public class CookieBuilder {

    private CookieStore cookieStore = new BasicCookieStore();

    private BasicClientCookie clientCookie = null;

    public CookieBuilder create(String cookieName, String cookieValue) {
        clientCookie = new BasicClientCookie(cookieName, cookieValue);
        return this;
    }

    public CookieBuilder setAttribute(String key, String value) {
        switch (key) {
            case "path":
                clientCookie.setPath(value);
                break;
            case "domain":
                clientCookie.setDomain(value);
                break;
            case "comment":
                clientCookie.setComment(value);
                break;
            case "value":
                clientCookie.setValue(value);
                break;
            default:
                clientCookie.setAttribute(key, value);
        }
        return this;
    }

    public CookieBuilder setVersion(int version) {
        clientCookie.setVersion(version);
        return this;
    }

    public CookieBuilder setDate(String key, Date value) {
        if (key.equalsIgnoreCase("createDate"))
            clientCookie.setCreationDate(value);
        else if (key.equalsIgnoreCase("ExpiryDate"))
            clientCookie.setExpiryDate(value);
        return this;
    }

    public CookieStore bulid() {
        cookieStore.addCookie(clientCookie);
        return cookieStore;
    }
}
