package com.tianji.blockchain.activity.collect;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicActionBarActivity;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.entity.AssetsDetailsItemEntity;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.CompanyUtil;
import com.tianji.blockchain.utils.ImageLoaderHelper;
import com.tianji.blockchain.utils.QrCodeUtils;
import com.tianji.blockchain.utils.ShareHelper;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class CollectActivity extends BasicConnectShowActivity implements View.OnClickListener {
    private ImageView img_icon;
    private TextView tv_collect_title;
    private ImageView img_share;
    private EditText edt_set_amount;
    private ImageView img_qrcode;
    private TextView tv_address;
    private Button btn_copy_address;

    private WalletInfo walletInfo;

    private Bitmap qrBitmap;

    private AssetsDetailsItemEntity assetsDetails;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
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
        walletInfo = (WalletInfo) getIntent().getSerializableExtra("_walletInfo");
        assetsDetails = (AssetsDetailsItemEntity) getIntent().getSerializableExtra("_assetsDetailsItemEntity");
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.collect));
        ViewCommonUtils.buildBackImageView(this, mActionBar);

        img_icon = findViewById(R.id.img_icon);
        tv_collect_title = findViewById(R.id.tv_collect_title);
        img_share = findViewById(R.id.img_share);
        edt_set_amount = findViewById(R.id.edt_set_amount);
        img_qrcode = findViewById(R.id.img_qrcode);
        tv_address = findViewById(R.id.tv_address);
        btn_copy_address = findViewById(R.id.btn_copy_address);

        btn_copy_address.setOnClickListener(this);
        img_share.setOnClickListener(this);

        edt_set_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Bitmap qrBitmap = QrCodeUtils.createSimpleQRCodeBitmap(walletInfo.getAddress() + "&" + s, CompanyUtil.dip2px(CollectActivity.this, 228f), CompanyUtil.dip2px(CollectActivity.this, 228f), "UTF-8", "H", "2", Color.BLACK, Color.WHITE);
                img_qrcode.setImageBitmap(qrBitmap);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        qrBitmap = QrCodeUtils.createSimpleQRCodeBitmap(walletInfo.getAddress(), CompanyUtil.dip2px(this, 228f), CompanyUtil.dip2px(this, 228f), "UTF-8", "H", "2", Color.BLACK, Color.WHITE);
        if (qrBitmap != null) {
            img_qrcode.setImageBitmap(qrBitmap);
        }

        if (assetsDetails != null) {
            tv_collect_title.setText(getResources().getString(R.string.scan_qrcord_collect) + " FIL");
            img_icon.setImageResource(R.drawable.file_coin_select);
        }

        tv_address.setText(walletInfo.getAddress());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_copy_address:
                CommonUtils.copyContent(this, walletInfo.getAddress());
                break;
            case R.id.img_share:
                if (qrBitmap != null) {
                    ShareHelper.getInstance(this).shareImage(qrBitmap);
                }
                break;
        }
    }
}
