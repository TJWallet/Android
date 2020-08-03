package com.tianji.blockchain.activity.aboutus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicActionBarActivity;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.adapter.basic.OnItemClickListener;
import com.tianji.blockchain.adapter.recyclerView.RVAdapterQuestionList;
import com.tianji.blockchain.entity.QuestionItemEntity;
import com.tianji.blockchain.entity.QuestionListEntity;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class HelpMoreActivity extends BasicConnectShowActivity {
    private RecyclerView recyclerView;

    private QuestionListEntity entity;

    private RVAdapterQuestionList adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_more);
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
        entity = (QuestionListEntity) getIntent().getSerializableExtra("_questionListEntity");
        String columnName = (String) getIntent().getSerializableExtra("_columnName");
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(columnName);
        ViewCommonUtils.buildBackImageView(this, mActionBar);


        recyclerView = findViewById(R.id.recyclerView);
        adapter = new RVAdapterQuestionList(this, entity.getContent(), 1);
        adapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object obj) {
                QuestionItemEntity entity = (QuestionItemEntity) obj;
                Intent intent = new Intent(HelpMoreActivity.this, HelpDetailsActivity.class);
                intent.putExtra("_questionItemEntity", entity);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

}
