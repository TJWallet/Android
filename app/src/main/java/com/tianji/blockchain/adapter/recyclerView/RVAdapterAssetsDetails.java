package com.tianji.blockchain.adapter.recyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.IntentFilterConstant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.adapter.basic.BasicItemClickRecyclerViewAdapter;
import com.tianji.blockchain.btcApi.BitcoinApi;
import com.tianji.blockchain.entity.AssetsDetailsItemEntity;
import com.tianji.blockchain.sharepreferences.AssetsListSharedPreferences;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.HttpVolley;
import com.tianji.blockchain.utils.ImageLoaderHelper;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.MathUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

import java.util.HashMap;
import java.util.List;

/**
 * 钱包首页资产明细列表
 */
public class RVAdapterAssetsDetails extends BasicItemClickRecyclerViewAdapter<AssetsDetailsItemEntity> {
    private ImageView img_eye;
    private double[] valueString;
    private int valueCount;
    private HashMap<Integer, Boolean> hiddenMap = new HashMap<Integer, Boolean>();
    private WalletInfo walletInfo;

    public RVAdapterAssetsDetails(Context context, List<AssetsDetailsItemEntity> data, WalletInfo walletInfo) {
        this.context = context;
        this.data = data;
        this.walletInfo = walletInfo;
        valueString = new double[data.size()];
        for (int i = 0; i < valueString.length; i++) {
            valueString[i] = -1d;
        }
        valueCount = 0;
        for (int i = 0; i < data.size(); i++) {
            hiddenMap.put(i, WalletApplication.getApp().isAssetsIsHidden());
        }
    }

    public void setEyes(boolean isHidden, ImageView img_eye) {
        this.img_eye = img_eye;
        for (int i = 0; i < data.size(); i++) {
            hiddenMap.put(i, isHidden);
        }
    }


    @Override
    protected void onBindContentViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        AssetsDetailsViewHolder assetsDetailsViewHolder = (AssetsDetailsViewHolder) holder;
        final AssetsDetailsItemEntity assetsDetailsItemEntity = data.get(position);
        LogUtils.log("列表中传入的对象精度是=" + assetsDetailsItemEntity.getDecimals());

