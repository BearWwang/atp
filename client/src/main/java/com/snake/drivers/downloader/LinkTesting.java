package com.snake.drivers.downloader;


import com.snake.drivers.util.Listenable;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * 被观察者，如果有错误则通知观察者
 */
public class LinkTesting extends Listenable {

    private Logger logger = Logger.getLogger(LinkTesting.class);

    private static final int SUCCEED = 200;
    private static final int FAILED = 400;
    private static final int INTETNAL_ERROR = 500;
    private static final int REDICT = 300;
    private boolean testSuccess = false;
    private int code;
    private OpenConnection connection;

    public LinkTesting() {
        this.connection = new OpenConnection();
    }

    public void testing(String url) {
        logger.info("正在测试链接有效性");
        setChanged();
        HttpURLConnection con = null;
        try {
            con = connection.Connection(url);
            code = con.getResponseCode();
            if (isSucceed()) {
                testSuccess = true;
                logger.info("URL" + url + "测试通过");
            } else
                notifyListener("测试失败状态码为" + code);
        } catch (IOException e) {
            notifyListener(e);
            return;
        } finally {
            if (con != null)
                con.disconnect();
        }
        notifyListener(testSuccess);
    }


    private boolean isSucceed() {
        return SUCCEED <= code && code < REDICT;
    }

}
