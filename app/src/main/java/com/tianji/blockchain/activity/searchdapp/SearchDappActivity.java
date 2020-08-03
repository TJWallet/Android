package com.tianji.blockchain.activity.searchdapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicMvpActivity;
import com.tianji.blockchain.activity.dappdetail.DappDetailActivity;
import com.tianji.blockchain.adapter.basic.ItemClickableAdapter;
import com.tianji.blockchain.adapter.recyclerView.DiscoveryCompleteListAdapter;
import com.tianji.blockchain.adapter.recyclerView.DiscoveryHistoryListAdapter;
import com.tianji.blockchain.adapter.recyclerView.DiscoverySimpleListAdapter;
import com.tianji.blockchain.adapter.recyclerView.DiscoverySimpleListSkeletonAdapter;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.dialog.LoadingDialog;
import com.tianji.blockchain.entity.DappEntity;
import com.tianji.blockchain.entity.RecentHistoryEntity;
import com.tianji.blockchain.sharepreferences.RecentHistorySharedPreferences;
import com.tianji.blockchain.sharepreferences.SearchHistorySharedPreferences;
import com.tianji.blockchain.utils.LogUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** author:jason **/
public class SearchDappActivity extends BasicMvpActivity implements BasicMvpInterface,View.OnClickListener {
    private EditText etSearchKeyword;
    private ImageButton ivClearContent;
    private Button btnCancel;
    private LinearLayout layoutSearchHistory;
    private NestedScrollView svDefault;
    private RecyclerView rvHistoryList;
    private RecyclerView rvHotDappList;
    private RecyclerView rvSearchResult;
    private RecyclerView rvHotDappListSkeleton;
    private TextView tvWebsite;

    private LoadingDialog loadingDialog;