        if (assetsDetailsItemEntity.getAssetsName().equals("ETH")) {
            assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.eth_icon_selected);
            if (hiddenMap.get(position) != null) {
                if (!hiddenMap.get(position)) {
                    assetsDetailsViewHolder.tv_item_assets_amount.setText(MathUtils.doubleKeep4(assetsDetailsItemEntity.getBalance()));
                    CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, MathUtils.doubleKeep2(assetsDetailsItemEntity.getTotalPrice()), false);
                } else {
                    clearStatus(assetsDetailsViewHolder, position, 1);
                }
            } else {
                assetsDetailsViewHolder.tv_item_assets_amount.setText(MathUtils.doubleKeep4(assetsDetailsItemEntity.getBalance()));
                CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, MathUtils.doubleKeep2(assetsDetailsItemEntity.getTotalPrice()), false);
            }
        } else if (assetsDetailsItemEntity.getAssetsName().equals("BTC")) {
            assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.btc);
            if (hiddenMap.get(position) != null) {
                if (!hiddenMap.get(position)) {
                    List<AssetsDetailsItemEntity> assetsDetailsItemEntityListBTC = AssetsListSharedPreferences.getInstance(context).getAssetsList(walletInfo.getAddress() + walletInfo.getChain());

                    if (assetsDetailsItemEntityListBTC.size() > 0) {
                        if (assetsDetailsItemEntityListBTC.get(0).getAssetsName().equals("BTC")) {
                            assetsDetailsViewHolder.tv_item_assets_amount.setText(MathUtils.doubleKeep8(assetsDetailsItemEntityListBTC.get(0).getBalance()));
                            CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, MathUtils.doubleKeep2(assetsDetailsItemEntityListBTC.get(0).getTotalPrice()), false);
                        } else {
                            assetsDetailsViewHolder.tv_item_assets_amount.setText("0.0000");
                            CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, "0.00", false);
                        }
                    } else {
                        assetsDetailsViewHolder.tv_item_assets_amount.setText("0.0000");
                        CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, "0.00", false);
                    }
                    clearStatus(assetsDetailsViewHolder, position, 0);
                    BitcoinApi.getAccountBalance(context, walletInfo.getAddress(), new BitcoinApi.GetAccountBalanceCallback() {
                        @Override
                        public void onGetBalanceSuccess(Double balance) {
                            LogUtils.log("获取BTC余额成功 =" + balance);
                            assetsDetailsViewHolder.tv_item_assets_amount.setText(MathUtils.doubleKeep8(balance));
                            assetsDetailsItemEntity.setBalance(balance);

                            checkBTCCoinValue(new HttpVolley.VolleyCallBack() {
                                @Override
                                public void onSuccess(String data) {
                                    double btcValueDouble = Double.parseDouble(data);
                                    double result = balance * btcValueDouble;
                                    assetsDetailsItemEntity.setTotalPrice(result);
                                    String key = walletInfo.getAddress() + walletInfo.getChain();
                                    AssetsListSharedPreferences.getInstance(context).addAssetes(key, assetsDetailsItemEntity);
                                    CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, MathUtils.doubleKeep2(result), false);
                                    valueString[position] = result;
                                    valueCount += 1;
                                    if (valueCount >= valueString.length) {
                                        //完了
                                        LogUtils.log("获取BTC估值完成，发出广播");
                                        Intent intent = new Intent(IntentFilterConstant.ASSETS_VALUE_GET_FINISH);
                                        intent.putExtra("_values", valueString);
                                        context.sendBroadcast(intent);
                                    } else {
                                        LogUtils.log("获取估值未完成 valueCount = " + valueCount + "/" + valueString.length);
                                    }
                                }

                                @Override
                                public void onFail(VolleyError error) {
                                    LogUtils.logError("请求BTC估值失败 = " + error);
                                    checkValueFailed(assetsDetailsViewHolder, position);
                                    if (assetsDetailsItemEntityListBTC.size() > 0) {
                                        if (assetsDetailsItemEntityListBTC.get(0).getAssetsName().equals("BTC")) {
                                            assetsDetailsViewHolder.tv_item_assets_amount.setText(MathUtils.doubleKeep4(assetsDetailsItemEntityListBTC.get(0).getBalance()));
                                            CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, MathUtils.doubleKeep2(assetsDetailsItemEntityListBTC.get(0).getTotalPrice()), false);
                                        } else {
                                            assetsDetailsViewHolder.tv_item_assets_amount.setText("0.0000");
                                            CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, "0.00", false);

                                        }
                                    } else {
                                        assetsDetailsViewHolder.tv_item_assets_amount.setText("0.0000");
                                        CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, "0.00", false);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onGetBalanceFailed(Exception e) {
                            LogUtils.logError("获取BTC余额失败 =" + e);
                            checkValueFailed(assetsDetailsViewHolder, position);
                            if (assetsDetailsItemEntityListBTC.size() > 0) {
                                if (assetsDetailsItemEntityListBTC.get(0).getAssetsName().equals("BTC")) {
                                    assetsDetailsViewHolder.tv_item_assets_amount.setText(MathUtils.doubleKeep4(assetsDetailsItemEntityListBTC.get(0).getBalance()));
                                    CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, MathUtils.doubleKeep2(assetsDetailsItemEntityListBTC.get(0).getTotalPrice()), false);
                                } else {
                                    assetsDetailsViewHolder.tv_item_assets_amount.setText("0.0000");
                                    CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, "0.00", false);

                                }
                            } else {
                                assetsDetailsViewHolder.tv_item_assets_amount.setText("0.0000");
                                CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, "0.00", false);
                            }
                        }
                    });
                } else {
                    clearStatus(assetsDetailsViewHolder, position, 1);
                }
            }
        } else if (assetsDetailsItemEntity.getAssetsName().equals("ACL")) {
            assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.acl_icon_select);
            if (hiddenMap.get(position) != null) {
                if (!hiddenMap.get(position)) {
                    List<AssetsDetailsItemEntity> assetsDetailsItemEntityListACL = AssetsListSharedPreferences.getInstance(context).getAssetsList(walletInfo.getAddress() + walletInfo.getChain());
                    if (assetsDetailsItemEntityListACL.size() > 0) {
                        if (assetsDetailsItemEntityListACL.get(0).getAssetsName().equals("ACL")) {
                            LogUtils.log("余额缓存== " + MathUtils.doubleKeep4(assetsDetailsItemEntityListACL.get(0).getBalance()));
                            assetsDetailsViewHolder.tv_item_assets_amount.setText(MathUtils.doubleKeep4(assetsDetailsItemEntityListACL.get(0).getBalance()));
                            CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, MathUtils.doubleKeep2(assetsDetailsItemEntityListACL.get(0).getTotalPrice()), false);
                        } else {
                            assetsDetailsViewHolder.tv_item_assets_amount.setText("0.0000");
                            CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, "0.00", false);
                        }

                    } else {
                        assetsDetailsViewHolder.tv_item_assets_amount.setText("0.0000");
                        CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, "0.00", false);
                    }
                    clearStatus(assetsDetailsViewHolder, position, 0);
                    checkACLBlance(walletInfo.getAddress(), new HttpVolley.VolleyCallBack() {
                        @Override
                        public void onSuccess(String data) {
                            LogUtils.log("获取ACL余额成功 =" + data);
                            double aclDouble = Double.parseDouble(data);
                            assetsDetailsViewHolder.tv_item_assets_amount.setText(MathUtils.doubleKeep4(aclDouble));
                            assetsDetailsItemEntity.setBalance(aclDouble);

                            checkACLCoinValue(new HttpVolley.VolleyCallBack() {
                                @Override
                                public void onSuccess(String data) {
                                    double aclValueDouble = Double.parseDouble(data);
                                    double result = aclDouble * aclValueDouble;
                                    assetsDetailsItemEntity.setTotalPrice(result);
                                    String key = walletInfo.getAddress() + walletInfo.getChain();
                                    AssetsListSharedPreferences.getInstance(context).addAssetes(key, assetsDetailsItemEntity);
                                    CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, MathUtils.doubleKeep2(result), false);
                                    valueString[position] = result;
                                    valueCount += 1;
                                    if (valueCount >= valueString.length) {
                                        //完了
                                        LogUtils.log("获取ACL估值完成，发出广播");
                                        Intent intent = new Intent(IntentFilterConstant.ASSETS_VALUE_GET_FINISH);
                                        intent.putExtra("_values", valueString);
                                        context.sendBroadcast(intent);
                                    } else {
                                        LogUtils.log("获取估值未完成 valueCount = " + valueCount + "/" + valueString.length);
                                    }
                                }

                                @Override
                                public void onFail(VolleyError error) {
                                    LogUtils.logError("请求ACL估值失败 = " + error);
                                    checkValueFailed(assetsDetailsViewHolder, position);
                                    if (assetsDetailsItemEntityListACL.size() > 0) {
                                        if (assetsDetailsItemEntityListACL.get(0).getAssetsName().equals("ACL")) {
                                            LogUtils.log("余额缓存== " + MathUtils.doubleKeep4(assetsDetailsItemEntityListACL.get(0).getBalance()));
                                            assetsDetailsViewHolder.tv_item_assets_amount.setText(MathUtils.doubleKeep4(assetsDetailsItemEntityListACL.get(0).getBalance()));
                                            CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, MathUtils.doubleKeep2(assetsDetailsItemEntityListACL.get(0).getTotalPrice()), false);
                                        } else {
                                            assetsDetailsViewHolder.tv_item_assets_amount.setText("0.0000");
                                            CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, "0.00", false);

                                        }

                                    } else {
                                        assetsDetailsViewHolder.tv_item_assets_amount.setText("0.0000");
                                        CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, "0.00", false);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFail(VolleyError error) {
                            LogUtils.logError("获取ACL余额失败 =" + error);
                            if (assetsDetailsItemEntityListACL.size() > 0) {
                                if (assetsDetailsItemEntityListACL.get(0).getAssetsName().equals("ACL")) {
                                    LogUtils.log("余额缓存== " + MathUtils.doubleKeep4(assetsDetailsItemEntityListACL.get(0).getBalance()));
                                    assetsDetailsViewHolder.tv_item_assets_amount.setText(MathUtils.doubleKeep4(assetsDetailsItemEntityListACL.get(0).getBalance()));
                                    CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, MathUtils.doubleKeep2(assetsDetailsItemEntityListACL.get(0).getTotalPrice()), false);
                                } else {
                                    assetsDetailsViewHolder.tv_item_assets_amount.setText("0.0000");
                                    CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, "0.00", false);
                                }

                            } else {
                                assetsDetailsViewHolder.tv_item_assets_amount.setText("0.0000");
                                CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, "0.00", false);
                            }
                            checkValueFailed(assetsDetailsViewHolder, position);
                        }
                    });
                } else {
                    clearStatus(assetsDetailsViewHolder, position, 1);
                }
            }
        } else {
            LogUtils.log("需要显示的图片地址是 =" + assetsDetailsItemEntity.getIconUrl());
            switch (assetsDetailsItemEntity.getAssetsName()) {
                case "CPCT":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.cpct_icon);
                    break;
                case "USDT":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.usdt_icon);
                    break;
                case "AE":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.ae_icon);
                    break;
                case "BAT":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.bat_icon);
                    break;
                case "BDT":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.bdt_icon);
                    break;
                case "BNB":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.bnb_icon);
                    break;
                case "BTM":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.btm_icon);
                    break;
                case "BUSD":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.busd_icon);
                    break;
                case "CRO":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.cro_icon);
                    break;
                case "DAI":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.dai_icon);
                    break;
                case "ENJ":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.enj_icon);
                    break;
                case "GNT":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.gnt_icon);
                    break;
                case "HEDG":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.hedg_icon);
                    break;
                case "HT":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.ht_icon);
                    break;
                case "HUSD":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.husd_icon);
                    break;
                case "INB":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.inb_icon);
                    break;
                case "INO":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.ino_icon);
                    break;
                case "KCS":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.kcs_icon);
                    break;
                case "knc":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.knc_icon);
                    break;
                case "LEND":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.lend_icon);
                    break;
                case "LEO":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.leo_icon);
                    break;
                case "LINK":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.link_icon);
                    break;
                case "LRC":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.lrc_icon);
                    break;
                case "MKR":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.mkr_icon);
                    break;
