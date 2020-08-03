package com.tianji.blockchain.activity.createwallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.tianji.blockchainwallet.WalletManager;
import com.tianji.blockchainwallet.constant.enums.Chain;
import com.tianji.blockchainwallet.constant.enums.ResultCode;
import com.tianji.blockchainwallet.constant.enums.StorageSaveType;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchainwallet.wallet.IRequestListener;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.MainActivity;
import com.tianji.blockchain.activity.MainPresenter;
import com.tianji.blockchain.activity.basic.BasicDataActivity;
import com.tianji.blockchain.activity.basic.UsbCallbackListener;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.entity.AllObWalletEntity;
import com.tianji.blockchain.sharepreferences.CurrentWalletSharedPreferences;
import com.tianji.blockchain.sharepreferences.MnemonicSharedPreferences;
import com.tianji.blockchain.sharepreferences.ObserverWalletListSharedPreferences;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.SharePreferencesHelper;
import com.tianji.blockchain.utils.ThreadManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VerificationSuccessActivity extends BasicDataActivity {
    private static final int TYPE_VERIFICATION_SUCCESS = 2;
    private TextView tv_ok;

    private WalletInfo walletInfo;

    private boolean canNext = false;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_success);
    }

    @Override
    protected void setData() {
    }

    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case TYPE_VERIFICATION_SUCCESS:
                tv_ok.setBackgroundResource(R.drawable.btn_collect_copy);
                break;
        }
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void initReceiver() {

    }

    @Override
    protected void initView() {
        tv_ok = findViewById(R.id.tv_ok);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(true);
        tv_ok.setBackgroundResource(R.drawable.radius_btn_noenabled);

        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canNext) {
                    MnemonicSharedPreferences.getInstance(VerificationSuccessActivity.this).saveBackUpMnemonic(walletInfo.getAddress(), true);
                    WalletApplication.setCurrentWallet(walletInfo);
                    CurrentWalletSharedPreferences.getInstance(VerificationSuccessActivity.this).changeCurrentWallet(walletInfo);
                    Intent intent = new Intent(VerificationSuccessActivity.this, MainActivity.class);
                    intent.putExtra("_walletInfo", walletInfo);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    LogUtils.log(className + " -- 暂时不能点击");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        walletInfo = (WalletInfo) getIntent().getSerializableExtra("_walletInfo");
        canNext = true;
        mHandler.sendEmptyMessage(TYPE_VERIFICATION_SUCCESS);
    }

}
