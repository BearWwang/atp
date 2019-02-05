package com.snake.drivers.util;



import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author venmosnake
 * 本类主要负责CSV文件读取
 */
public class CsvUtil {

    public static Object[][] readCSV(String  files) throws IOException {

        //读取CSV文件的方法
        List<Object[]> records = new ArrayList<>();
        String record;
        BufferedReader file = new BufferedReader(new InputStreamReader(new FileInputStream(files), StandardCharsets.UTF_8 ));
        file.readLine();
        while ((record = file.readLine()) != null) {
            String fields[] = record.split(",");
            records.add(fields);
        }
        file.close();

        Object[][] results = new Object[records.size()][];
        for (int i = 0; i < records.size(); i++) {
            results[i] = records.get(i);
        }
        return results;
    }

}
