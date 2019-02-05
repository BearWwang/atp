package com.snake.drivers.util;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ToJson {

    public static String FileToJson(String fileName) {
        InputStream is = ToJson.class.getClassLoader().getResourceAsStream(fileName);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);

        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = br.readLine()) != null){
                builder.append(line);
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        return builder.toString();
    }
}
