package com.snake.drivers.downloader;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.log4j.Logger;

public class OpenConnection {

    private Logger logger = Logger.getLogger(OpenConnection.class);
    private String dataRange;


    private boolean useRange;

    public OpenConnection() {
        dataRange = String.format("bytes=%d-",0);
        useRange = true;
    }

    public OpenConnection(int start, int end) {
        dataRange = String.format("bytes=%d-%d", start, end);
        useRange = true;
    }

    /**
     * 开启连接
     *
     * @param urlStr url
     * @return HttpURLConnection
     */
    public HttpURLConnection Connection(String urlStr) throws IOException {
        URL url = new URL(urlCheck(urlStr));
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (useRange)
            connection.setRequestProperty("Range", dataRange);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel …) Gecko/20100101 Firefox/64.0");
        connection.connect();
        return connection;
    }


    /**
     * 检查url
     *
     * @param url 传入url
     * @return 检测后url
     */
    private String urlCheck(String url) {
        logger.info("正在检测URL结构有效性");
        if (!checkHTTPHead(url)) {
            if (startWithDot(url)) {
                return "http://www" + url;
            }
            return "http://" + url;
        }
        return url;
    }

    /**
     * 检测url是否是http或者https开头
     *
     * @param url 待检测的url
     * @return 如果是返回true否则返回false
     */
    private boolean checkHTTPHead(String url) {
        return url.substring(0,8).equals("https://") || url.substring(0,7).equals("http://");
    }

    /**
     * 检测时是否为.开头
     *
     * @param url 待检测的url
     * @return 如果是返回true否则返回false
     */
    private boolean startWithDot(String url) {
        return url.startsWith(".");
    }


}
