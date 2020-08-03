package com.tianji.blockchain.activity.importwallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.tianji.blockchain.adapter.basic.OnItemClickListener;
import com.tianji.blockchain.dialog.SelectDialog;
import com.tianji.blockchain.sharepreferences.CurrentWalletSharedPreferences;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.RegexUtils;
import com.tianji.blockchain.utils.SharePreferencesHelper;
import com.tianji.blockchain.utils.ViewCommonUtils;

import java.util.ArrayList;
import java.util.List;

public class CompleteInfoActivity extends BasicConnectShowActivity implements View.OnClickListener {

    private StorageSaveType saveType;

    private TextView tv_save_path;
    private LinearLayout ll_save_path;
    private EditText edt_wallet_name;
    private EditText edt_password;
    private RelativeLayout rl_pwd_eye;
    private ImageView img_pwd_eye;
    private EditText edt_password_again;
    private RelativeLayout rl_pwd_eye_again;
    private ImageView img_pwd_eye_again;
    private EditText edt_pwd_remind;
    private Button btn_ok;

    private SelectDialog dialog;

    private boolean pwdIsHid = true;
    private boolean pwdAIsHid = true;

    private int importType;
    private String mnemonics;
    private String privatekey;
    private WalletInfo walletInfo;
    private String keystorePassword;
    private int source;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_info);
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
        importType = getIntent().getIntExtra("_importType", -1);
        source = getIntent().getIntExtra("_source", -1);
        switch (importType) {
            case Constant.ImportWallet.IMPORTTYPE_MNEMONICS:
                mnemonics = getIntent().getStringExtra("_mnemonics");
                break;
            case Constant.ImportWallet.IMPORTTYPE_PRIVATEKEY:
                privatekey = getIntent().getStringExtra("_privatekey");
                break;
            case Constant.ImportWallet.IMPORTTYPE_KEYSTORE:
                walletInfo = (WalletInfo) getIntent().getSerializableExtra("_keystoreWallet");
                keystorePassword = getIntent().getStringExtra("_password");
                break;
        }
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.complete_info));
        ViewCommonUtils.buildBackImageView(this, mActionBar);

        tv_save_path = findViewById(R.id.tv_save_path);
        ll_save_path = findViewById(R.id.ll_save_path);
        edt_wallet_name = findViewById(R.id.edt_wallet_name);
        edt_password = findViewById(R.id.edt_password);
        rl_pwd_eye = findViewById(R.id.rl_pwd_eye);
        img_pwd_eye = findViewById(R.id.img_pwd_eye);
        edt_password_again = findViewById(R.id.edt_password_again);
        rl_pwd_eye_again = findViewById(R.id.rl_pwd_eye_again);
        img_pwd_eye_again = findViewById(R.id.img_pwd_eye_again);
        edt_pwd_remind = findViewById(R.id.edt_pwd_remind);
        btn_ok = findViewById(R.id.btn_ok);

        edt_password_again.setTransformationMethod(PasswordTransformationMethod.getInstance());
        edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        ll_save_path.setOnClickListener(this);
        rl_pwd_eye.setOnClickListener(this);
        rl_pwd_eye_again.setOnClickListener(this);
        btn_ok.setOnClickListener(this);


        //存储默认为本地
        saveType = StorageSaveType.LOCAL;
        final List<String> saveTypeList = new ArrayList<>();
        saveTypeList.add(getResources().getString(R.string.save_local));
        saveTypeList.add(getResources().getString(R.string.save_hardware));
