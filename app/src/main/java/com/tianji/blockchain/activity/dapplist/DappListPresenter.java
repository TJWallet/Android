package com.tianji.blockchain.activity.dapplist;

import android.content.Context;

import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.basic.BasicPresenter;

import java.util.Map;

/** author:jason **/
public class DappListPresenter extends BasicPresenter {
    public DappListPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        super(context, basicMvpInterface);
    }

    @Override
    public void getData(Map<String, String> params) {
        String action = params.get("action");
        if ("".equals(action)) {

        }
    }
}
