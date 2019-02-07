package com.snake.drivers.main;

import com.google.gson.JsonObject;
import com.snake.drivers.configuration.TestConfig;
import com.snake.drivers.downloader.ChromeDownload;
import com.snake.drivers.downloader.DriverDownloader;
import com.snake.drivers.downloader.FireFoxDriverDownload;
import com.snake.drivers.util.ExtentTestNGIReporterListener;
import org.testng.TestNG;

import javax.naming.directory.NoSuchAttributeException;


class Start {

    private TestConfig config;
    private boolean needDownloadDriver;
    private DriverDownloader downloader;


    Start(JsonObject data) {
        config = TestConfig.getInstance(data);
        needDownloadDriver = convertToBoolean(SeleniumClient.getProperties().getProperty("needDownload"));
        try {
            downloader = witchBrowser();
        } catch (NoSuchAttributeException e) {
            e.printStackTrace();
        }
    }

    private boolean convertToBoolean(String data) {
        return data.equalsIgnoreCase("true") || data.equals("1") || data.equalsIgnoreCase("yes") || data.equalsIgnoreCase("t");
    }


    private DriverDownloader witchBrowser() throws NoSuchAttributeException {
        String name = config.getBc().getBrowserName().getName();
        if (name.equalsIgnoreCase("firefox"))
            return new FireFoxDriverDownload(config.getBc().getBrowserVersion());
        else if (name.equalsIgnoreCase("chrome"))
            return new ChromeDownload(config.getBc().getBrowserVersion());

        throw new NoSuchAttributeException();

    }


    /**
     *
     */
    void start() {

        if (needDownloadDriver) {
            downloader.startDownload();
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