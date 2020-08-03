package com.tianji.blockchain.activity.createwallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.MainActivity;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.sharepreferences.CurrentWalletSharedPreferences;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.SharePreferencesHelper;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class CreateWalletStepThreeActivity extends BasicConnectShowActivity implements View.OnClickListener {
    private String walletName;
    private String oldPwd;

    private EditText edt_password;
    private RelativeLayout rl_pwd_eye;
    private ImageView img_pwd_eye;
    private TextView tv_step;

    private TextView tv_next;


    /***根据不同的界面跳转过来的带的不同的参数***/
    private int addSource;
    private String mnemonic;
    private String privateKey;

    private boolean createLock = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet_step_3);
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
        walletName = getIntent().getStringExtra("_walletName");
        oldPwd = getIntent().getStringExtra("_oldPwd");
        addSource = getIntent().getIntExtra("_addSource", -1);



        ViewCommonUtils.createDefaultActionBar(mActionBar, this, R.string.create_wallet);
        edt_password = findViewById(R.id.edt_password);
        rl_pwd_eye = findViewById(R.id.rl_pwd_eye);

        tv_next = findViewById(R.id.tv_next);
        img_pwd_eye = findViewById(R.id.img_pwd_eye);
        tv_step = findViewById(R.id.tv_step);
        rl_pwd_eye.setOnClickListener(this);
        tv_next.setOnClickListener(this);

        if (addSource == Constant.AddWalletSource.ADD_WALLET_BY_CREATE) {
            ViewCommonUtils.createDefaultActionBar(mActionBar, this, R.string.create_wallet);
        } else {
            ViewCommonUtils.createDefaultActionBar(mActionBar, this, R.string.import_wallet);
            tv_step.setText(getResources().getString(R.string.step_4_4));
        }

        switch (addSource) {
            case Constant.AddWalletSource.ADD_WALLET_BY_IMPORT_MNEMONIC:
                mnemonic = getIntent().getStringExtra("_mnemonic");
                break;
            case Constant.AddWalletSource.ADD_WALLET_BY_IMPORT_PRIVATE_KEY:
                privateKey = getIntent().getStringExtra("_privatekey");
                break;
        }

        edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newPwd = s.toString();
                if (newPwd.equals(oldPwd)) {
                    tv_next.setBackgroundResource(R.drawable.btn_collect_copy);
                } else {
                    tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private boolean pwdIsHid = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_pwd_eye:
                ViewCommonUtils.showPwd(pwdIsHid, edt_password, img_pwd_eye);
                pwdIsHid = !pwdIsHid;
                break;
            case R.id.tv_next:
                String pwd = ViewCommonUtils.getEdtString(edt_password);
                if (pwd.equals(oldPwd)) {
                    if (!createLock) return;
                    createLock = false;
                    switch (addSource) {
                        case Constant.AddWalletSource.ADD_WALLET_BY_IMPORT_MNEMONIC:
                            importWalletByMnemonic(pwd);
                            break;
                        case Constant.AddWalletSource.ADD_WALLET_BY_IMPORT_PRIVATE_KEY:
                            importWalletByPrivateKey(pwd);
                            break;
                        case Constant.AddWalletSource.ADD_WALLET_BY_CREATE:
                            createWallet(pwd);
                            break;
                    }
                } else {
                    //两次密码匹配不上
                    showToast(R.string.password_not_same);
                }
                break;
        }
    }

    /***创建新钱包***/
    private void createWallet(String pwd) {
        WalletManager.getInstance().createWallet(this, WalletCreatedType.LOCAL_CREATED, StorageSaveType.LOCAL, chainType, 0, hdWalletAddressPath, null, walletName, pwd, "", new IRequestListener<WalletInfo>() {
            @Override
            public void onResult(ResultCode resultCode, WalletInfo result) {
                switch (resultCode) {
                    case FAIL:
                        LogUtils.logError(className + " -- 创建钱包失败");
                        showToast(R.string.create_wallet_failed);
                        createLock = true;
                        break;
                    case SUCCESS:
                        Intent intent = new Intent(CreateWalletStepThreeActivity.this, CreateWalletSuccessActivity.class);
                        intent.putExtra("_walletInfo", result);
                        intent.putExtra("_pwd", pwd);
                        startActivity(intent);
                        //添加默认资产
                        SharePreferencesHelper.addDefaultAssets(CreateWalletStepThreeActivity.this, result.getAddress(), chainType);
                        break;
                    case WALLET_FILE_ADDRESS_EXISTS:
                        showToast(R.string.WALLET_FILE_ADDRESS_EXISTS);
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

    /***通过助记词导入钱包***/
    private void importWalletByMnemonic(String pwd) {
        WalletManager.getInstance().createWallet(this,
                WalletCreatedType.IMPORTED_MNEMONIC,
                StorageSaveType.LOCAL,
                chainType,
                0,
                hdWalletAddressPath,
                mnemonic,
                walletName,
                pwd,
                "",
                new IRequestListener<WalletInfo>() {
                    @Override
                    public void onResult(ResultCode resultCode, WalletInfo result) {
                        switch (resultCode) {
                            case SUCCESS:
                                LogUtils.log(className + " -- 通过助记词导入钱包成功");
                                Intent intent = new Intent(CreateWalletStepThreeActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("_walletInfo", result);
                                startActivity(intent);
                                WalletApplication.setCurrentWallet(result);
                                CurrentWalletSharedPreferences.getInstance(CreateWalletStepThreeActivity.this).changeCurrentWallet(result);
                                //添加默认资产
                                SharePreferencesHelper.addDefaultAssets(CreateWalletStepThreeActivity.this, result.getAddress(), chainType);
                                break;
                            case FAIL:
                                LogUtils.logError(className + " -- 通过助记词导入钱包失败");
                                showToast(R.string.import_failed);
                                createLock = true;
                                break;
                            case WALLET_FILE_ADDRESS_EXISTS:
                                showToast(R.string.WALLET_FILE_ADDRESS_EXISTS);
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

    /***
     * 通过私钥导入钱包接口
     */
    public void importWalletByPrivateKey(String pwd) {
        WalletManager.getInstance().createWallet(this,
                WalletCreatedType.IMPORTED_PRIVATE_KEY,
                StorageSaveType.LOCAL,
                chainType,
                0,
                hdWalletAddressPath,
                privateKey,
                walletName,
                pwd,
                "",
                new IRequestListener<WalletInfo>() {
                    @Override
                    public void onResult(ResultCode resultCode, WalletInfo result) {
                        switch (resultCode) {
                            case SUCCESS:
                                LogUtils.log(className + " -- 通过私钥导入钱包成功");
                                Intent intent = new Intent(CreateWalletStepThreeActivity.this, MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("_walletInfo", result);
                                startActivity(intent);
                                WalletApplication.setCurrentWallet(result);
                                CurrentWalletSharedPreferences.getInstance(CreateWalletStepThreeActivity.this).changeCurrentWallet(result);
                                //添加默认资产
                                SharePreferencesHelper.addDefaultAssets(CreateWalletStepThreeActivity.this, result.getAddress(), chainType);
                                break;
                            case FAIL:
                                LogUtils.logError(className + " -- 通过私钥导入钱包失败");
                                showToast(R.string.import_failed);
                                createLock = true;
                                break;
                            case WALLET_FILE_ADDRESS_EXISTS:
                                showToast(R.string.WALLET_FILE_ADDRESS_EXISTS);
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
