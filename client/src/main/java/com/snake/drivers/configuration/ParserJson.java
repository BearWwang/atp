package com.snake.drivers.configuration;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ParserJson {

    private JsonArray cases;
    private JsonArray elements;
    private JsonObject testConfig;
    private JsonObject browserConfig;

    public ParserJson(JsonObject data) {
        this.cases = data.get("Cases").getAsJsonArray();
        this.elements = data.get("Elements").getAsJsonArray();
        this.testConfig = data.get("TestConfig").getAsJsonObject();
        this.browserConfig = data.get("BrowserConfig").getAsJsonObject();
    }

    public JsonArray getCases() {
        return cases;
    }

    public JsonArray getElements() {
        return elements;
    }

    public JsonObject getTestConfig() {
        return testConfig;
    }

    public JsonObject getBrowserConfig() {
        return browserConfig;
    }


}
