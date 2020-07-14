package com.tianji.blockchain.entity;

import com.alibaba.fastjson.JSONObject;

public class WalletRemarksInfo {

    // 钱包名
    private String name;
    // 密码提示
    private String passwordTips;
    // 钱包创建方式
    private int createdTypeCode;
    // 钱包创建日期
    private long createdTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswordTips() {
        return passwordTips;
    }

    public void setPasswordTips(String passwordTips) {
        this.passwordTips = passwordTips;
    }

    public int getCreatedTypeCode() {
        return createdTypeCode;
    }

    public void setCreatedTypeCode(int createdTypeCode) {
        this.createdTypeCode = createdTypeCode;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "WalletRemarksInfo{" +
                "name='" + name + '\'' +
                ", passwordTips='" + passwordTips + '\'' +
                ", createdTypeCode=" + createdTypeCode +
                ", createdTime=" + createdTime +
                '}';
    }

    public JSONObject toJSONObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("passwordTips", passwordTips);
        jsonObject.put("createdTypeCode", createdTypeCode);
        jsonObject.put("createdTime", createdTime);
        return jsonObject;
    }
}
