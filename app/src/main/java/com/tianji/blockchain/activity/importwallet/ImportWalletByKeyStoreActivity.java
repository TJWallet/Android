package com.tianji.blockchain.activity.importwallet;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tianji.blockchainwallet.WalletManager;
import com.tianji.blockchainwallet.constant.enums.Chain;
import com.tianji.blockchainwallet.constant.enums.ResultCode;
import com.tianji.blockchainwallet.wallet.IRequestListener;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicActionBarActivity;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.activity.createwallet.CreateWalletStepOneActivity;
import com.tianji.blockchain.activity.sacn.MipcaActivityCapture;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class ImportWalletByKeyStoreActivity extends BasicConnectShowActivity implements View.OnClickListener {
    private Chain chainType;
    private int source;

    private EditText edt_content;
    private EditText edt_password;
    private ImageView img_pwd_eye;
    private RelativeLayout rl_pwd_eye;
    private TextView tv_next;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRightImgPosition(2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_by_key_store);
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
        chainType = (Chain) getIntent().getSerializableExtra("_chainType");
        source = getIntent().getIntExtra("_source", -1);

        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.import_wallet));
        ViewCommonUtils.buildBackImageView(this, mActionBar);
        ImageView img_qrcode = ViewCommonUtils.buildImageView(this, R.drawable.scan);
        mActionBar.addViewToRight(img_qrcode, 1);
        img_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.openScan(ImportWalletByKeyStoreActivity.this);
            }
        });
        edt_content = findViewById(R.id.edt_content);
        edt_password = findViewById(R.id.edt_password);
        img_pwd_eye = findViewById(R.id.img_pwd_eye);
        rl_pwd_eye = findViewById(R.id.rl_pwd_eye);
        tv_next = findViewById(R.id.tv_next);

        edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        rl_pwd_eye.setOnClickListener(this);
        tv_next.setOnClickListener(this);

        edt_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pwd = ViewCommonUtils.getEdtString(edt_password);
                if (!s.toString().equals("") && !pwd.equals("")) {
                    tv_next.setBackgroundResource(R.drawable.btn_collect_copy);
                } else {
                    tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyStore = ViewCommonUtils.getEdtString(edt_content);
                if (!s.toString().equals("") && !keyStore.equals("")) {
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

    private boolean pwdIsHid = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_pwd_eye:
                ViewCommonUtils.showPwd(pwdIsHid, edt_password, img_pwd_eye);
                pwdIsHid = !pwdIsHid;
                break;
            case R.id.tv_next:
                String keyStore = ViewCommonUtils.getEdtString(edt_content);
                String pwd = ViewCommonUtils.getEdtString(edt_password);
                if (!keyStore.equals("") && !pwd.equals("")) {
                    WalletManager.getInstance().keystoreVerify(this, keyStore, pwd, new IRequestListener() {
                        @Override
                        public void onResult(ResultCode resultCode, Object result) {
                            if (resultCode == ResultCode.SUCCESS) {
                                Intent createIntent = new Intent(ImportWalletByKeyStoreActivity.this, CreateWalletStepOneActivity.class);
                                createIntent.putExtra("_chainType", chainType);
                                createIntent.putExtra("_source", source);
                                createIntent.putExtra("_addSource", Constant.AddWalletSource.ADD_WALLET_BY_IMPORT_KEY_STORE);
                                createIntent.putExtra("_keystore", keyStore.trim());
                                createIntent.putExtra("_keystorepwd", pwd);
                                startActivity(createIntent);
                            } else {
                                LogUtils.log(className + " -- keystore 密码错误");
                                showToast(R.string.password_wrong);
                            }
                        }
                    });
                } else {
                    LogUtils.log(className + " -- keyStore 和 密码 不能为空");
                    showToast(R.string.keystore_password_isempty);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constant.PermissionsCode.CAMERA_PERMISSIONS_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, MipcaActivityCapture.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, 1);
                } else {
                    LogUtils.log("相机权限申请失败");
                }
                break;
        }
    }
}
