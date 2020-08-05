package com.tianji.blockchain.activity.basic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.tianji.blockchain.R;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.CompanyUtil;
import com.tianji.blockchain.view.ActionBar;


public abstract class BasicActionBarActivity extends BasicNetCheckActivity  {
    protected ViewGroup mViewGroup;
    protected ActionBar mActionBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = new ActionBar(this);
        mActionBar.setVisibility(View.GONE);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        mViewGroup = findViewById(R.id.root_view);
        ViewGroup.LayoutParams LP = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, CompanyUtil.dip2px(this, 61f));
        mActionBar.setLayoutParams(LP);
        mViewGroup.addView(mActionBar);

        //点击空白处，收起键盘
        CommonUtils.stopKeyboard(this, mViewGroup);
    }

}
