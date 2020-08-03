package com.tianji.blockchain.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.tianji.blockchain.Constant;

/** author:jason **/
public class PreferenceHelper {
    private static PreferenceHelper instance;
    private Context context;
    private SharedPreferences sharedPreference;

    private PreferenceHelper(Context context) {
        this.context = context.getApplicationContext();
        this.sharedPreference = this.context.getSharedPreferences(Constant.PreferenceKeys.FILE_NAME, Context.MODE_PRIVATE);
        this.checkVersion();
    }

    private void checkVersion() {
        final String key = Constant.PreferenceKeys.FILE_NAME + ":version";

        if (this.sharedPreference.contains(key)){
            int version = this.sharedPreference.getInt(key, 1);
            if (version < Constant.PreferenceKeys.VERSION) {
                this.sharedPreference.edit().clear().commit();
            }
        } else {
            this.sharedPreference.edit().clear().commit();
        }
        this.sharedPreference.edit().putInt(key, Constant.PreferenceKeys.VERSION).commit();
    }

    public static PreferenceHelper getInstance(Context context) {
        if (PreferenceHelper.instance == null) {
            PreferenceHelper.instance = new PreferenceHelper(context);
        }
        return PreferenceHelper.instance;
    }

    public void save(String key, String data) {
        try{
            this.sharedPreference.edit().putString(key, data).commit();
        } catch(Exception e) {
            Log.e("PreferenceHelper", "saveEntity error.", e);
        }
    }

    public void saveEntity(String key, Object data) {
        try{
            this.sharedPreference.edit().putString(key, JSON.toJSONString(data)).commit();
        } catch(Exception e) {
            Log.e("PreferenceHelper", "saveEntity error.", e);
        }
    }

    public <T> T readEntity(String key, TypeReference<T> typeRef) {
        if(this.sharedPreference.contains(key)) {
            String data = this.sharedPreference.getString(key, "{}");
            try {
                return JSON.parseObject(data, typeRef);
            } catch(Exception e) {
                Log.e("PreferenceHelper", "readEntity error.", e);
                return null;
            }
        }
        return null;
    }
}