//                case "NPXS":
//                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.npxs_icon);
//                    break;
                case "OKB":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.okb_icon);
                    break;
                case "OMG":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.omg_icon);
                    break;
                case "PAX":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.pax_icon);
                    break;
                case "REP":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.rep_icon);
                    break;
                case "SNX":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.snx_icon);
                    break;
                case "THETA":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.theta_icon);
                    break;
                case "TUSD":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.tusd_icon);
                    break;
                case "USDC":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.usdc_icon);
                    break;
                case "VEN":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.ven_icon);
                    break;
                case "VEST":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.vest_icon);
                    break;
                case "VET":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.vet_icon);
                    break;
                case "WTC":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.wtc_icon);
                    break;
                case "ZIL":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.zil_icon);
                    break;
                case "ZRX":
                    assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.zrx_icon);
                    break;
                default:
                    ImageLoaderHelper.getInstance().loadImage(context, assetsDetailsViewHolder.img_item_assets_icon, assetsDetailsItemEntity.getIconUrl());
                    break;
            }


            if (hiddenMap.get(position) != null) {
                if (!hiddenMap.get(position)) {
                    LogUtils.log("ERC20余额缓存== " + assetsDetailsItemEntity.getAssetsName() + MathUtils.doubleKeep4(assetsDetailsItemEntity.getBalance()));
                    clearStatus(assetsDetailsViewHolder, position, 0);
                    assetsDetailsViewHolder.tv_item_assets_amount.setText(MathUtils.doubleKeep4(assetsDetailsItemEntity.getBalance()));
                    CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, MathUtils.doubleKeep2(assetsDetailsItemEntity.getTotalPrice()), false);
                } else {
                    clearStatus(assetsDetailsViewHolder, position, 1);
                }
            } else {
                assetsDetailsViewHolder.tv_item_assets_amount.setText(MathUtils.doubleKeep4(assetsDetailsItemEntity.getBalance()));
                CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, MathUtils.doubleKeep2(assetsDetailsItemEntity.getTotalPrice()), false);

                LogUtils.log("hiddenMap.get(position) == null");
            }
        }

        assetsDetailsViewHolder.tv_item_assets_name.setText(assetsDetailsItemEntity.getAssetsName());


        if (position == data.size() - 1) {
            assetsDetailsViewHolder.view_line.setVisibility(View.GONE);
        } else {
            assetsDetailsViewHolder.view_line.setVisibility(View.VISIBLE);
        }
        assetsDetailsViewHolder.root_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, position, assetsDetailsItemEntity);

            }
        });
    }

    @Override
    protected RecyclerView.ViewHolder getTypeContentViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_assets_details, parent, false);
        AssetsDetailsViewHolder assetsDetailsViewHolder = new AssetsDetailsViewHolder(view);
        return assetsDetailsViewHolder;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class AssetsDetailsViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout root_view;
        private ImageView img_item_assets_icon;
        private TextView tv_item_assets_name;
        private TextView tv_item_assets_amount;
        private TextView tv_item_assets_value;
        private View view_line;

        public AssetsDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            root_view = itemView.findViewById(R.id.root_view);
            img_item_assets_icon = itemView.findViewById(R.id.img_item_assets_icon);
            tv_item_assets_name = itemView.findViewById(R.id.tv_item_assets_name);
            tv_item_assets_amount = itemView.findViewById(R.id.tv_item_assets_amount);
            tv_item_assets_value = itemView.findViewById(R.id.tv_item_assets_value);
            view_line = itemView.findViewById(R.id.view_line);
        }
    }

    /**
     * 清除显示状态
     *
     * @param holder
     * @param position
     */
    private void clearStatus(AssetsDetailsViewHolder holder, int position, int type) {
        if (type == 0) {
            if (img_eye != null) {
                ViewCommonUtils.showPwd(hiddenMap.get(position), holder.tv_item_assets_amount, img_eye);
                ViewCommonUtils.showPwd(hiddenMap.get(position), holder.tv_item_assets_value, img_eye);
            }
        } else if (type == 1) {
            if (img_eye != null) {
                ViewCommonUtils.showPwd(hiddenMap.get(position), holder.tv_item_assets_amount, img_eye);
                ViewCommonUtils.showPwd(hiddenMap.get(position), holder.tv_item_assets_value, img_eye);
                valueCount = 0;
            }
        }

    }

    /**
     * 查询ACL余额
     *
     * @param address
     * @param callBack
     */
    private void checkACLBlance(String address, HttpVolley.VolleyCallBack callBack) {
        String url = "";
        if (WalletApplication.getApp().isAclTest()) {
            url = Constant.HttpUrl.ACL_TEST_GET_BALANCE + "?address=" + address;
        } else {
            url = Constant.HttpUrl.ACL_GET_BALANCE + "?address=" + address;
        }
        HttpVolley.getInstance(context).HttpVolleyGet(url, callBack);
    }

    /**
     * 查询ERC20余额
     *
     * @param address
     * @param contractAddress
     * @param callBack
     */
    private void checkERC20Blance(String address, String contractAddress, HttpVolley.VolleyCallBack callBack) {
        String url = Constant.HttpUrl.checkERC20Balance + address + "&contractAddress=" + contractAddress;
        LogUtils.log("请求地址checkERC20Blance=" + url);
        HttpVolley.getInstance(context).HttpVolleyGet(url, callBack);
    }

    /**
     * 查询ETH余额
     *
     * @param walletAddress
     * @param callBack
     */
    private void checkETHBlance(String walletAddress, HttpVolley.VolleyCallBack callBack) {
        String url = Constant.HttpUrl.checkETHBalance + walletAddress;
        HttpVolley.getInstance(context).HttpVolleyGet(url, callBack);
    }

    /**
     * 查询货币估值
     *
     * @param contractAddress
     * @param callBack
     */
    private void checkCoinValue(String contractAddress, HttpVolley.VolleyCallBack callBack) {

        String url;
        if (contractAddress.equals("")) {
            url = Constant.HttpUrl.getCoinValue;
        } else {
            url = Constant.HttpUrl.getCoinValue + "?contractAddress=" + contractAddress;
        }
        LogUtils.log("请求货币估值 =" + url);
        HttpVolley.getInstance(context).HttpVolleyGet(url, callBack);
    }

    /**
     * 查询BTC货币估值
     *
     * @param callBack
     */
    private void checkBTCCoinValue(HttpVolley.VolleyCallBack callBack) {
        String url = null;
        if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_CNY) {
            url = Constant.HttpUrl.BTC_GET_COIN_VALUE + "?currencyType=0";
        } else if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_USD) {
            url = Constant.HttpUrl.BTC_GET_COIN_VALUE + "?currencyType=1";
        }
        HttpVolley.getInstance(context).HttpVolleyGet(url, callBack);
    }

    /**
     * 查询ACL货币估值
     *
     * @param callBack
     */
    private void checkACLCoinValue(HttpVolley.VolleyCallBack callBack) {
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
     * 查询货币价值失败
     *
     * @param assetsDetailsViewHolder
     * @param position
     */
    private void checkValueFailed(AssetsDetailsViewHolder assetsDetailsViewHolder, int position) {
        valueString[position] = 0d;
        valueCount += 1;
        if (valueCount >= valueString.length) {
            //完了
            LogUtils.log("获取估值完成，发出广播");
            Intent intent = new Intent(IntentFilterConstant.ASSETS_VALUE_GET_FINISH);
            intent.putExtra("_values", valueString);
            context.sendBroadcast(intent);
        } else {
            LogUtils.log("获取估值未完成 valueCount = " + valueCount + "/" + valueString.length);
        }
    }
}
