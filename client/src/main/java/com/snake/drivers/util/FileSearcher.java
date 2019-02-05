package com.snake.drivers.util;

import java.io.File;
import java.net.URL;
import java.nio.file.NoSuchFileException;

/**
 * @author VenmoSnake
 * 本类主要负责WebDriver 驱动文件查找
 */
public class FileSearcher {


    /**
     * 获取driver文件
     *
     * @return driver文件绝对路径
     */
    public String getDriverPath() {
        return getPath("driver");
    }

    /**
     * 获取测试失败截图文件夹路径
     *
     * @return images文件绝对路径
     */
    private String getImagePath() {
        return getPath("images");
    }


    /**
     * 获取文件绝对路径
     *
     * @param filename 文件夹名称
     * @return 绝对路径
     */
    private String getPath(String filename) {
        String path = null;
        URL url = Thread.currentThread().getContextClassLoader().getResource("");
        File file = null;
        if (url != null) {
            try {
                file = new File(url.getPath());
                path = search(file, filename);
            } catch (NoSuchFileException e) {
                e.printStackTrace();
            }
        }
        if (path == null)
            throw new RuntimeException("目录" + filename + "不存在");
        return path;
    }


    /**
     * 搜索文件
     *
     * @param file     file对象一般设置为目录
     * @param filename 查找的文件名
     * @return 文件绝对路径
     * @throws NoSuchFileException 如果没有找到报此异常
     */
    public String search(File file, String filename) throws NoSuchFileException {
        File[] files = file.listFiles();
        if (files == null)
            return null;
        for (File file1 : files) {
            if (file1.isFile())
                return null;
            else
                search(file1, filename);
            if (file1.getName().equals(filename)) {
                return file1.getAbsolutePath();
            }
        }
        throw new NoSuchFileException("文件 " + filename + "不存在");
    }

    /**
     * 查找浏览器驱动
     *
     * @param driver driver名称
     * @return 返回文件绝对路径
     */
    public String searchDriver(String driver) {
        File[] files = new File(getDriverPath()).listFiles();
        if (files == null)
            throw new RuntimeException("目录driver不存在");
        for (File file : files) {
            if (file.getName().equals(driver))
                return file.getAbsolutePath();
        }
        throw new RuntimeException("文件 " + driver + " 不存在");
    }


}
