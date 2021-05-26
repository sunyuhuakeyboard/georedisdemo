package com.chen.www.georedisdemo.utils;

public class CommonUtil {
    public static String buildRedisKey(String head_str,String str){
        return head_str+str;
    }
}
