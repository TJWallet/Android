package com.tianji.blockchain.activity.basic;

import android.annotation.SuppressLint;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.Nullable;

import com.tianji.blockchain.R;
import com.tianji.blockchain.broadcast.NetChangeBroadCastReciver;
import com.tianji.blockchain.broadcast.NetChangeListener;
import com.tianji.blockchain.dialog.TipsDialog;
import com.tianji.blockchain.entity.DialogEntity;
import com.tianji.blockchain.utils.LogUtils;

public abstract class BasicNetCheckActivity extends BasicAppActivity implements NetChangeListener {
    private NetChangeBroadCastReciver netChangeBroadCastReciver;
    protected TipsDialog tipsDialog;
    protected NetChangeListener listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHandler();
        initReceiver();


        DialogEntity entity = new DialogEntity(getResources().getString(R.string.no_network_title), getResources().getString(R.string.no_network_desc), getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tipsDialog.isShowing()) {
                    tipsDialog.dismiss();
                }
            }
        });
        tipsDialog = new TipsDialog(this, entity, false);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (netChangeBroadCastReciver == null) {
            netChangeBroadCastReciver = new NetChangeBroadCastReciver(this);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netChangeBroadCastReciver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.log("BasicNetCheckActivity onPause");
        try {
            unregisterReceiver(netChangeBroadCastReciver);
        } catch (IllegalArgumentException e) {

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public Handler mHandler;

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

    protected void setNetChangeListener(NetChangeListener listener) {
        this.listener = listener;
    }
}
