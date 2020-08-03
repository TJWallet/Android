package com.tianji.blockchain.activity.assets;

import android.content.Context;

import com.android.volley.VolleyError;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.basic.BasicPresenter;
import com.tianji.blockchain.entity.AssetsDetailsItemEntity;
import com.tianji.blockchain.utils.HttpVolley;
import com.tianji.blockchain.utils.LogUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddAssetsPresenter extends BasicPresenter {
    public static final int TYPE_SEARCH_COIN = 1;

    public AddAssetsPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        super(context, basicMvpInterface);
    }

    @Override
    public void getData(Map<String, String> params) {
        HttpVolley.getInstance(context).HttpVolleyGet(Constant.HttpUrl.assetsListUrl, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("请求货币列表" + data);
                List<AssetsDetailsItemEntity> entityList = new ArrayList<>();
                try {
                    JSONArray rootArray = new JSONArray(data);
                    for (int i = 0; i < rootArray.length(); i++) {
                        AssetsDetailsItemEntity entity = new AssetsDetailsItemEntity();
                        JSONObject resultObj = rootArray.optJSONObject(i);
                        entity.setAssetsName(resultObj.optString("symbol"));
                        entity.setAssetsCompleteName(resultObj.optString("name"));
                        entity.setId(resultObj.optInt("id"));
                        entity.setContractAddress(resultObj.optString("address"));
                        entity.setIconUrl(resultObj.optString("iconUrl"));
                        entity.setDecimals(resultObj.optInt("decimals"));

                        entityList.add(entity);
                    }
                    basicMvpInterface.getDataSuccess(entityList, 0);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.logError("货币列表请求失败" + error);
                basicMvpInterface.getDataFail(error, 0);
            }
        });
    }

    /**
     * 搜索货币
     *
     * @param params
     */
    public void searchCoinName(Map<String, String> params) {
        String url = Constant.HttpUrl.searchCoinName + "?page=" + params.get("page") + "&size=" + params.get("pageSize") + "&symbol=" + params.get("symbol");
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("搜索货币接口返回" + data);
                List<AssetsDetailsItemEntity> entityList = new ArrayList<>();

                try {
                    JSONObject rootObj = new JSONObject(data);
                    JSONArray rootArray = rootObj.optJSONArray("content");
                    for (int i = 0; i < rootArray.length(); i++) {
                        AssetsDetailsItemEntity entity = new AssetsDetailsItemEntity();
                        JSONObject resultObj = rootArray.optJSONObject(i);
                        entity.setAssetsName(resultObj.optString("symbol"));
                        entity.setAssetsCompleteName(resultObj.optString("name"));
                        entity.setId(resultObj.optInt("id"));
                        entity.setContractAddress(resultObj.optString("address"));
                        entity.setIconUrl(resultObj.optString("iconUrl"));
                        entity.setDecimals(resultObj.optInt("decimals"));

                        entityList.add(entity);
                    }
                    basicMvpInterface.getDataSuccess(entityList, TYPE_SEARCH_COIN);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(VolleyError error) {
                basicMvpInterface.getDataFail(error, TYPE_SEARCH_COIN);
            }
        });
    }
}
