package com.tianji.blockchain.adapter.recyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.tianji.blockchainwallet.constant.enums.Chain;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.createwallet.ServiceAgreementActivity;
import com.tianji.blockchain.adapter.basic.CustomRecyclerViewAdapter;
import com.tianji.blockchain.adapter.basic.ITail;
import com.tianji.blockchain.btcApi.BitcoinApi;
import com.tianji.blockchain.dialog.SelectDialog;
import com.tianji.blockchain.entity.AssetsDetailsItemEntity;
import com.tianji.blockchain.sharepreferences.AssetsListSharedPreferences;
import com.tianji.blockchain.sharepreferences.MnemonicSharedPreferences;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.HttpVolley;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.MathUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RVAdapterWalletList extends CustomRecyclerViewAdapter<WalletInfo> implements ITail {
    private Dialog walletManagerDialog;
    private Activity activity;
    private boolean isHardware;
    /**
     * 1 == 首页列表, 2 == 钱包管理列表
     */
    private int type;
    /*** 0 ：全部 1：ETH 2：BTC 3：ACL ***/
    private int chainType;
    private Chain chain;
    private SelectDialog selectDialog;

    public RVAdapterWalletList(Context context, Activity activity, List<WalletInfo> data, Dialog walletManagerDialog, int type, Chain chain) {
        this.activity = activity;
        this.data = data;
        this.walletManagerDialog = walletManagerDialog;
        this.isHardware = isHardware;
        this.type = type;
        this.context = context;
        this.chain = chain;
        String[] strArray = context.getResources().getStringArray(R.array.select_address);
        selectDialog = new SelectDialog(context, R.style.Wallet_Manager_Dialog, strArray, context.getResources().getString(R.string.operate));
        settail(this);
    }

    public RVAdapterWalletList(Context context, Activity activity, List<WalletInfo> data, int type, Chain chain) {
        this.activity = activity;
        this.data = data;
        this.type = type;
        this.context = context;
        this.chain = chain;
        String[] strArray = context.getResources().getStringArray(R.array.select_address);
        selectDialog = new SelectDialog(context, R.style.Wallet_Manager_Dialog, strArray, context.getResources().getString(R.string.operate));
        settail(this);
    }

    public RVAdapterWalletList(Context context, Activity activity, List<WalletInfo> data, boolean isHardware, int type, Chain chain) {
        this.activity = activity;
        this.data = data;
        this.isHardware = isHardware;
        this.type = type;
        this.context = context;
        this.chain = chain;
        String[] strArray = context.getResources().getStringArray(R.array.select_address);
        selectDialog = new SelectDialog(context, R.style.Wallet_Manager_Dialog, strArray, context.getResources().getString(R.string.operate));
        settail(this);
    }

    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_CONTENT) {
            final WalletListViewHolder walletListViewHolder = (WalletListViewHolder) holder;
            final WalletInfo entity = data.get(position);
            walletListViewHolder.tv_wallet_name.setText(entity.getWalletName());
            walletListViewHolder.tv_address.setText(entity.getAddress());
            walletListViewHolder.img_copy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.showOperateSelectDialog(selectDialog, entity, entity.getAddress());
//                    CommonUtils.copyContent(activity, entity.getAddress());
                }
            });
            if (type == 1) {
                walletListViewHolder.img_enter.setVisibility(View.GONE);
                if (entity.isSelected()) {
                    walletListViewHolder.img_selected.setVisibility(View.VISIBLE);
                } else {
                    walletListViewHolder.img_selected.setVisibility(View.GONE);
                }
            } else {
                walletListViewHolder.img_selected.setVisibility(View.GONE);
                if (isHardware) {
                    walletListViewHolder.img_enter.setVisibility(View.GONE);
                } else {
                    walletListViewHolder.img_enter.setVisibility(View.VISIBLE);
                }
            }

            if (isHardware) {
                switch (entity.getChain()) {
                    case ACL:
                        walletListViewHolder.root_view.setBackgroundResource(R.drawable.acl_hardware_wallet_bg);
                        break;
                    case ETH:
                        walletListViewHolder.root_view.setBackgroundResource(R.drawable.eth_hardware_wallet_bg);
                        break;
                    case BTC:
                        walletListViewHolder.root_view.setBackgroundResource(R.drawable.btc_hardware_wallet_bg);
                        break;
                }
            } else {
                switch (entity.getChain()) {
                    case ACL:
                        if (position % 4 == 1) {
                            //第二个
                            walletListViewHolder.root_view.setBackgroundResource(R.drawable.acl_wallet_bg_03);
                        } else if (position % 4 == 2) {
                            //第三个
                            walletListViewHolder.root_view.setBackgroundResource(R.drawable.acl_wallet_bg_02);
                        } else if (position % 4 == 0) {
                            //第一个
                            walletListViewHolder.root_view.setBackgroundResource(R.drawable.acl_wallet_bg_04);
                        } else if (position % 4 == 3) {
                            //第四个
                            walletListViewHolder.root_view.setBackgroundResource(R.drawable.acl_wallet_bg_01);
                        }
                        break;
                    case ETH:
                        if (position % 4 == 1) {
                            //第二个
                            walletListViewHolder.root_view.setBackgroundResource(R.drawable.eth_wallet_bg_03);
                        } else if (position % 4 == 2) {
                            //第三个
                            walletListViewHolder.root_view.setBackgroundResource(R.drawable.eth_wallet_bg_02);
                        } else if (position % 4 == 0) {
                            //第一个
                            walletListViewHolder.root_view.setBackgroundResource(R.drawable.eth_wallet_bg_04);
                        } else if (position % 4 == 3) {
                            //第四个
                            walletListViewHolder.root_view.setBackgroundResource(R.drawable.eth_wallet_bg_01);
                        }
                        break;
                    case BTC:
                        if (position % 4 == 1) {
                            //第二个
                            walletListViewHolder.root_view.setBackgroundResource(R.drawable.btc_wallet_bg_03);
                        } else if (position % 4 == 2) {
                            //第三个
                            walletListViewHolder.root_view.setBackgroundResource(R.drawable.btc_wallet_bg_02);
                        } else if (position % 4 == 0) {
                            //第一个
                            walletListViewHolder.root_view.setBackgroundResource(R.drawable.btc_wallet_bg_04);
                        } else if (position % 4 == 3) {
                            //第四个
                            walletListViewHolder.root_view.setBackgroundResource(R.drawable.btc_wallet_bg_01);
                        }
                        break;
                }
                boolean b = MnemonicSharedPreferences.getInstance(activity).isBackUpMnemonic(entity.getAddress());
                if (b) {
                    walletListViewHolder.tv_no_backup.setVisibility(View.GONE);
                } else {
                    walletListViewHolder.tv_no_backup.setVisibility(View.VISIBLE);
                }
            }
            CommonUtils.setCurrency(walletListViewHolder.tv_amount, "0.00", false);

            switch (entity.getChain()) {
                case ETH:
                    LogUtils.log("请求ETH总资产");
                    //获取资产列表缓存
                    String key = entity.getAddress() + entity.getChain();
                    List<AssetsDetailsItemEntity> assetsDetailsItemEntityList = AssetsListSharedPreferences.getInstance(WalletApplication.getApp()).getAssetsList(key);
                    checkAssetsList(activity, entity.getAddress(), assetsDetailsItemEntityList, new HttpVolley.VolleyCallBack() {
                        @Override
                        public void onSuccess(String data) {
                            LogUtils.log("请求ETH总资产成功");
                            try {
                                JSONObject rootObj = new JSONObject(data);
                                Iterator iterator = rootObj.keys();
                                double totalPrice = 0;
                                while (iterator.hasNext()) {
                                    String key = (String) iterator.next();
                                    JSONObject resultObj = rootObj.optJSONObject(key);
                                    totalPrice += resultObj.optDouble("totalPrice");
                                }

                                CommonUtils.setCurrency(walletListViewHolder.tv_amount, MathUtils.doubleKeep2(totalPrice), false);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFail(VolleyError error) {
                            LogUtils.logError("请求ETH总资产失败 = " + error);
                        }
                    });
                    break;
                case BTC:
                    BitcoinApi.getAccountBalance(activity, entity.getAddress(), new BitcoinApi.GetAccountBalanceCallback() {
                        @Override
                        public void onGetBalanceSuccess(Double balance) {
                            LogUtils.log("获取BTC余额成功 =" + balance);

                            checkBTCCoinValue(activity, new HttpVolley.VolleyCallBack() {
                                @Override
                                public void onSuccess(String data) {
                                    LogUtils.log("请求BTC总资产成功 =" + data);
                                    double btcValueDouble = Double.parseDouble(data);
                                    double totalPrice = balance * btcValueDouble;
                                    CommonUtils.setCurrency(walletListViewHolder.tv_amount, MathUtils.doubleKeep2(totalPrice), false);
                                }

                                @Override
                                public void onFail(VolleyError error) {
                                    LogUtils.log("请求BTC总资产失败 =" + error);
                                }
                            });
                        }

                        @Override
                        public void onGetBalanceFailed(Exception e) {
                            LogUtils.logError("获取BTC余额失败 =" + e);
                        }
                    });
                    break;
                case ACL:
                    checkACLBlance(activity, entity.getAddress(), new HttpVolley.VolleyCallBack() {
                        @Override
                        public void onSuccess(String data) {
                            LogUtils.log("获取ACL余额成功 =" + data);
                            double aclDouble = Double.parseDouble(data);

                            checkACLCoinValue(activity, new HttpVolley.VolleyCallBack() {
                                @Override
                                public void onSuccess(String data) {
                                    LogUtils.log("请求ACL估值成功 = " + data);
                                    double aclValueDouble = Double.parseDouble(data);
                                    double totalPrice = aclDouble * aclValueDouble;

                                    CommonUtils.setCurrency(walletListViewHolder.tv_amount, MathUtils.doubleKeep2(totalPrice), false);
                                }

                                @Override
                                public void onFail(VolleyError error) {
                                    LogUtils.logError("请求ACL估值失败 = " + error);
                                }
                            });
                        }

                        @Override
                        public void onFail(VolleyError error) {
                            LogUtils.logError("获取ACL余额失败 =" + error);
                        }
                    });
                    break;
            }

            walletListViewHolder.root_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(v, position, entity);
                    }
                }
            });
            walletListViewHolder.ll_wallet_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.showOperateSelectDialog(selectDialog, entity, entity.getAddress());
