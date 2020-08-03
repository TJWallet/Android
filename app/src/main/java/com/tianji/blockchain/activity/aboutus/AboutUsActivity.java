package com.tianji.blockchain.activity.aboutus;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.Nullable;

import android.widget.*;

import com.android.volley.VolleyError;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.activity.commonwebview.CommonWebviewActivity;
import com.tianji.blockchain.activity.home.HomeActivity;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.dialog.QrcodeDialog;
import com.tianji.blockchain.dialog.TipsDialog;
import com.tianji.blockchain.dialog.UpDateDialog;
import com.tianji.blockchain.entity.DialogEntity;
import com.tianji.blockchain.entity.UpdateEntity;
import com.tianji.blockchain.sharepreferences.CommonSharedPreferences;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

import java.util.HashMap;
import java.util.Map;

public class AboutUsActivity extends BasicConnectShowActivity implements View.OnClickListener, BasicMvpInterface {
    private TextView tv_version;
    private RelativeLayout rl_agreement;
    private RelativeLayout rl_privacy;
    private RelativeLayout rl_updates;
    private RelativeLayout rl_off_website;
    private RelativeLayout rl_wechat_public_account;
    private RelativeLayout rl_off_email;
    private TextView tv_check_update;
    private View line_developer;


    private UpDateDialog dateDialog;
    private TipsDialog emailDialog;
    private QrcodeDialog wechatDialog;
    private RelativeLayout rl_developer;

    private int versionCount = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
    }

    @Override
    protected void setData() {
        if (presenter == null) {
            presenter = new AboutUsPresenter(this, this);
        }
        Map<String, String> params = new HashMap<>();
        presenter.getData(params);
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void initReceiver() {

    }

    @Override
    protected void initView() {
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.about_as));
        ViewCommonUtils.buildBackImageView(this, mActionBar);

        tv_version = findViewById(R.id.tv_version);
        rl_agreement = findViewById(R.id.rl_agreement);
        rl_privacy = findViewById(R.id.rl_privacy);
        rl_updates = findViewById(R.id.rl_updates);
        rl_off_website = findViewById(R.id.rl_off_website);
        rl_wechat_public_account = findViewById(R.id.rl_wechat_public_account);
        rl_off_email = findViewById(R.id.rl_off_email);
//        rl_feedback = findViewById(R.id.rl_feedback);
        tv_check_update = findViewById(R.id.tv_check_update);
        rl_developer = findViewById(R.id.rl_developer);
        line_developer = findViewById(R.id.line_developer);

        tv_version.setText("V" + CommonUtils.getAppVersionName(this));

        rl_agreement.setOnClickListener(this);
        rl_privacy.setOnClickListener(this);
        rl_updates.setOnClickListener(this);
        rl_off_website.setOnClickListener(this);
        rl_wechat_public_account.setOnClickListener(this);
        rl_off_email.setOnClickListener(this);
        tv_version.setOnClickListener(this);
        rl_developer.setOnClickListener(this);
//        rl_feedback.setOnClickListener(this);

        /**
         * 硬件提示
         */
        DialogEntity dialogEntity = new DialogEntity(getResources().getString(R.string.off_email),
                getResources().getString(R.string.off_email_content),
                getResources().getString(R.string.copy),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommonUtils.copyContent(AboutUsActivity.this, getResources().getString(R.string.off_email_content));
                        if (emailDialog.isShowing()) {
                            emailDialog.dismiss();
                        }
                    }
                });
        emailDialog = new TipsDialog(this, dialogEntity);

        wechatDialog = new QrcodeDialog(this, getResources().getString(R.string.wechat_public_account), getResources().getString(R.string.wechat_public_account_content), R.drawable.wechat_public_account, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wechat_public_account);
                MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, getResources().getString(R.string.wechat_public_account), getResources().getString(R.string.wechat_public_account_content));
                showToast(R.string.save_img_success);
                if (wechatDialog.isShowing()) {
                    wechatDialog.dismiss();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (WalletApplication.getApp().isDeveloperVersion()) {
            rl_developer.setVisibility(View.VISIBLE);
            line_developer.setVisibility(View.VISIBLE);
        } else {
            rl_developer.setVisibility(View.GONE);
            line_developer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_agreement:
                Intent intentAgreement = new Intent(this, CommonWebviewActivity.class);
                intentAgreement.putExtra("_title", getResources().getString(R.string.service_agreement));
                intentAgreement.putExtra("_url", Constant.HttpUrl.AGGREEMENT_URL);
                startActivity(intentAgreement);
                break;
            case R.id.rl_privacy:
                Intent intentPrivacy = new Intent(this, CommonWebviewActivity.class);
                intentPrivacy.putExtra("_title", getResources().getString(R.string.privacy_policy));
                intentPrivacy.putExtra("_url", Constant.HttpUrl.PRIVACY_URL);
                startActivity(intentPrivacy);
                break;
            case R.id.rl_updates:
                if (tv_check_update.getVisibility() == View.VISIBLE) {
                    if (dateDialog != null) {
                        dateDialog.show();
                    }
                } else {
                    showToast(R.string.already_new_version);
                }
                break;
            case R.id.rl_off_website:
                String url = "https://tjwallet.net";
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            case R.id.rl_wechat_public_account:
                wechatDialog.show();
                break;
            case R.id.rl_off_email:
                emailDialog.show();
                break;
//            case R.id.rl_feedback:
//                break;
            case R.id.tv_version:
                if (rl_developer.getVisibility() == View.GONE) {
                    if (versionCount < 10) {
                        versionCount += 1;
                    } else {
                        rl_developer.setVisibility(View.VISIBLE);
                        line_developer.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.rl_developer:
                startActivity(DeveloperActivity.class);
                break;
        }
    }

    @Override
    public void getDataSuccess(Object object, int type) {
        UpdateEntity entity = (UpdateEntity) object;
        long code = CommonUtils.getAppVersionCode(this);
        if (code < entity.getVersionCode()) {
            tv_check_update.setVisibility(View.VISIBLE);
            dateDialog = new UpDateDialog(this, entity);
        } else {
            tv_check_update.setVisibility(View.GONE);
        }
    }

    @Override
    public void getDataFail(Object error, int type) {

    }

    @Override
    public void getDataFail(int errCode, String errMsg, int type) {

    }


}
