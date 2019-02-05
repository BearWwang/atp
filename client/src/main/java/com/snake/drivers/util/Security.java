package com.snake.drivers.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Security {
    public static final String KEY_SHA = "SHA";

    public static String getResult(String inputStr) {

        BigInteger sha = null;

        System.out.println("=======加密前的数据:" + inputStr);

        byte[] inputData = inputStr.getBytes();

        try {

            MessageDigest messageDigest = MessageDigest.getInstance(KEY_SHA);

            messageDigest.update(inputData);

            sha = new BigInteger(messageDigest.digest());

            System.out.println("SHA加密后:" + sha.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sha.toString();

    }

    public static String getSha1(String str) {

        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
            mdTemp.update(str.getBytes(StandardCharsets.UTF_8));
            byte[] md = mdTemp.digest();
            int j = md.length;
            char buf[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getSha1(String str, String salt) {
        return getSha1(str + salt);
    }

}

