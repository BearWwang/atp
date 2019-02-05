package com.snake.drivers.configuration;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.Annotation;

import java.text.MessageFormat;

/**
 * 根据每个Testcase生成一个TestNg的@Test方法
 */
public class Cases {
    private StringBuilder actions;
    private JsonObject data;
    private CtMethod cases;
    private CtClass cc;

    Cases(JsonObject data, CtClass cc) {
        this.data = data;
        this.cc = cc;
        actions = new StringBuilder();
        cases = buildMethod(data.get("functionName").getAsString(), makeArgs(data.get("functionArgType")), cc);
        cases.setModifiers(Modifier.PUBLIC);
        actions.append("{");
    }


    /**
     * 动态生成一个方法
     *
     * @param methodName 方法名
     * @param parameters 方法参数
     * @param cc         所属的类
     * @return 创建好的方法
     */
    static CtMethod buildMethod(String methodName, CtClass[] parameters, CtClass cc) {
        TestConfig.deFrozen(cc);
        return new CtMethod(CtClass.voidType, methodName, parameters, cc);
    }

    /**
     * 为方法创建注解
     *
     * @param pool           pool
     * @param info           方法信息类
     * @param annotationName 添加的注解名称
     */
    static void addAnnotation(ConstPool pool, MethodInfo info, String annotationName) {
        AnnotationsAttribute aa = new AnnotationsAttribute(pool, AnnotationsAttribute.visibleTag);
        Annotation annotation = new Annotation(annotationName, pool);
        aa.addAnnotation(annotation);
        info.addAttribute(aa);

    }


    /**
     * 创建方法后添加方法内容最后创建方法并且添加到相应的类中
     *
     * @param pool           pool
     * @param annotationName 注解名
     */
    void build(ConstPool pool, String annotationName) {
        try {
            addAnnotation(pool, cases.getMethodInfo(), annotationName);
            // 获取方法信息,添加方法注解
            actions.append("}");
            cases.setBody(actions.toString());
            cc.addMethod(cases);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用另一个创建
     *
     * @param pool pool
     */
    void build(ConstPool pool) {
        this.build(pool, "org.testng.annotations.Test");
    }

    /**
     * 根据Json中的数组内容生成所需的方法参数数组
     *
     * @param args 方法参数Json
     * @return 参数数组
     */
    private static CtClass[] makeArgs(JsonElement args) {
        JsonArray arr;
        if (args == null || args.getAsString().equals(""))
            return new CtClass[]{};
        else
            arr = args.getAsJsonArray();

        CtClass[] arg = new CtClass[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            String argType = arr.get(i).getAsString();
            switch (argType) {
                case "int":
                    arg[i] = CtClass.intType;
                    break;
                case "double":
                    arg[i] = CtClass.doubleType;
                    break;
                default:
            }
        }
        return arg;
    }


    /**
     * 创建测试流程
     *
     * @return 建造者模式
     */
    Cases makeAction() {
        JsonArray array = data.get("action").getAsJsonArray();
        for (int i = 0; i < array.size(); i++) {
            ElementAction action = new ElementAction().setAction(array.get(i).getAsJsonObject());
            actions.append(action.perform() != null ? action.perform() : "");
        }
        return this;
    }

    /**
     * 添加断言方式
     *
     * @param a 预期结果
     * @param b 实际结果
     * @return 建造者模式
     */
    Cases makeAssert(Object a, Object b) {
        String type = data.get("assertType").getAsString();
        if (type.equals("equal")) {
            actions.append(MessageFormat.format("org.testng.Assert.assertEquals({0},{1});", a, b));
        } else if (type.equals("false")) {
            actions.append(MessageFormat.format("org.testng.Assert.assertFalse({0});", a));
        }
        return this;
    }

}
