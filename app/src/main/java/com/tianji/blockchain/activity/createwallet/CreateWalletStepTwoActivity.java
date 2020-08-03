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
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.RegexUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class CreateWalletStepTwoActivity extends BasicConnectShowActivity implements View.OnClickListener {
    private String walletName;

    private EditText edt_password;
    private RelativeLayout rl_pwd_eye;
    private ImageView img_pwd_eye;
    private TextView tv_step;

    private ImageView img_factor_1, img_factor_2, img_factor_3, img_factor_4;
    private TextView tv_factor_1, tv_factor_2, tv_factor_3, tv_factor_4;

    private TextView tv_next;

    /***根据不同的界面跳转过来的带的不同的参数***/
    private int addSource;
    private String mnemonic;
    private String privateKey;
    private String keyStore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet_step_2);
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
        addSource = getIntent().getIntExtra("_addSource", -1);

        edt_password = findViewById(R.id.edt_password);
        rl_pwd_eye = findViewById(R.id.rl_pwd_eye);
        img_factor_1 = findViewById(R.id.img_factor_1);
        img_factor_2 = findViewById(R.id.img_factor_2);
        img_factor_3 = findViewById(R.id.img_factor_3);
        img_factor_4 = findViewById(R.id.img_factor_4);
        tv_factor_1 = findViewById(R.id.tv_factor_1);
        tv_factor_2 = findViewById(R.id.tv_factor_2);
        tv_factor_3 = findViewById(R.id.tv_factor_3);
        tv_factor_4 = findViewById(R.id.tv_factor_4);
        tv_next = findViewById(R.id.tv_next);
        img_pwd_eye = findViewById(R.id.img_pwd_eye);
        tv_step = findViewById(R.id.tv_step);

        rl_pwd_eye.setOnClickListener(this);
        tv_next.setOnClickListener(this);

        if (addSource == Constant.AddWalletSource.ADD_WALLET_BY_CREATE) {
            ViewCommonUtils.createDefaultActionBar(mActionBar, this, R.string.create_wallet);
        } else {
            ViewCommonUtils.createDefaultActionBar(mActionBar, this, R.string.import_wallet);
            tv_step.setText(getResources().getString(R.string.step_4_3));
        }

        switch (addSource) {
            case Constant.AddWalletSource.ADD_WALLET_BY_IMPORT_MNEMONIC:
                mnemonic = getIntent().getStringExtra("_mnemonic");
                break;
            case Constant.AddWalletSource.ADD_WALLET_BY_IMPORT_PRIVATE_KEY:
                privateKey = getIntent().getStringExtra("_privatekey");
                break;
            case Constant.AddWalletSource.ADD_WALLET_BY_IMPORT_KEY_STORE:
                keyStore = getIntent().getStringExtra("_keystore");
                break;
        }

        edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());

        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LogUtils.log("输入框内容是" + s.toString());
                if (RegexUtils.upWord(s.toString())) {
                    LogUtils.log("包含大写字母");
                    changeType(true, 1);
                } else {
                    changeType(false, 1);
                }
                if (RegexUtils.downWord(s.toString())) {
                    changeType(true, 2);
                } else {
                    changeType(false, 2);
                }
                if (RegexUtils.number(s.toString())) {
                    changeType(true, 3);
                } else {
                    changeType(false, 3);
                }

                if (s.toString().length() > 7 && s.toString().length() <= 20) {
                    changeType(true, 4);
                } else {
                    changeType(false, 4);
                }

                if (!s.toString().equals("")) {
                    if (RegexUtils.walletPassword(s.toString())) {
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
    }

    private void changeType(boolean b, int i) {
        switch (i) {
            case 1:
                if (b) {
                    img_factor_1.setImageResource(R.drawable.meet_conditions);
                    tv_factor_1.setTextColor(getResources().getColor(R.color.darkCyan));
                } else {
                    img_factor_1.setImageResource(R.drawable.unsatisfied_condition);
                    tv_factor_1.setTextColor(getResources().getColor(R.color.warning_red));
                }
                break;
            case 2:
                if (b) {
                    img_factor_2.setImageResource(R.drawable.meet_conditions);
                    tv_factor_2.setTextColor(getResources().getColor(R.color.darkCyan));
                } else {
                    img_factor_2.setImageResource(R.drawable.unsatisfied_condition);
                    tv_factor_2.setTextColor(getResources().getColor(R.color.warning_red));
                }
                break;
            case 3:
                if (b) {
                    img_factor_3.setImageResource(R.drawable.meet_conditions);
                    tv_factor_3.setTextColor(getResources().getColor(R.color.darkCyan));
                } else {
                    img_factor_3.setImageResource(R.drawable.unsatisfied_condition);
                    tv_factor_3.setTextColor(getResources().getColor(R.color.warning_red));
                }
                break;
            case 4:
                if (b) {
                    img_factor_4.setImageResource(R.drawable.meet_conditions);
                    tv_factor_4.setTextColor(getResources().getColor(R.color.darkCyan));
                } else {
                    img_factor_4.setImageResource(R.drawable.unsatisfied_condition);
                    tv_factor_4.setTextColor(getResources().getColor(R.color.warning_red));
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
                String pwd = ViewCommonUtils.getEdtString(edt_password);
                if (RegexUtils.walletPassword(pwd)) {
                    Intent createIntent = new Intent(CreateWalletStepTwoActivity.this, CreateWalletStepThreeActivity.class);
                    createIntent.putExtra("_walletName", walletName);
                    createIntent.putExtra("_oldPwd", pwd);
                    createIntent.putExtra("_addSource", addSource);
                    switch (addSource) {
                        case Constant.AddWalletSource.ADD_WALLET_BY_IMPORT_MNEMONIC:
                            createIntent.putExtra("_mnemonic", mnemonic);
                            break;
                        case Constant.AddWalletSource.ADD_WALLET_BY_IMPORT_PRIVATE_KEY:
                            createIntent.putExtra("_privatekey", privateKey);
                            break;
                        case Constant.AddWalletSource.ADD_WALLET_BY_IMPORT_KEY_STORE:
                            createIntent.putExtra("_keystore", keyStore);
                            break;
                    }
                    startActivity(createIntent);
                } else {
                    showToast(R.string.password_has_error);
                }
                break;
        }
    }
}
