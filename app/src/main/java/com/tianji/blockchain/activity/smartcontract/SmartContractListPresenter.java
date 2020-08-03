package com.tianji.blockchain.activity.smartcontract;

import android.content.Context;

import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.basic.BasicPresenter;

import java.util.Map;

public class SmartContractListPresenter extends BasicPresenter {
    public SmartContractListPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        super(context, basicMvpInterface);
    }

    @Override
    public void getData(Map<String, String> params) {

    }
}
