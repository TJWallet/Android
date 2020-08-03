package com.tianji.blockchain.activity.assets;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tianji.blockchainwallet.WalletManager;
import com.tianji.blockchainwallet.constant.enums.ResultCode;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchainwallet.wallet.IRequestListener;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.activity.collect.CollectActivity;
import com.tianji.blockchain.activity.createwallet.BackUpMnemonicActivity;
import com.tianji.blockchain.activity.transfer.TransferActivity;
import com.tianji.blockchain.adapter.basic.OnItemClickListener;
import com.tianji.blockchain.adapter.recyclerView.RVAdapterTransferDetails;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.dialog.PasswordDialog;
import com.tianji.blockchain.dialog.TipsDialog;
import com.tianji.blockchain.entity.AssetsDetailsItemEntity;
import com.tianji.blockchain.entity.CoinTransferRecode;
import com.tianji.blockchain.entity.DialogEntity;
import com.tianji.blockchain.entity.TransferRecode;
import com.tianji.blockchain.entity.TransferWaitEntity;
import com.tianji.blockchain.sharepreferences.AssetsDetailsSharedPreferences;
import com.tianji.blockchain.sharepreferences.MnemonicSharedPreferences;
import com.tianji.blockchain.sharepreferences.TransferWaitSharedPreferences;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.ImageLoaderHelper;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.MathUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetsDetailsActivity extends BasicConnectShowActivity implements View.OnClickListener, BasicMvpInterface {
    private static final int TYPE_ALL = 0;
    private static final int TYPE_TRANSFER_OUT = 1;
    private static final int TYPE_TRANSFER_INTO = 2;
    private RecyclerView recyclerView;
    private List<TransferRecode> transferDetailsInfoList = new ArrayList<>();
    private List<TransferRecode> transferWaitList = new ArrayList<>();
    private List<TransferRecode> transferOutList = new ArrayList<>();
    private List<TransferRecode> transferIntoList = new ArrayList<>();
    private List<TransferRecode> transferAllList = new ArrayList<>();
    private RVAdapterTransferDetails adapter;


    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView img_icon;
    private TextView tv_icon_name;
    private TextView tv_amount;
    private TextView tv_all;
    private TextView tv_transfer_out;
    private TextView tv_transfer_into;
    private RelativeLayout rl_transer_out;
    private RelativeLayout rl_transer_into;
    private TextView tv_value;
    private RelativeLayout rl_chain_info;

    private WalletInfo walletInfo;
    private double now_amount;

    private AssetsDetailsItemEntity assetsDetails;

    private AssetsDetailsPresenter presenter;
    private RelativeLayout rl_no_assets;

    private int cuurentType;
    private TipsDialog backUpDialog;
    private boolean isBackUp;
    private PasswordDialog passwordDialog;
    /***将交易等待转化为交易可能出问题的时间间隔 单位毫秒***/
    private long transferMaybeErrorTimeInterval = 259200000;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_details);
    }

    @Override
    protected void initView() {
        assetsDetails = (AssetsDetailsItemEntity) getIntent().getSerializableExtra("_assetsDetails");
        LogUtils.log("资产详情界面获取到的对象精度 = " + assetsDetails.getDecimals());
        walletInfo = (WalletInfo) getIntent().getSerializableExtra("_walletInfo");
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(assetsDetails.getAssetsName());
        ViewCommonUtils.buildBackImageView(this, mActionBar);

        recyclerView = findViewById(R.id.recyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        img_icon = findViewById(R.id.img_icon);
        tv_icon_name = findViewById(R.id.tv_icon_name);
        tv_amount = findViewById(R.id.tv_amount);
        tv_all = findViewById(R.id.tv_all);
        tv_transfer_out = findViewById(R.id.tv_transfer_out);
        tv_transfer_into = findViewById(R.id.tv_transfer_into);
        rl_transer_out = findViewById(R.id.rl_transer_out);
        rl_transer_into = findViewById(R.id.rl_transer_into);
        tv_value = findViewById(R.id.tv_value);
        rl_chain_info = findViewById(R.id.rl_chain_info);
        rl_no_assets = findViewById(R.id.rl_no_assets);

        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadPage();
            }
        });

        if (assetsDetails.getAssetsName().equals("ETH")) {
            img_icon.setImageResource(R.drawable.eth_icon_selected);
            tv_amount.setText(MathUtils.doubleKeep4(assetsDetails.getBalance()));
        } else if (assetsDetails.getAssetsName().equals("BTC")) {
            img_icon.setImageResource(R.drawable.btc);
            tv_amount.setText(MathUtils.doubleKeep8(assetsDetails.getBalance()));
        } else if (assetsDetails.getAssetsName().equals("ACL")) {
            img_icon.setImageResource(R.drawable.acl_icon_select);
            tv_amount.setText(MathUtils.doubleKeep4(assetsDetails.getBalance()));
        } else {
            ImageLoaderHelper.getInstance().loadImage(this, img_icon, assetsDetails.getIconUrl());
            tv_amount.setText(MathUtils.doubleKeep4(assetsDetails.getBalance()));
        }


        CommonUtils.setCurrency(tv_value, MathUtils.doubleKeep2(assetsDetails.getTotalPrice()), false);
        tv_icon_name.setText(assetsDetails.getAssetsName());


        switch (walletInfo.getChain()) {
            case ETH:
                rl_chain_info.setBackgroundResource(R.drawable.eth_white);
                break;
            case ACL:
                rl_chain_info.setBackgroundResource(R.drawable.acl_white);
                break;
            case BTC:
                rl_chain_info.setBackgroundResource(R.drawable.btc_white);
                break;
            default:
                break;
        }
        tv_all.setOnClickListener(this);
        tv_transfer_out.setOnClickListener(this);
        tv_transfer_into.setOnClickListener(this);
        rl_transer_out.setOnClickListener(this);
        rl_transer_into.setOnClickListener(this);


        isBackUp = MnemonicSharedPreferences.getInstance(this).isBackUpMnemonic(walletInfo.getAddress());
        DialogEntity backUpDialogEntity = new DialogEntity(getResources().getString(R.string.tips),
                getResources().getString(R.string.no_backup),
                getResources().getString(R.string.goto_backup),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPasswordDialog();
                    }
                });
        backUpDialog = new TipsDialog(this, backUpDialogEntity);

        passwordDialog = new PasswordDialog(this, R.style.Wallet_Manager_Dialog, 1, false, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMnemonic(new IRequestListener<String>() {
                    @Override
                    public void onResult(ResultCode resultCode, String result) {
                        if (resultCode == ResultCode.SUCCESS) {
                            String mnemonics = result;
                            if (mnemonics != null) {
                                Intent mnemonicsIntent = new Intent(AssetsDetailsActivity.this, BackUpMnemonicActivity.class);
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
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        loadPage();
    }

    private void loadPage() {
        cuurentType = TYPE_ALL;
        //优先显示缓存内容
        transferDetailsInfoList.clear();
        transferWaitList.clear();
        List<CoinTransferRecode> coinTransferRecodeList = AssetsDetailsSharedPreferences.getInstance(this).getCoinTransferRecodeList(walletInfo.getAddress());
        /***交易等待记录***/
        List<TransferWaitEntity> transferWaitEntities = TransferWaitSharedPreferences.getInstance(this).getTransferWaitList(walletInfo.getAddress(), assetsDetails.getAssetsName());
        LogUtils.log("交易等待记录长度 = " + transferWaitEntities.size());
        LogUtils.log("交易等待记录 = " + transferWaitEntities.toString());
        if (transferWaitEntities.size() > 0) {
            //修改交易等待记录得状态
            for (int i = 0; i < transferWaitEntities.size(); i++) {
                if (System.currentTimeMillis() > transferWaitEntities.get(i).getTransferRecode().getTimestamp() * 1000 + transferMaybeErrorTimeInterval) {
                    LogUtils.log(className + " -- 比对时间超过3天，转为可能失败");
                    TransferRecode recode = transferWaitEntities.get(i).getTransferRecode();
                    recode.setTransferType(4);
                    transferWaitEntities.get(i).setTransferRecode(recode);
                    transferWaitEntities.get(i).setStatus(1);
                    TransferWaitSharedPreferences.getInstance(this).removeRecord(walletInfo.getAddress(), transferWaitEntities.get(i).getCoinName(), transferWaitEntities.get(i).getTransferRecode().getHash());
                    TransferWaitSharedPreferences.getInstance(this).addTransferWait(walletInfo.getAddress(), transferWaitEntities.get(i));
                } else {
                    LogUtils.log(className + " -- 比对时间不到3天，保持等待");
                    TransferRecode recode = transferWaitEntities.get(i).getTransferRecode();
                    recode.setTransferType(3);
                    transferWaitEntities.get(i).setTransferRecode(recode);
                    transferWaitEntities.get(i).setStatus(0);
                    TransferWaitSharedPreferences.getInstance(this).removeRecord(walletInfo.getAddress(), transferWaitEntities.get(i).getCoinName(), transferWaitEntities.get(i).getTransferRecode().getHash());
                    TransferWaitSharedPreferences.getInstance(this).addTransferWait(walletInfo.getAddress(), transferWaitEntities.get(i));
                }
            }
            for (int i = 0; i < transferWaitEntities.size(); i++) {
                transferWaitList.add(transferWaitEntities.get(i).getTransferRecode());
            }
        }
        /***交易记录缓存***/
        List<TransferRecode> recordList = new ArrayList<>();

        if (coinTransferRecodeList.size() > 0) {
            for (int i = 0; i < coinTransferRecodeList.size(); i++) {
                if (assetsDetails.getAssetsName().equals(coinTransferRecodeList.get(i).getCoinName())) {
                    if (coinTransferRecodeList.get(i).getTransferRecodeList() != null) {
                        recordList.addAll(coinTransferRecodeList.get(i).getTransferRecodeList());
                    }
                }
            }
        }
        /***有交易等待记录，需要核对，再拼接***/
        if (transferWaitList.size() > 0) {
            compareTransferHash(transferWaitList, recordList);

//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
//                transferWaitList.sort(new Comparator<TransferRecode>() {
//                    @Override
//                    public int compare(TransferRecode o1, TransferRecode o2) {
//                        Long long1 = o1.getTimestamp();
//                        Long long2 = o2.getTimestamp();
//                        return long1.compareTo(long2);
//                    }
//                });
//            }
        }
        if (recordList != null) {
            transferDetailsInfoList.addAll(recordList);
        }

        if (transferDetailsInfoList.size() > 0) {
            rl_no_assets.setVisibility(View.GONE);
        } else {
            rl_no_assets.setVisibility(View.VISIBLE);
        }
        transferAllList.clear();
        transferAllList.addAll(transferDetailsInfoList);
        adapter = new RVAdapterTransferDetails(this, transferAllList, walletInfo.getChain());
        adapter.setItemClickListener(listener);
        ViewCommonUtils.recyclerViewNoSliding(this, recyclerView);
        recyclerView.setAdapter(adapter);

        setData();
    }

    /**
     * 获取助记词
     *
     * @param listener
     */
    public void getMnemonic(IRequestListener<String> listener) {
        WalletManager.getInstance().getMnemonic(this, walletInfo, passwordDialog.getEdtText(), listener);
    }

    @Override
    protected void setData() {
        if (presenter == null) {
            presenter = new AssetsDetailsPresenter(this, this);
        }
        if (assetsDetails.getAssetsName().equals("ETH")) {
            Map<String, String> ethParams = new HashMap<>();
            ethParams.put("address", walletInfo.getAddress());
            ethParams.put("pageNo", "0");
            ethParams.put("pageSize", "100");
            presenter.getData(ethParams);

            presenter.checkETHBlance(walletInfo.getAddress());
        } else if (assetsDetails.getAssetsName().equals("BTC")) {
            presenter.checkBTCBalance(walletInfo.getAddress());
            presenter.getBTCTransferRecode(walletInfo.getAddress());
        } else if (assetsDetails.getAssetsName().equals("ACL")) {
            Map<String, String> aclParams = new HashMap<>();
            aclParams.put("addressHash", walletInfo.getAddress());
            aclParams.put("current", "1");
            aclParams.put("size", "100");
            presenter.getAclTransferRecode(aclParams);
            presenter.checkACLBalance(walletInfo.getAddress());
        } else {
            Map<String, String> erc20Params = new HashMap<>();
            erc20Params.put("address", walletInfo.getAddress());
            erc20Params.put("contractAddress", assetsDetails.getContractAddress());
            erc20Params.put("pageNo", "0");
            erc20Params.put("pageSize", "100");
            presenter.getERCtransferRecode(erc20Params);

            presenter.checkERC20Blance(walletInfo.getAddress(), assetsDetails.getContractAddress());
        }
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void initReceiver() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_all:
                changeType(TYPE_ALL);
                break;
            case R.id.tv_transfer_out:
                changeType(TYPE_TRANSFER_OUT);
                break;
            case R.id.tv_transfer_into:
                changeType(TYPE_TRANSFER_INTO);
                break;
            case R.id.rl_transer_out:
                if (isBackUp) {
                    Intent intent = new Intent(this, TransferActivity.class);
                    intent.putExtra("_walletInfo", walletInfo);
                    intent.putExtra("_assetsDetailsItemEntity", assetsDetails);
                    startActivity(intent);
                } else {
                    backUpDialog.show();
                }

                break;
            case R.id.rl_transer_into:
                if (isBackUp) {
                    Intent collectIntent = new Intent(this, CollectActivity.class);
                    collectIntent.putExtra("_walletInfo", walletInfo);
                    collectIntent.putExtra("_assetsDetailsItemEntity", assetsDetails);
                    startActivity(collectIntent);
                } else {
                    backUpDialog.show();
                }

                break;
        }
    }

    /**
     * 显示密码弹窗
     */
    private void showPasswordDialog() {
        passwordDialog.show();
        ViewCommonUtils.showKeyBorad(this);
    }

    private void changeType(int type) {
        switch (type) {
            case TYPE_ALL:
                LogUtils.log(className + " -- 切换到TYPE_ALL");
                cuurentType = TYPE_ALL;
                tv_all.setTextColor(getResources().getColor(R.color.textMajor));
                tv_transfer_into.setTextColor(getResources().getColor(R.color.textMinor));
                tv_transfer_out.setTextColor(getResources().getColor(R.color.textMinor));
                transferAllList.clear();
                transferAllList.addAll(transferDetailsInfoList);
                adapter.updateData(transferAllList);
                break;
            case TYPE_TRANSFER_INTO:
                LogUtils.log(className + " -- 切换到TYPE_TRANSFER_INTO");
                cuurentType = TYPE_TRANSFER_INTO;
                tv_all.setTextColor(getResources().getColor(R.color.textMinor));
                tv_transfer_into.setTextColor(getResources().getColor(R.color.textMajor));
                tv_transfer_out.setTextColor(getResources().getColor(R.color.textMinor));
                transferIntoList.clear();
                for (int i = 0; i < transferDetailsInfoList.size(); i++) {
                    if (transferDetailsInfoList.get(i).isTransferInTo()) {
                        transferIntoList.add(transferDetailsInfoList.get(i));
                    }
                }
                adapter.updateData(transferIntoList);
                break;
            case TYPE_TRANSFER_OUT:
                LogUtils.log(className + " -- 切换到TYPE_TRANSFER_OUT");
                cuurentType = TYPE_TRANSFER_OUT;
                tv_all.setTextColor(getResources().getColor(R.color.textMinor));
                tv_transfer_into.setTextColor(getResources().getColor(R.color.textMinor));
                tv_transfer_out.setTextColor(getResources().getColor(R.color.textMajor));
                transferOutList.clear();
                for (int i = 0; i < transferDetailsInfoList.size(); i++) {
                    if (!transferDetailsInfoList.get(i).isTransferInTo()) {
                        transferOutList.add(transferDetailsInfoList.get(i));
                    }
                }
                adapter.updateData(transferOutList);
                break;
        }
    }

    @Override
    public void getDataSuccess(Object object, int type) {
        switch (type) {
            case AssetsDetailsPresenter.TYPE_GET_ETH_TRANSFER_RECODE:
                List<TransferRecode> list = (List<TransferRecode>) object;
                if (list.size() > 0) {
                    rl_no_assets.setVisibility(View.GONE);

                    for (int i = 0; i < list.size(); i++) {

                        if (list.get(i).getFrom().equals(walletInfo.getAddress())) {
                            list.get(i).setTransferInTo(false);
                        } else {
                            list.get(i).setTransferInTo(true);
                        }
                    }
                    CoinTransferRecode coinTransferRecode = new CoinTransferRecode();
                    coinTransferRecode.setCoinName(assetsDetails.getAssetsName());
                    coinTransferRecode.setTransferRecodeList(list);
                    //添加缓存
                    AssetsDetailsSharedPreferences.getInstance(this).removeRecord(walletInfo.getAddress(), assetsDetails.getAssetsName());
                    AssetsDetailsSharedPreferences.getInstance(this).addAssetsDetails(walletInfo.getAddress(), coinTransferRecode);

                    transferDetailsInfoList.clear();
                    /***对比是否有交易等待***/
                    compareTransferHash(transferWaitList, list);
                    transferDetailsInfoList.addAll(list);

                    switch (cuurentType) {
                        case TYPE_ALL:
                            changeType(TYPE_ALL);
                            break;
                        case TYPE_TRANSFER_INTO:
                            changeType(TYPE_TRANSFER_INTO);
                            break;
                        case TYPE_TRANSFER_OUT:
                            changeType(TYPE_TRANSFER_OUT);
                            break;
                    }
                } else {
                    AssetsDetailsSharedPreferences.getInstance(this).removeRecord(walletInfo.getAddress(), assetsDetails.getAssetsName());
                    rl_no_assets.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                break;
            case AssetsDetailsPresenter.TYPE_GET_VALUE:
                String data = (String) object;
                double dataDouble = Double.parseDouble(data);
                double result = dataDouble * now_amount;
                CommonUtils.setCurrency(tv_value, MathUtils.doubleKeep2(result), false);
                break;
            case AssetsDetailsPresenter.TYPE_GET_ERC20_BLANCE:
                String erc20Blance = (String) object;
                now_amount = Double.parseDouble(erc20Blance);
                tv_amount.setText(MathUtils.doubleKeep4(now_amount));
                presenter.checkCoinValue(assetsDetails.getContractAddress());
                break;
            case AssetsDetailsPresenter.TYPE_GET_ETH_BLANCE:
                String ethBlance = (String) object;
                now_amount = Double.parseDouble(ethBlance);
                tv_amount.setText(MathUtils.doubleKeep4(now_amount));
                presenter.checkCoinValue("");
                break;
            case AssetsDetailsPresenter.TYPE_CHECK_ACL_BALANCE:
                String aclBlance = (String) object;
                now_amount = Double.parseDouble(aclBlance);
                tv_amount.setText(MathUtils.doubleKeep4(now_amount));
                presenter.checkACLCoinValue();
                break;
            case AssetsDetailsPresenter.TYPE_GET_ACL_TRANSFER_RECODE:
                List<TransferRecode> aclList = new ArrayList<>();
                try {
                    List<TransferRecode> objectList = (List<TransferRecode>) object;
                    aclList.addAll(objectList);
                } catch (ClassCastException e) {
                }

                LogUtils.log(className + " -- 请求ACL交易记录成功 =" + aclList.toString());
                if (aclList.size() > 0) {
                    rl_no_assets.setVisibility(View.GONE);

                    for (int i = 0; i < aclList.size(); i++) {
                        if (aclList.get(i).getFrom().equals(walletInfo.getAddress())) {
                            aclList.get(i).setTransferInTo(false);
                        } else {
                            aclList.get(i).setTransferInTo(true);
                        }
                    }
                    CoinTransferRecode coinTransferRecode = new CoinTransferRecode();
                    coinTransferRecode.setCoinName(assetsDetails.getAssetsName());
                    coinTransferRecode.setTransferRecodeList(aclList);
                    //添加缓存
                    AssetsDetailsSharedPreferences.getInstance(this).removeRecord(walletInfo.getAddress(), assetsDetails.getAssetsName());
                    AssetsDetailsSharedPreferences.getInstance(this).addAssetsDetails(walletInfo.getAddress(), coinTransferRecode);

                    transferDetailsInfoList.clear();
                    compareTransferHash(transferWaitList, aclList);
                    transferDetailsInfoList.addAll(aclList);
                    switch (cuurentType) {
                        case TYPE_ALL:
                            changeType(TYPE_ALL);
                            break;
                        case TYPE_TRANSFER_INTO:
                            changeType(TYPE_TRANSFER_INTO);
                            break;
                        case TYPE_TRANSFER_OUT:
                            changeType(TYPE_TRANSFER_OUT);
                            break;
                    }
                } else {
                    AssetsDetailsSharedPreferences.getInstance(this).removeRecord(walletInfo.getAddress(), assetsDetails.getAssetsName());
                    rl_no_assets.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                break;
            case AssetsDetailsPresenter.TYPE_GET_BTC_TRANSFER_RECODE:
                List<TransferRecode> btcList = (List<TransferRecode>) object;
                LogUtils.log(className + " -- 请求BTC交易记录成功 =" + btcList.toString());
                if (btcList.size() > 0) {
                    rl_no_assets.setVisibility(View.GONE);


                    CoinTransferRecode coinTransferRecode = new CoinTransferRecode();
                    coinTransferRecode.setCoinName(assetsDetails.getAssetsName());
                    coinTransferRecode.setTransferRecodeList(btcList);
                    //添加缓存
                    AssetsDetailsSharedPreferences.getInstance(this).removeRecord(walletInfo.getAddress(), assetsDetails.getAssetsName());
                    AssetsDetailsSharedPreferences.getInstance(this).addAssetsDetails(walletInfo.getAddress(), coinTransferRecode);

                    transferDetailsInfoList.clear();
                    compareTransferHash(transferWaitList, btcList);
                    transferDetailsInfoList.addAll(btcList);

                    switch (cuurentType) {
                        case TYPE_ALL:
                            changeType(TYPE_ALL);
                            break;
                        case TYPE_TRANSFER_INTO:
                            changeType(TYPE_TRANSFER_INTO);
                            break;
                        case TYPE_TRANSFER_OUT:
                            changeType(TYPE_TRANSFER_OUT);
                            break;
                    }
                } else {
                    AssetsDetailsSharedPreferences.getInstance(this).removeRecord(walletInfo.getAddress(), assetsDetails.getAssetsName());
                    rl_no_assets.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }

                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                break;
            case AssetsDetailsPresenter.TYPE_CHECK_BTC_BALANCE:
                now_amount = (double) object;
                tv_amount.setText(MathUtils.doubleKeep8(now_amount));
                presenter.checkBTCCoinValue();
                break;
            case AssetsDetailsPresenter.TYPE_GET_ACL_VALUE:
                String aclValue = (String) object;
                double aclValueDouble = Double.parseDouble(aclValue);
                double aclValueResult = aclValueDouble * now_amount;
                CommonUtils.setCurrency(tv_value, MathUtils.doubleKeep2(aclValueResult), false);
                break;
            case AssetsDetailsPresenter.TYPE_GET_BTC_VALUE:
                String btcValue = (String) object;
                double btcValueDouble = Double.parseDouble(btcValue);
                double btcValueResult = btcValueDouble * now_amount;
                CommonUtils.setCurrency(tv_value, MathUtils.doubleKeep2(btcValueResult), false);
                break;
        }
    }

    @Override
    public void getDataFail(Object error, int type) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void getDataFail(int errCode, String errMsg, int type) {

    }


    private OnItemClickListener listener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position, Object obj) {
            TransferRecode transferRecode = (TransferRecode) obj;
            Intent intent = new Intent(AssetsDetailsActivity.this, TransferDetailsActivity.class);
            intent.putExtra("_transferRecode", transferRecode);
            intent.putExtra("_transferCoinName", assetsDetails.getAssetsName());
            if (assetsDetails.getAssetsName().equals("ETH")) {
                if (transferRecode.isWait()) {
                    LogUtils.log(className + " -- ETH交易详情状态 = " + transferRecode.getTransferType());
                    intent.putExtra("_transferType", transferRecode.getTransferType());
                } else {
                    if (transferRecode.getIsError().equals("0")) {
                        intent.putExtra("_transferType", 1);
                    } else {
                        intent.putExtra("_transferType", 2);
                    }
                }
            } else if (assetsDetails.getAssetsName().equals("ACL")) {
                if (transferRecode.isWait()) {
                    LogUtils.log(className + " -- ACL交易详情状态 = " + transferRecode.getTransferType());
                    intent.putExtra("_transferType", transferRecode.getTransferType());
                } else {
                    intent.putExtra("_transferType", 1);
                }
            } else if (assetsDetails.getAssetsName().equals("BTC")) {
                if (transferRecode.isWait()) {
                    LogUtils.log(className + " -- BTC交易详情状态 = " + transferRecode.getTransferType());
                    intent.putExtra("_transferType", transferRecode.getTransferType());
                } else {
                    intent.putExtra("_transferType", 1);
                }
            } else {
                if (transferRecode.isWait()) {
                    LogUtils.log(className + " -- ERC20交易详情状态 = " + transferRecode.getTransferType());
                    intent.putExtra("_transferType", transferRecode.getTransferType());
                } else {
                    if (transferRecode.getIsError() != null) {
                        if (transferRecode.getIsError().equals("0")) {
                            intent.putExtra("_transferType", 1);
                        } else {
                            intent.putExtra("_transferType", 2);
                        }
                    } else {
                        intent.putExtra("_transferType", 1);
                    }

                }
            }
            startActivity(intent);
        }
    };

    /**
     * transferWaitList 核对
     *
     * @param transferWaitList
     * @param transferRecodeList
     */
    private void compareTransferHash(List<TransferRecode> transferWaitList, List<TransferRecode> transferRecodeList) {
        for (int i = 0; i < transferWaitList.size(); i++) {
            for (int j = 0; j < transferRecodeList.size(); j++) {
                if (transferWaitList.get(i).getHash().equals(transferRecodeList.get(j).getHash())) {
                    TransferWaitSharedPreferences.getInstance(this).removeRecord(walletInfo.getAddress(), assetsDetails.getAssetsName(), transferWaitList.get(i).getHash());
                    transferWaitList.remove(i);
                    break;
                }
            }
        }
        /***transferWaitList 核对完成，剩下的就是缓存列表中没有的交易等待记录***/
        transferDetailsInfoList.addAll(transferWaitList);
    }

}
