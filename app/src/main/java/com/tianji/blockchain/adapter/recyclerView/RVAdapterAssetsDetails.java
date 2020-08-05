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
import com.tianji.blockchain.entity.AssetsDetailsItemEntity;
import com.tianji.blockchain.sharepreferences.AssetsListSharedPreferences;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.HttpVolley;
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

        assetsDetailsViewHolder.img_item_assets_icon.setImageResource(R.drawable.file_coin_select);

        if (hiddenMap.get(position) != null) {
            if (!hiddenMap.get(position)) {
                List<AssetsDetailsItemEntity> assetsDetailsItemEntityList = AssetsListSharedPreferences.getInstance(context).getAssetsList(walletInfo.getAddress() + walletInfo.getChain());
                if (assetsDetailsItemEntityList.size() > 0) {
                    assetsDetailsViewHolder.tv_item_assets_amount.setText(MathUtils.doubleKeep4(assetsDetailsItemEntityList.get(0).getBalance()));
                } else {
                    assetsDetailsViewHolder.tv_item_assets_amount.setText("0.0000");
                }
                clearStatus(assetsDetailsViewHolder, position, 0);
                checkFILBlance(context, walletInfo.getAddress(), new HttpVolley.VolleyCallBack() {
                    @Override
                    public void onSuccess(String data) {
                        LogUtils.log("请求FIL余额成功");
                        double filBlance = Double.parseDouble(data);
                        assetsDetailsViewHolder.tv_item_assets_amount.setText(MathUtils.doubleKeep4(filBlance));
                        assetsDetailsItemEntity.setBalance(filBlance);

                        checkFILCoinValue(new HttpVolley.VolleyCallBack() {
                            @Override
                            public void onSuccess(String data) {
                                double filValueDouble = Double.parseDouble(data);
                                double result = filBlance * filValueDouble;
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

                            }
                        });
                    }

                    @Override
                    public void onFail(VolleyError error) {
                        LogUtils.log("请求FIL余额失败 == " + error.networkResponse.statusCode);
                    }
                });
            } else {
                clearStatus(assetsDetailsViewHolder, position, 1);
            }
        } else {
            assetsDetailsViewHolder.tv_item_assets_amount.setText(MathUtils.doubleKeep4(assetsDetailsItemEntity.getBalance()));
            CommonUtils.setCurrency(assetsDetailsViewHolder.tv_item_assets_value, MathUtils.doubleKeep2(assetsDetailsItemEntity.getTotalPrice()), false);
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
     * 查询FIL余额
     *
     * @param address
     * @param callBack
     */
    private void checkFILBlance(Context context, String address, HttpVolley.VolleyCallBack callBack) {
        String url = Constant.HttpUrl.CHECK_FILECOIN_BALANCE + address;
        HttpVolley.getInstance(context).HttpVolleyGet(url, callBack);
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
