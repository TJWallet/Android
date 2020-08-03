package com.tianji.blockchain.basic;

import android.content.Context;

import com.google.gson.Gson;
import com.tianji.blockchain.basic.BasicMvpInterface;

import java.util.Map;

public abstract class BasicPresenter {


    public Context context;

    public BasicMvpInterface basicMvpInterface;

    public Gson gson;

    public BasicPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        this.context = context;
        this.basicMvpInterface = basicMvpInterface;
        gson = new Gson();
    }

    public abstract void getData(Map<String, String> params);
}
