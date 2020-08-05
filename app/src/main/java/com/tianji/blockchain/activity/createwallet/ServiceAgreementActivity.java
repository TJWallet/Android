package com.tianji.blockchain.activity.createwallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.utils.ViewCommonUtils;
import com.tianji.blockchainwallet.constant.enums.Chain;

public class ServiceAgreementActivity extends BasicConnectShowActivity implements View.OnClickListener {
    private WebView webView;
    private Button btn_agreed;
    private int chainType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_agreement);
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
        chainType = getIntent().getIntExtra("_chainType", -1);
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.service_agree));
        ViewCommonUtils.buildBackImageView(this, mActionBar);

        webView = findViewById(R.id.webView);
        btn_agreed = findViewById(R.id.btn_agreed);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        webView.loadUrl(Constant.HttpUrl.AGGREEMENT_URL);

        btn_agreed.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (chainType == -1) {
            Intent createIntent = new Intent(ServiceAgreementActivity.this, SelectWalletTypeActivity.class);
            startActivity(createIntent);
        } else {
            Intent createIntent = new Intent(ServiceAgreementActivity.this, SelectAddWalletTypeActivity.class);
            switch (chainType) {
                case 0:
                    Intent createIntentSelect = new Intent(ServiceAgreementActivity.this, SelectWalletTypeActivity.class);
                    startActivity(createIntentSelect);
                    break;
                case 4:
                    createIntent.putExtra("_chainType", Chain.FIL);
                    startActivity(createIntent);
                    break;
            }
        }
    }
}
