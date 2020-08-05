package com.tianji.blockchain.fragment.wallet;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tianji.blockchain.Constant;
import com.tianji.blockchain.IntentFilterConstant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.assets.AssetsDetailsActivity;
import com.tianji.blockchain.activity.collect.CollectActivity;
import com.tianji.blockchain.activity.createwallet.BackUpMnemonicActivity;
import com.tianji.blockchain.activity.information.InformationListActivity;
import com.tianji.blockchain.activity.main.WalletDetailsActivity;
import com.tianji.blockchain.activity.main.WalletManagerActivity;
import com.tianji.blockchain.activity.transfer.TransferActivity;
import com.tianji.blockchain.adapter.basic.OnItemClickListener;
import com.tianji.blockchain.adapter.recyclerView.RVAdapterAssetsDetails;
import com.tianji.blockchain.adapter.recyclerView.RVAdapterWalletList;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.dialog.PasswordDialog;
import com.tianji.blockchain.dialog.SelectDialog;
import com.tianji.blockchain.dialog.TipsDialog;
import com.tianji.blockchain.entity.AssetsDetailsItemEntity;
import com.tianji.blockchain.entity.DialogEntity;
import com.tianji.blockchain.entity.InformationEntity;
import com.tianji.blockchain.entity.InformationListEntity;
import com.tianji.blockchain.entity.WalletFragmentAssetsListEntity;
import com.tianji.blockchain.fragment.basic.BaseFragment;
import com.tianji.blockchain.sharepreferences.AssetsListSharedPreferences;
import com.tianji.blockchain.sharepreferences.CurrentWalletSharedPreferences;
import com.tianji.blockchain.sharepreferences.InformationListSharedPreferences;
import com.tianji.blockchain.sharepreferences.MnemonicSharedPreferences;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.MathUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;
import com.tianji.blockchain.utils.WalletListHelper;
import com.tianji.blockchainwallet.WalletManager;
import com.tianji.blockchainwallet.constant.enums.Chain;
import com.tianji.blockchainwallet.constant.enums.ResultCode;
import com.tianji.blockchainwallet.constant.enums.WalletCreatedType;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchainwallet.wallet.IRequestListener;

import java.util.ArrayList;
import java.util.List;


public class WalletFragment extends BaseFragment implements View.OnClickListener {
    private static final int LOAD_PAGE = 0;

    private RecyclerView recyclerView;
    private RVAdapterAssetsDetails adapter;

    private RelativeLayout rl_wallet_name;
    private TextView tv_wallet_name;
    private ImageView img_scan;
    private ImageView img_eye;
    private RelativeLayout rl_eye;
    private ImageView img_more;
    private TextView tv_my_assets_value;
    private TextView tv_address;
    private ImageView img_copy;
    private RelativeLayout rl_assets_card;
    private TextView tv_no_mnemonic;


    private RelativeLayout rl_transfer, rl_collect;

    private WalletFgBroadcastReceiver walletFgBroadcastReceiver;

    private SwipeRefreshLayout swipeRefreshLayout;


    private WalletInfo walletInfo;
    private boolean isBackUp;
    private LinearLayout ll_mnemonic;

    private PasswordDialog passwordDialog;

    private List<AssetsDetailsItemEntity> assetsDetailsItemEntityList = new ArrayList<>();

    /**
     * 钱包管理弹窗相关变量
     */
    private Dialog walletManagerDialog;

    private ImageView img_setting;
    private ImageView img_close;
    private RelativeLayout rl_all;
    private TextView tv_coin_name;
    private RecyclerView walletRecyclerView;
    private ImageView img_all_icon, img_file_coin;
    private View line_all, line_file_coin;
    private RelativeLayout rl_file_coin;
    private RelativeLayout rl_copy;

    private RVAdapterWalletList walletAdapter;

    private WalletFragmentPresenter presenter;

    private String fragmentClassName = "WalletFragment";
    private SwipeRefreshLayout walletListDialogSwiperefresh;


    private TipsDialog backUpDialog;

