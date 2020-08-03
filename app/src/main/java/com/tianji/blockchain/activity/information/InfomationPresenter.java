package com.tianji.blockchain.activity.information;

import android.content.Context;

import com.android.volley.VolleyError;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.basic.BasicPresenter;
import com.tianji.blockchain.entity.InformationListEntity;
import com.tianji.blockchain.utils.HttpVolley;
import com.tianji.blockchain.utils.LogUtils;

import java.util.Map;

public class InfomationPresenter extends BasicPresenter {
    public InfomationPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        super(context, basicMvpInterface);
    }

    @Override
    public void getData(Map<String, String> params) {
        String url = Constant.HttpUrl.getSystemMsgList + "page=" + params.get("page") + "&size=" + params.get("pageSize");
        HttpVolley.getInstance(context).HttpVolleyGet(url, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                InformationListEntity entity = gson.fromJson(data, InformationListEntity.class);
                LogUtils.log("获取到的消息列表数据是" + entity.getContent().size());
                basicMvpInterface.getDataSuccess(entity, 0);
            }

            @Override
            public void onFail(VolleyError error) {
                basicMvpInterface.getDataFail(error, 0);
            }
        });
    }
}
