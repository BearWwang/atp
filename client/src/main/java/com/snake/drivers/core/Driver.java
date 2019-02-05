package com.snake.drivers.core;

import com.snake.drivers.configuration.Configuration;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.WebDriverEventListener;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Driver{


    private static WebDriver driver;
    private static Actions act;
    private static final Configuration configuration = new Configuration();


    public static final Class FIREFOX = FirefoxDriver.class;
    public static final Class CHROME = ChromeDriver.class;
    public static final Class IE = InternetExplorerDriver.class;
    public static final Class EDGE = EdgeDriver.class;


    public Driver(Class<?> clazz) {
        driver = getDriver(clazz);
        act = new Actions(driver);
    }

    /**
     * 获取Driver对象
     * @param clazz 浏览器对象
     * @return 实例化对象
     */
    private static WebDriver getDriver(Class<?> clazz) {
        Object obj;
        try {
//            configuration.init(clazz);
            obj = Class.forName(clazz.getName()).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return (WebDriver) obj;
    }



    /**
     * 打开网页
     *
     * @param url 网页URL
     */
    public void getUrl(String url) {
        driver.get(url);
    }

    /**
     * 强制等待
     *
     * @param sec 等待秒数
     */
    public void wait(int sec) {
        try {
            Thread.sleep(sec * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 切换到frame
     *
     * @param ele frame元素
     */
    public void switchToIframe(WebElement ele) {
        driver.switchTo().frame(ele);
    }

    public void switchToIframe(String fa) {
        driver.switchTo().frame(fa);
    }

    public void switchToIframe(int i) {
        driver.switchTo().frame(i);
    }


    /**
     * 隐式等待方式
     *
     * @param sec 等待秒数
     */
    public void implicitlyWait(int sec) {
        driver.manage().timeouts().implicitlyWait(sec, TimeUnit.SECONDS);
    }

    /**
     * 以导航方式打开url
     *
     * @param url 网页URL
     */
    public void navigateGet(String url) {
        driver.navigate().to(url);
    }

    /**
     * 网页前进
     */
    public void forward() {
        driver.navigate().forward();
    }

    /**
     * 网页后退
     */
    public void back() {
        driver.navigate().back();
    }

    public void refresh() {
        driver.navigate().refresh();
    }

    /**
     * 浏览器最大化
     */
    public void maxSize() {
        driver.manage().window().maximize();
    }

    /**
     * 浏览器关闭
     */
    public void close() {
        driver.close();
    }

    /**
     * 显示等待
     *
     * @param by  定位元素
     * @param sec 等待秒数
     */
    public void driverWait(By by, int sec) {
        try {

            new WebDriverWait(driver, sec).until(ExpectedConditions.presenceOfElementLocated(by));

        } catch (TimeoutException e) {
            throw new TimeoutException(e);
        }
    }

    /**
     * 封装By方法
     *
     * @param method 定位方法
     * @param value  定位值
     * @return By对象
     */
    private By getBy(String method, String value) {
        switch (method) {
            case "id":
                return By.id(value);
            case "name":
                return By.name(value);
            case "tagName":
                return By.tagName(value);
            case "css":
                return By.cssSelector(value);
            case "xpath":
                return By.xpath(value);
            case "linkText":
                return By.linkText(value);
            case "className":
                return By.className(value);
            case "partialLinkText":
                return By.partialLinkText(value);
            default:
                throw new IllegalArgumentException("method " + method + " 不支持");
        }

    }

    /**
     * 根据传递的定位方式等待元素出现(隐式等待)
     *
     * @param method 定位方式
     * @param value  定位值
     * @param sec    等待时间
     */
    public void waitForElement(String method, String value, int sec) {
        driverWait(getBy(method, value), sec);
    }

    /**
     * 定位元素时使用何种方式以及对应的定位表达式
     *
     * @param method 所使用的定位方式
     * @param value  定位表达式
     * @return WebElement对象
     */
    public WebElement find(String method, String value) {
        return driver.findElement(getBy(method, value));
    }

    /**
     * 定位元素时使用何种方式以及对应的定位表达式
     *
     * @param method 所使用的定位方式
     * @param value  定位表达式
     * @return 返回一组WebElement对象
     */
    public List<WebElement> findMore(String method, String value) {
        return driver.findElements(getBy(method, value));
    }

    /**
     * @return 返回driver对象
     */
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * 获取当前窗口句柄
     *
     * @return String
     */
    public String getHandle() {
        return driver.getWindowHandle();
    }

    /**
     * 获取所有窗口句柄
     *
     * @return Set
     */
    public Set<String> getHandles() {
        return driver.getWindowHandles();
    }

    /**
     * 根据索引获取窗口句柄
     *
     * @param index 索引
     * @return String
     */
    public String getHandleByIndex(int index) {
        LinkedList<String> list = new LinkedList<>(this.getHandles());
        return list.get(index);
    }

    /**
     * 获取最后一个窗口句柄
     *
     * @return String 窗口句柄
     */
    public String getLastHandle() {
        return getHandleByIndex(getHandles().size() - 1);
    }

    /**
     * 获取第一个窗口句柄
     *
     * @return String 窗口句柄
     */
    public String getFirstHandle() {
        return getHandleByIndex(0);
    }

    /**
     * 切换句柄
     *
     * @param handle String 句柄
     */
    public void switchWindow(String handle) {
        driver.switchTo().window(handle);
    }

    /**
     * 切换到最后一个窗口
     */
    public void switchToLast() {
        switchWindow(getLastHandle());
    }

    /**
     * 切换到第一个窗口
     */
    public void switchToFirst() {
        switchWindow(getFirstHandle());
    }

    /**
     * 获取当前的URL地址
     *
     * @return String URL地址
     */
    public String getCurrentURL() {
        return driver.getCurrentUrl();
    }

    /**
     * 获取网页源代码
     *
     * @return String 网页源代码
     */
    public String getPageSource() {
        return driver.getPageSource();
    }

    /**
     * 通过名称获取Cookie
     *
     * @param name Cookie名称
     * @return Cookie
     */
    public Cookie getCookie(String name) {
        return driver.manage().getCookieNamed(name);
    }

    /**
     * 获取所有的Cookie
     *
     * @return Set Cookies
     */
    public Set<Cookie> getCookies() {
        return driver.manage().getCookies();
    }

    /**
     * 添加Cokie
     *
     * @param cookie Cookie对象
     */
    public void addCookie(Cookie cookie) {
        driver.manage().addCookie(cookie);
    }

    /**
     * 拖动元素到某个位置
     *
     * @param ele 定位的元素
     * @param x   x坐标
     * @param y   坐标
     */
    public void drap(WebElement ele, int x, int y) {
        act.clickAndHold(ele).moveByOffset(x, y).release().perform();
    }

    /**
     * 双击某个元素
     *
     * @param ele 定位的元素
     */
    public void doubleClick(WebElement ele) {
        act.doubleClick(ele);
    }

    /**
     * 全屏截图保存到当前目录下
     */
    public void screen() {
        screen("");
    }

    /**
     * 全屏截图保存到指定目录下
     *
     * @param path 保存的路径
     */
    public void screen(String path) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");  //转换时间格式
        String time = dateFormat.format(Calendar.getInstance().getTime());  //获取当前时间
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);  //执行屏幕截取
        try {
            FileUtils.copyFile(srcFile, new File(path + "屏幕截图", time + ".png")); //利用FileUtils工具类的copyFile()方法保存getScreenshotAs()返回的文件;"屏幕截图"即时保存截图的文件夹
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对定位的元素进行截图
     *
     * @param ele  定位的元素
     * @param path 保存的路径
     * @throws IOException IO异常
     */
    public void screenForElement(WebElement ele, String path) throws IOException {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        BufferedImage fullImg = null;
        fullImg = ImageIO.read(screenshot);
        //定位该元素的所在的坐标
        Point point = ele.getLocation();
        // 获取元素的长度和宽度
        int eleWidth = ele.getSize().getWidth();
        int eleHeight = ele.getSize().getHeight();
        // 将原来的整张截图根据元素的长宽进行裁剪
        BufferedImage eleScreenshot = fullImg.getSubimage(point.getX(), point.getY(),
                eleWidth, eleHeight);
        ImageIO.write(eleScreenshot, "png", screenshot);
        // 拷贝到截图到某个路径中
        File screenshotLocation = new File(path);
        FileUtils.copyFile(screenshot, screenshotLocation);
    }

    /**
     * 执行JS代码
     *
     * @param script js代码
     */
    public void executor(String script) {
        ((JavascriptExecutor) driver).executeAsyncScript(script);
    }

    /**
     * 通过JS设置元素属性
     *
     * @param locateMethod 定位方式
     * @param tagValue     定位元素值
     * @param style        设置的风格
     * @param styleValue   风格值
     */
    private void setAttr(String locateMethod, String tagValue, String style, String styleValue) {
        // 将单词首字母转为大写
        String res = locateMethod.substring(0, 1).toUpperCase();
        res = res + locateMethod.substring(2);
        StringBuilder builder = new StringBuilder();
        builder.append("document.getElement").append(res).append("(").append(tagValue).append(").style").append(style).append("=\"").append(styleValue).append("\"");
        executor(builder.toString());
    }

    /**
     * 设置元素可见
     *
     * @param method    定位方式
     * @param elementId 值
     */
    public void setElementVisable(String method, String elementId) {
        setAttr(method, elementId, "visibility", "visible");
    }

    /**
     * 设置元素可见
     *
     * @param method    定位方式
     * @param elementId 值
     */
    public void setElementDisplay(String method, String elementId) {
        setAttr(method, elementId, "display", "block");
    }

    /**
     * 向浏览器添加Cookie
     * @param key cookie键
     * @param value cookie值
     */
    public void addCookie(String key,String value){
        Cookie cookie = new Cookie(key,value);
        driver.manage().addCookie(cookie);
    }

    /**
     * 获取所有Cookie
     * @return Set
     */
    public Set<Cookie> getAllCookies(){
        return driver.manage().getCookies();
    }


    /**
     * 通过cookie名称删除cookie
     * @param name cookie名
     */
    public void deleteCookieByName(String name){
        driver.manage().deleteCookieNamed(name);
    }

    /**
     * 传入cookie对象进行删除
     * @param cookie cookie对象
     */
    public void deleteCookieByName(Cookie cookie){
        driver.manage().deleteCookie(cookie);
    }

    /**
     * 删除所有cookie
     */
    public void deleteAllCookies(){
        driver.manage().deleteAllCookies();
    }

    /**
     * 判断cookie是否存在浏览器中
     * @param name cookie名称
     * @return 如果存在返回true否则返回false
     */
    public boolean isContainsCookie(String name){
        for(Cookie cookie: getCookies()){
            if (cookie.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断cookie是否存在浏览器中
     * @param cookies cookie对象
     * @return 如果存在返回true否则返回false
     */
    public boolean containsCookie(Cookie cookies){
        for(Cookie cookie: getCookies()){
            if (cookie.getName().equals(cookies.getName()) && cookie.getDomain().equals(cookies.getDomain()) && cookie.getValue().equals(cookies.getValue())){
                return true;
            }
        }
        return false;
    }

    private class SelectOption{
        private Select select;


        private SelectOption(WebElement ele) {
             select = new Select(ele);

        }

        /**
         * 根据option的name值进行选择
         * @param name name值
         */
        public void selectByName(String name){
            select.selectByVisibleText(name);
        }

        /**
         * 根据option的value值进行选择
         * @param value value值
         */
        public void selectByValue(String value){
            select.selectByValue(value);
        }

        /**
         * 根据索引位置选择
         * @param index 索引位置
         */
        public void selectByIndex(int index){
            select.selectByIndex(index);
        }

        /**
         * 获取所有的option对象
         * @return list
         */
        public List<WebElement> getAllOptions(){
            return select.getOptions();
        }

        /**
         * 获取所有选择的值
         * @return List
         */
        public List<WebElement> getAllSelectedOptions(){
            return select.getAllSelectedOptions();
        }

        /**
         * 获取第一个选择的值
         * @return Webelement
         */
        public WebElement getFirstSelectedOption(){
            return select.getFirstSelectedOption();
        }

        /**
         * 根据索引位置反选
         * @param index 索引位置
         */
        public void deselectByIndex(int index){
            select.deselectByIndex(index);
        }

        /**
         * 根据option的value值进行反选
         * @param value value值
         */
        public void deselectByValue(String value){
            select.deselectByValue(value);
        }

        /**
         * 根据option的name值进行反选
         * @param name name值
         */
        public void deselectByVisibleText(String name){
            select.deselectByVisibleText(name);
        }
    }

    /**
     * 根据定位方式获取选项框对象
     * @param method 定位方式
     * @param value 定位值
     * @return SelectOption
     */
    public SelectOption getOption(String method,String value){
        return new SelectOption(find(method,value));
    }

}
