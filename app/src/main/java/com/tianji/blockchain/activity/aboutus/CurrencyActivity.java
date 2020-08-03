package com.tianji.blockchain.activity.aboutus;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.sharepreferences.CommonSharedPreferences;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class CurrencyActivity extends BasicConnectShowActivity implements View.OnClickListener {
    private RelativeLayout rl_cny, rl_usd;
    private ImageView img_cny, img_usd;
    private TextView tv_ok;

    private int currentCurrencyType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
    }

    @Override
    protected void initView() {
        super.initView();
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.currency_unit));
        ViewCommonUtils.buildBackImageView(this, mActionBar);

        rl_cny = findViewById(R.id.rl_cny);
        rl_usd = findViewById(R.id.rl_usd);
        img_cny = findViewById(R.id.img_cny);
        img_usd = findViewById(R.id.img_usd);
        tv_ok = findViewById(R.id.tv_ok);

        currentCurrencyType = WalletApplication.getApp().getCurrentCurrency();
        setCurrencySelect(currentCurrencyType);

        rl_cny.setOnClickListener(this);
        rl_usd.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_cny:
                setCurrencySelect(Constant.CurrencyType.TYPE_CNY);
                break;
            case R.id.rl_usd:
                setCurrencySelect(Constant.CurrencyType.TYPE_USD);
                break;
            case R.id.tv_ok:
                WalletApplication.getApp().setCurrentCurrency(currentCurrencyType);
                CommonSharedPreferences.getInstance(this).changeCurrency(currentCurrencyType);
                finish();
                break;
        }
    }

    private void setCurrencySelect(int language) {
        switch (language) {
            case Constant.CurrencyType.TYPE_CNY:
                img_cny.setVisibility(View.VISIBLE);
                img_usd.setVisibility(View.GONE);
                currentCurrencyType = 1;
                break;
            case Constant.CurrencyType.TYPE_USD:
                img_cny.setVisibility(View.GONE);
                img_usd.setVisibility(View.VISIBLE);
                currentCurrencyType = 2;
                break;
        }
    }
}
