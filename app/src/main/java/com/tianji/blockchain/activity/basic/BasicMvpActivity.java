package com.tianji.blockchain.activity.basic;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.Nullable;

import com.android.volley.VolleyError;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.basic.BasicPresenter;

/** author:jason **/
public abstract class BasicMvpActivity extends BasicAppActivity implements BasicMvpInterface {
    protected Handler mHandler;
    protected BasicPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(this.setResourceId());
        setHandler();
        initView();
        setData();
        initReceiver();
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

    protected abstract int setResourceId();

    protected abstract void handleMsg(Message msg);

    protected abstract void setData();

    protected abstract void initReceiver();

    @Override
    protected abstract void initView();

    @Override
    public abstract void getDataSuccess(Object object, int type);

    @Override
    public abstract void getDataFail(Object error, int type);
}