//        //初始化底部弹窗
//        dialog = new SelectDialog(this, R.style.Wallet_Manager_Dialog, saveTypeList, "储存位置", new OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position, Object obj) {
//                tv_save_path.setText(saveTypeList.get(position));
//                switch (position) {
//                    case 0:
//                        saveType = StorageSaveType.LOCAL;
//                        break;
//                    case 1:
//                        saveType = StorageSaveType.EXTERNAL;
//                        break;
//                }
//
//                dialog.dismiss();
//            }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_save_path:
                dialog.show();
                break;
            case R.id.rl_pwd_eye:
                ViewCommonUtils.showPwd(pwdIsHid, edt_password, img_pwd_eye);
                pwdIsHid = !pwdIsHid;
                break;
            case R.id.rl_pwd_eye_again:
                ViewCommonUtils.showPwd(pwdAIsHid, edt_password_again, img_pwd_eye_again);
                pwdAIsHid = !pwdAIsHid;
                break;
            case R.id.btn_ok:
                String walletName = CommonUtils.replaceEnter(ViewCommonUtils.getEdtString(edt_wallet_name));
                String pwd = ViewCommonUtils.getEdtString(edt_password);
                String pwda = ViewCommonUtils.getEdtString(edt_password_again);
                String tips = CommonUtils.replaceEnter(ViewCommonUtils.getEdtString(edt_pwd_remind));
                if (!walletName.equals("") && !pwd.equals("") && !pwda.equals("")) {
                    if (pwd.equals(pwda)) {
                        if (RegexUtils.checkWalletName(walletName)) {
                            if (RegexUtils.walletPassword(pwd)) {
                                switch (importType) {
                                    case Constant.ImportWallet.IMPORTTYPE_MNEMONICS:
                                        importWalletByMnemonic(walletName, pwd, tips, saveType, new IRequestListener<WalletInfo>() {
                                            @Override
                                            public void onResult(ResultCode resultCode, WalletInfo result) {
                                                if (resultCode == ResultCode.SUCCESS) {
                                                    LogUtils.log("助记词导入成功");
                                                    startPage(result);
                                                } else {
                                                    showToast("导入失败");
                                                }
                                            }
                                        });
                                        break;
                                    case Constant.ImportWallet.IMPORTTYPE_PRIVATEKEY:
                                        importWalletByPrivateKey(walletName, pwd, tips, saveType, new IRequestListener<WalletInfo>() {
                                            @Override
                                            public void onResult(ResultCode resultCode, WalletInfo result) {
                                                if (resultCode == ResultCode.SUCCESS) {
                                                    LogUtils.log("私钥导入成功");
                                                    startPage(result);
                                                } else {
                                                    showToast("导入失败");
                                                }
                                            }
                                        });
                                        break;
                                    case Constant.ImportWallet.IMPORTTYPE_KEYSTORE:
                                        LogUtils.log("旧密码" + keystorePassword + "||新密码" + pwd);
                                        changeWalletPassword(walletInfo, keystorePassword, pwd, walletName, tips, new IRequestListener<WalletInfo>() {
                                            @Override
                                            public void onResult(ResultCode resultCode, WalletInfo result) {
                                                if (resultCode == ResultCode.SUCCESS) {
                                                    LogUtils.log("keystore导入成功");
                                                    startPage(result);
                                                } else {
                                                    showToast("导入失败");
                                                }
                                            }
                                        });
                                        break;
                                }
                            } else {
                                showToast("钱包密码为8-20位大小写字母数字组成");
                            }
                        } else {
                            showToast("钱包名称不符合规范");
                        }
                    } else {
                        showToast("两次密码输入不同");
                    }
                } else {
                    showToast("钱包名称，密码不能为空");
                }
                break;
        }
    }


    /***
     * 通过助记词导入钱包接口
     * @return
     */
    public void importWalletByMnemonic(String walletName, String password, String passwordTips, StorageSaveType saveType, IRequestListener<WalletInfo> listener) {
        WalletManager.getInstance().createWallet(this,
                WalletCreatedType.IMPORTED_MNEMONIC,
                saveType,
                Chain.ETH,
                0,
                HDWalletAddressPath.ETH,
                mnemonics,
                walletName,
                password,
                passwordTips, listener);

    }

    /***
     * 通过私钥导入钱包接口
     */
    public void importWalletByPrivateKey(String walletName, String password, String passwordTips, StorageSaveType saveType, IRequestListener<WalletInfo> listener) {
        WalletManager.getInstance().createWallet(this,
                WalletCreatedType.IMPORTED_PRIVATE_KEY,
                saveType,
                Chain.ETH,
                0,
                HDWalletAddressPath.ETH,
                privatekey,
                walletName,
                password,
                passwordTips,
                listener);
    }

    /***
     * 修改钱包密码接口
     */
    public void changeWalletPassword(WalletInfo walletInfo, String password, String newPassword, String walletName, String newPasswordTips, IRequestListener<WalletInfo> listener) {
        LogUtils.log("changeWalletPassword");
        WalletManager.getInstance().changeWalletPassword(this, walletInfo, password, newPassword, walletName, newPasswordTips, listener);
    }

    private void startPage(WalletInfo info) {
        if (source == Constant.CreateWalletSource.SOURCE_HOME_ACTIVITY) {

            WalletInfo walletInfo = CommonUtils.writeWalletInfo(info);

            //添加默认资产
            LogUtils.log(className + " -- addDefaultAssets");
            SharePreferencesHelper.addDefaultAssets(CompleteInfoActivity.this, walletInfo.getAddress(), Chain.ETH);
            WalletApplication.setCurrentWallet(walletInfo);
            CurrentWalletSharedPreferences.getInstance(CompleteInfoActivity.this).changeCurrentWallet(walletInfo);

            Intent intent = new Intent(CompleteInfoActivity.this, MainActivity.class);
            intent.putExtra("_walletInfo", walletInfo);
            startActivity(intent);

//            for (int i = 0; i < WalletApplication.getApp().getActivityList().size(); i++) {
//                WalletApplication.getApp().getActivityList().get(i).finish();
//            }
//            WalletApplication.getApp().getActivityList().clear();
            finish();
        } else if (source == Constant.CreateWalletSource.SOURCE_MIAN_ACTIVITY) {
            WalletInfo walletInfo = CommonUtils.writeWalletInfo(info);
            SharePreferencesHelper.addDefaultAssets(CompleteInfoActivity.this, walletInfo.getAddress(), Chain.ETH);
            WalletApplication.setCurrentWallet(walletInfo);
            CurrentWalletSharedPreferences.getInstance(this).changeCurrentWallet(walletInfo);
            Intent resultIntent = new Intent();
            setResult(Constant.StartActivityResultCode.START_CREATE_WALLET, resultIntent);

//            for (int i = 0; i < WalletApplication.getApp().getActivityList().size(); i++) {
//                WalletApplication.getApp().getActivityList().get(i).finish();
//            }
//            WalletApplication.getApp().getActivityList().clear();
            finish();
        }

    }

}
