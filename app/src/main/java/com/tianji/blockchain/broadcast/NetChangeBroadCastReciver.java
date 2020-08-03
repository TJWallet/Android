package com.tianji.blockchain.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.NetUtils;

public class NetChangeBroadCastReciver extends BroadcastReceiver {
    private NetChangeListener listener;

    public NetChangeBroadCastReciver(NetChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetUtils.NetworkStatus status = NetUtils.getNetWorkState(context);
            listener.onNetChange(status);
            if (status == NetUtils.NetworkStatus.NETWORK_NONE) {
                LogUtils.log("网络断开");
                WalletApplication.getApp().setNetWork(false);
            } else {
                LogUtils.log("网络恢复");
                WalletApplication.getApp().setNetWork(true);
            }
        }
    }
}
