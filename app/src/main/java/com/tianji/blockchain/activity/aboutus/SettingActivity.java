package com.tianji.blockchain.activity.aboutus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.PreferencesUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;


public class SettingActivity extends BasicConnectShowActivity implements View.OnClickListener {
    private RelativeLayout rl_language, rl_currency;
    private TextView tv_language, tv_currency;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @Override
    protected void initView() {
        super.initView();
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.setting));
        ViewCommonUtils.buildBackImageView(this, mActionBar);

        rl_language = findViewById(R.id.rl_language);
        tv_language = findViewById(R.id.tv_language);
        rl_currency = findViewById(R.id.rl_currency);
        tv_currency = findViewById(R.id.tv_currency);

        rl_language.setOnClickListener(this);
        rl_currency.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTvAppLanguage();
        updateTvCurrency();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_language:
                startActivity(new Intent(this, LanguageActivity.class));
                break;
            case R.id.rl_currency:
                startActivity(new Intent(this, CurrencyActivity.class));
                break;
        }
    }

    private void updateTvCurrency() {
        switch (WalletApplication.getApp().getCurrentCurrency()) {
            case Constant.CurrencyType.TYPE_CNY:
                tv_currency.setText(R.string.rmb);
                break;
            case Constant.CurrencyType.TYPE_USD:
                tv_currency.setText(R.string.usd);
                break;
        }
    }

    private void updateTvAppLanguage() {
        // 获取当前系统国家和语言设置
        String appLanguage = PreferencesUtils.getString(SettingActivity.this, "appLanguage", Constant.LANGUAGE_DEFAULT);
        LogUtils.log("当前记录的默认语言为 " + appLanguage);

        if (appLanguage.equals("default")) {
            LogUtils.i("已打开自定义语言设置，但没有设置默认语言");
            tv_language.setText("none");
        } else if (appLanguage.equals(Constant.LANGUAGE_ARRAY[0])) {
            LogUtils.i("当前默认语言是中文");
            tv_language.setText(Constant.LANGUAGE_ARRAY[0]);
        } else if (appLanguage.equals(Constant.LANGUAGE_ARRAY[1])) {
            LogUtils.i("当前默认语言是英语");
            tv_language.setText(Constant.LANGUAGE_ARRAY[1]);
        } else {
            LogUtils.i("没有设置默认语言");
            tv_language.setText(Constant.LANGUAGE_ARRAY[0]);
        }
    }
}
