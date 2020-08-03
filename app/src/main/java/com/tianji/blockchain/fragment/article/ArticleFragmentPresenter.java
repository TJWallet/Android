package com.tianji.blockchain.fragment.article;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.VolleyError;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.basic.BasicPresenter;
import com.tianji.blockchain.entity.ArticlePagerEntity;
import com.tianji.blockchain.entity.BannerEntity;
import com.tianji.blockchain.entity.DappEntity;
import com.tianji.blockchain.entity.DappTopicEntity;
import com.tianji.blockchain.entity.RecentHistoryEntity;
import com.tianji.blockchain.entity.RecommendEntity;
import com.tianji.blockchain.restful.Api;
import com.tianji.blockchain.restful.bean.ArticlePagerBean;
import com.tianji.blockchain.restful.bean.BannerBean;
import com.tianji.blockchain.restful.bean.DappBean;
import com.tianji.blockchain.restful.bean.PagerBean;
import com.tianji.blockchain.restful.bean.PagerResponseBean;
import com.tianji.blockchain.restful.bean.RecommendBean;
import com.tianji.blockchain.restful.bean.ResponseBean;
import com.tianji.blockchain.restful.bean.TopicBean;
import com.tianji.blockchain.sharepreferences.RecentHistorySharedPreferences;
import com.tianji.blockchain.utils.HttpVolley;
import com.tianji.blockchain.utils.LogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** author:jason **/
public class ArticleFragmentPresenter extends BasicPresenter {
    public ArticleFragmentPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        super(context, basicMvpInterface);
    }

    @Override
    public void getData(Map<String, String> params) {
        String action = params.get("action");
        boolean fromCache = params.containsKey("cache");

        if ("fetchArticleList".equals(action)) {
            this.fetchArticleList(fromCache, params);
        }
    }

    /**
     * 获取快讯列表
     * @param fromCache
     * @param params
     */
    private void fetchArticleList(boolean fromCache, Map<String, String> params) {
        String startIndex = params.get("startIndex");

//        String pageSize = params.get("pageSize");
//        Api.getInstance().fetchArticleListFromSomewhere(Integer.parseInt(startIndex), Integer.parseInt(pageSize), false, new HttpVolley.VolleyCallBack() {
//            @Override
//            public void onSuccess(String data) {
//                ArticlePagerBean response = JSON.parseObject(data, new TypeReference<ArticlePagerBean>(){});
//                basicMvpInterface.getDataSuccess(response.transfer(), 1);
//            }
//
//            @Override
//            public void onFail(VolleyError error) {
//                basicMvpInterface.getDataFail(0, error.getMessage(), 1);
//            }
//        });

        Api.getInstance().fetchArticleList(fromCache, startIndex, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                ResponseBean<ArticlePagerBean> response = JSON.parseObject(data, new TypeReference<ResponseBean<ArticlePagerBean>>(){});
                if (response.getStatus() == 1) {
                    ArticlePagerBean result = response.getResult();
                    basicMvpInterface.getDataSuccess(result.transfer(), 1);
                } else {
                    basicMvpInterface.getDataFail(0, response.getErrMsg(), 1);
                }
            }

            @Override
            public void onFail(VolleyError error) {
                basicMvpInterface.getDataFail(0, error.getMessage(), 1);
            }
        });
    }
}