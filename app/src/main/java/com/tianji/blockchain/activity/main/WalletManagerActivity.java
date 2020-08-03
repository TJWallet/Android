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

import com.android.volley.VolleyError;
import com.tianji.blockchainwallet.WalletManager;
import com.tianji.blockchainwallet.constant.enums.Chain;
import com.tianji.blockchainwallet.constant.enums.ResultCode;
import com.tianji.blockchainwallet.constant.enums.StorageSaveType;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchainwallet.wallet.IRequestListener;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.MainActivity;
import com.tianji.blockchain.activity.basic.BasicActionBarActivity;
import com.tianji.blockchain.activity.basic.UsbCallbackListener;
import com.tianji.blockchain.activity.createwallet.ServiceAgreementActivity;
import com.tianji.blockchain.activity.hardware.HardwareManagerActivity;
import com.tianji.blockchain.activity.hardware.HardwareWalletGuideActivity;
import com.tianji.blockchain.adapter.basic.OnItemClickListener;
import com.tianji.blockchain.adapter.recyclerView.HardwareListAdapter;
import com.tianji.blockchain.adapter.recyclerView.RVAdapterWalletList;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.dialog.HardwareTipsDialog;
import com.tianji.blockchain.dialog.PasswordDialog;
import com.tianji.blockchain.dialog.PinDialog;
import com.tianji.blockchain.dialog.ShowContentDialog;
import com.tianji.blockchain.dialog.TipsDialog;
import com.tianji.blockchain.entity.AllObWalletEntity;
import com.tianji.blockchain.sharepreferences.CurrentWalletSharedPreferences;
import com.tianji.blockchain.sharepreferences.ObserverWalletListSharedPreferences;
import com.tianji.blockchain.usbInterfack.UsbDeleteCallbackListener;
import com.tianji.blockchain.usbInterfack.UsbIsBackupCallbackListener;
import com.tianji.blockchain.usbInterfack.UsbIsInitCallbackListener;
import com.tianji.blockchain.utils.DialogUtils;
import com.tianji.blockchain.utils.ListUtils;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.NetUtils;
import com.tianji.blockchain.utils.SharePreferencesHelper;
import com.tianji.blockchain.utils.ThreadManager;
import com.tianji.blockchain.utils.ViewCommonUtils;
import com.tianji.blockchain.utils.WalletHelper;
import com.tianji.blockchain.utils.WalletListHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class WalletManagerActivity extends BasicActionBarActivity implements View.OnClickListener {

    private RVAdapterWalletList adapter;


    private RelativeLayout rl_all;
    private TextView tv_coin_name;
    private RecyclerView recyclerView;
    private ImageView img_all_icon;

    private SwipeRefreshLayout swipeRefreshLayout;

    private View line_all;
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
                showToast("Filecoin 即将开放，敬请期待");
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
            case ETH:
                LogUtils.log(className + "-- ETH钱包");
                setChainSelected(chain);
                typeList = WalletListHelper.getInstance(this).getSoftwareWalletInfoListEth();
                adapter = new RVAdapterWalletList(this, this, typeList, 2, Chain.ETH);
                adapter.setItemClickListener(softwareListener);
                recyclerView.setAdapter(adapter);
                break;
            case BTC:
                LogUtils.log(className + "-- BTC钱包");
                setChainSelected(chain);
                typeList = WalletListHelper.getInstance(this).getSoftwareWalletInfoListBTC();
                adapter = new RVAdapterWalletList(this, this, typeList, 2, Chain.BTC);
                adapter.setItemClickListener(softwareListener);
                recyclerView.setAdapter(adapter);
                break;
            case ACL:
                LogUtils.log(className + "-- ACL钱包");
                setChainSelected(chain);
                typeList = WalletListHelper.getInstance(this).getSoftwareWalletInfoListACL();
                adapter = new RVAdapterWalletList(this, this, typeList, 2, Chain.ACL);
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

        img_all_icon.setImageResource(R.drawable.all_icon_normal);

        switch (chain) {
            case ALL:
                img_all_icon.setImageResource(R.drawable.all_icon_selected);
                line_all.setVisibility(View.VISIBLE);
                tv_coin_name.setText(getResources().getString(R.string.all_wallet));
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
