package com.tianji.blockchain.activity.information;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicActionBarActivity;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.entity.InformationEntity;
import com.tianji.blockchain.utils.TimeUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class InfomationDetailsActivity extends BasicConnectShowActivity {
    private TextView tv_title;
    private TextView tv_time;
    private TextView tv_content;

    private InformationEntity entity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation_details);
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
        entity = (InformationEntity) getIntent().getSerializableExtra("_informationEntity");
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.information_details));
        ViewCommonUtils.buildBackImageView(this, mActionBar);
        tv_title = findViewById(R.id.tv_title);
        tv_time = findViewById(R.id.tv_time);
        tv_content = findViewById(R.id.tv_content);

        tv_title.setText(entity.getTitle());
        tv_content.setText(entity.getContent());
        tv_time.setText("更新于" + TimeUtils.timeStamp2Date(entity.getCreatedAt(), "yyyy-MM-dd"));
    }
}
