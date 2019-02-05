package com.snake.drivers.element;

import com.google.gson.JsonArray;
import com.snake.drivers.core.Driver;
import com.snake.drivers.core.ElementContainer;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author venmosnake
 * 封装后的元素容器类，主要存储容器和使用
 */
public class Container{

    private Map<String, WebElement> map;
    private  Driver driver;

    /**
     * 初始化容器类
     * @param clazz 浏览器
     * @param json 解析的json格式
     */
    public Container(Class clazz, String json) {
        driver = new Driver(clazz);
        map = ElementContainer.container(driver.getDriver(),json);
    }

    /**
     * 初始化容器类
     * @param clazz 浏览器
     * @param json 解析的json格式
     */
    public Container(Class clazz, JsonArray json) {
        driver = new Driver(clazz);
        map = ElementContainer.container(driver.getDriver(),json);
    }


    /**
     * 根据给定的元素名获取元素对象
     * @param name 类名
     * @return WebElement对象
     */
    public WebElement getElement(String name){
        WebElement element =  map.get(name);
        if (element != null)
            return element;
        throw new NoSuchElementException("Name : "+name+" do not exist");
    }

    public Driver getDriver(){
        return driver;
    }
}