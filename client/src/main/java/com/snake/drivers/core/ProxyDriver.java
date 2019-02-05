package com.snake.drivers.core;

import com.snake.drivers.downloader.FireFoxDriverDownload;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyDriver implements InvocationHandler {

    private Object target = null;
    private FireFoxDriverDownload fireFoxDriverDownload;

    public ProxyDriver(String version) {
        this.fireFoxDriverDownload = new FireFoxDriverDownload(version);;
    }

    public Object bind(Object target){
        this.target = target;
        return Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (!fireFoxDriverDownload.isDownload()){
            fireFoxDriverDownload.startDownload("v0.22");
        }
        return method.invoke(proxy,args);
    }
}
