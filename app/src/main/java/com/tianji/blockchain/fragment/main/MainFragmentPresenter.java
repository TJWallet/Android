package com.tianji.blockchain.fragment.main;

import android.content.Context;

import com.android.volley.VolleyError;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.basic.BasicPresenter;
import com.tianji.blockchain.entity.UpdateEntity;
import com.tianji.blockchain.utils.HttpVolley;

import java.util.Map;

public class MainFragmentPresenter extends BasicPresenter {

    public MainFragmentPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        super(context, basicMvpInterface);
    }

    @Override
    public void getData(Map<String, String> params) {

    }
}
