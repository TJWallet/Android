package com.tianji.blockchain.activity.information;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.adapter.basic.OnItemClickListener;
import com.tianji.blockchain.adapter.recyclerView.RVAdapterInformationList;
import com.tianji.blockchain.entity.InformationEntity;
import com.tianji.blockchain.entity.InformationListEntity;
import com.tianji.blockchain.sharepreferences.InformationListSharedPreferences;
import com.tianji.blockchain.utils.ViewCommonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class InformationListActivity extends BasicConnectShowActivity {
    private RecyclerView recyclerView;
    private RVAdapterInformationList adapter;
    private RelativeLayout rl_no_assets;
    private TextView allReaded;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_list);
    }


    @Override
    protected void initView() {
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.information));
        ViewCommonUtils.buildBackImageView(this, mActionBar);
        allReaded = new TextView(this);
        allReaded.setTextColor(Color.WHITE);
        allReaded.setText(getResources().getString(R.string.all_readed));
        allReaded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InformationListEntity entity = InformationListSharedPreferences.getInstance(InformationListActivity.this).getInformationList();
                if (entity != null) {
                    for (int i = 0; i < entity.getContent().size(); i++) {
                        entity.getContent().get(i).setReaded(true);
                    }
                    InformationListSharedPreferences.getInstance(InformationListActivity.this).removeInformationList();
                    InformationListSharedPreferences.getInstance(InformationListActivity.this).addInformationList(entity);
                    List<InformationEntity> listAll = new ArrayList<>();
                    listAll.addAll(entity.getContent());
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        listAll.sort(new Comparator<InformationEntity>() {
                            @Override
                            public int compare(InformationEntity o1, InformationEntity o2) {
                                Integer int1 = o1.getId();
                                Integer int2 = o2.getId();
                                return int2.compareTo(int1);
                            }
                        });
                    }
                    if (adapter != null) {
                        adapter.updateData(listAll);
                    }
                }

            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        rl_no_assets = findViewById(R.id.rl_no_assets);
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<InformationEntity> listNotRead = new ArrayList<>();
        List<InformationEntity> listIsRead = new ArrayList<>();
        List<InformationEntity> listAll = new ArrayList<>();
        listNotRead.clear();
        listIsRead.clear();
        listAll.clear();

        InformationListEntity entity = InformationListSharedPreferences.getInstance(this).getInformationList();
        if (entity != null) {
            for (int i = 0; i < entity.getContent().size(); i++) {
                if (entity.getContent().get(i).isReaded()) {
                    listIsRead.add(entity.getContent().get(i));
                } else {
                    listNotRead.add(entity.getContent().get(i));
                }
            }

            rl_no_assets.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                listNotRead.sort(new Comparator<InformationEntity>() {
                    @Override
                    public int compare(InformationEntity o1, InformationEntity o2) {
                        Integer int1 = o1.getId();
                        Integer int2 = o2.getId();
                        return int2.compareTo(int1);
                    }
                });
                listIsRead.sort(new Comparator<InformationEntity>() {
                    @Override
                    public int compare(InformationEntity o1, InformationEntity o2) {
                        Integer int1 = o1.getId();
                        Integer int2 = o2.getId();
                        return int2.compareTo(int1);
                    }
                });
            }

            listAll.addAll(listNotRead);
            listAll.addAll(listIsRead);

            if (adapter == null) {
                adapter = new RVAdapterInformationList(this, listAll);

                adapter.setItemClickListener(listener);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);
            } else {
                adapter.updateData(listAll);
            }
        } else {
            rl_no_assets.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position, Object obj) {
            InformationListEntity informationListEntity = InformationListSharedPreferences.getInstance(InformationListActivity.this).getInformationList();
            InformationEntity informationEntity = (InformationEntity) obj;
            for (int i = 0; i < informationListEntity.getContent().size(); i++) {
                if (informationListEntity.getContent().get(i).getId() == informationEntity.getId()) {
                    informationListEntity.getContent().get(i).setReaded(true);
                }
            }
            InformationListSharedPreferences.getInstance(InformationListActivity.this).removeInformationList();
            InformationListSharedPreferences.getInstance(InformationListActivity.this).addInformationList(informationListEntity);
            Intent intent = new Intent(InformationListActivity.this, InfomationDetailsActivity.class);
            intent.putExtra("_informationEntity", informationEntity);
            startActivity(intent);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

    }

}