    private double allTotalPrice;

    private boolean isFristOpen = true;

    private SelectDialog selectDialog;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.log(fragmentClassName + " -- onCreate");
    }

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_wallet;
    }

    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case LOAD_PAGE:
                LogUtils.log(fragmentClassName + " -- LOAD_PAGE");
                walletInfo = (WalletInfo) msg.obj;
                int position = msg.arg1;
                if (walletInfo == null) {
                    return;
                }
                tv_wallet_name.setText(walletInfo.getWalletName());
                tv_address.setText(walletInfo.getAddress());
                WalletApplication.setCurrentWallet(walletInfo);
                CurrentWalletSharedPreferences.getInstance(getActivity()).changeCurrentWallet(walletInfo);

                if (walletInfo.getWalletCreatedType() == WalletCreatedType.IMPORTED_PRIVATE_KEY || walletInfo.getWalletCreatedType() == WalletCreatedType.IMPORTED_KEYSTORE || walletInfo.getWalletCreatedType() == WalletCreatedType.IMPORTED_MNEMONIC) {
                    //导入钱包全部默认为true
                    MnemonicSharedPreferences.getInstance(getActivity()).saveBackUpMnemonic(walletInfo.getAddress(), true);
                }
                isBackUp = MnemonicSharedPreferences.getInstance(getActivity()).isBackUpMnemonic(walletInfo.getAddress());
                if (isBackUp) {
                    ll_mnemonic.setVisibility(View.GONE);
                } else {
                    ll_mnemonic.setVisibility(View.VISIBLE);
                }

                switch (walletInfo.getChain()) {
                    case FIL:
                        if (position % 4 == 1) {
                            //第二个
                            rl_assets_card.setBackgroundResource(R.drawable.filecoin_card_bg_03);
                        } else if (position % 4 == 2) {
                            //第三个
                            rl_assets_card.setBackgroundResource(R.drawable.filecoin_card_bg_02);
                        } else if (position % 4 == 0) {
                            //第一个
                            rl_assets_card.setBackgroundResource(R.drawable.filecoin_card_bg_04);
                        } else if (position % 4 == 3) {
                            //第四个
                            rl_assets_card.setBackgroundResource(R.drawable.filecoin_card_bg_01);
                        }
                        break;

                }
                //初始化钱包列表
                initWalletManagerDialog();
                //获取资产列表缓存
                String key = walletInfo.getAddress() + walletInfo.getChain();
                LogUtils.log(fragmentClassName + " -- 获取资产列表的KEY == " + key);
                assetsDetailsItemEntityList = AssetsListSharedPreferences.getInstance(WalletApplication.getApp()).getAssetsList(key);
                LogUtils.log(fragmentClassName + " -- 获取到的资产列表是 = " + assetsDetailsItemEntityList.toString());
                if (allTotalPrice == 0) {
                    for (int i = 0; i < assetsDetailsItemEntityList.size(); i++) {
                        AssetsListSharedPreferences.getInstance(getActivity()).addAssetes(key, assetsDetailsItemEntityList.get(i));
                        allTotalPrice += assetsDetailsItemEntityList.get(i).getTotalPrice();
                    }
                    if (WalletApplication.getApp().isAssetsIsHidden()) {
                        tv_my_assets_value.setText("****");
                    } else {
                        if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_CNY) {
                            tv_my_assets_value.setText("¥ " + MathUtils.doubleKeep2(allTotalPrice));
                        } else if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_USD) {
                            tv_my_assets_value.setText("$ " + MathUtils.doubleKeep2(allTotalPrice));
                        }
                    }
                }
                //加载数据
                //加载资产列表
                adapter = new RVAdapterAssetsDetails(getActivity(), assetsDetailsItemEntityList, walletInfo);
                adapter.setItemClickListener(assetsListener);
                adapter.setEyes(WalletApplication.getApp().isAssetsIsHidden(), img_eye);
                recyclerView.setAdapter(adapter);
                setData();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                break;
            case 0x88:
                List<WalletInfo> infoList = (List<WalletInfo>) msg.obj;
                walletAdapter.updateData(infoList);
                break;
        }
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.recyclerView);
        ViewCommonUtils.recyclerViewNoSliding(WalletApplication.getApp(), recyclerView);
        rl_wallet_name = findViewById(R.id.rl_wallet_name);
        tv_wallet_name = findViewById(R.id.tv_wallet_name);
        img_scan = findViewById(R.id.img_scan);
        img_eye = findViewById(R.id.img_eye);
        img_more = findViewById(R.id.img_more);
        tv_my_assets_value = findViewById(R.id.tv_my_assets_value);
        tv_address = findViewById(R.id.tv_address);
        img_copy = findViewById(R.id.img_copy);
        rl_transfer = findViewById(R.id.rl_transfer);
        rl_collect = findViewById(R.id.rl_collect);
        rl_eye = findViewById(R.id.rl_eye);
        ll_mnemonic = findViewById(R.id.ll_mnemonic);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        rl_assets_card = findViewById(R.id.rl_assets_card);
        tv_no_mnemonic = findViewById(R.id.tv_no_mnemonic);
        rl_copy = findViewById(R.id.rl_copy);


        tv_no_mnemonic.setSelected(true);

        rl_wallet_name.setOnClickListener(this);
        img_scan.setOnClickListener(this);
        rl_eye.setOnClickListener(this);
        img_more.setOnClickListener(this);
        rl_copy.setOnClickListener(this);
        rl_transfer.setOnClickListener(this);
        rl_collect.setOnClickListener(this);
        ll_mnemonic.setOnClickListener(this);
        tv_address.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (WalletApplication.getApp().isAssetsIsHidden()) {
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } else {
                    WalletInfo walletInfo = WalletApplication.getCurrentWallet();
                    Message msg = Message.obtain();
                    msg.what = LOAD_PAGE;
                    msg.obj = walletInfo;
                    mHandler.sendMessage(msg);
                }
            }
        });


        String[] strArray = getResources().getStringArray(R.array.select_address);
        selectDialog = new SelectDialog(getActivity(), R.style.Wallet_Manager_Dialog, strArray, getResources().getString(R.string.operate));

        passwordDialog = new PasswordDialog(getActivity(), R.style.Wallet_Manager_Dialog, 1, false, this);

        DialogEntity backUpDialogEntity = new DialogEntity(getResources().getString(R.string.tips),
                getResources().getString(R.string.no_backup),
                getResources().getString(R.string.goto_backup),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPasswordDialog();
                    }
                });
        backUpDialog = new TipsDialog(getActivity(), backUpDialogEntity);
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtils.log(fragmentClassName + " -- onStart   =" + isFristOpen);
        swipeRefreshLayout.setRefreshing(true);
        if (isFristOpen) {
            WalletInfo walletInfo = WalletApplication.getCurrentWallet();
            Message msg = Message.obtain();
            msg.what = LOAD_PAGE;
            msg.obj = walletInfo;
            mHandler.sendMessage(msg);
        }
        isFristOpen = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.log(fragmentClassName + " -- onDestroy");
        getActivity().unregisterReceiver(walletFgBroadcastReceiver);
    }

    @Override
    protected void setData() {
    }

    @Override
    protected void initReceiver() {
        if (walletFgBroadcastReceiver == null) {
            walletFgBroadcastReceiver = new WalletFgBroadcastReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(IntentFilterConstant.RELOAD_WALLET_FRAGMENT);
        filter.addAction(IntentFilterConstant.MAINACTIVITY_START_TYPE);
        filter.addAction(IntentFilterConstant.MAIN_ACTIVITY_GET_QRCODE_RESULT);
        filter.addAction(IntentFilterConstant.UsbIntentFilter.USB_CONNECT);
        filter.addAction(IntentFilterConstant.UsbIntentFilter.USB_DISCONNECT);
        filter.addAction(IntentFilterConstant.UsbIntentFilter.USB_IS_CONNECTED);
        filter.addAction(IntentFilterConstant.UsbIntentFilter.USB_NOT_CONNECTED);
        filter.addAction(IntentFilterConstant.ASSETS_VALUE_GET_FINISH);
        getActivity().registerReceiver(walletFgBroadcastReceiver, filter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_wallet_name:
                startWalletListDialog();
                break;
            case R.id.tv_address:
                CommonUtils.showOperateSelectDialog(selectDialog, walletInfo, walletInfo.getAddress());
                break;
            case R.id.img_scan:
                CommonUtils.openScan(getActivity());
                break;
            case R.id.rl_eye:
                WalletApplication.getApp().setAssetsIsHidden(!WalletApplication.getApp().isAssetsIsHidden());
                ViewCommonUtils.showPwd(WalletApplication.getApp().isAssetsIsHidden(), tv_my_assets_value, img_eye);
                if (adapter != null) {
                    adapter.setEyes(WalletApplication.getApp().isAssetsIsHidden(), img_eye);
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.img_more:
                Intent intent = new Intent(WalletApplication.getApp(), WalletDetailsActivity.class);
                intent.putExtra("_walletInfo", walletInfo);
                startActivity(intent);
                break;
            case R.id.rl_copy:
                CommonUtils.copyContent(getActivity(), walletInfo.getAddress());
                break;
            case R.id.rl_transfer:
                if (isBackUp) {
                    LogUtils.log("首页直接打开转账界面对象精度是= " + assetsDetailsItemEntityList.get(0).getDecimals());
                    Intent transferIntent = new Intent(getActivity(), TransferActivity.class);
                    transferIntent.putExtra("_walletInfo", walletInfo);
                    transferIntent.putExtra("_assetsDetailsItemEntity", assetsDetailsItemEntityList.get(0));
                    startActivity(transferIntent);
                } else {
                    backUpDialog.show();
                }
                break;
            case R.id.rl_collect:
                if (isBackUp) {
                    Intent collectIntent = new Intent(getActivity(), CollectActivity.class);
                    collectIntent.putExtra("_walletInfo", walletInfo);
                    collectIntent.putExtra("_assetsDetailsItemEntity", assetsDetailsItemEntityList.get(0));
                    startActivity(collectIntent);
                } else {
                    backUpDialog.show();
                }

                break;
            case R.id.ll_mnemonic:
                showPasswordDialog();
                break;
            case R.id.btn_ok:
                getMnemonic(new IRequestListener<String>() {
                    @Override
                    public void onResult(ResultCode resultCode, String result) {
                        if (resultCode == ResultCode.SUCCESS) {
                            String mnemonics = result;
                            if (mnemonics != null) {
                                Intent mnemonicsIntent = new Intent(getActivity(), BackUpMnemonicActivity.class);
                                mnemonicsIntent.putExtra("_mnemonic", mnemonics);
                                mnemonicsIntent.putExtra("_pageType", Constant.StartPageType.TYPE_WALLETDETAILS);
                                mnemonicsIntent.putExtra("_walletInfo", walletInfo);
                                startActivity(mnemonicsIntent);
                            }
                        } else if (resultCode == ResultCode.WALLET_FILE_PASSWORD_ERROR) {
                            showToast("密码验证失败");
                        } else {
                            showToast("导入失败");
                        }
                    }
                });
                passwordDialog.dismiss();
                backUpDialog.dismiss();
                break;

            //底部钱包列表弹窗
            case R.id.img_setting:
                //跳转钱包管理页面
                startActivity(WalletManagerActivity.class);
                walletManagerDialog.dismiss();
                break;
            case R.id.img_close:
                if (walletManagerDialog != null) {
                    walletManagerDialog.dismiss();
                }
                break;
            case R.id.rl_all:
                changeChainType(Chain.ALL);
                break;
            case R.id.rl_file_coin:
                changeChainType(Chain.FIL);
                break;

        }
    }

    /**
     * 广播监听
     */
    class WalletFgBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(IntentFilterConstant.MAINACTIVITY_START_TYPE)) {
                if (!isFristOpen) {
                    if (WalletApplication.getApp().isAssetsIsHidden()) {
                        if (swipeRefreshLayout.isRefreshing()) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    } else {
                        WalletInfo walletInfo = (WalletInfo) intent.getSerializableExtra("_walletInfo");
                        Message msg = Message.obtain();
                        msg.what = LOAD_PAGE;
                        msg.obj = walletInfo;
                        mHandler.sendMessage(msg);
                    }
                }
            } else if (intent.getAction().equals(IntentFilterConstant.MAIN_ACTIVITY_GET_QRCODE_RESULT)) {
                String rqcodeResult = intent.getStringExtra("_qrcodeResult");
                Intent intentOne = new Intent(getActivity(), TransferActivity.class);
                intentOne.putExtra("_walletInfo", walletInfo);
                intentOne.putExtra("_assetsDetailsItemEntity", assetsDetailsItemEntityList.get(0));
                intentOne.putExtra("_toAddress", rqcodeResult);
                startActivity(intentOne);
            } else if (intent.getAction().equals(IntentFilterConstant.ASSETS_VALUE_GET_FINISH)) {
                LogUtils.log(fragmentClassName + " -- 收到列表获取估值完成的广播");
                double[] doubles = intent.getDoubleArrayExtra("_values");
                double allValue = 0;
                for (int i = 0; i < doubles.length; i++) {
                    allValue += doubles[i];
                }
                if (WalletApplication.getApp().isAssetsIsHidden()) {
                    tv_my_assets_value.setText("****");
                } else {
                    if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_CNY) {
                        tv_my_assets_value.setText("¥ " + MathUtils.doubleKeep2(allValue));
                    } else if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_USD) {
                        tv_my_assets_value.setText("$ " + MathUtils.doubleKeep2(allValue));
                    }
                }
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }
    }

    /**
     * 显示密码弹窗
     */
    private void showPasswordDialog() {
        passwordDialog.show();
        ViewCommonUtils.showKeyBorad(getActivity());
    }

    /**
     * 获取助记词
     *
     * @param listener
     */
    public void getMnemonic(IRequestListener<String> listener) {
        WalletManager.getInstance().getMnemonic(getActivity(), walletInfo, passwordDialog.getEdtText(), listener);
    }

    /**
     * 显示底部弹窗
     */
    public void startWalletListDialog() {
        initWalletManagerDialog();
        walletManagerDialog.show();
    }

    /**
     * 初始化钱包列表弹窗
     */
    private void initWalletManagerDialog() {
        if (walletManagerDialog == null) {
            walletManagerDialog = new Dialog(getActivity(), R.style.Wallet_Manager_Dialog);
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_wallet_manager, null);
            img_setting = view.findViewById(R.id.img_setting);
            img_close = view.findViewById(R.id.img_close);
            rl_all = view.findViewById(R.id.rl_all);
            tv_coin_name = view.findViewById(R.id.tv_coin_name);
            walletRecyclerView = view.findViewById(R.id.recyclerView);
            line_all = view.findViewById(R.id.line_all);
            rl_file_coin = view.findViewById(R.id.rl_file_coin);
            img_file_coin = view.findViewById(R.id.img_file_coin);
            line_file_coin = view.findViewById(R.id.line_file_coin);
            img_all_icon = view.findViewById(R.id.img_all_icon);
            walletListDialogSwiperefresh = view.findViewById(R.id.walletListDialogSwiperefresh);
            walletListDialogSwiperefresh.setEnabled(false);


            img_setting.setOnClickListener(this);
            img_close.setOnClickListener(this);
            rl_all.setOnClickListener(this);
            rl_file_coin.setOnClickListener(this);

            //软件列表数据加载
            List<WalletInfo> walletInfoList = WalletListHelper.getInstance(getActivity()).getSoftwareWalletInfoListAll();
            setWalletSelected(walletInfoList);
            walletAdapter = new RVAdapterWalletList(getActivity(), getActivity(), walletInfoList, walletManagerDialog, 1, Chain.ALL);
            walletAdapter.setItemClickListener(walletListIntentListener);
            walletRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            walletRecyclerView.setAdapter(walletAdapter);

            changeChainType(Chain.ALL);

            walletManagerDialog.setContentView(view);

            Window dialogWindow = walletManagerDialog.getWindow();
            dialogWindow.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = (int) (CommonUtils.getWindowHeight(getActivity()) * 0.8);
            lp.y = 0; //设置Dialog距离底部的距离
            dialogWindow.setAttributes(lp); //将属性设置给窗体
        } else {
            changeChainType(Chain.ALL);
        }
    }

    /**
     * 遍历设置被选中的钱包
     *
     * @param walletInfoList
     */
    private void setWalletSelected(List<WalletInfo> walletInfoList) {
        String currentWalletKey = WalletApplication.getCurrentWallet().getAddress() + WalletApplication.getCurrentWallet().getChain();
        for (int i = 0; i < walletInfoList.size(); i++) {
            walletInfoList.get(i).setSelected(false);
            String key = walletInfoList.get(i).getAddress() + walletInfoList.get(i).getChain();
            if (key.equals(currentWalletKey)) {
                walletInfoList.get(i).setSelected(true);
            }
        }
    }

    /**
     * 更改chain类型
     *
     * @param chain
     */
    private void changeChainType(Chain chain) {
        List<WalletInfo> typeList = null;
        switch (chain) {
            case ALL:
                LogUtils.d(className + " -- 全部钱包");
                setChainSelected(chain);
                typeList = WalletListHelper.getInstance(getActivity()).getSoftwareWalletInfoListAll();
                setWalletSelected(typeList);
                walletAdapter = new RVAdapterWalletList(getActivity(), getActivity(), typeList, walletManagerDialog, 1, Chain.ALL);
                walletAdapter.setItemClickListener(walletListIntentListener);
                walletRecyclerView.setAdapter(walletAdapter);
                break;
            case FIL:
                LogUtils.d(className + " -- FIL钱包");
                setChainSelected(chain);
                typeList = WalletListHelper.getInstance(getActivity()).getSoftwareWalletInfoListFIL();
                setWalletSelected(typeList);
                walletAdapter = new RVAdapterWalletList(getActivity(), getActivity(), typeList, walletManagerDialog, 1, Chain.FIL);
                walletAdapter.setItemClickListener(walletListIntentListener);
                walletRecyclerView.setAdapter(walletAdapter);
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
                line_file_coin.setVisibility(View.VISIBLE);
                img_file_coin.setImageResource(R.drawable.file_coin_select);
                tv_coin_name.setText("FileCoin钱包");
                break;
        }
    }

    /**
     * 钱包列表item监听
     */
    private OnItemClickListener walletListIntentListener = new OnItemClickListener() {

        @Override
        public void onItemClick(View view, int position, Object obj) {
            WalletInfo info = (WalletInfo) obj;

            Message msg = Message.obtain();
            msg.obj = info;
            msg.what = LOAD_PAGE;
            msg.arg1 = position;
            mHandler.sendMessage(msg);
            WalletApplication.setCurrentWallet(info);
            CurrentWalletSharedPreferences.getInstance(getActivity()).changeCurrentWallet(info);
            walletManagerDialog.dismiss();
            swipeRefreshLayout.setRefreshing(true);
        }
    };

    /**
     * 资产列表监听
     */
    private OnItemClickListener assetsListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position, Object obj) {
            AssetsDetailsItemEntity assetsDetailsItemEntity = (AssetsDetailsItemEntity) obj;
            LogUtils.log("点击跳转资产详情界面对象精度是 = " + assetsDetailsItemEntity.getDecimals());
            Intent intent = new Intent(getActivity(), AssetsDetailsActivity.class);
            intent.putExtra("_assetsDetails", assetsDetailsItemEntity);
            intent.putExtra("_walletInfo", walletInfo);
            getActivity().startActivity(intent);
        }
    };
}
