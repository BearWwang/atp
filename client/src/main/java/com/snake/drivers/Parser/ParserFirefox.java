package com.snake.drivers.Parser;

import com.snake.drivers.downloader.OpenConnection;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserFirefox {

    private Logger logger = Logger.getLogger(ParserFirefox.class);
    private String url;
    private List<String> DownloadUrl;
    private String version;

    /**
     * @param url            baseURL
     * @param broswerVersion 浏览器版本
     */
    public ParserFirefox(String url, String broswerVersion) {
        this.url = url;
        version = broswerVersion;
        DownloadUrl = new ArrayList<>();
        parser();
    }

    /**
     * 解析方法
     */
    private void parser() {
        BufferedReader br;
        try {
            HttpURLConnection connection = new OpenConnection().Connection(url);
            br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            logger.info("正在查找相关资源");
            while ((line = br.readLine()) != null) {
                if (line.contains(version))
                    addPlatform(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 解析平台
     *
     * @param str
     */
    private void addPlatform(String str) {
        Pattern platformPattern = Pattern.compile("<a .+?class=\"d-flex flex-items-center\">");
        Matcher matcher = platformPattern.matcher(str);
        if (matcher.find()) {
            getUrl(matcher.group());
        }
    }

    private void getUrl(String str) {
        Pattern urlPattern = Pattern.compile("href=\".+?download.+?\"");
        Matcher matcher = urlPattern.matcher(str);
        if (matcher.find()) {
            String res = matcher.group();
            res = res.replaceAll("href=\"", "");
            res = "https://github.com" + res.replaceAll("\"", "");
            if (res.contains(version)) {
                DownloadUrl.add(res);
            }
        }
    }

    public List<String> getDownloadUrlList() {
        return DownloadUrl;
    }

    /**
     * @param system 平台版本
     * @param bit    系统位数
     * @return
     */
    public String findSystemVersion(String system, String bit) {
        logger.info("正在查找" + bit + "位" + system + "系统");
        logger.info("共找到" + DownloadUrl.size() + "个驱动");

        if (DownloadUrl.size() > 0) {
            for (String str : DownloadUrl) {
                if (str.contains("macos")) {
                   return str;
                } else if (str.contains(system) && str.contains(bit)) {
                    return str;
                }
            }
        }
        throw new RuntimeException(bit + "位" +system+ "系统没有驱动");
    }
}
