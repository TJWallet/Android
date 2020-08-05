package com.tianji.blockchain.activity.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextPaint;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tianji.blockchainwallet.constant.enums.Chain;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.basic.BasicActionBarActivity;
import com.tianji.blockchain.adapter.basic.OnItemClickListener;
import com.tianji.blockchain.adapter.recyclerView.RVAdapterWalletList;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.NetUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;
import com.tianji.blockchain.utils.WalletListHelper;

import java.util.List;


public class WalletManagerActivity extends BasicActionBarActivity implements View.OnClickListener {

    private RVAdapterWalletList adapter;


    private RelativeLayout rl_all;
    private TextView tv_coin_name;
    private RecyclerView recyclerView;
    private ImageView img_all_icon, img_file_coin;

    private SwipeRefreshLayout swipeRefreshLayout;

    private View line_all, line_file_coin;
    private RelativeLayout rl_file_coin;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_mannager);
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
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.wallet_manager));
        ViewCommonUtils.buildBackImageView(this, mActionBar);

        rl_all = findViewById(R.id.rl_all);
        tv_coin_name = findViewById(R.id.tv_coin_name);
        recyclerView = findViewById(R.id.recyclerView);
        img_all_icon = findViewById(R.id.img_all_icon);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        line_all = findViewById(R.id.line_all);
        rl_file_coin = findViewById(R.id.rl_file_coin);
        img_file_coin = findViewById(R.id.img_file_coin);
        line_file_coin = findViewById(R.id.line_file_coin);

        swipeRefreshLayout.setEnabled(false);
        rl_all.setOnClickListener(this);
        rl_file_coin.setOnClickListener(this);

        adapter = new RVAdapterWalletList(this, this, WalletListHelper.getInstance(this).getSoftwareWalletInfoListAll(), 2, Chain.ALL);
        adapter.setItemClickListener(softwareListener);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        changeChainType(Chain.ALL);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_all:
                changeChainType(Chain.ALL);
                break;
            case R.id.rl_file_coin:
                changeChainType(Chain.FIL);
                break;


        }
    }


    /**
     * 更改chain类型
     *
     * @param chain
     */
    private void changeChainType(Chain chain) {
        LogUtils.d(className + " -- changeChainType");
        List<WalletInfo> typeList = null;
        switch (chain) {
            case FIL:
                LogUtils.log(className + "-- FIL钱包");
                setChainSelected(chain);
                typeList = WalletListHelper.getInstance(this).getSoftwareWalletInfoListFIL();
                adapter = new RVAdapterWalletList(this, this, typeList, 2, Chain.FIL);
                adapter.setItemClickListener(softwareListener);
                recyclerView.setAdapter(adapter);
                break;
            case ALL:
                LogUtils.d(className + " -- 全部钱包");
                setChainSelected(chain);
                typeList = WalletListHelper.getInstance(this).getSoftwareWalletInfoListAll();
                adapter = new RVAdapterWalletList(this, this, typeList, 2, Chain.ALL);
                adapter.setItemClickListener(softwareListener);
                recyclerView.setAdapter(adapter);
                break;
        }

    }

    private void setChainSelected(Chain chain) {
        line_all.setVisibility(View.GONE);
        line_file_coin.setVisibility(View.GONE);
        img_all_icon.setImageResource(R.drawable.all_icon_normal);
        img_file_coin.setImageResource(R.drawable.file_coin_normal);
        switch (chain) {
            case ALL:
                img_all_icon.setImageResource(R.drawable.all_icon_selected);
                line_all.setVisibility(View.VISIBLE);
                tv_coin_name.setText(getResources().getString(R.string.all_wallet));
                break;
            case FIL:
                img_file_coin.setImageResource(R.drawable.file_coin_select);
                line_file_coin.setVisibility(View.VISIBLE);
                tv_coin_name.setText(getResources().getString(R.string.filecoin_wallet));
                break;
        }

    }

    /**
     * item监听
     */
    private OnItemClickListener softwareListener = new OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position, Object obj) {
            WalletInfo info = (WalletInfo) obj;
            Intent intent = new Intent(WalletManagerActivity.this, WalletDetailsActivity.class);
            intent.putExtra("_walletInfo", info);
            startActivity(intent);
        }
    };

    @Override
    public void onNetChange(NetUtils.NetworkStatus status) {
        if (!WalletApplication.getApp().isNetWorkFirstShowed()) {
            if (status == NetUtils.NetworkStatus.NETWORK_NONE) {
                tipsDialog.show();
                WalletApplication.getApp().setNetWorkFirstShowed(true);
            }
        }
    }
}
