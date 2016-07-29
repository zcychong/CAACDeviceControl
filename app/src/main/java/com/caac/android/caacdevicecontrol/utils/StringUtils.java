package com.caac.android.caacdevicecontrol.utils;

/**
 * Created by YHT on 2016/7/26.
 */
public class StringUtils {
    public static boolean isEmpty(String str){
        if(str != null){
            if(!str.isEmpty() || str.length() == 0){
                return false;
            }
        }
        return true;
    }

    public static boolean isNotEmpty(String str){
        if(isEmpty(str)){
            return false;
        }
        return true;
    }
}
