package com.snake.drivers.configuration;


import com.snake.drivers.util.FileSearcher;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author VenmoSnake
 * 本类主要负责Selenium相关配置
 */
public final class Configuration {

    private static final DesiredCapabilities dc = new DesiredCapabilities();

    /**
     * 获取类名
     * 如org.openqa.selenium.firefox.FirefoxDriver;
     * 转为FireFox
     *
     * @param clazz Driver类名
     * @return 浏览器名
     */
    private static String getName(Class clazz) {
        if (clazz == null)
            throw new NullPointerException();
        String name = clazz.getName();
        return name.substring(name.lastIndexOf("."), name.lastIndexOf("Driver")).replace(".", "");
    }

    /**
     * 初始化并设置属性，首先搜索文件获取driver.exe文件绝对路径
     * 并设置System property
     *
     * @param clazz Driver类
     */
    public void init(Class clazz) {
        FileSearcher fileSearcher = new FileSearcher();
        String browser = getName(clazz);
        String drivers;
        String paths;
        switch (browser) {
            case "Chrome":
                paths = fileSearcher.searchDriver("chromedriver.exe");
                drivers = "webdriver.chrome.driver";
                break;
            case "Firefox":
                drivers = "webdriver.gecko.driver";
                paths = fileSearcher.searchDriver("geckodriver.exe");
                break;
            case "InternetExplorer":
                paths = fileSearcher.searchDriver("IEDriverServer.exe");
                drivers = "webdriver.ie.driver";
                break;
            case "Edge":
                drivers = "webdriver.gecko.driver";
                paths = fileSearcher.searchDriver("MicrosoftWebDriver.exe");
                break;
            default:
                throw new RuntimeException(browser + "不存在");
        }
        System.setProperty(drivers, paths);
    }

    private static Properties getProp() {
        InputStream is;
        Properties prop = null;
        try {
            is = getResource();
            prop = new Properties();
            prop.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }


    /**
     * 通过config.properties文件设置属性，如果文件不存在则抛出FileNotFoundException
     */
    private static void setProerty() {
        InputStream is;
        Properties prop;
        try {
            is = getResource();
            prop = new Properties();
            prop.load(is);
            System.setProperty(prop.getProperty("webdriver.driver"), prop.getProperty("webdriver.path"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取属性值
     *
     * @param property 属性
     * @return 属性值
     */
    public static String getProperty(String property) {
        return getProp().getProperty(property);
    }

    private static InputStream getResource() throws FileNotFoundException {
        return new FileInputStream("config.properties");
    }

}
