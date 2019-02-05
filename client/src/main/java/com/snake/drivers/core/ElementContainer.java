package com.snake.drivers.core;

import com.google.gson.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.snake.drivers.configuration.TestConfig;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

/**
 * @author venmosnake
 * 本类主要负责动态生成元素类
 */
public final class ElementContainer {


    private static final ClassPool classPool = ClassPool.getDefault();
    private static final CtClass ctClass;
    private static final ConstPool constPool;
    private static final Map<String, WebElement> ELEMENT_MAP;
    private static Object obj;


    static {
        //获取类名
        ctClass = classPool.makeClass("com.snake.Element"+TestConfig.getNo());
        constPool = ctClass.getClassFile().getConstPool();
        ELEMENT_MAP = new HashMap<>();

    }

    public static Map<String, WebElement> container(WebDriver driver, String url) {
        JsonParser parse = new JsonParser();
        JsonObject object = (JsonObject) parse.parse(url);
        return container(driver, object);
    }

    public static Map<String, WebElement> container(WebDriver driver, JsonElement object) {
        nullCheck(object);
        init(driver, makeMap(object));
        return ELEMENT_MAP;
    }

    /**
     * 获取元素列表
     *
     * @param baseObject 获取元素
     * @return json Array
     */
    private static JsonArray getElement(JsonObject baseObject) {
        nullCheck(baseObject);
        try {
            return baseObject.get("elements").getAsJsonArray();
        } catch (Exception e) {
            throw new IllegalArgumentException("elements is JsonArray no JsonObject");
        }
    }


    private static Map<String, Map<String, String>> makeMap(JsonElement obj) {
        nullCheck(obj);
        JsonArray array;
        if (obj instanceof JsonArray) {
            array = (JsonArray) obj;
        } else if (obj instanceof JsonObject) {
            array = getElement((JsonObject) obj);
        }else {
            throw new IllegalArgumentException("is not JsonElement");
        }

        Map<String, Map<String, String>> map = new HashMap<>();
        for (int i = 0; i < array.size(); i++) {
            JsonObject object = array.get(i).getAsJsonObject();
            Map<String, String> eleMap = new HashMap<>();
            // 找到定位方式，以及对应的值
            eleMap.put(object.get("method").getAsString(), object.get("value").getAsString());
            map.put(object.get("name").getAsString(), eleMap);
        }
        return map;
    }

    /**
     * 更具config.properties文件读取配置
     *
     * @return 返回配置
     */
    private static String getProperty() {
        Properties properties;
        try {
            //读取config.properties文件
            InputStream is = new FileInputStream("config.properties");
            properties = new Properties();
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties.getProperty("element.className");
    }

    private static void nullCheck(Object o) {
        if (o == null)
            throw new IllegalArgumentException();
    }


    /**
     * 操作字节码通过属性名称动态生成java成员变量
     *
     * @param fieldName 属性名称
     * @param method    定位方式
     * @param attribute 定位元素值
     * @return 返回CtField
     * @throws CannotCompileException 无法编译异常
     */
    private static CtField addField(String fieldName, String method, String attribute) throws CannotCompileException {

        // 将WebElement转为字节码
        CtClass ele = new CtClass("org.openqa.selenium.WebElement") {
        };
        deFrozen();


        // 添加名字为field的成员遍历
        CtField ctField = new CtField(ele, fieldName, ctClass);
        // 将成员变量设置为static
        ctField.setModifiers(Modifier.STATIC);

        // 获取成员变量结构
        FieldInfo fieldInfo = ctField.getFieldInfo();
        // 生成可见的注解对象
        AnnotationsAttribute annotationsAttribute = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        // 获取注解
        Annotation annotation = new Annotation("org.openqa.selenium.support.FindBy", constPool);
        // 生成注解的属性
        MemberValue value = new StringMemberValue(attribute, constPool);

        // 添加注解属性
        annotation.addMemberValue(method, value);
        // 添加注解
        annotationsAttribute.setAnnotation(annotation);

        // 向属性中添加注解
        fieldInfo.addAttribute(annotationsAttribute);

        // 判断是否冻结类，如果冻结进行解冻
        deFrozen();

        ctClass.addField(ctField);
        System.out.println(ctField.getName());
        return ctField;
    }

    /**
     * 生成getter方法
     *
     * @param elenmentNname 元素名称
     * @param ctField       field
     */
    private static void createGetter(String elenmentNname, CtField ctField) {
        CtMethod getter;
        try {
            getter = CtNewMethod.getter(makeGetterStr(elenmentNname), ctField);
            ctClass.addMethod(getter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 类生成有会冻结，判断类是否冻结，如果冻结解冻类
     */
    private static void deFrozen() {
        if (ctClass.isFrozen())
            ctClass.defrost();
    }

    /**
     * 创造空构造方法
     */
    private static void createConstructor() {
        CtConstructor ctConstructor = new CtConstructor(new CtClass[]{}, ctClass);
        try {
            ctConstructor.setBody("{}");
            ctClass.addConstructor(ctConstructor);
        } catch (CannotCompileException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 添加field方法 map对象中存放一个String一个一个Map对象
     * String对象为元素的名称
     * Map对象中存放的定位方式与值
     * 例如 <name, <xpath, //div[@id='kw']>>
     *
     * @param map 元素名，定位方式，定位值集合
     */
    private static void add(Map<String, Map<String, String>> map) {
        nullCheck(map);
        for (String key : map.keySet()) {
            Map<String, String> methodMap = map.get(key);
            for (String method : methodMap.keySet()) {
                try {
                    //首先创建对Field
                    //然后用创建的Field在创建Getter方法
                    createGetter(key, addField(key, method, methodMap.get(method)));
                } catch (CannotCompileException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 初始化
     * 首先循环遍历map生成Field和getter
     * 然后创建空构造器，生成该类
     *
     * @param map
     */
    private static void init(WebDriver driver, Map<String, Map<String, String>> map) {
        nullCheck(map);
        // 添加Field
        add(map);
        // 添加空构造器
        createConstructor();
        Class clazz;
        try {
            // 将生成好的类转为Class对象
            clazz = ctClass.toClass();
            // 实例化该类
            obj = clazz.newInstance();
            // 初始化元素
            PageFactory.initElements(driver, clazz.newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // 初始化所有ELement
        initElement(map);

    }

    /**
     * 初始化容器
     *
     * @param map 容器
     */
    private static void initElement(Map<String, Map<String, String>> map) {
        for (String key : map.keySet()) {
            try {
                Method method = obj.getClass().getDeclaredMethod(makeGetterStr(key));
                ELEMENT_MAP.put(key, (WebElement) method.invoke(obj));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将元素名转为getter名
     * 例如 name 将首字母大写后为Name
     * 然后前面增加get后为getName
     *
     * @param elementName 元素名称
     * @return 返回getter名
     */
    private static String makeGetterStr(String elementName) {
        return "get" + elementName.substring(0, 1).toUpperCase() + elementName.substring(1);
    }
}
