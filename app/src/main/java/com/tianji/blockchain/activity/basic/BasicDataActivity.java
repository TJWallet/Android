package com.tianji.blockchain.activity.basic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;

public abstract class BasicDataActivity extends BasicAppActivity {
    public Handler mHandler;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHandler();
        initReceiver();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setData();
    }

    @SuppressLint("HandlerLeak")
    protected void setHandler() {
        if (mHandler != null) {
            return;
        }
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                handleMsg(msg);
            }
        };
    }


    protected abstract void setData();

    protected abstract void handleMsg(Message msg);

    protected abstract void initReceiver();
}
