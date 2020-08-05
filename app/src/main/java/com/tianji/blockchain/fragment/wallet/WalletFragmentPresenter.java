package com.tianji.blockchain.fragment.wallet;

import android.content.Context;

import com.android.volley.VolleyError;
import com.tianji.blockchain.activity.basic.BasicCustomPresenter;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.basic.BasicPresenter;
import com.tianji.blockchain.entity.AssetsDetailsItemEntity;
import com.tianji.blockchain.entity.InformationListEntity;
import com.tianji.blockchain.entity.WalletFragmentAssetsListEntity;
import com.tianji.blockchain.sharepreferences.InformationListSharedPreferences;
import com.tianji.blockchain.utils.HttpVolley;
import com.tianji.blockchain.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WalletFragmentPresenter extends BasicCustomPresenter {


    public WalletFragmentPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        super(context, basicMvpInterface);
    }


}
