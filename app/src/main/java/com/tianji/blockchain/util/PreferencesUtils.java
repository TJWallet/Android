package com.tianji.blockchain.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtils {
    public static String PREFERENCE_NAME = "TJSJ_WALLET_SDK";

    /**
     * put一个String类型数据
     *
     * @param context 上下文
     * @param key     存储的键
     * @param value   要存储的值
     * @return 布尔值，保存成功返回true，保存失败返回false
     */
    public static boolean putString(Context context, String key, String value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    /**
     * 根据key获取String类型的值,可自定义缺省值(默认值)
     *
     * @param context      上下文
     * @param key          存储的键
     * @param defaultValue 获取不到时的默认返回值
     * @return 返回String类型结果
     */
    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getString(key, defaultValue);
    }

    /**
     * put一个int类型的数据
     *
     * @param context 上下文
     * @param key     存储的键
     * @param value   要存储的值
     * @return 布尔值，保存成功返回true，保存失败返回false
     */
    public static boolean putInt(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    /**
     * 根据key获取int类型的值,可自定义缺省值(默认值)
     *
     * @param context      上下文
     * @param key          存储的键
     * @param defaultValue 获取不到时的默认返回值
     * @return 返回int类型结果
     */
    public static int getInt(Context context, String key, int defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getInt(key, defaultValue);
    }

    /**
     * put一个long类型的数据
     *
     * @param context 上下文
     * @param key     存储的键
     * @param value   要存储的值
     * @return 布尔值，保存成功返回true，保存失败返回false
     */
    public static boolean putLong(Context context, String key, long value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        return editor.commit();
    }


    /**
     * 根据key获取存储long类型的值，可自定义缺省值(默认值)
     *
     * @param context      上下文
     * @param key          存储的键
     * @param defaultValue 获取不到时的默认返回值
     * @return 返回long类型结果
     */
    public static long getLong(Context context, String key, long defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getLong(key, defaultValue);
    }

    /**
     * put一个float类型的数据
     *
     * @param context 上下文
     * @param key     存储的键
     * @param value   要存储的值
     * @return 布尔值，保存成功返回true，保存失败返回false
     */
    public static boolean putFloat(Context context, String key, float value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(key, value);
        return editor.commit();
    }

    /**
     * 根据key获取float类型的值,可自定义缺省值(默认值)
     *
     * @param context      上下文
     * @param key          存储的键
     * @param defaultValue 获取不到时的默认返回值
     * @return 返回float类型结果
     */
    public static float getFloat(Context context, String key, float defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getFloat(key, defaultValue);
    }

    /**
     * put一个boolean类型的数据
     *
     * @param context 上下文
     * @param key     存储的键
     * @param value   要存储的值
     * @return 布尔值，保存成功返回true，保存失败返回false
     */
    public static boolean putBoolean(Context context, String key, boolean value) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    /**
     * 根据key获取boolean类型的值,可自定义缺省值(默认值)
     *
     * @param context      上下文
     * @param key          存储的键
     * @param defaultValue 获取不到时的默认返回值
     * @return 返回boolean类型结果
     */
    public static boolean getBoolean(Context context, String key, boolean defaultValue) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return settings.getBoolean(key, defaultValue);
    }

    /**
     * 删除指定键值
     *
     * @param context 上下文
     * @param key     存储的键
     */
    public static boolean deleteObject(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        return editor.commit();
    }

}
