package com.tianji.blockchain.activity.assets;

import android.os.Bundle;
import android.os.Message;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.widget.*;


import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.adapter.basic.OnItemClickListener;
import com.tianji.blockchain.adapter.recyclerView.RVAdapterAssetsList;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.entity.AssetsDetailsItemEntity;
import com.tianji.blockchain.sharepreferences.AssetsListSharedPreferences;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddAssetsActivity extends BasicConnectShowActivity implements BasicMvpInterface, View.OnClickListener {
    private EditText edt_search;
    private TextView tv_search;
    private RecyclerView recyclerView;
    private WalletInfo walletInfo;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RVAdapterAssetsList adapter;
    private ImageView img_clear;

    private List<AssetsDetailsItemEntity> entityList = new ArrayList<>();

    private AddAssetsPresenter presenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assets);
    }

    @Override
    protected void setData() {
        swipeRefreshLayout.setRefreshing(true);
        if (presenter == null) {
            presenter = new AddAssetsPresenter(this, this);
        }
        Map<String, String> params = new HashMap<>();
        presenter.getData(params);
    }


    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void initReceiver() {

    }

    @Override
    protected void initView() {
        walletInfo = (WalletInfo) getIntent().getSerializableExtra("_walletInfo");
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.add_coin));
        ViewCommonUtils.buildBackImageView(this, mActionBar);


        edt_search = findViewById(R.id.edt_search);
        tv_search = findViewById(R.id.tv_search);
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        img_clear = findViewById(R.id.img_clear);

        tv_search.setOnClickListener(this);
        img_clear.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                edt_search.setText("");
                setData();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void getDataSuccess(Object object, int type) {
        if (type == 0) {
            List<AssetsDetailsItemEntity> list = (List<AssetsDetailsItemEntity>) object;
            entityList.clear();
            entityList.addAll(list);
            String key = walletInfo.getAddress() + walletInfo.getChain();
            List<AssetsDetailsItemEntity> recodeList = AssetsListSharedPreferences.getInstance(this).getAssetsList(key);
            for (int i = 0; i < entityList.size(); i++) {
                for (int j = 0; j < recodeList.size(); j++) {
                    if (entityList.get(i).getAssetsName().equals(recodeList.get(j).getAssetsName())) {
//                        LogUtils.log("0被设置为已选择的是 =" + entityList.get(i).getIconUrl());
                        entityList.get(i).setChoosed(true);
                    }
                }
            }
            if (adapter == null) {
                adapter = new RVAdapterAssetsList(this, entityList, walletInfo.getAddress(), walletInfo.getChain(), true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setItemViewCacheSize(200);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.updateData(entityList);
            }

            swipeRefreshLayout.setRefreshing(false);
        } else if (type == AddAssetsPresenter.TYPE_SEARCH_COIN) {
            entityList.clear();
            entityList.addAll((List<AssetsDetailsItemEntity>) object);
            String key = walletInfo.getAddress() + walletInfo.getChain();
            List<AssetsDetailsItemEntity> recodeList = AssetsListSharedPreferences.getInstance(this).getAssetsList(key);
            for (int i = 0; i < entityList.size(); i++) {
                for (int j = 0; j < recodeList.size(); j++) {
                    if (entityList.get(i).getAssetsName().equals(recodeList.get(j).getAssetsName())) {
//                        LogUtils.log("搜索被设置为已选择的是 =" + entityList.get(i).getIconUrl());
                        entityList.get(i).setChoosed(true);
                        entityList.get(i).setIconUrl(recodeList.get(j).getIconUrl());
                    }
                }
            }
            if (adapter == null) {
                adapter = new RVAdapterAssetsList(this, entityList, walletInfo.getAddress(), walletInfo.getChain(), true);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(adapter);
                recyclerView.setItemViewCacheSize(200);
            } else {
                adapter.updateData(entityList);
            }
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void getDataFail(Object error, int type) {

    }

    @Override
    public void getDataFail(int errCode, String errMsg, int type) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                String content = ViewCommonUtils.getEdtString(edt_search);
                if (!content.equals("")) {
                    Map<String, String> params = new HashMap<>();
                    params.put("page", "0");
                    params.put("pageSize", "500");
                    params.put("symbol", content);
                    presenter.searchCoinName(params);
                } else {
                    setData();
//                    showToast("搜索框不能为空");
                }
                break;
            case R.id.img_clear:
                edt_search.setText("");
                setData();
                break;
        }
    }

}
