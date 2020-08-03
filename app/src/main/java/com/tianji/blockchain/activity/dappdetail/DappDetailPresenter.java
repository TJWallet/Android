package com.tianji.blockchain.activity.dappdetail;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.VolleyError;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.basic.BasicPresenter;
import com.tianji.blockchain.entity.DappDetailEntity;
import com.tianji.blockchain.entity.RatingStatisticsEntity;
import com.tianji.blockchain.entity.RelevantArticleEntity;
import com.tianji.blockchain.restful.Api;
import com.tianji.blockchain.restful.bean.ArticleBean;
import com.tianji.blockchain.restful.bean.DappDetailBean;
import com.tianji.blockchain.restful.bean.DappRatingStatisticsBean;
import com.tianji.blockchain.restful.bean.PagerBean;
import com.tianji.blockchain.restful.bean.PagerResponseBean;
import com.tianji.blockchain.restful.bean.ResponseBean;
import com.tianji.blockchain.utils.HttpVolley;
import com.tianji.blockchain.utils.LogUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * author:jason
 **/
public class DappDetailPresenter extends BasicPresenter {
    public DappDetailPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        super(context, basicMvpInterface);
    }

    @Override
    public void getData(Map<String, String> params) {
        String action = params.get("action");
        if ("fetchDetailData".equals(action)) {
            this.fetchDetailData(params);
        } else if ("fetchRelevantArticle".equals(action)) {
            this.fetchRelevantArticle(params);
        } else if ("fetchRatingStatistics".equals(action)) {
            this.fetchRatingStatistics(params);
        }
    }

    private void fetchDetailData(Map<String, String> params) {
        String mid = params.get("mid");
        Api.getInstance().fetchDappDetail(params.containsKey("cache"), mid, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                ResponseBean<DappDetailBean> response = JSON.parseObject(data, new TypeReference<ResponseBean<DappDetailBean>>() {
                });
                if (response.getStatus() == 1) {
                    DappDetailEntity result = response.getResult().transfer();
                    basicMvpInterface.getDataSuccess(result, 1);
                } else {
                    // 获取详情信息失败
                }
            }

            @Override
            public void onFail(VolleyError error) {
                // 获取详情信息失败
            }
        });
    }

    private void fetchRelevantArticle(Map<String, String> params) {
        String mid = params.get("mid");
        Api.getInstance().fetchRelevantArticle(params.containsKey("cache"), mid, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.log("获取到的相关动态json=" + data);
                PagerResponseBean<ArticleBean> response = JSON.parseObject(data, new TypeReference<PagerResponseBean<ArticleBean>>() {
                });
                if (response.getStatus() == 1) {
                    PagerBean<ArticleBean> result = response.getResult();
                    List<ArticleBean> resultData = result.getData();
                    if (resultData != null && resultData.size() > 0) {
                        List<RelevantArticleEntity> temp = new ArrayList<>();
                        for (ArticleBean item : resultData) {
                            temp.add(item.transfer());
                        }
                        basicMvpInterface.getDataSuccess(temp, 2);
                    } else {
                        // 相关新闻无数据
                    }
                } else {
                    // 获取相关新闻失败

                }
            }

            @Override
            public void onFail(VolleyError error) {
                // 获取相关新闻失败
            }
        });
    }

    private void fetchRatingStatistics(Map<String, String> params) {
        String mid = params.get("mid");
        Api.getInstance().fetchRatingStatistics(params.containsKey("cache"), mid, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                ResponseBean<DappRatingStatisticsBean> response = JSON.parseObject(data, new TypeReference<ResponseBean<DappRatingStatisticsBean>>() {
                });
                if (response.getStatus() == 1) {
                    RatingStatisticsEntity result = response.getResult().transfer();
                    basicMvpInterface.getDataSuccess(result, 3);
                } else {
                    // 获取评分信息失败
                }
            }

            @Override
            public void onFail(VolleyError error) {
                // 获取评分信息失败
            }
        });
    }
}
