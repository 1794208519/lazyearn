package com.utils;

/**
 * Created by qq944 on 2017/10/14.
 */

public class MyStringUtil {
    //public static String cutHaoYou(String str) {
       // return str.substring(0, str.length() - 12);
   // }

    public static String[] cutLiaoTian(String str) {
        return str.split("\\)");
    }
}
