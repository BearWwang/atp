package com.snake.drivers.main;

import com.google.gson.JsonObject;
import com.snake.drivers.configuration.TestConfig;
import com.snake.drivers.downloader.FireFoxDriverDownload;
import com.snake.drivers.util.ExtentTestNGIReporterListener;
import org.testng.TestNG;


public class Start {

    private TestConfig config;
    private boolean needDownloadDriver = false;


    Start(JsonObject data) {
        config = TestConfig.getInstance(data);
    }


    public void NeedDownloadDriver() {
        SeleniumClient.getProperties().getProperty("key");
        this.needDownloadDriver = true;
    }


    /**
     * 
     */
    void start() {
        if (needDownloadDriver) {
            new FireFoxDriverDownload(config.getBc().getBrowserVersion()).startDownload();
        }
        config.build();
        TestNG testNG = new TestNG();
        testNG.setTestClasses(new Class[]{config.getTarget()});
        testNG.addListener(new ExtentTestNGIReporterListener());
//        testNG.setListenerClasses(Collections.singletonList(ExtentTestNGIReporterListener.class));
        testNG.addClassLoader(config.getTargetInstance().getClass().getClassLoader());
        testNG.run();
    }


}