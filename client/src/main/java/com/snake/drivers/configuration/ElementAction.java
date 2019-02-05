package com.snake.drivers.configuration;

import com.google.gson.JsonObject;

import java.text.MessageFormat;
import java.util.NoSuchElementException;

/**
 * 本类封装了元素执行动作的相关操作
 */
public class ElementAction {

    private String eleName;
    private String eleMethod;
    private String sendValue;
    private String selectMethod;
    private String selectBy;
    private String index;


    /**
     * 使用时传入对应的JsonObject进行解析
     * @param data 解析键为action的JsonArray中的每一个JsonObject
     * @return 采用建造者模式
     */
    public ElementAction setAction(JsonObject data) {
        if (data == null) {
            throw new IllegalArgumentException("JsonData is empty");
        }
        this.eleName = data.get("elementName").getAsString();
        this.eleMethod = data.get("elementMethod").getAsString();
        this.sendValue = data.get("sendValue") != null ? data.get("sendValue").getAsString() : null;
        this.selectMethod = data.get("selectMethod") != null ? data.get("selectMethod").getAsString() : null;
        this.selectBy = data.get("selectBy") != null ? data.get("selectBy").getAsString() : null;
        this.index = data.get("index") != null ? data.get("index").getAsString() : null;
        return this;
    }

    /**
     * 最后的生成方法，根据所传递的内容生成相应的流程然后写入方法中
     * @return String
     */
     String perform() {
        StringBuilder builder = new StringBuilder();

        switch (eleMethod) {
            case "sendKeys":
                builder.append("container.getElement(\"").append(eleName).append("\").sendKeys(\"").append(sendValue).append("\");");
            case "click":
                builder.append("container.getElement(\"").append(eleName).append("\").click();");
                break;
            case "clear":
                builder.append("container.getElement(\"").append(eleName).append("\").clear();");
                break;
            case "wait":
                builder.append("container.getDriver().wait(").append(Integer.valueOf(sendValue)).append(");");
                break;
            case "switchToFirst":
                builder.append("container.getDriver().switchToFirst();");
                break;
            case "select":
                builder.append(MessageFormat.format("container.getDriver().getOption(\"{0}\",\"{1}\").",eleName,eleMethod));
                if (selectMethod.equalsIgnoreCase("selected")) {
                    builder.append(By());
                } else if (selectMethod.equalsIgnoreCase("unselected")) {
                    builder.append(deSelected());
                }
                break;
            default:
                throw new NoSuchElementException(eleMethod + " action is no support");
        }
        return builder.toString().length() > 0 ? builder.toString() : null;
    }

    private String deSelected() {
        switch (selectBy) {
            case "name":
                return MessageFormat.format("deselectByVisibleText(\"{0}\");", index);
            case "value":
                return MessageFormat.format("deselectByValue(\"{0}\");", index);
            case "index":
                return MessageFormat.format("deselectByIndex({0});", index);
            default:
                throw new NoSuchElementException(eleMethod + " action is no support");
        }
    }

    private String By() {
        switch (selectBy) {
            case "name":
                return MessageFormat.format("selectByName(\"{0}\");", index);
            case "value":
                return MessageFormat.format("selectByValue(\"{0}\");", index);
            case "index":
                return MessageFormat.format("selectByIndex({0});", index);
            default:
                throw new NoSuchElementException(eleMethod + " action is no support");
        }
    }
}
