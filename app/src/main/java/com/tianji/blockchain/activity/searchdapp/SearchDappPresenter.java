package com.tianji.blockchain.activity.searchdapp;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.VolleyError;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.basic.BasicPresenter;
import com.tianji.blockchain.entity.DappEntity;
import com.tianji.blockchain.entity.DappTopicEntity;
import com.tianji.blockchain.entity.RecentHistoryEntity;
import com.tianji.blockchain.restful.Api;
import com.tianji.blockchain.restful.bean.DappBean;
import com.tianji.blockchain.restful.bean.PagerBean;
import com.tianji.blockchain.restful.bean.PagerResponseBean;
import com.tianji.blockchain.restful.bean.ResponseBean;
import com.tianji.blockchain.restful.bean.TopicBean;
import com.tianji.blockchain.sharepreferences.SearchHistorySharedPreferences;
import com.tianji.blockchain.utils.HttpVolley;
import com.tianji.blockchain.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** author:jason **/
public class SearchDappPresenter extends BasicPresenter {
    public SearchDappPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        super(context, basicMvpInterface);
    }

    @Override
    public void getData(Map<String, String> params) {
        String action = params.get("action");

        if ("fetchSearchHistory".equals(action)) {
            if (Constant.IS_MOCK_MODE) {
                this.mockHistoryData();
            } else {
                this.fetchSearchHistory(params);
            }
        } else if ("fetchHotDapp".equals(action)) {
            if (Constant.IS_MOCK_MODE) {
                this.mockHotDappData();
            } else {
                this.fetchHotDapp(params);
            }
        } else if ("search".equals(action)) {
            if (Constant.IS_MOCK_MODE) {
                this.mockSearch();
            } else {
                this.search(params);
            }
        }
    }

    private void fetchSearchHistory(Map<String, String> params) {
        List<String> history = SearchHistorySharedPreferences.getInstance(context).getHistory();
        basicMvpInterface.getDataSuccess(history, 1);
    }

    private void fetchHotDapp(Map<String, String> params) {
        Api.getInstance().fetchHotTopic(new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                ResponseBean<List<TopicBean>> response = JSON.parseObject(data, new TypeReference<ResponseBean<List<TopicBean>>>(){});
                if (response.getStatus() == 1) {
                    List<TopicBean> result = response.getResult();
                    if (result != null && result.size() > 0) {
                        List<DappTopicEntity> temp = new ArrayList<>();
                        for (TopicBean item : result) {
                            temp.add(item.transfer());
                        }
                        basicMvpInterface.getDataSuccess(temp.get(0).getItems(), 2);
                    } else {
                        basicMvpInterface.getDataSuccess(null, 2);
                    }
                } else {
                    String errorMsg = response.getErrMsg();
                    basicMvpInterface.getDataFail(0, errorMsg, 2);
                }
            }

            @Override
            public void onFail(VolleyError error) {
                String errorMsg = error.getMessage();
                basicMvpInterface.getDataFail(-1, errorMsg, 2);
            }
        });
    }

    private void search(Map<String, String> params) {
        String keyword = params.get("keyword");
        Api.getInstance().searchDapp(keyword, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                LogUtils.e(data);
                PagerResponseBean<DappBean> response = JSON.parseObject(data, new TypeReference<PagerResponseBean<DappBean>>(){});
                if(response.getStatus() == 1) {
                    PagerBean<DappBean> result = response.getResult();
                    List<DappBean> resultData = result.getData();
                    if (resultData != null && resultData.size() > 0) {
                        List<DappEntity> temp = new ArrayList<>();
                        for (DappBean item : resultData) {
                            temp.add(item.transfer());
                        }
                        basicMvpInterface.getDataSuccess(temp, 3);
                    } else {
                        // 没有数据
                        basicMvpInterface.getDataSuccess(null, 3);
                    }
                } else {
                    // 搜索失败
                    basicMvpInterface.getDataFail(0, response.getErrMsg(), 3);
                }
            }

            @Override
            public void onFail(VolleyError error) {
                LogUtils.e(error.getMessage());
                basicMvpInterface.getDataFail(-1, error.getMessage(), 3);
            }
        });
    }

    // 创建模拟数据
    private void mockHistoryData() {
        List<RecentHistoryEntity> historyList = new ArrayList<RecentHistoryEntity>();
        RecentHistoryEntity history01 = new RecentHistoryEntity();
        history01.setLink("https://dapponline.io/dapp-detail/1428");
        RecentHistoryEntity history02 = new RecentHistoryEntity();
        history02.setLink("https://dapponline.io/dapp-ranking/tron");
        historyList.add(history01);
        historyList.add(history02);
        this.basicMvpInterface.getDataSuccess(historyList, 1);
    }

    private void mockHotDappData() {
        final String defaultIcon = "https://staticfiles.dapponline.io//uploads/images/20200110/56780d1c8285f253158f3c4bcc9ad2c5.png";

        List<DappEntity> dappList = new ArrayList<>();
        DappEntity item01 = new DappEntity();
        item01.setIcon(defaultIcon);
        item01.setName("Crypto Hero");
        DappEntity item02 = new DappEntity();
        item02.setIcon(defaultIcon);
        item02.setName("Crypto Hero");
        DappEntity item03 = new DappEntity();
        item03.setIcon(defaultIcon);
        item03.setName("Crypto Hero");
        DappEntity item04 = new DappEntity();
        item04.setIcon(defaultIcon);
        item04.setName("Crypto Hero");
        dappList.add(item01);
        dappList.add(item02);
        dappList.add(item03);
        dappList.add(item04);

        this.basicMvpInterface.getDataSuccess(dappList, 2);
    }

    private void mockSearch() {
        final String defaultIcon = "https://staticfiles.dapponline.io//uploads/images/20200110/56780d1c8285f253158f3c4bcc9ad2c5.png";

        List<DappEntity> searchResult = new ArrayList<>();
        DappEntity item01 = new DappEntity();
        item01.setIcon(defaultIcon);
        item01.setName("Hex");
        item01.setSummary("an ether platform game");
        item01.setTypeName("游戏");
        item01.setUser24Hours(32806);
        searchResult.add(item01);

        DappEntity item02 = new DappEntity();
        item02.setIcon(defaultIcon);
        item02.setName("DoubleWay");
        item02.setSummary("an ether platform game");
        item02.setTypeName("游戏");
        item02.setUser24Hours(29562);
        searchResult.add(item02);
        this.basicMvpInterface.getDataSuccess(searchResult, 3);
    }
}
