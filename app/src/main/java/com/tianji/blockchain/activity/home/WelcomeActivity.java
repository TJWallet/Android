package com.tianji.blockchain.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.MainActivity;
import com.tianji.blockchain.activity.basic.BasicDataActivity;
import com.tianji.blockchain.sharepreferences.CurrentWalletSharedPreferences;
import com.tianji.blockchain.utils.WalletListHelper;
import com.tianji.blockchainwallet.entity.WalletInfo;

import java.util.ArrayList;
import java.util.List;


public class WelcomeActivity extends BasicDataActivity {
    private List<WalletInfo> walletInfoList = new ArrayList<>();
    private static final int START_MAINACTIVITY = 0;
    private TextView tv_desc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    @Override
    protected void setData() {

    }

    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case START_MAINACTIVITY:
                WalletInfo walletInfo = null;
                if (walletInfoList.size() > 0) {

                    walletInfo = CurrentWalletSharedPreferences.getInstance(this).getCurrentWallet();

                    if (walletInfo == null) {
                        walletInfo = walletInfoList.get(0);
                    }
                    WalletApplication.setCurrentWallet(walletInfo);
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("_walletInfo", walletInfo);
                    startActivity(intent);
                } else {
                    startActivity(HomeActivity.class);
                }
                finish();
                break;
        }
    }


    @Override
    protected void initReceiver() {

    }

    @Override
    protected void onStart() {
        super.onStart();

        walletInfoList.clear();
        walletInfoList.addAll(WalletListHelper.getInstance(this).getSoftwareWalletInfoListAll());

        //有filecoin钱包
        mHandler.sendEmptyMessageDelayed(START_MAINACTIVITY, 2000);
    }


    @Override
    protected void initView() {
        tv_desc = findViewById(R.id.tv_desc);
        if (!WalletApplication.lang.equals("")) tv_desc.setVisibility(View.GONE);
    }

}