    private List<String> historyList = new ArrayList<>();
    private List<DappEntity> hotDappList = new ArrayList<>();
    private List<String> hotDappSkeletonList = new ArrayList<>();
    private List<DappEntity> searchResult = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initData();
    }

    private void initData() {
        this.hotDappSkeletonList.clear();
        for (int i = 0; i < 8; i ++) {
            this.hotDappSkeletonList.add("placeholder");
        }
        this.rvHotDappListSkeleton.getAdapter().notifyDataSetChanged();
    }

    @Override
    protected int setResourceId() {
        return R.layout.activity_search_dapp;
    }

    @Override
    protected void initView() {
        this.etSearchKeyword = findViewById(R.id.etSearchKeyword);
        this.ivClearContent = findViewById(R.id.ivClearContent);
        this.btnCancel = findViewById(R.id.btnCancel);
        this.layoutSearchHistory = findViewById(R.id.layoutSearchHistory);
        this.svDefault = findViewById(R.id.svDefault);
        this.rvHistoryList = findViewById(R.id.rvHistoryList);
        this.rvHotDappList = findViewById(R.id.rvHotDappList);
        this.rvSearchResult = findViewById(R.id.rvSearchResult);
        this.rvHotDappListSkeleton = findViewById(R.id.rvHotDappListSkeleton);
        this.tvWebsite = this.findViewById(R.id.tvWebsite);
        this.tvWebsite.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        this.tvWebsite.getPaint().setAntiAlias(true);

        this.rvHistoryList.setLayoutManager(new LinearLayoutManager(this));
        DiscoveryHistoryListAdapter historyAdapter = new DiscoveryHistoryListAdapter(this, this.historyList, 10);
        this.rvHistoryList.setAdapter(historyAdapter);
        historyAdapter.setItemClickListener(new ItemClickableAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(View itemView, Object itemData, int position) {
                String keyword = itemData.toString();
                SearchHistorySharedPreferences.getInstance(getApplicationContext()).pushHistory(keyword);
                loadingDialog.show();
                startSearch(keyword);
            }
        });

        this.rvHotDappListSkeleton.setLayoutManager(new GridLayoutManager(this, 4));
        this.rvHotDappListSkeleton.setAdapter(new DiscoverySimpleListSkeletonAdapter(this, this.hotDappSkeletonList, 8));

        this.rvHotDappList.setLayoutManager(new GridLayoutManager(this, 4));
        DiscoverySimpleListAdapter recommendAdapter = new DiscoverySimpleListAdapter(this, this.hotDappList, 8);
        this.rvHotDappList.setAdapter(recommendAdapter);
        recommendAdapter.setItemClickListener(new ItemClickableAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(View itemView, Object itemData, int position) {
                DappEntity data = (DappEntity) itemData;

                RecentHistoryEntity entity = new RecentHistoryEntity();
                entity.setCreateTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                entity.setDescription(data.getName());
                entity.setIcon(data.getIcon());
                entity.setType(data.getTypeName());
                entity.setTypeColor(data.getTypeColor());
                entity.setSummary(data.getSummary());
                entity.setPlatformName(data.getPlatformName());
                entity.setMid(data.getMid());
                RecentHistorySharedPreferences.getInstance(getApplicationContext()).pushHistory(entity);

                Intent intent = new Intent(getApplicationContext(), DappDetailActivity.class);
                intent.putExtra("mid", data.getMid());
                startActivity(intent);
            }
        });

        this.rvSearchResult.setLayoutManager(new LinearLayoutManager(this));
        DiscoveryCompleteListAdapter adapter = new DiscoveryCompleteListAdapter(this, this.searchResult);
        this.rvSearchResult.setAdapter(adapter);
        adapter.setItemClickListener(new ItemClickableAdapter.OnItemClickListener() {
            @Override
            public void onClickItem(View itemView, Object itemData, int position) {
                DappEntity data = (DappEntity) itemData;

                RecentHistoryEntity entity = new RecentHistoryEntity();
                entity.setCreateTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                entity.setDescription(data.getName());
                entity.setIcon(data.getIcon());
                entity.setType(data.getTypeName());
                entity.setTypeColor(data.getTypeColor());
                entity.setSummary(data.getSummary());
                entity.setPlatformName(data.getPlatformName());
                entity.setMid(data.getMid());
                RecentHistorySharedPreferences.getInstance(getApplicationContext()).pushHistory(entity);

                Intent intent = new Intent(SearchDappActivity.this, DappDetailActivity.class);
                intent.putExtra("mid", data.getMid());
                startActivity(intent);
            }
        });

        this.etSearchKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH){
                    String keyword = etSearchKeyword.getText().toString().trim();
                    if(keyword == null || keyword.trim().length() == 0){
                        Toast.makeText(getApplicationContext(), "请输入搜索内容", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    SearchHistorySharedPreferences.getInstance(getApplicationContext()).pushHistory(keyword);
                    loadingDialog.show();
                    startSearch(keyword);
                    hideKeyBoard();
                    return true;
                }
                return false;
            }
        });
        this.ivClearContent.setOnClickListener(this);
        this.btnCancel.setOnClickListener(this);
        this.tvWebsite.setOnClickListener(this);
        this.loadingDialog = new LoadingDialog(this, R.style.BottomDialogTheme, "正在搜索，请稍后...");
    }

    private void hideKeyBoard(){
        InputMethodManager manager = ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE));
        if (manager != null) manager.hideSoftInputFromWindow(this.etSearchKeyword.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void startSearch(String keyword) {
        Map<String, String> params = new HashMap<>();
        params.put("action", "search");
        params.put("keyword", keyword);
        this.presenter.getData(params);
    }

    @Override
    protected void handleMsg(Message msg) {
        switch(msg.what) {
            case 1: {
                List<String> temp = (List<String>) msg.obj;
                if (temp != null && temp.size() > 0) {
                    layoutSearchHistory.setVisibility(View.VISIBLE);
                    this.historyList.addAll(temp);
                    this.rvHistoryList.getAdapter().notifyDataSetChanged();
                }
            }break;
            case 2: {
                this.rvHotDappListSkeleton.setVisibility(View.GONE);
                this.rvHotDappList.setVisibility(View.VISIBLE);

                List<DappEntity> temp = (List<DappEntity>) msg.obj;
                if (temp != null && temp.size() > 0) {
                    this.hotDappList.addAll(temp);
                    this.rvHotDappList.getAdapter().notifyDataSetChanged();
                }
            }break;
            case 3:{
                this.loadingDialog.dismiss();
                this.svDefault.setVisibility(View.GONE);
                this.rvSearchResult.setVisibility(View.VISIBLE);
                this.searchResult.clear();
                List<DappEntity> temp = (List<DappEntity>) msg.obj;
                if (temp != null && temp.size() > 0) {
                    this.searchResult.addAll(temp);
                    this.rvSearchResult.getAdapter().notifyDataSetChanged();
                }
            }break;
        }
    }

    @Override
    protected void setData() {
        if (this.presenter == null) {
            presenter = new SearchDappPresenter(this.getApplicationContext(), this);
        }
        Map<String, String> params01 = new HashMap<>();
        params01.put("action", "fetchSearchHistory");
        this.presenter.getData(params01);

        Map<String, String> params02 = new HashMap<>();
        params02.put("action", "fetchHotDapp");
        this.presenter.getData(params02);

        this.rvHotDappListSkeleton.setVisibility(View.VISIBLE);
        this.rvHotDappList.setVisibility(View.GONE);
    }

    @Override
    protected void initReceiver() {

    }

    @Override
    public void onClick(View v) {
        int componentID = v.getId();
        switch(componentID) {
            case R.id.ivClearContent:
                this.etSearchKeyword.setText("");
                break;
            case R.id.btnCancel:
                finish();
                break;
            case R.id.tvWebsite:{
                Uri uri = Uri.parse("https://dapponline.io");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }break;
        }
    }

    @Override
    public void getDataSuccess(Object object, int type) {
        Message msg = mHandler.obtainMessage();
        msg.what = type;
        msg.obj = object;
        msg.sendToTarget();
    }

    @Override
    public void getDataFail(Object error, int type) {

    }

    @Override
    public void getDataFail(int errCode, String errMsg, int type) {
        LogUtils.e(errMsg);

        switch(type) {
            case 1:{

            }break;
            case 2:{
                Toast.makeText(this, "获取推荐信息失败", Toast.LENGTH_SHORT).show();
            }break;
            case 3:{
                this.loadingDialog.dismiss();
                Toast.makeText(this, "搜索失败，请稍后再试", Toast.LENGTH_SHORT).show();
            }break;
        }
    }
}