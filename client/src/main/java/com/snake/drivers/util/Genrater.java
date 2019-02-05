package com.snake.drivers.util;

import java.util.Locale;

/**
 * 本类用于伪造数据
 */
public class Genrater {

    private static com.snake.genrater.Genrater genrater;

    /**
     * 设置语言
     * 支持以下语言
     *
     * bg，ca，ca-CAT，da-DK，de，de-AT，de-CH，en，en-AUen-au-ocker，en-BORK，en-CA，en-GB，en-IND，en-MS，en-NEP
     * en-NG，en-NZ，en-PAK，en-SG，en-UG，en-US，en-ZA，es，es-MX，fa，fi-FI，fr，he，in-ID，it，ja，ko，nb-NO
     * nl，pl，pt，pt-BR，ru，sk，sv，sv-SE，tr，uk，vi，zh-CN，zh-TW
     * @param local 语言
     */
    private static com.snake.genrater.Genrater Genrate(String local){
        if (genrater == null) {
            synchronized (Genrater.class) {
                if (genrater == null){
                    genrater = new com.snake.genrater.Genrater(new Locale(local));
                }
            }
        }
        return genrater;
    }


}
