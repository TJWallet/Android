package com.tianji.blockchain.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.tianji.blockchainwallet.WalletManager;
import com.tianji.blockchainwallet.constant.Constants;
import com.tianji.blockchainwallet.constant.enums.ResultCode;
import com.tianji.blockchainwallet.constant.enums.StorageSaveType;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchainwallet.wallet.IRequestListener;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.basic.BasicActionBarActivity;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.EditTextFilterUtils;
import com.tianji.blockchain.utils.RegexUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class EditPasswordActivity extends BasicConnectShowActivity implements View.OnClickListener {
    private EditText edt_password, edt_new_password, edt_new_password_again;
    private RelativeLayout rl_pwd_eye, rl_new_pwd_eye, rl_new_pwda_eye;
    private ImageView img_pwd_eye, img_new_pwd_eye, img_new_pwda_eye;
    private Button btn_ok;
    private WalletInfo walletInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
    }

    @Override
    protected void initView() {
        walletInfo = (WalletInfo) getIntent().getSerializableExtra("_walletInfo");
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.edit_password));
        ViewCommonUtils.buildBackImageView(this, mActionBar);

        edt_password = findViewById(R.id.edt_password);
        edt_new_password = findViewById(R.id.edt_new_password);
        edt_new_password_again = findViewById(R.id.edt_new_password_again);
//        edt_password_tips = findViewById(R.id.edt_password_tips);
        rl_pwd_eye = findViewById(R.id.rl_pwd_eye);
        rl_new_pwd_eye = findViewById(R.id.rl_new_pwd_eye);
        rl_new_pwda_eye = findViewById(R.id.rl_new_pwda_eye);
        img_pwd_eye = findViewById(R.id.img_pwd_eye);
        img_new_pwd_eye = findViewById(R.id.img_new_pwd_eye);
        img_new_pwda_eye = findViewById(R.id.img_new_pwda_eye);
        btn_ok = findViewById(R.id.btn_ok);


        edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        edt_new_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        edt_new_password_again.setTransformationMethod(PasswordTransformationMethod.getInstance());

        rl_pwd_eye.setOnClickListener(this);
        rl_new_pwd_eye.setOnClickListener(this);
        rl_new_pwda_eye.setOnClickListener(this);
        btn_ok.setOnClickListener(this);
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

    private boolean pwdIsHid = true;
    private boolean newpwdIsHid = true;
    private boolean newpwdAIsHid = true;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_pwd_eye:
                ViewCommonUtils.showPwd(pwdIsHid, edt_password, img_pwd_eye);
                pwdIsHid = !pwdIsHid;
                break;
            case R.id.rl_new_pwd_eye:
                ViewCommonUtils.showPwd(newpwdIsHid, edt_new_password, img_new_pwd_eye);
                newpwdIsHid = !newpwdIsHid;
                break;
            case R.id.rl_new_pwda_eye:
                ViewCommonUtils.showPwd(newpwdAIsHid, edt_new_password_again, img_new_pwda_eye);
                newpwdAIsHid = !newpwdAIsHid;
                break;
            case R.id.btn_ok:
                String oldPwd = ViewCommonUtils.getEdtString(edt_password);
                String newPwd = ViewCommonUtils.getEdtString(edt_new_password);
                String newPwda = ViewCommonUtils.getEdtString(edt_new_password_again);
//                String tips = CommonUtils.replaceEnter(ViewCommonUtils.getEdtString(edt_password_tips));
                if (!oldPwd.equals("") && !newPwd.equals("") && !newPwda.equals("")) {
                    if (newPwd.equals(newPwda)) {
                        if (RegexUtils.walletPassword(newPwd)) {
                            WalletManager.getInstance().changeWalletPassword(this, walletInfo, oldPwd, newPwd, walletInfo.getWalletName(), "", new IRequestListener<WalletInfo>() {
                                @Override
                                public void onResult(ResultCode resultCode, WalletInfo result) {

                                    switch (resultCode) {
                                        case SUCCESS:
                                            Intent resultIntent = new Intent();
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("_walletInfo", walletInfo);
                                            resultIntent.putExtras(bundle);
                                            EditPasswordActivity.this.setResult(0x20, resultIntent);
                                            EditPasswordActivity.this.finish();
                                            break;
                                        case FAIL:
                                            showToast(R.string.edit_password_failed);
                                            break;
                                        default:
                                            showToast(R.string.password_wrong);
                                            break;
                                    }
                                }
                            });
                        } else {
                            showToast(R.string.password_has_error);
                        }
                    } else {
                        showToast(R.string.password_not_same);
                    }
                } else {
                    showToast(R.string.password_isempty);
                }

                break;
        }
    }
}
