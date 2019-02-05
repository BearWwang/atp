package com.snake.drivers.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    private JsonObject baseObject;

    public Parser(String url) {
        JsonParser parse = new JsonParser();
        if (url != null)
            baseObject = (JsonObject) parse.parse(url);
    }

    private JsonObject getData() {
        return baseObject.get("data").getAsJsonObject();
    }


    private JsonArray getDatas() {
        return baseObject.getAsJsonArray("data");
    }

    private int getSize() {
        return getDatas().size();
    }


    private JsonObject getOneData(int i) {
        return getDatas().get(i).getAsJsonObject();
    }

    private String getAttr(int i, String key) {
        return getOneData(i).get(key).getAsString();
    }

    private Map<String, String> keyValueMap(int i) {
        Map<String, String> map = new HashMap<>();
        map.put(getAttr(i, "key"), getAttr(i, "value"));
        return map;
    }

    public boolean verify() {
        String salt = baseObject.get("salt").getAsString();
        String code = baseObject.get("user").getAsString();
        code = Security.getSha1(code, "administrator");
        return salt.equals(code);
    }


//    private String executeOne() {
//        JsonObject o = getData();
//        String res = null;
//        Map<String, String> map = new HashMap<>();
//        map.put(o.get("key").getAsString(), o.get("value").getAsString());
//        try {
//            res = new Request().send(o.get("method").getAsString(), o.get("url").getAsString(), map);
//        } catch (IOException e) {
//            map.put("error", e.getMessage());
//        }
//        return new Gson().toJson(res);
//    }
//
//    public String execute() {
//        String type = baseObject.get("number").getAsString();
//        if (type.equalsIgnoreCase("many"))
////            return executeMany();
//        else if (type.equalsIgnoreCase("one"))
//            return executeOne();
//        else
//            return new Gson().toJson("{\"msg\":\"参数错误\"}");
//    }


//    private String executeMany() {
//        Map<String, String> map = new HashMap<>();
//
//        for (int i = 0; i < getSize(); i++) {
//            String res = null;
//            try {
////                res = new Request().send(getAttr(i, "method"), getAttr(i, "url"), keyValueMap(i));
//            } catch (IOException e) {
//                map.put("error", e.getMessage());
//            }
//            map.put(getAttr(i, "id"), res);
//        }
//        return new Gson().toJson(map);
//    }

}
