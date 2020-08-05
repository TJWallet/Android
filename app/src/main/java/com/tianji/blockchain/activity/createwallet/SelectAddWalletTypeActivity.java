package com.tianji.blockchain.activity.createwallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.activity.importwallet.ImportWalletByKeyStoreActivity;
import com.tianji.blockchain.activity.importwallet.ImportWalletByMnemonicActivity;
import com.tianji.blockchain.activity.importwallet.ImportWalletByPrivateKeyActivity;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class SelectAddWalletTypeActivity extends BasicConnectShowActivity implements View.OnClickListener {
    private RelativeLayout rl_import_mnemonic;
    private RelativeLayout rl_import_private_key;
    private RelativeLayout rl_import_keystore;
    private RelativeLayout rl_create_new_wallet;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_add_wallet_type);
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
        String chainName = "FileCoin";
        String title = String.format(getResources().getString(R.string.select_add_wallet_type), chainName);
        ViewCommonUtils.createDefaultActionBar(mActionBar, this, title);

        rl_import_mnemonic = findViewById(R.id.rl_import_mnemonic);
        rl_import_private_key = findViewById(R.id.rl_import_private_key);
        rl_import_keystore = findViewById(R.id.rl_import_keystore);
        rl_create_new_wallet = findViewById(R.id.rl_create_new_wallet);

        rl_import_mnemonic.setOnClickListener(this);
        rl_import_private_key.setOnClickListener(this);
        rl_import_keystore.setOnClickListener(this);
        rl_create_new_wallet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_import_mnemonic:
                Intent mnemonicIntent = new Intent(this, ImportWalletByMnemonicActivity.class);
                startActivity(mnemonicIntent);
                break;
            case R.id.rl_import_private_key:
                Intent privateKeyIntent = new Intent(this, ImportWalletByPrivateKeyActivity.class);
                startActivity(privateKeyIntent);
                break;
            case R.id.rl_import_keystore:
                showToast(R.string.filecoin_no_keystore);
//                Intent keyStoreIntent = new Intent(this, ImportWalletByKeyStoreActivity.class);
//                startActivity(keyStoreIntent);
                break;
            case R.id.rl_create_new_wallet:
                LogUtils.log(className + " -- 点击创建钱包 rl_create_new_wallet");
                Intent createIntent = new Intent(this, CreateWalletStepOneActivity.class);
                createIntent.putExtra("_addSource", Constant.AddWalletSource.ADD_WALLET_BY_CREATE);
                startActivity(createIntent);
                break;
        }
    }
}