//                    CommonUtils.copyContent(activity, entity.getAddress());
                }
            });
        }
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_CONTENT) {
            view = LayoutInflater.from(activity).inflate(R.layout.item_wallet_list, parent, false);
            WalletListViewHolder holder = new WalletListViewHolder(view);
            return holder;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @Override
    public void onBindTailViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        AddWalletViewHolder addWalletViewHolder = (AddWalletViewHolder) holder;
        if (isHardware) {
            addWalletViewHolder.rl_add_wallet.setVisibility(View.GONE);
        } else {
            addWalletViewHolder.rl_add_wallet.setVisibility(View.VISIBLE);
        }
        addWalletViewHolder.rl_add_wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (chain) {
                    case BTC:
                        chainType = 2;
                        break;
                    case ACL:
                        chainType = 3;
                        break;
                    case ETH:
                        chainType = 1;
                        break;
                    case ALL:
                        chainType = 0;
                        break;
                }
                Intent createIntent = new Intent(activity, ServiceAgreementActivity.class);
                createIntent.putExtra("_source", Constant.CreateWalletSource.SOURCE_MIAN_ACTIVITY);
                createIntent.putExtra("_chainType", chainType);
                LogUtils.log("WalletList startActivityForResult" + Constant.StartActivityResultCode.START_CREATE_WALLET);
//                activity.startActivityForResult(createIntent, Constant.StartActivityResultCode.START_CREATE_WALLET);
                context.startActivity(createIntent);
                if (walletManagerDialog != null) {
                    if (walletManagerDialog.isShowing()) {
                        walletManagerDialog.dismiss();
                    }
                }
            }
        });
    }


    @Override
    public RecyclerView.ViewHolder getTypeTailViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_add_wallet, parent, false);
        return new AddWalletViewHolder(view);
    }

    class WalletListViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout root_view;
        private TextView tv_wallet_name;
        private ImageView img_selected;
        private TextView tv_address;
        private ImageView img_copy;
        private TextView tv_amount;
        private ImageView img_enter;
        private TextView tv_no_backup;
        private LinearLayout ll_wallet_address;

        public WalletListViewHolder(@NonNull View itemView) {
            super(itemView);
            root_view = itemView.findViewById(R.id.root_view);
            tv_wallet_name = itemView.findViewById(R.id.tv_wallet_name);
            img_selected = itemView.findViewById(R.id.img_selected);
            tv_address = itemView.findViewById(R.id.tv_address);
            img_copy = itemView.findViewById(R.id.img_copy);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            img_enter = itemView.findViewById(R.id.img_enter);
            tv_no_backup = itemView.findViewById(R.id.tv_no_backup);
            ll_wallet_address = itemView.findViewById(R.id.ll_wallet_address);
        }
    }

    class AddWalletViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout rl_add_wallet;

        public AddWalletViewHolder(@NonNull View itemView) {
            super(itemView);
            rl_add_wallet = itemView.findViewById(R.id.rl_add_wallet);
        }
    }


    /**
     * 查询ETH，ERC20资产列表
     *
     * @param context
     * @param walletAddress
     * @param assetsDetailsItemEntityList
     * @param callBack
     */
    public static void checkAssetsList(Context context, String walletAddress, List<AssetsDetailsItemEntity> assetsDetailsItemEntityList, HttpVolley.VolleyCallBack callBack) {
        String contractAddress = "";
        List<AssetsDetailsItemEntity> assetsList = new ArrayList<>();
        assetsList.addAll(assetsDetailsItemEntityList);
        for (int i = 1; i < assetsDetailsItemEntityList.size(); i++) {
            if (i == assetsDetailsItemEntityList.size() - 1) {
                contractAddress += assetsDetailsItemEntityList.get(i).getContractAddress();
            } else {
                contractAddress += assetsDetailsItemEntityList.get(i).getContractAddress() + ",";
            }
        }
        LogUtils.log("请求首页资产列表的参数 == " + contractAddress);
        String url = null;
        if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_CNY) {
            url = Constant.HttpUrl.ETH_GET_ASSETS_INFO_LIST + "address=" + walletAddress + "&contractAddresses=" + contractAddress + "&currencyType=0";
        } else if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_USD) {
            url = Constant.HttpUrl.ETH_GET_ASSETS_INFO_LIST + "address=" + walletAddress + "&contractAddresses=" + contractAddress + "&currencyType=1";
        }
        HttpVolley.getInstance(context).HttpVolleyGet(url, callBack);
    }

    /**
     * 查询ACL货币估值
     *
     * @param callBack
     */
    private void checkACLCoinValue(Context context, HttpVolley.VolleyCallBack callBack) {
        String url = null;
        if (WalletApplication.getApp().isAclTest()) {
            if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_CNY) {
                url = Constant.HttpUrl.ACL_TEST_GET_COIN_VALUE + "?currencyType=0";
            } else if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_USD) {
                url = Constant.HttpUrl.ACL_TEST_GET_COIN_VALUE + "?currencyType=1";
            }
        } else {
            if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_CNY) {
                url = Constant.HttpUrl.ACL_GET_COIN_VALUE + "?currencyType=0";
            } else if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_USD) {
                url = Constant.HttpUrl.ACL_GET_COIN_VALUE + "?currencyType=1";
            }
        }

        HttpVolley.getInstance(context).HttpVolleyGet(url, callBack);
    }

    /**
     * 查询ACL余额
     *
     * @param address
     * @param callBack
     */
    private void checkACLBlance(Context context, String address, HttpVolley.VolleyCallBack callBack) {
        String url = "";
        if (WalletApplication.getApp().isAclTest()) {
            url = Constant.HttpUrl.ACL_TEST_GET_BALANCE + "?address=" + address;
        } else {
            url = Constant.HttpUrl.ACL_GET_BALANCE + "?address=" + address;
        }
        HttpVolley.getInstance(context).HttpVolleyGet(url, callBack);
    }

    /**
     * 查询BTC货币估值
     *
     * @param callBack
     */
    private void checkBTCCoinValue(Context context, HttpVolley.VolleyCallBack callBack) {
        String url = null;
        if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_CNY) {
            url = Constant.HttpUrl.BTC_GET_COIN_VALUE + "?currencyType=0";
        } else if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_USD) {
            url = Constant.HttpUrl.BTC_GET_COIN_VALUE + "?currencyType=1";
        }
        HttpVolley.getInstance(context).HttpVolleyGet(url, callBack);
    }

}
