package com.tianji.blockchain.activity.createwallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tianji.blockchainwallet.WalletManager;
import com.tianji.blockchainwallet.constant.enums.ResultCode;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchainwallet.wallet.IRequestListener;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.MainActivity;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.activity.home.HomeActivity;
import com.tianji.blockchain.adapter.gridView.GVAdapterMnemonic;
import com.tianji.blockchain.dialog.TipsDialog;
import com.tianji.blockchain.entity.DialogEntity;
import com.tianji.blockchain.entity.MnemoinicButtonEntity;
import com.tianji.blockchain.sharepreferences.CurrentWalletSharedPreferences;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

import java.util.ArrayList;
import java.util.List;

public class BackUpMnemonicActivity extends BasicConnectShowActivity {
    private GridView gv_mnemonic;
    private TextView tv_next;

    private GVAdapterMnemonic adapter;
    private String mnemonic;

    private WalletInfo walletInfo;

    private TipsDialog backDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_up_mnemonic);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
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
    public void onBackPressed() {
        if (pageType == Constant.StartPageType.TYPE_CREATE) {
            backDialog.show();
        } else if (pageType == Constant.StartPageType.TYPE_WALLETDETAILS) {
            BackUpMnemonicActivity.this.finish();
        } else {
            backDialog.show();
        }
    }


    @Override
    protected void initView() {
        mnemonic = getIntent().getStringExtra("_mnemonic");
        pageType = getIntent().getIntExtra("_pageType", -1);
        source = getIntent().getIntExtra("_source", -1);
        LogUtils.log(className + " -- 创建钱包成功,来源是 == " + source);
        walletInfo = (WalletInfo) getIntent().getSerializableExtra("_walletInfo");
        if (walletInfo != null) {
            LogUtils.log("BackUpMnemonicActivity钱包" + walletInfo.toString());
        }


        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.write_mnemonic));
        ViewCommonUtils.buildBackImageView(this, mActionBar, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pageType == Constant.StartPageType.TYPE_CREATE) {
                    backDialog.show();
                } else if (pageType == Constant.StartPageType.TYPE_WALLETDETAILS) {
                    BackUpMnemonicActivity.this.finish();
                }
            }
        });
        DialogEntity backDialogEntity = new DialogEntity(getResources().getString(R.string.tips),
                getResources().getString(R.string.jump_backup),
                getResources().getString(R.string.backup_goon),
                getResources().getString(R.string.jump),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickBack();
                    }
                });
        backDialog = new TipsDialog(this, backDialogEntity);

        gv_mnemonic = findViewById(R.id.gv_mnemonic);
        tv_next = findViewById(R.id.tv_next);

        LogUtils.log("mnemonic = " + mnemonic);
        String[] mnemonics = mnemonic.split(" ");
        List<MnemoinicButtonEntity> mnemoinicList = new ArrayList<>();
        for (int i = 0; i < mnemonics.length; i++) {
            MnemoinicButtonEntity entity = new MnemoinicButtonEntity();
            entity.setTextContent(mnemonics[i]);
            mnemoinicList.add(entity);
        }
        adapter = new GVAdapterMnemonic(this, mnemoinicList);
        gv_mnemonic.setAdapter(adapter);

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BackUpMnemonicActivity.this, VerificationMnemonicActivity.class);
                intent.putExtra("_mnemonic", mnemonic);
                intent.putExtra("_walletInfo", walletInfo);
                startActivity(intent);
            }
        });
    }

    private void onClickBack() {
        Intent intent = new Intent(BackUpMnemonicActivity.this, MainActivity.class);
        intent.putExtra("_walletInfo", walletInfo);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        WalletApplication.setCurrentWallet(walletInfo);
        CurrentWalletSharedPreferences.getInstance(this).changeCurrentWallet(walletInfo);
    }
}
