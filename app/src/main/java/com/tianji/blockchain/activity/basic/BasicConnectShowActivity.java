package com.tianji.blockchain.activity.basic;

import android.os.Bundle;
import android.os.Message;

import androidx.annotation.Nullable;

import com.tianji.blockchainwallet.WalletManager;
import com.tianji.blockchainwallet.constant.enums.ResultCode;
import com.tianji.blockchainwallet.wallet.IRequestListener;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.NetUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class BasicConnectShowActivity extends BasicActionBarActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setData() {

    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void initReceiver() {

    }

    @Override
    protected void initView() {

    }


    @Override
    public void onNetChange(NetUtils.NetworkStatus status) {
        if (!WalletApplication.getApp().isNetWorkFirstShowed()) {
            if (status == NetUtils.NetworkStatus.NETWORK_NONE) {
                tipsDialog.show();
                WalletApplication.getApp().setNetWorkFirstShowed(true);
            }
        }
    }
}
