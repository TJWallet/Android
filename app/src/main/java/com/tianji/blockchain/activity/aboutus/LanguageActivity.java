package com.tianji.blockchain.activity.aboutus;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.PreferencesUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

import java.util.Locale;

public class LanguageActivity extends BasicConnectShowActivity implements View.OnClickListener {
    private RelativeLayout rl_chinese, rl_english;
    private ImageView img_chinese, img_english;
    private TextView tv_ok;

    private int oldLanguagePosition;
    /***0：中文，1：英文***/
    private int languagePosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
    }

    @Override
    protected void initView() {
        super.initView();
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.select_language));
        ViewCommonUtils.buildBackImageView(this, mActionBar);

        rl_chinese = findViewById(R.id.rl_chinese);
        rl_english = findViewById(R.id.rl_english);
        img_chinese = findViewById(R.id.img_chinese);
        img_english = findViewById(R.id.img_english);
        tv_ok = findViewById(R.id.tv_ok);

        updateTvAppLanguage();

        rl_chinese.setOnClickListener(this);
        rl_english.setOnClickListener(this);
        tv_ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_chinese:
                setLanguageSelect(0);
                break;
            case R.id.rl_english:
                setLanguageSelect(1);
                break;
            case R.id.tv_ok:
                changeLanguage(languagePosition);
                finish();
                break;
        }
    }

    private void setLanguageSelect(int language) {
        switch (language) {
            case 0:
                img_chinese.setVisibility(View.VISIBLE);
                img_english.setVisibility(View.GONE);
                languagePosition = 0;
                break;
            case 1:
                img_chinese.setVisibility(View.GONE);
                img_english.setVisibility(View.VISIBLE);
                languagePosition = 1;
                break;
        }
    }

    private void updateTvAppLanguage() {
        // 获取当前系统国家和语言设置
        String appLanguage = PreferencesUtils.getString(LanguageActivity.this, "appLanguage", Constant.LANGUAGE_DEFAULT);
        LogUtils.log("当前记录的默认语言为 " + appLanguage);

        if (appLanguage.equals("default")) {
            LogUtils.i("已打开自定义语言设置，但没有设置默认语言");
            oldLanguagePosition = 0;
            setLanguageSelect(0);
        } else if (appLanguage.equals(Constant.LANGUAGE_ARRAY[0])) {
            LogUtils.i("当前默认语言是中文");
            oldLanguagePosition = 0;
            setLanguageSelect(0);
        } else if (appLanguage.equals(Constant.LANGUAGE_ARRAY[1])) {
            LogUtils.i("当前默认语言是英语");
            oldLanguagePosition = 1;
            setLanguageSelect(1);
        } else {
            LogUtils.i("没有设置默认语言");
            oldLanguagePosition = 0;
            setLanguageSelect(0);
        }
    }

    private void changeLanguage(int position) {
        Resources resources = getResources(); // 获得res资源对象    
        Configuration config = resources.getConfiguration(); // 获得设置对象    
        DisplayMetrics dm = resources.getDisplayMetrics(); // 获得屏幕参数：主要是分辨率，像素等。   
        switch (position) {
            case 0:
                config.locale = Locale.CHINESE;// 设置APP语言设置为中文
                PreferencesUtils.putString(LanguageActivity.this, "appLanguage", Constant.LANGUAGE_ARRAY[0]);
                break;
            case 1:
                config.locale = Locale.ENGLISH;// 设置APP语言设置为英文
                PreferencesUtils.putString(LanguageActivity.this, "appLanguage", Constant.LANGUAGE_ARRAY[1]);
                break;
            default:
                break;
        }

        if (oldLanguagePosition != position) {
            resources.updateConfiguration(config, dm);
            //杀死进程
            CommonUtils.killApp(LanguageActivity.this);
        }
    }
}
