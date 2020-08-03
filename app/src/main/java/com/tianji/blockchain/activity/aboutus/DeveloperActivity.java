package com.tianji.blockchain.activity.aboutus;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.activity.home.HomeActivity;
import com.tianji.blockchain.sharepreferences.CommonSharedPreferences;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class DeveloperActivity extends BasicConnectShowActivity implements View.OnClickListener {
    private Switch sw_developer;
    //    private Switch sw_acl;
    private LinearLayout ll_content;
    private RelativeLayout rl_go_home;
    //    private RelativeLayout  rl_acl_environment;
//    private TextView tv_acl_environment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
    }

    @Override
    protected void initView() {
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.developer));
        ViewCommonUtils.buildBackImageView(this, mActionBar);

        sw_developer = findViewById(R.id.sw_developer);
//        sw_acl = findViewById(R.id.sw_acl);
        ll_content = findViewById(R.id.ll_content);
        rl_go_home = findViewById(R.id.rl_go_home);
//        rl_acl_environment = findViewById(R.id.rl_acl_environment);
//        tv_acl_environment = findViewById(R.id.tv_acl_environment);
        loadPage();
        rl_go_home.setOnClickListener(this);
        sw_developer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    WalletApplication.getApp().setDeveloperVersion(true);
                    CommonSharedPreferences.getInstance(DeveloperActivity.this).changeDeveloperMode(true);
                } else {
                    WalletApplication.getApp().setDeveloperVersion(false);
                    CommonSharedPreferences.getInstance(DeveloperActivity.this).changeDeveloperMode(false);
                }
                loadPage();
            }
        });

//        sw_acl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    WalletApplication.getApp().setAclTest(true);
//                    CommonSharedPreferences.getInstance(DeveloperActivity.this).changeAclTest(true);
//                } else {
//                    WalletApplication.getApp().setAclTest(false);
//                    CommonSharedPreferences.getInstance(DeveloperActivity.this).changeAclTest(false);
//                }
//                loadPage();
//            }
//        });
    }

    protected void loadPage() {
        if (WalletApplication.getApp().isDeveloperVersion()) {
            sw_developer.setChecked(true);
            ll_content.setVisibility(View.VISIBLE);
//            if (WalletApplication.getApp().isAclTest()) {
//                sw_acl.setChecked(true);
//            } else {
//                sw_acl.setChecked(false);
//            }
        } else {
            sw_developer.setChecked(false);
            ll_content.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_go_home:
                startActivity(HomeActivity.class);
                break;
        }
    }
}
