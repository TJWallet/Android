package com.tianji.blockchain.activity.createwallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tianji.blockchainwallet.WalletManager;
import com.tianji.blockchainwallet.constant.enums.Chain;
import com.tianji.blockchainwallet.constant.enums.HDWalletAddressPath;
import com.tianji.blockchainwallet.constant.enums.ResultCode;
import com.tianji.blockchainwallet.constant.enums.StorageSaveType;
import com.tianji.blockchainwallet.constant.enums.WalletCreatedType;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchainwallet.wallet.IRequestListener;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.MainActivity;
import com.tianji.blockchain.activity.basic.BasicActionBarActivity;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.sharepreferences.CurrentWalletSharedPreferences;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.RegexUtils;
import com.tianji.blockchain.utils.SharePreferencesHelper;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class CreateWalletStepOneActivity extends BasicConnectShowActivity {

    private EditText edt_wallet_name;
    private TextView tv_next;
    private TextView tv_step;

    /***根据不同的界面跳转过来的带的不同的参数***/
    private int addSource;
    private String mnemonic;
    private String privateKey;
    private String keyStore;

    private String walletName;
    private String pwd;

    private boolean createLock = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet_step_1);
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
        addSource = getIntent().getIntExtra("_addSource", -1);
        edt_wallet_name = findViewById(R.id.edt_wallet_name);
        tv_next = findViewById(R.id.tv_next);
        tv_step = findViewById(R.id.tv_step);

        if (addSource == Constant.AddWalletSource.ADD_WALLET_BY_CREATE) {
            ViewCommonUtils.createDefaultActionBar(mActionBar, this, R.string.create_wallet);
        } else {
            ViewCommonUtils.createDefaultActionBar(mActionBar, this, R.string.import_wallet);

        }

        switch (addSource) {
            case Constant.AddWalletSource.ADD_WALLET_BY_IMPORT_MNEMONIC:
                mnemonic = getIntent().getStringExtra("_mnemonic");
                tv_step.setText(getResources().getString(R.string.step_4_2));
                break;
            case Constant.AddWalletSource.ADD_WALLET_BY_IMPORT_PRIVATE_KEY:
                privateKey = getIntent().getStringExtra("_privatekey");
                tv_step.setText(getResources().getString(R.string.step_4_2));
                break;
            case Constant.AddWalletSource.ADD_WALLET_BY_IMPORT_KEY_STORE:
                keyStore = getIntent().getStringExtra("_keystore");
                pwd = getIntent().getStringExtra("_keystorepwd");
                tv_step.setText(getResources().getString(R.string.step_2_2));
                break;
        }

        edt_wallet_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals("")) {
                    if (RegexUtils.checkWalletName(s.toString())) {
                        tv_next.setBackgroundResource(R.drawable.btn_collect_copy);
                    } else {
                        tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                    }
                } else {
                    tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletName = ViewCommonUtils.getEdtString(edt_wallet_name);
                if (!walletName.equals("")) {
                    if (RegexUtils.checkWalletName(walletName)) {
                        if (addSource == Constant.AddWalletSource.ADD_WALLET_BY_IMPORT_KEY_STORE) {
                            if (!createLock) return;
                            createLock = false;
                            importWalletByKeystore(pwd);
                        } else {
                            if (!WalletManager.getInstance().walletNameExists(CreateWalletStepOneActivity.this, walletName)) {
                                LogUtils.log("当前输入的名字是=" + walletName);
                                Intent createIntent = new Intent(CreateWalletStepOneActivity.this, CreateWalletStepTwoActivity.class);
                                createIntent.putExtra("_walletName", walletName);
                                createIntent.putExtra("_addSource", addSource);
                                switch (addSource) {
                                    case Constant.AddWalletSource.ADD_WALLET_BY_IMPORT_MNEMONIC:
                                        createIntent.putExtra("_mnemonic", mnemonic);
                                        break;
                                    case Constant.AddWalletSource.ADD_WALLET_BY_IMPORT_PRIVATE_KEY:
                                        createIntent.putExtra("_privatekey", privateKey);
                                        break;
                                }
                                startActivity(createIntent);
                            } else {
                                showToast(R.string.WALLET_FILE_NAME_EXISTS);
                                createLock = true;
                            }
                        }
                    } else {
                        showToast(R.string.wallet_name_not_format);
                        createLock = true;
                    }
                } else {
                    showToast(R.string.wallet_name_isempty);
                    createLock = true;
                }
            }
        });
    }

    /***
     * 通过keystore导入钱包接口
     */
    public void importWalletByKeystore(String pwd) {
        WalletManager.getInstance().createWallet(this,
                WalletCreatedType.IMPORTED_KEYSTORE,
                StorageSaveType.LOCAL,
                chainType,
                0,
                hdWalletAddressPath,
                keyStore,
                walletName,
                pwd,
                "",
                new IRequestListener<WalletInfo>() {
                    @Override
                    public void onResult(ResultCode resultCode, WalletInfo result) {
                        switch (resultCode) {
                            case SUCCESS:
                                LogUtils.log(className + " -- 通过keyStore导入钱包成功");
                                Intent intent = new Intent(CreateWalletStepOneActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("_walletInfo", result);
                                intent.putExtra("_source", source);
                                startActivity(intent);
                                WalletApplication.setCurrentWallet(result);
                                CurrentWalletSharedPreferences.getInstance(CreateWalletStepOneActivity.this).changeCurrentWallet(result);
                                //添加默认资产
                                SharePreferencesHelper.addDefaultAssets(CreateWalletStepOneActivity.this, result.getAddress(), chainType);
                                break;
                            case FAIL:
                                showToast(R.string.import_failed);
                                createLock = true;
                                break;
                            case WALLET_FILE_KEYSTORE_PWD_ERROR:
                                showToast(R.string.WALLET_FILE_KEYSTORE_PWD_ERROR);
                                createLock = true;
                                break;
                            case WALLET_FILE_ADDRESS_EXISTS:
                                showToast(R.string.WALLET_FILE_ADDRESS_EXISTS);
                                createLock = true;
                                break;
                            case WALLET_FILE_PASSWORD_ERROR:
                                showToast(R.string.WALLET_FILE_PASSWORD_ERROR);
                                createLock = true;
                                break;
                            case KEYSTORE_NOT_SUPPORT:
                                showToast(R.string.KEYSTORE_NOT_SUPPORT);
                                createLock = true;
                                break;
                            case WALLET_FILE_NAME_EXISTS:
                                showToast(R.string.WALLET_FILE_NAME_EXISTS);
                                createLock = true;
                                break;
                            default:
                                showToast(R.string.nothing_err);
                                createLock = true;
                                break;

                        }
                    }
                });
    }
}
