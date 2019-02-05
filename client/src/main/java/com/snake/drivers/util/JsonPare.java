package com.snake.drivers.util;

import com.google.gson.*;
import org.openqa.selenium.json.Json;

public class JsonPare {

    private JsonParser parser = new JsonParser();
    private JsonElement root;
    private String rootElement;

    public JsonPare(String rootElement) {
        this.rootElement = rootElement;
    }

    public JsonArray parser(String str){
        root = parser.parse(str).getAsJsonObject();
        return ((JsonObject) root).getAsJsonArray(rootElement);
    }


}
