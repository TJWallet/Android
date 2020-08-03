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

public class SelectAssetsPresenter extends BasicPresenter {
    public SelectAssetsPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        super(context, basicMvpInterface);
    }

    @Override
    public void getData(Map<String, String> params) {
        HttpVolley.getInstance(context).HttpVolleyGet(Constant.HttpUrl.assetsListUrl, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("选择货币界面请求货币列表" + data);
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
                LogUtils.log("货币列表请求失败" + error);
                basicMvpInterface.getDataFail(error, 0);
            }
        });
    }
}
