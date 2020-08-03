package com.tianji.blockchain.activity.importwallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.tianji.blockchain.activity.basic.BasicActionBarActivity;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.RegexUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class ImportWalletActivity extends BasicConnectShowActivity implements View.OnClickListener {

    private TextView tv_mnemonics, tv_private_key, tv_keystore;
    private EditText edt_content, edt_password;
    private Button btn_ok;
    private LinearLayout ll_password;
    private ImageView img_pwd_eye;
    private RelativeLayout rl_pwd_eye;

    private int importType = Constant.ImportWallet.IMPORTTYPE_MNEMONICS;

    private boolean passwordIsHid = true;
    private int source;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRightImgPosition(2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_improt_walelt);
    }

    @Override
    protected void initView() {
//        WalletApplication.getApp().addActivity(this);
        source = getIntent().getIntExtra("_source", -1);
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.import_wallet));
        ViewCommonUtils.buildBackImageView(this, mActionBar);
        ImageView img_qrcode = ViewCommonUtils.buildImageView(this, R.drawable.scan);
        mActionBar.addViewToRight(img_qrcode, 1);
        img_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.openScan(ImportWalletActivity.this);
            }
        });
        tv_mnemonics = findViewById(R.id.tv_mnemonics);
        tv_private_key = findViewById(R.id.tv_private_key);
        tv_keystore = findViewById(R.id.tv_keystore);
        edt_content = findViewById(R.id.edt_content);
        edt_password = findViewById(R.id.edt_password);
        btn_ok = findViewById(R.id.btn_ok);
        ll_password = findViewById(R.id.ll_password);
        img_pwd_eye = findViewById(R.id.img_pwd_eye);
        rl_pwd_eye = findViewById(R.id.rl_pwd_eye);

        edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        tv_mnemonics.setOnClickListener(this);
        tv_private_key.setOnClickListener(this);
        tv_keystore.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
        rl_pwd_eye.setOnClickListener(this);
        btn_ok.setEnabled(false);

        edt_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = s.toString();
                if (!str.equals("")) {
                    btn_ok.setBackgroundResource(R.drawable.radius_btn_normal);
                    btn_ok.setEnabled(true);
                } else {
                    btn_ok.setBackgroundResource(R.drawable.radius_btn_noenabled);
                    btn_ok.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_mnemonics:
                cutoverImportType(tv_mnemonics, Constant.ImportWallet.IMPORTTYPE_MNEMONICS);
                break;
            case R.id.tv_private_key:
                cutoverImportType(tv_private_key, Constant.ImportWallet.IMPORTTYPE_PRIVATEKEY);
                break;
            case R.id.tv_keystore:
                cutoverImportType(tv_keystore, Constant.ImportWallet.IMPORTTYPE_KEYSTORE);
                break;
            case R.id.btn_ok:
                final Intent importIntent = new Intent(this, CompleteInfoActivity.class);
                String content = ViewCommonUtils.getEdtString(edt_content);
                switch (importType) {
                    case Constant.ImportWallet.IMPORTTYPE_MNEMONICS:
                        String mnemonic = CommonUtils.formatMnemonic(content);
                        if (mnemonic != null) {
                            importIntent.putExtra("_importType", Constant.ImportWallet.IMPORTTYPE_MNEMONICS);
                            importIntent.putExtra("_mnemonics", mnemonic);
                            importIntent.putExtra("_source", source);
                            startActivity(importIntent);
                        } else {
                            showToast("助记词格式不对");
                        }
                        break;
                    case Constant.ImportWallet.IMPORTTYPE_PRIVATEKEY:
                        importIntent.putExtra("_importType", Constant.ImportWallet.IMPORTTYPE_PRIVATEKEY);
                        importIntent.putExtra("_privatekey", content);
                        importIntent.putExtra("_source", source);
                        startActivity(importIntent);
                        break;
                    case Constant.ImportWallet.IMPORTTYPE_KEYSTORE:
                        LogUtils.log("点击下一步");
                        final String password = edt_password.getText().toString().trim();
                        if (!password.equals("")) {
                            if (RegexUtils.walletPassword(password)) {
                                importWalletByKeystore("", password, "", content, StorageSaveType.LOCAL, new IRequestListener<WalletInfo>() {
                                    @Override
                                    public void onResult(ResultCode resultCode, WalletInfo result) {
                                        if (resultCode == ResultCode.SUCCESS) {
                                            if (result != null) {
                                                WalletInfo walletInfo = CommonUtils.writeWalletInfo(result);
                                                importIntent.putExtra("_importType", Constant.ImportWallet.IMPORTTYPE_KEYSTORE);
                                                importIntent.putExtra("_keystoreWallet", walletInfo);
                                                importIntent.putExtra("_password", password);
                                                importIntent.putExtra("_source", source);
                                                startActivity(importIntent);
                                            } else {
                                                showToast("密码验证失败");
                                            }
                                        } else if (resultCode == ResultCode.WALLET_FILE_KEYSTORE_PWD_ERROR) {
                                            showToast("密码验证失败");
                                        } else {
                                            showToast("导入失败");
                                        }
                                    }
                                });

                            } else {
                                showToast("密码格式不对");
                            }
                        } else {
                            showToast("密码不能为空");
                        }
                        break;
                }

                break;
            case R.id.rl_pwd_eye:
                ViewCommonUtils.showPwd(passwordIsHid, edt_password, img_pwd_eye);
                passwordIsHid = !passwordIsHid;
                break;
        }
    }

    /**
     * 切换导入模式
     *
     * @param textView
     * @param type
     */
    private void cutoverImportType(TextView textView, int type) {
        tv_mnemonics.setTextColor(getResources().getColor(R.color.tertiary));
        tv_private_key.setTextColor(getResources().getColor(R.color.tertiary));
        tv_keystore.setTextColor(getResources().getColor(R.color.tertiary));
        textView.setTextColor(getResources().getColor(R.color.textMajor));
        importType = type;
        ll_password.setVisibility(View.GONE);
        switch (type) {
            case Constant.ImportWallet.IMPORTTYPE_MNEMONICS:
                edt_content.setHint(getResources().getString(R.string.import_by_mnemonics_hint));
                break;
            case Constant.ImportWallet.IMPORTTYPE_PRIVATEKEY:
                edt_content.setHint(getResources().getString(R.string.import_by_private_key_hint));
                break;
            case Constant.ImportWallet.IMPORTTYPE_KEYSTORE:
                edt_content.setHint(getResources().getString(R.string.import_by_keystore_hint));
                ll_password.setVisibility(View.VISIBLE);
                break;
        }
    }

    /***
     * 通过keystore导入钱包接口
     */
    public void importWalletByKeystore(String walletName, String password, String passwordTips, String keystore, StorageSaveType saveType, IRequestListener<WalletInfo> listener) {
        WalletManager.getInstance().createWallet(this,
                WalletCreatedType.IMPORTED_KEYSTORE,
                saveType,
                Chain.ETH,
                0,
                HDWalletAddressPath.ETH,
                keystore,
                walletName,
                password,
                passwordTips, listener);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    LogUtils.log("首页扫描内容" + bundle.getString("result"));
                    edt_content.setText(bundle.getString("result"));
                }
                break;
        }
    }

}
