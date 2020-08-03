package com.tianji.blockchain.activity.basic;

import android.content.Context;

import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.basic.BasicPresenter;

import java.util.Map;

public abstract class BasicCustomPresenter extends BasicPresenter {

    public BasicCustomPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        super(context, basicMvpInterface);
    }

    @Override
    public void getData(Map<String, String> params) {

    }
}
