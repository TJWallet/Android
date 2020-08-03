package com.tianji.blockchain.sharepreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tianji.blockchain.entity.TransferRecode;
import com.tianji.blockchain.entity.TransferWaitEntity;
import com.tianji.blockchain.utils.LogUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TransferWaitSharedPreferences {

    public static final String TRANSFER_WAIT_KEY = "transferwait_sp_key";
    private SharedPreferences.Editor mSharedPreferncesEditor;
    private SharedPreferences mSharedPrefernces;

    /**
     * 单例模式
     **/
    private static TransferWaitSharedPreferences instance;

    public Gson gson;

    private TransferWaitSharedPreferences(Context context) {
        mSharedPrefernces = context.getSharedPreferences(TRANSFER_WAIT_KEY, Context.MODE_PRIVATE);
        mSharedPreferncesEditor = mSharedPrefernces.edit();
        gson = new Gson();
    }

    public static TransferWaitSharedPreferences getInstance(Context context) {
        if (instance == null) {
            if (context != null) {
                instance = new TransferWaitSharedPreferences(context);
            }
        }
        return instance;
    }

    /**
     * 获取当前记录的JSON
     *
     * @param walletAddress
     * @return
     */
    private String getRecord(String walletAddress) {
        String record = mSharedPrefernces.getString(walletAddress, "");
        return record;
    }

    /**
     * 添加一条交易等待缓存
     *
     * @param walletAddress
     * @param entity
     */
    public void addTransferWait(String walletAddress, TransferWaitEntity entity) {
        String record = getRecord(walletAddress);
        LogUtils.log("添加交易等待记录 , 当前记录是 =" + record);
        if (record.equals("")) {
            List<TransferWaitEntity> transferWaitEntityList = new ArrayList<>();
            transferWaitEntityList.add(entity);
            String newRecord = gson.toJson(transferWaitEntityList);
            LogUtils.log("添加交易等待记录 , 没有记录，添加成功，当前记录是 =" + newRecord);
            mSharedPreferncesEditor.putString(walletAddress, newRecord);
            mSharedPreferncesEditor.commit();
        } else {
            List<TransferWaitEntity> transferWaitEntityList = gson.fromJson(record, new TypeToken<List<TransferWaitEntity>>() {
            }.getType());
            LogUtils.log("解析记录后的长度是 = " + transferWaitEntityList.size());
            transferWaitEntityList.add(entity);
            String newRecord = gson.toJson(transferWaitEntityList);
            LogUtils.log("添加交易等待记录 , 有记录，添加成功，当前记录是 =" + newRecord);

            mSharedPreferncesEditor.putString(walletAddress, newRecord);
            mSharedPreferncesEditor.commit();
        }
    }

    /**
     * 移除某个货币名称下某一条交易等待记录
     *
     * @param walletAddress
     * @param coinName
     * @param transferHash
     */
    public void removeRecord(String walletAddress, String coinName, String transferHash) {
        String record = getRecord(walletAddress);
        LogUtils.log("移除等待记录 , 当前记录是 =" + record);
        if (record.equals("")) {

            List<TransferWaitEntity> transferWaitEntityList = new ArrayList<>();
            String newRecord = gson.toJson(transferWaitEntityList);
            LogUtils.log("移除等待记录 , 记录为空，当前记录是 =" + newRecord);
            mSharedPreferncesEditor.putString(walletAddress, newRecord);
            mSharedPreferncesEditor.commit();
        } else {
            List<TransferWaitEntity> transferWaitEntityList = gson.fromJson(record, new TypeToken<List<TransferWaitEntity>>() {
            }.getType());
            for (int i = 0; i < transferWaitEntityList.size(); i++) {
                if (coinName.equals(transferWaitEntityList.get(i).getCoinName())) {
                    if (transferHash.equals(transferWaitEntityList.get(i).getTransferRecode().getHash())) {
                        transferWaitEntityList.remove(i);
                    }
                }
            }

            String newRecord = gson.toJson(transferWaitEntityList);
            LogUtils.log("移除等待记录 , 有记录，当前记录是 =" + newRecord);
            mSharedPreferncesEditor.putString(walletAddress, newRecord);
            mSharedPreferncesEditor.commit();
        }
    }

    /**
     * 获取某个货币名称下所有的等待交易列表
     *
     * @param walletAddress
     * @param coinName
     * @return
     */
    public List<TransferWaitEntity> getTransferWaitList(String walletAddress, String coinName) {
        String record = getRecord(walletAddress);
        LogUtils.log("获取交易等待列表当前记录是 =" + record);
        if (record.equals("")) {
            List<TransferWaitEntity> transferWaitEntityList = new ArrayList<>();
            LogUtils.log("获取交易等待列表当前记录空 ");
            return transferWaitEntityList;
        } else {
            List<TransferWaitEntity> transferWaitEntityList = gson.fromJson(record, new TypeToken<List<TransferWaitEntity>>() {
            }.getType());
            List<TransferWaitEntity> transferWaitEntities = new ArrayList<>();
            for (int i = 0; i < transferWaitEntityList.size(); i++) {
                if (coinName.equals(transferWaitEntityList.get(i).getCoinName())) {
                    transferWaitEntities.add(transferWaitEntityList.get(i));
                }
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                transferWaitEntities.sort(new Comparator<TransferWaitEntity>() {
                    @Override
                    public int compare(TransferWaitEntity o1, TransferWaitEntity o2) {
                        Long long1 = o1.getTransferRecode().getTimestamp();
                        Long long2 = o2.getTransferRecode().getTimestamp();
                        return long2.compareTo(long1);
                    }
                });
            }
            LogUtils.log("获取交易等待列表当前记录不为空 coinName 是 =" + coinName);
            return transferWaitEntities;
        }
    }

}
