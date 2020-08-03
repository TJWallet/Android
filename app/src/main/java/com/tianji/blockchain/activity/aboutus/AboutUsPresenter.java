package com.tianji.blockchain.activity.aboutus;

import android.content.Context;

import com.android.volley.VolleyError;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.basic.BasicPresenter;
import com.tianji.blockchain.entity.UpdateEntity;
import com.tianji.blockchain.utils.HttpVolley;

import java.util.Map;

public class AboutUsPresenter extends BasicPresenter {
    public AboutUsPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        super(context, basicMvpInterface);
    }

    @Override
    public void getData(Map<String, String> params) {
        if (WalletApplication.getApp().isDeveloperVersion()) {
            HttpVolley.getInstance(context).HttpVolleyGet(Constant.HttpUrl.updateUrl + "?flag=1", new HttpVolley.VolleyCallBack() {
                @Override
                public void onSuccess(String data) {
                    UpdateEntity entity = gson.fromJson(data, UpdateEntity.class);
                    basicMvpInterface.getDataSuccess(entity, 0);
                }

                @Override
                public void onFail(VolleyError error) {
                    basicMvpInterface.getDataFail(error, 0);
                }
            });
        } else {
            HttpVolley.getInstance(context).HttpVolleyGet(Constant.HttpUrl.updateUrl, new HttpVolley.VolleyCallBack() {
                @Override
                public void onSuccess(String data) {
                    UpdateEntity entity = gson.fromJson(data, UpdateEntity.class);
                    basicMvpInterface.getDataSuccess(entity, 0);
                }

                @Override
                public void onFail(VolleyError error) {
                    basicMvpInterface.getDataFail(error, 0);
                }
            });
        }

    }
}
