package com.snake.drivers.downloader;

import com.snake.drivers.Parser.ParserFirefox;
import com.snake.drivers.util.CsvUtil;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class FireFoxDriverDownload implements DriverDownloader {

    private Logger logger = Logger.getLogger(FireFoxDriverDownload.class);
    private static final String url = "https://github.com/mozilla/geckodriver/releases";
    private static final String BIT_32 = "32";
    private static final String BIT_64 = "64";
    private static final String LINUX = "linux";
    private static final String MACOS = "macos";
    private static final String WINDOWS = "windows";
    private static String BASE_DIR;

    private String browserVersion;

    public FireFoxDriverDownload(String browserVersion) {
        logger.info("正在查找版本为" + browserVersion + "的浏览器驱动");
        this.browserVersion = browserVersion;
    }

    public void startDownload(String version) {
        logger.info("正在解析相关资源");
        ParserFirefox parserFirefox = new ParserFirefox(url, version);
        Downloader downloader = new Downloader(parserFirefox.findSystemVersion(getPlatformVersion(), getPlatform()));
        BASE_DIR = downloader.getBaseDri();
        logger.info("正在下载");
        downloader.download();
    }

    public void startDownload() {
        startDownload(getDriverVersion());
    }


    public boolean isDownload() {
        try {
            File file = new File(BASE_DIR);
            for (File list : Objects.requireNonNull(file.listFiles())) {
                return list.isFile() && list.getName().contains("geckodriver");
            }
        } catch (NullPointerException ignore) {
        }
        return false;
    }

    private String getDriverVersion() {
        URL url = FireFoxDriverDownload.class.getClassLoader().getResource("chromeFile.csv");
        try {
            if (url != null) {
                Object[][] data = CsvUtil.readCSV(url.getPath());
                for (Object[] aData : data) {
                    if (((String) aData[1]).contains(browserVersion)) {
                        return (String) aData[0];
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("暂不支持的版本：" + browserVersion);
    }

    public String getPlatform() {
        String plat = System.getProperty("os.name");
        plat = plat.replaceAll(" ", "").toLowerCase();
        if (plat.contains(LINUX)) {
            return LINUX;
        } else if (plat.contains(MACOS)) {
            return MACOS;
        } else if (plat.contains(WINDOWS)) {
            return WINDOWS;
        }
        throw new RuntimeException("平台不支持");
    }

    private String getPlatformVersion() {
        String version = System.getProperty("os.arch");
        try {
            version = version.split("_")[1];
            if (version.contains("86")) {
                version = BIT_32;
            } else if (version.contains(BIT_64)) {
                version = BIT_64;
            }
        } catch (IndexOutOfBoundsException e) {
            version = BIT_32;
        }
        return version;
    }

}
