package com.snake.drivers.downloader;

import com.snake.drivers.util.Listenable;
import com.snake.drivers.util.Listener;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 下载基本步骤
 * 1.测试链接(LinkTesting.java)
 * 解析URL
 * 打开链接
 * 测试状态码
 * 测试文件是否可下载
 * 2.下载文件(DownLoader.java)
 * 获取文件大小
 * 将文件拆分为几个部分
 * 创建临时文件，并记录下载字节数(断点续传)
 * 创建多线程池(ThreadPool.java)，每个线程下载一个部分文件进行标号
 * 3.整合文件(Integration.java)
 * 将下载好的临时文件进行整合
 * 删除临时文件
 */
public class Downloader implements Listener {

    private Logger logger = Logger.getLogger(Downloader.class);

    private static String BASE_DRI;
    private boolean succeed;
    private LinkTesting testing;
    private String url;
    private static final int BUFFER_SIZE = 1024;
    private BufferedInputStream bis;
    private RandomAccessFile file = null;
    private int fileSize;
    private int downloadSize;
    private double process;
    private HttpURLConnection connection;

    public static void setBaseDri(String baseDri) {
        BASE_DRI = baseDri;
    }

    public double getProcess() {
        return process;
    }

    /**
     * 构造器用于初始化相关类
     *
     * @param url URL地址
     */
    public Downloader(String url) {
        testing = new LinkTesting();
        // 添加观察者
        testing.addListener(this);

        this.url = url;
        try {
            connection = new OpenConnection().Connection(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (BASE_DRI == null) {
            BASE_DRI = "./";
        }
    }

    /**
     * 获取下载文件的名称
     *
     * @return String
     */
    private String getFileName() {
        URL url;
        try {
            url = new URL(this.url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String name = url.getFile();
        return name.substring(name.lastIndexOf('/') + 1);
    }

    /**
     * 进行初始化操作
     */
    private void init() {
        logger.info("正在链接URL: " + url);
        testing.testing(url);
        if (!succeed) {
            logger.info("测试失败");
            return;
        }

        try {
            // 获取ContentLength
            fileSize = connection.getContentLength();
            bis = new BufferedInputStream(connection.getInputStream(), BUFFER_SIZE);
            String path = BASE_DRI + getFileName();
            file = new RandomAccessFile(path, "rw");
            file.setLength(fileSize);
            file.seek(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载主方法
     */
    public void download() {
        // 初始化操作
        init();
        logger.info("下载大小为" + fileSize / 1024 / 1024 + "MB");
        while (downloadSize < fileSize) {
            byte[] buffer;
            // 判断下载的内容是否超出最大缓存
            if ((fileSize - downloadSize) > BUFFER_SIZE) {
                // 如果超出则使用最大缓存容量
                buffer = new byte[BUFFER_SIZE];
            } else {
                // 如果没有超出则表示即将下载完毕，利用文件大小-已下载的文件大小
                buffer = new byte[fileSize - downloadSize];
            }
            int read;
            try {
                // 每次读取的字节长度
                read = file.read(buffer);
                // 进行写入
                file.write(buffer, 0, read);
                downloadSize += read;
                process = downloadSize * 1.0 / fileSize * 10000 / 100;
                logger.info(String.format("已下载 .2%f", process));
                // 如果已经到文件结尾则停止下载
                if (process == 100) {
                    close();
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
                logger.info(e.getMessage());
                break;
            }
        }
        close();

    }

    private void close() {
        try {
            file.close();
            bis.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Listenable o, Object arg) {
        if (arg instanceof Exception) {
            Exception e = (Exception) arg;
            logger.error(e.getMessage());
        } else if (arg instanceof Boolean) {
            succeed = (Boolean) arg;
        } else if (arg instanceof String) {
            logger.info(arg);
        }
    }

    public String getBaseDri() {
        return BASE_DRI;
    }
}
