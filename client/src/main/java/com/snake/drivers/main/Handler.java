package com.snake.drivers.main;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Handler {
    @RequestMapping(value = "/api", method = RequestMethod.POST)
    public String home(@RequestBody(required = false) String url) {
        System.out.println(url);
        JsonObject data = new JsonParser().parse(url).getAsJsonObject();
        Start start = new Start(data);
        start.start();
        return new Gson().toJson("{\"msg\":\"启动成功\",\"status\":200}");
    }

}