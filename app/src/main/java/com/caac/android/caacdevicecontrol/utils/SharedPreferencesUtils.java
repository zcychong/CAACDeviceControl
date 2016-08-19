package com.caac.android.caacdevicecontrol.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * SharedPreferences的一个工具类，调用setParam就能保存String, Integer, Boolean, Float, Long类型的参数
 * 同样调用getParam就能获取到保存在手机里面的数据
 *
 * @author xiaanming
 */
public class SharedPreferencesUtils {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_date";


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context, String key, Object object) {

        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        } else {
            saveObject(sp, editor, key, object);
        }
        editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context, String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, "");
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        } else if (defaultObject != null) {
            return readObject(sp, key);
        }
        return readObject(sp, key);
    }

    public static void saveObject(SharedPreferences preferences, SharedPreferences.Editor editor, String key, Object object) {

        //创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            //创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            //将对象写入字节流
            oos.writeObject(object);

            //将字节流编码成base64的字符窜
            String productBase64 = new String(Base64.encodeBase64(baos
                    .toByteArray()));

            editor.putString(key, productBase64);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object readObject(SharedPreferences preferences, String key) {
        String productBase64 = preferences.getString(key, "");
        Object object = new Object();
        //读取字节
        byte[] base64 = Base64.decodeBase64(productBase64.getBytes());

        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            //再次封装
            if (!bais.equals("") && !productBase64.equals("")) {
                ObjectInputStream bis = new ObjectInputStream(bais);
                try {
                    //读取对象
                    object = bis.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                return null;
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return object;
    }

    public static void clearData(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().clear().commit();
    }

}