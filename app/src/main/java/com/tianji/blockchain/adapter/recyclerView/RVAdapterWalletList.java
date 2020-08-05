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
import com.tianji.blockchain.utils.MathUtils;
import com.tianji.blockchainwallet.constant.enums.Chain;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.createwallet.ServiceAgreementActivity;
import com.tianji.blockchain.adapter.basic.CustomRecyclerViewAdapter;
import com.tianji.blockchain.adapter.basic.ITail;
import com.tianji.blockchain.dialog.SelectDialog;
import com.tianji.blockchain.entity.AssetsDetailsItemEntity;
import com.tianji.blockchain.sharepreferences.AssetsListSharedPreferences;
import com.tianji.blockchain.sharepreferences.MnemonicSharedPreferences;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.HttpVolley;
import com.tianji.blockchain.utils.LogUtils;


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


            switch (entity.getChain()) {
                case FIL:
                    if (position % 4 == 1) {
                        //第二个
                        walletListViewHolder.root_view.setBackgroundResource(R.drawable.filecoin_wallet_bg_03);
                    } else if (position % 4 == 2) {
                        //第三个
                        walletListViewHolder.root_view.setBackgroundResource(R.drawable.filecoin_wallet_bg_02);
                    } else if (position % 4 == 0) {
                        //第一个
                        walletListViewHolder.root_view.setBackgroundResource(R.drawable.filecoin_wallet_bg_04);
                    } else if (position % 4 == 3) {
                        //第四个
                        walletListViewHolder.root_view.setBackgroundResource(R.drawable.filecoin_wallet_bg_01);
                    }
                    break;
            }
            boolean b = MnemonicSharedPreferences.getInstance(activity).isBackUpMnemonic(entity.getAddress());
            if (b) {
                walletListViewHolder.tv_no_backup.setVisibility(View.GONE);
            } else {
                walletListViewHolder.tv_no_backup.setVisibility(View.VISIBLE);
            }

            CommonUtils.setCurrency(walletListViewHolder.tv_amount, "0.00", false);

            switch (entity.getChain()) {
                case FIL:
                    //获取资产列表缓存
                    String key = entity.getAddress() + entity.getChain();
                    List<AssetsDetailsItemEntity> assetsDetailsItemEntityList = AssetsListSharedPreferences.getInstance(WalletApplication.getApp()).getAssetsList(key);
                    checkFILBlance(activity, entity.getAddress(), new HttpVolley.VolleyCallBack() {
                        @Override
                        public void onSuccess(String data) {
                            double filBlance = Double.parseDouble(data);
                            checkFILCoinValue(new HttpVolley.VolleyCallBack() {
                                @Override
                                public void onSuccess(String data) {
                                    double filValueDouble = Double.parseDouble(data);
                                    double result = filBlance * filValueDouble;
                                    CommonUtils.setCurrency(walletListViewHolder.tv_amount, MathUtils.doubleKeep2(result), false);
                                }

                                @Override
                                public void onFail(VolleyError error) {
                                    CommonUtils.setCurrency(walletListViewHolder.tv_amount, MathUtils.doubleKeep2(assetsDetailsItemEntityList.get(0).getTotalPrice()), false);
                                }
                            });
                        }

                        @Override
                        public void onFail(VolleyError error) {
                            CommonUtils.setCurrency(walletListViewHolder.tv_amount, MathUtils.doubleKeep2(assetsDetailsItemEntityList.get(0).getTotalPrice()), false);
                        }
                    });
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
                    case FIL:
                        chainType = 4;
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
     * 查询FIL货币估值
     *
     * @param callBack
     */
    private void checkFILCoinValue(HttpVolley.VolleyCallBack callBack) {
        String url = null;
        if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_CNY) {
            url = Constant.HttpUrl.CHECK_FILECOIN_VALUE + "?currencyType=0";
        } else if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_USD) {
            url = Constant.HttpUrl.CHECK_FILECOIN_VALUE + "?currencyType=1";
        }
        HttpVolley.getInstance(context).HttpVolleyGet(url, callBack);
    }

    /**
     * 查询FIL余额
     *
     * @param address
     * @param callBack
     */
    private void checkFILBlance(Context context, String address, HttpVolley.VolleyCallBack callBack) {
        String url = Constant.HttpUrl.CHECK_FILECOIN_BALANCE + address;
        HttpVolley.getInstance(context).HttpVolleyGet(url, callBack);
    }


}
