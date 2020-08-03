package com.tianji.blockchain.activity.createwallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.MainActivity;
import com.tianji.blockchain.activity.basic.BasicActionBarActivity;
import com.tianji.blockchain.activity.home.HomeActivity;
import com.tianji.blockchain.dialog.TipsDialog;
import com.tianji.blockchain.entity.DialogEntity;
import com.tianji.blockchain.sharepreferences.CurrentWalletSharedPreferences;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.NetUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;


public class CreateWalletSuccessActivity extends BasicActionBarActivity implements View.OnClickListener {
    private static final int GET_ENEMOINIC_SUCCESS = 0;
    private TextView tv_jump;
    private TextView tv_next;
    private SwipeRefreshLayout swipeRefreshLayout;

    private String mnemonic;
    private String pwd;

    private TipsDialog backDialog;

    private boolean createLock = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet_success);

    }

    @Override
    protected void setData() {

    }

    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case GET_ENEMOINIC_SUCCESS:
                mnemonic = (String) msg.obj;
                Intent intent = new Intent(CreateWalletSuccessActivity.this, BackUpMnemonicActivity.class);
                intent.putExtra("_mnemonic", mnemonic);
                intent.putExtra("_walletInfo", walletInfo);
                startActivity(intent);
                CreateWalletSuccessActivity.this.finish();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                break;
        }
    }

    @Override
    protected void initReceiver() {

    }

    @Override
    protected void initView() {
        walletInfo = (WalletInfo) getIntent().getSerializableExtra("_walletInfo");
        pwd = getIntent().getStringExtra("_pwd");
        mActionBar.setVisibility(View.VISIBLE);
        ViewCommonUtils.buildBackImageView(this, mActionBar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backDialog.show();
            }
        });


        tv_jump = findViewById(R.id.tv_jump);
        tv_next = findViewById(R.id.tv_next);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        tv_jump.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        swipeRefreshLayout.setEnabled(false);
        DialogEntity backDialogEntity = new DialogEntity(getResources().getString(R.string.tips),
                getResources().getString(R.string.jump_backup),
                getResources().getString(R.string.backup_goon),
                getResources().getString(R.string.jump),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtils.log(className + " -- 来源是mian 点击返回按钮");
                        onClickBack();
                    }
                });
        backDialog = new TipsDialog(this, backDialogEntity);

        tv_jump.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        createLock = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_jump:
                backDialog.show();
                break;
            case R.id.tv_next:
                if (createLock) {
                    createLock = false;
                    LogUtils.log(className + "-- 验证密码  " + pwd);
                    if (!swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(true);
                    }
                    WalletManager.getInstance().getMnemonic(CreateWalletSuccessActivity.this, walletInfo, pwd, new IRequestListener<String>() {
                        @Override
                        public void onResult(ResultCode resultCode, String result) {
                            if (resultCode == ResultCode.SUCCESS) {
                                mnemonic = result;
                                if (!mnemonic.equals("") && mnemonic != null) {
                                    Intent intent = new Intent(CreateWalletSuccessActivity.this, BackUpMnemonicActivity.class);
                                    intent.putExtra("_mnemonic", mnemonic);
                                    intent.putExtra("_walletInfo", walletInfo);
                                    startActivity(intent);
                                    CreateWalletSuccessActivity.this.finish();
                                }
                            } else {
                                LogUtils.log(className + " -- 获取助记词出现未知错误 =" + resultCode);
                                createLock = true;
                            }
                            if (swipeRefreshLayout.isRefreshing()) {
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        }
                    });
                }

                break;
        }
    }


    @Override
    public void onBackPressed() {
        backDialog.show();
    }

    private void onClickBack() {
        Intent intent = new Intent(CreateWalletSuccessActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("_walletInfo", walletInfo);
        startActivity(intent);
        WalletApplication.setCurrentWallet(walletInfo);
        CurrentWalletSharedPreferences.getInstance(this).changeCurrentWallet(walletInfo);
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
