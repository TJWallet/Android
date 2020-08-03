package com.tianji.blockchain.sharepreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.tianji.blockchain.entity.InformationListEntity;
import com.tianji.blockchain.utils.LogUtils;

public class InformationListSharedPreferences {
    public static final String INFORMATION_LIST_SP_KEY = "information_list_sp_key";
    private int mVersion = 2;
    private SharedPreferences mSharedPrefernces;
    private SharedPreferences.Editor mSharedPreferncesEditor;
    public Gson gson;

    private Context context;

    /**
     * 单例模式
     **/
    private static InformationListSharedPreferences instance;

    private InformationListSharedPreferences(Context context) {
        mSharedPrefernces = context.getSharedPreferences(INFORMATION_LIST_SP_KEY, Context.MODE_PRIVATE);
        mSharedPreferncesEditor = mSharedPrefernces.edit();
        gson = new Gson();
        this.context = context;
    }

    public static InformationListSharedPreferences getInstance(Context context) {
        if (instance == null) {
            if (context != null) {
                instance = new InformationListSharedPreferences(context);
            }
        }
        return instance;
    }

    public void checkVersion() {
        String key = INFORMATION_LIST_SP_KEY + ":version";
        if (this.mSharedPrefernces.contains(key)) {
            int version = this.mSharedPrefernces.getInt(key, 1);
            LogUtils.log("InformationListSharedPreferences 当前版本是 = " + version);
            if (version < mVersion) {
                mSharedPreferncesEditor.putInt(key, mVersion).commit();
                setNewRecord();
            }

        } else {
            mSharedPreferncesEditor.putInt(key, mVersion).commit();
            setNewRecord();
        }
    }

    private String getRecord() {
        String record = mSharedPrefernces.getString(INFORMATION_LIST_SP_KEY, "");
        return record;
    }

    /**
     * 添加消息列表,替换消息列表信息
     *
     * @param entity
     */
    public void addInformationList(InformationListEntity entity) {
        if (entity == null) return;
        String newRecord = gson.toJson(entity);
        LogUtils.log("新传入的json==" + newRecord);
        mSharedPreferncesEditor.putString(INFORMATION_LIST_SP_KEY, newRecord).commit();
    }

    /**
     * 清空消息列表
     */
    public void removeInformationList() {
        mSharedPreferncesEditor.clear().commit();
    }

    /**
     * 获取消息列表
     *
     * @return
     */
    public InformationListEntity getInformationList() {
        String record = getRecord();
        if (record.equals("")) return null;
        LogUtils.log("获取到的json==" + record);
        InformationListEntity entity = gson.fromJson(record, InformationListEntity.class);
        return entity;
    }

    /**
     * 缓存版本更新，替换
     */
    private void setNewRecord() {
        InformationListEntity oldEntity = getInformationList();
        removeInformationList();
        addInformationList(oldEntity);
    }
}
