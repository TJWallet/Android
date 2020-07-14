package com.tianji.blockchain.entity;

import com.alibaba.fastjson.JSONObject;
import com.tianji.blockchain.constant.enums.HDWalletAddressPath;
import com.tianji.blockchain.constant.enums.WalletProof;

public class WalletFileInfo {

    private String encWalletProofContent;

    private HDWalletAddressPath hdWalletAddressPath;

    private int childAccountPos;

    private WalletProof walletProof;

    private long createTime;

    public String getEncWalletProofContent() {
        return encWalletProofContent;
    }

    public void setEncWalletProofContent(String encWalletProofContent) {
        this.encWalletProofContent = encWalletProofContent;
    }

    public HDWalletAddressPath getHdWalletAddressPath() {
        return hdWalletAddressPath;
    }

    public void setHdWalletAddressPath(HDWalletAddressPath hdWalletAddressPath) {
        this.hdWalletAddressPath = hdWalletAddressPath;
    }

    public int getChildAccountPos() {
        return childAccountPos;
    }

    public void setChildAccountPos(int childAccountPos) {
        this.childAccountPos = childAccountPos;
    }

    public WalletProof getWalletProof() {
        return walletProof;
    }

    public void setWalletProof(WalletProof walletProof) {
        this.walletProof = walletProof;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "WalletFileInfo{" +
                "encWalletProofContent='" + encWalletProofContent + '\'' +
                ", hdWalletAddressPath=" + hdWalletAddressPath +
                ", childAccountPos=" + childAccountPos +
                ", walletProof=" + walletProof +
                ", createTime=" + createTime +
                '}';
    }

    public JSONObject toJSON() {
        JSONObject walletInfoJO = new JSONObject();
        walletInfoJO.put("encWalletProofContent", encWalletProofContent);
        walletInfoJO.put("walletProofCode", walletProof.getCode());
        walletInfoJO.put("hdWalletAddressPathCode", hdWalletAddressPath.getCode());
        walletInfoJO.put("childAccountPos", childAccountPos);
        walletInfoJO.put("createTime", createTime);
        return walletInfoJO;
    }
}
