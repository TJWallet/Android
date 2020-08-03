package com.tianji.blockchain.activity.aboutus;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.entity.QuestionItemEntity;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class HelpDetailsActivity extends BasicConnectShowActivity {
    private WebView tv_content;
    private QuestionItemEntity entity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_details);
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
        entity = (QuestionItemEntity) getIntent().getSerializableExtra("_questionItemEntity");
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(entity.getTitle());
        ViewCommonUtils.buildBackImageView(this, mActionBar);
        tv_content = findViewById(R.id.tv_content);
        tv_content.setVerticalScrollBarEnabled(false);
        tv_content.setHorizontalScrollBarEnabled(false);

        tv_content.loadDataWithBaseURL("", entity.getContent(), "text/html", "utf-8", "");
    }

}
