package com.tianji.blockchain.activity.dapplist;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicMvpActivity;
import com.tianji.blockchain.activity.dappdetail.DappDetailActivity;
import com.tianji.blockchain.adapter.basic.ItemClickableAdapter;
import com.tianji.blockchain.adapter.recyclerView.DiscoveryRecentCompleteListAdapter;
import com.tianji.blockchain.entity.DappEntity;
import com.tianji.blockchain.entity.RecentHistoryEntity;
import com.tianji.blockchain.entity.RecommendEntity;
import com.tianji.blockchain.sharepreferences.RecentHistorySharedPreferences;
import com.tianji.blockchain.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * author:jason
 **/
public class DappListActivity extends BasicMvpActivity implements View.OnClickListener {
    private ImageButton ivBack;
    private TextView tvTitle;
    private RecyclerView rvListItems;
    private TextView tvWebsite;

    private String title;
    private String from;
    private List<RecentHistoryEntity> listData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = this.getIntent().getExtras();
        this.title = extras.getString("title");
        this.from = extras.getString("from");
        this.tvTitle.setText(this.title);

        this.listData.clear();

        Object data = extras.getSerializable("data");
        if ("recent".equals(from)) {
            LogUtils.log("数据源recent");
            ArrayList<RecentHistoryEntity> temp = (ArrayList<RecentHistoryEntity>) data;
            if (temp != null && temp.size() > 0) {
                this.listData.addAll(temp);
            }
        } else if ("recommend".equals(from)) {
            LogUtils.log("数据源recommend");
            ArrayList<RecommendEntity> temp = (ArrayList<RecommendEntity>) data;
            if (temp != null && temp.size() > 0) {
                for (RecommendEntity item : temp) {
                    RecentHistoryEntity entity = new RecentHistoryEntity();
                    entity.setMid(item.getMid());
                    entity.setIcon(item.getIcon());
                    entity.setDescription(item.getTitle());
                    entity.setType(item.getTypeName());
                    entity.setLink(item.getLink());
                    entity.setTypeColor(item.getTypeColor());
                    entity.setSummary(item.getSubtitle());
                    entity.setPlatformName(item.getPlatformName());
                    entity.setPlatformNameEn(item.getPlatformNameEn());
                    entity.setDescriptionEn(item.getTitleEn());
                    entity.setSummaryEn(item.getSubtitleEn());
                    entity.setTypeEn(item.getTypeNameEn());
                    this.listData.add(entity);
                }
            }
        } else if ("ranking".equals(from) || "topic".equals(from)) {
            LogUtils.log("数据源ranking");
            ArrayList<DappEntity> temp = (ArrayList<DappEntity>) data;
            if (temp != null && temp.size() > 0) {
                for (DappEntity item : temp) {
                    RecentHistoryEntity entity = new RecentHistoryEntity();
                    entity.setMid(item.getMid());
                    entity.setIcon(item.getIcon());
                    entity.setDescription(item.getName());
                    entity.setType(item.getTypeName());
                    entity.setTypeColor(item.getTypeColor());
                    entity.setSummary(item.getSummary());
                    entity.setPlatformName(item.getPlatformName());
                    entity.setPlatformNameEn(item.getPlatformNameEn());
                    entity.setDescriptionEn(item.getNameEn());
                    entity.setSummaryEn(item.getSummaryEn());
                    entity.setTypeEn(item.getTypeNameEn());
                    this.listData.add(entity);
                }
            }
        }
        this.rvListItems.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected int setResourceId() {
        return R.layout.activity_dapp_list;
    }

    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case 1: {
            }
            break;
        }
    }

    @Override
    protected void setData() {
        if (presenter == null) {
            presenter = new DappListPresenter(this.getApplicationContext(), this);
        }

    }

    @Override
    protected void initReceiver() {

    }

    @Override
    protected void initView() {
        this.ivBack = findViewById(R.id.ivBack);
        this.tvTitle = findViewById(R.id.tvTitle);
        this.rvListItems = findViewById(R.id.rvListItems);
        this.tvWebsite = this.findViewById(R.id.tvWebsite);
        this.tvWebsite.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        this.tvWebsite.getPaint().setAntiAlias(true);

        this.rvListItems.setLayoutManager(new LinearLayoutManager(this));
        this.rvListItems.setNestedScrollingEnabled(false);
        DiscoveryRecentCompleteListAdapter adapter = new DiscoveryRecentCompleteListAdapter(this, this.listData);
        this.rvListItems.setAdapter(adapter);
        adapter.setItemClickListener(new ItemClickableAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(View itemView, Object itemData, int position) {
                RecentHistoryEntity entity = (RecentHistoryEntity) itemData;
                RecentHistorySharedPreferences.getInstance(getApplicationContext()).pushHistory(entity);

                if (entity.getMid() == null) {
//                    Intent intent = new Intent(getApplicationContext(), CommonWebviewActivity.class);
//                    intent.putExtra("data", entity.getDescription());
//                    startActivity(intent);
                    Uri uri = Uri.parse(entity.getLink().trim());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), DappDetailActivity.class);
                    intent.putExtra("mid", entity.getMid());
                    startActivity(intent);
                }
            }
        });

        this.ivBack.setOnClickListener(this);
        this.tvWebsite.setOnClickListener(this);
    }

    @Override
    public void getDataSuccess(Object object, int type) {

    }

    @Override
    public void getDataFail(Object error, int type) {

    }

    @Override
    public void getDataFail(int errCode, String errMsg, int type) {

    }

    @Override
    public void onClick(View v) {
        int resourceID = v.getId();
        switch (resourceID) {
            case R.id.ivBack: {
                this.finish();
            }
            break;
            case R.id.tvWebsite: {
                Uri uri = Uri.parse("https://dapponline.io");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
            break;
        }
    }
}