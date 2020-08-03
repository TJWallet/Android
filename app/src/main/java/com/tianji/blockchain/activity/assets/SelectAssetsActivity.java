package com.tianji.blockchain.activity.assets;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.VolleyError;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchain.IntentFilterConstant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicActionBarActivity;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.activity.main.AddressBookActivity;
import com.tianji.blockchain.adapter.basic.OnItemClickListener;
import com.tianji.blockchain.adapter.recyclerView.RVAdapterAssetsList;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.entity.AssetsDetailsItemEntity;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectAssetsActivity extends BasicConnectShowActivity implements BasicMvpInterface {
    private RecyclerView recyclerView;

    private SwipeRefreshLayout swipeRefreshLayout;
    private RVAdapterAssetsList adapter;

    private List<AssetsDetailsItemEntity> entityList = new ArrayList<>();

    private WalletInfo walletInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_assets);
    }

    @Override
    protected void setData() {
        if (presenter == null) {
            presenter = new SelectAssetsPresenter(this, this);
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
        mActionBar.setActionTitle(getResources().getString(R.string.choose_coin));
        ViewCommonUtils.buildBackImageView(this, mActionBar);
        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void getDataSuccess(Object object, int type) {
        List<AssetsDetailsItemEntity> list = (List<AssetsDetailsItemEntity>) object;
        entityList.clear();
        entityList.addAll(list);
        adapter = new RVAdapterAssetsList(this, entityList, walletInfo.getAddress(), walletInfo.getChain(), false);
        adapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object obj) {
                AssetsDetailsItemEntity entity = (AssetsDetailsItemEntity) obj;
                LogUtils.log("点击的货币名称是" + entity.getAssetsName());
                LogUtils.log("点击的货币合约地址是" + entity.getContractAddress());

                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("_assetsDetailsBack", entity);
                resultIntent.putExtras(bundle);
                SelectAssetsActivity.this.setResult(0x26, resultIntent);
                SelectAssetsActivity.this.finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemViewCacheSize(200);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getDataFail(Object error, int type) {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void getDataFail(int errCode, String errMsg, int type) {

    }
}
