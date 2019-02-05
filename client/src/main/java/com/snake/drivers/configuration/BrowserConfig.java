package com.snake.drivers.configuration;

import com.google.gson.JsonObject;
import com.snake.drivers.core.Driver;

public class BrowserConfig {

    private String BrowserName;
    private String BrowserVersion;
    private String url;

    /**
     * 根据BrowserConfig字段解析浏览器配置
     * @param data 传入的json对象，json对象由通过服务器传入，由handler传入交给start.ava进行解析
     */
    public BrowserConfig(JsonObject data) {
        BrowserName = data.get("BrowserName").getAsString();    ·+
        BrowserVersion = data.get("BrowserVersion").getAsString();
        url = data.get("index").getAsString();
    }

    /**
     * 获取浏览器名称
     * @return 获取后直接返回对应的Class传入Driver的构造方法
     */
    public Class getBrowserName() {
        if (BrowserName.equalsIgnoreCase("firefox")) {
            return Driver.FIREFOX;
        } else if (BrowserName.equalsIgnoreCase("chrome")) {
            return Driver.CHROME;
        } else if (BrowserName.equalsIgnoreCase("ie")) {
            return Driver.IE;
        } else if (BrowserName.equalsIgnoreCase("edge")) {
            return Driver.EDGE;
        } else
            throw new IllegalArgumentException(BrowserName + " is not allowed");

    }

    /**
     * 获取浏览器版本，通过浏览器版本自动下载对应的driver驱动
     * @return string
     */
    public String getBrowserVersion() {
        return BrowserVersion;
    }

    /**
     * 启动浏览器后显示的首页URL
     * @return string
     */
    public String getUrl() {
        return url;
    }
}
