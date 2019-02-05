package com.snake.drivers.main;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.snake.drivers.util.ToJson;

import java.io.UnsupportedEncodingException;

/**
 * 命令的用法
 * >>> java -jar driver.jar
 * 将会显示一下输出
 * <p>
 * *****************************************
 * **** 命令：
 * ****      -f: 使用本地的json文件进行测试
 * ****      -c: 使用本地的config文件
 * ****      -help: 帮助菜单
 * ****************************************
 * <p>
 * >>> java -jar driver.jar -c [文件名]
 * 将根据指定的配置文件运行程序
 * >>> java -jar driver.jar -f 使用本地json文件执行测试
 */

public class Command {

    private static final String HOST = "h";
    private static final String PORT = "p";
    private static final String HELP = "help";
    private static final String FILE = "f";
    private static final String CONFIG = "c";

    private boolean hasFile = false;
    private SeleniumClient client;


    private String[] args;

    Command(String[] args) {
        this.args = args;
    }

    private void showHelp() {
        System.out.println("*****************************************");
        System.out.println("**** 命令：");
        System.out.println("****      -f: 使用本地的json文件进行测试");
        System.out.println("****      -c: 使用本地的config文件");
        System.out.println("****      -help: 帮助菜单");
        System.out.println("*****************************************");
    }


    private void StartWithJsonFile(String path) {
        if (!path.endsWith(".json")) {
            System.out.println("文件必须为json格式");
            System.exit(1);
        }
        try {
            JsonParser parser = new JsonParser();
            JsonObject data = parser.parse(ToJson.FileToJson(path)).getAsJsonObject();
            Start start = new Start(data);
            start.start();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private void ConfigFile(String path) {
        if (!path.endsWith(".properties")) {
            System.out.println("请检查是否为.properties文件");
            System.exit(1);
        }
        hasFile = true;
        client = new SeleniumClient(path);
    }


    /**
     * 解析命令
     * @param cmd 命令
     * @param args 参数
     */
    private void ParserCmd(String cmd, String args) {
        cmd = cmd.replaceFirst("-", "");
        switch (cmd) {
            case FILE:
                StartWithJsonFile(args);
                break;
            case CONFIG:
                ConfigFile(args);
                break;
            case HELP:
                showHelp();
            default:
                showHelp();
        }
    }

    private void ShowFileHelp() {
        System.out.println("***     命令-f用法");
        System.out.println("***     -f [json文件]使用指定路径文件 或者");
        System.out.println("***     -f 使用当前路径的conf.json");

    }

    private void ShowConfigHelp() {
        System.out.println("***     命令-c用法");
        System.out.println("***     -c [properties文件]使用指定路径文件 或者");
        System.out.println("***     -c 使用当前路径的config.properties文件");
    }


    private void CmdHelp(String cmd) {
        cmd = cmd.replaceFirst("-", "");// 去除命令前的 '-'
        switch (cmd) {
            case FILE:
                ShowFileHelp();
            case CONFIG:
                ShowConfigHelp();
            case HELP:
                showHelp();
                ;
            default:
                showHelp();
        }
    }

    /**
     * 解析命令
     * 如果没有指定文件名该方法会尝试获取当前jar包的路径下一个config.properties文件，否则将执行用户指定的文件
     */

    void Parser() {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        try {
            path = java.net.URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        if (args.length == 0) {
            showHelp();
            return;
        }
        if (args.length >= 2)
            for (int i = 0; i < args.length; i += 2) {
                if (this.args[i].startsWith("-") && !args[i + 1].startsWith("-")) { // 如果当前字符以-开始 表示命令，如果下一个参数不是-开头表示其参数
                    ParserCmd(args[i], args[i + 1]);
                } else if (this.args[i].startsWith("-") && args[i + 1].startsWith("-")) { // 如果当前字符以-开始，下一字符也是-表示后面还有命令
                    StringBuffer buffer = new StringBuffer();
                    for (int j = 0; j < i + 1 + 4; j++) { // 读取4个字符
                        buffer.append(args[i]);
                    }
                    if (buffer.toString().equals("help")) // 如果读到的字符为help则解析help相关命令
                        CmdHelp(args[i]);
                }
            }
        if (!hasFile && client == null) {
            path = path.substring(0, path.lastIndexOf('/')) + "/";
            client = new SeleniumClient(path + "config.properties");
            client.execute();
        } else {
            System.out.println("请检查当前路径是否该文件");
        }
    }

}
