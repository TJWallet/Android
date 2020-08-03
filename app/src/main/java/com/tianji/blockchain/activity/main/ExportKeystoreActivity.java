package com.tianji.blockchain.activity.main;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicActionBarActivity;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.dialog.QrcodeDialog;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.CompanyUtil;
import com.tianji.blockchain.utils.QrCodeUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class ExportKeystoreActivity extends BasicConnectShowActivity implements View.OnClickListener {
    private TextView tv_keystore;
    private Button btn_copy;
    private Button btn_show_qrcode;
    private String keystore;
    private TextView tv_keystore_password;
    private String pwd;

    private QrcodeDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_keystore);
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
        keystore = getIntent().getStringExtra("_keystore");
        pwd = getIntent().getStringExtra("_pwd");
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.export_keystore));
        ViewCommonUtils.buildBackImageView(this, mActionBar);
        tv_keystore = findViewById(R.id.tv_keystore);
        btn_copy = findViewById(R.id.btn_copy);
        btn_show_qrcode = findViewById(R.id.btn_show_qrcode);
        tv_keystore_password = findViewById(R.id.tv_keystore_password);

        tv_keystore.setText(keystore);
        String showPwd = String.format(getResources().getString(R.string.show_keystore_password), pwd);
        tv_keystore_password.setText(showPwd);
        btn_copy.setOnClickListener(this);
        btn_show_qrcode.setOnClickListener(this);


        dialog = new QrcodeDialog(this, getResources().getString(R.string.keystore_qrcoed_title), getResources().getString(R.string.keystore_qrcoed_desc), keystore, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bit = QrCodeUtils.createSimpleQRCodeBitmap(keystore, CompanyUtil.dip2px(ExportKeystoreActivity.this, 168f), CompanyUtil.dip2px(ExportKeystoreActivity.this, 168f), "UTF-8", "H", "0", Color.BLACK, Color.WHITE);
                MediaStore.Images.Media.insertImage(getContentResolver(), bit, getResources().getString(R.string.wechat_public_account), getResources().getString(R.string.wechat_public_account_content));
                showToast(R.string.save_img_success);
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_copy:
                CommonUtils.copyContent(this, keystore);
                break;
            case R.id.btn_show_qrcode:
                dialog.show();
                break;
        }
    }
}
