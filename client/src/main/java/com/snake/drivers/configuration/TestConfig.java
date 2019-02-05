package com.snake.drivers.configuration;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.snake.drivers.element.Container;
import com.snake.drivers.exception.NoInitDataException;
import javassist.*;
import javassist.bytecode.AccessFlag;


import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

public class TestConfig {

    private CtClass cc;
    private BrowserConfig bc;
    private Class target;
    private ParserJson parser;
    private boolean needFile = false;
    private static ArrayList<Integer> arrayList;
    private static volatile TestConfig config;
    private static boolean isInit  = false;

    static {
        arrayList = new ArrayList<>();
    }

    public static int getNo() {
        Random random = new Random();
        int num = random.nextInt();
        arrayList.add(num);
        return num;
    }

    private TestConfig(JsonObject object) {
        parser = new ParserJson(object);
        bc = new BrowserConfig(parser.getBrowserConfig());
        this.cc = ClassPool.getDefault().makeClass("com.snake.TestNo" + getNo());

    }

    public BrowserConfig getBc() {
        return bc;
    }

    /**
     * 将动态生成的类转化为文件
     */
    public void NeedFile() {
        this.needFile = true;
    }

    /**
     * 添加TestNg中的方法
     *
     * @param annotationName 注解名称
     * @param body           方法中的内容
     * @param methodName     方法名
     */
    private void addMethod(String annotationName, String body, String methodName) {

        CtMethod method = Cases.buildMethod(methodName, null, cc);
        Cases.addAnnotation(cc.getClassFile().getConstPool(), method.getMethodInfo(), annotationName);
        try {
            method.setBody(body);
            cc.addMethod(method);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据TestConfig中的内容决定是否需要生成对应的方法
     */
    private void config(String caseName) {
        JsonObject array = parser.getTestConfig();
        addField("com.snake.drivers.element.Container", "container");
        if (array.get("BeforeClass").getAsBoolean()) {
            addMethod("org.testng.annotations.BeforeClass", buildBeforeClass(), "BeforeClass" + caseName);
        }
        if (array.get("AfterClass").getAsBoolean()) {
            addMethod("org.testng.annotations.AfterClass", buildAfterClass(), "AfterClass" + caseName);
        }
        if (array.get("BeforeTest").getAsBoolean()) {
            addMethod("org.testng.annotations.BeforeTest", buildBeforeTest(), "BeforeTest" + caseName);
        }
        if (array.get("AfterTest").getAsBoolean()) {
            addMethod("org.testng.annotations.AfterTest", buildAfterTest(), "AfterTest" + caseName);
        }
        if (array.get("DataProvider").getAsBoolean()) {
            addMethod("org.testng.annotations.DataProvider", buildDataProvider(), caseName + "Provider");
        }
    }

    /**
     * 单利模式，用于TestCase类中的，BeforeClass
     * @param data
     * @return
     */
    public static TestConfig getInstance(JsonObject data) {
        synchronized (TestConfig.class){
            if (config == null){
                synchronized (TestConfig.class){
                    isInit = true;
                    config = new TestConfig(data);
                }
            }
        }
        return config;
    }


    public static TestConfig getInstance(){
        if (isInit)
            return config;
        throw new NoInitDataException("TestConfig is not init");
    }



    /**
     * 创建BeforeClass中内容
     *
     * @return
     */
    private String buildBeforeClass() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("$0.container = com.snake.drivers.configuration.TestConfig.getInstance().getContainer();");
        builder.append("$0.container.getDriver().getUrl(\"").append(bc.getUrl()).append("\");");
        builder.append("}");
        return builder.toString();
    }

    private String buildBeforeTest() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("}");
        return builder.toString();

    }

    private String buildAfterClass() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("}");
        return builder.toString();

    }

    private String buildAfterTest() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("container.getDriver().close();");
        builder.append("}");
        return builder.toString();

    }

    private String buildDataProvider() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append("}");
        return builder.toString();
    }

    /**
     * 添加成员变量
     *
     * @param className 类型名称
     * @param fieldName 元素名称
     */
    private void addField(String className, String fieldName) {
        try {
            CtField field = new CtField(new CtClass(className) {
            }, fieldName, cc);
            deFrozen(cc);
            field.getFieldInfo().setAccessFlags(AccessFlag.PRIVATE);
            cc.addField(field);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }

    }

    /**
     * 类生成有会冻结，判断类是否冻结，如果冻结解冻类
     */
    static void deFrozen(CtClass cc) {
        if (cc.isFrozen())
            cc.defrost();
    }

    /**
     * 类生成有会冻结，判断类是否冻结，如果冻结解冻类
     */
    void deFrozen() {
        deFrozen(cc);
    }


    private void initValue(Object object) {
        try {
            Method method = target.getMethod("setContainer", Class.class, JsonArray.class);
            method.setAccessible(true);
            method.invoke(object, bc.getBrowserName(), parser.getElements());
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    private void makeSetter() {
        CtClass[] c = {new CtClass("java.lang.Class") {
        }, new CtClass("com.google.gson.JsonArray") {
        }};
        CtMethod method = Cases.buildMethod("setContainer", c, cc);
        try {
            method.setBody("{$0.container=new com.snake.drivers.element.Container($1,$2);}");
            cc.addMethod(method);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成最终的Test类
     *
     * @throws CannotCompileException 编译错误
     */
    public void makeTestClass() throws CannotCompileException {
        JsonArray array = parser.getCases();
        for (int i = 0; i < array.size(); i++) {
            config(array.get(i).getAsJsonObject().get("functionName").getAsString());

            //TODO:
            new Cases(array.get(i).getAsJsonObject(), cc).makeAction().makeAssert("1", "1").build(cc.getClassFile().getConstPool());
        }

        if (needFile) {
            try {
                cc.writeFile();
            } catch (IOException | NotFoundException e) {
                e.printStackTrace();
            }
        }
        target = cc.toClass();
    }

    /**
     * 将测试类实例化
     *
     * @return Object
     */
    public Object getTargetInstance() {
        Object c = null;
        try {
            if (target == null) {
                makeTestClass();
                deFrozen();
            }
            c = target.newInstance();
        } catch (IllegalAccessException | InstantiationException | CannotCompileException e) {
            e.printStackTrace();
        }
        return c;
    }

    public Container getContainer() {
        return new Container(bc.getBrowserName(), parser.getElements());
    }


    public Class getTarget() {
        return target;
    }

    /**
     * 创建最终的类
     */
    public void build() {
        try {
            if (target == null) {
                makeTestClass();
                deFrozen();
            }
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }

}
