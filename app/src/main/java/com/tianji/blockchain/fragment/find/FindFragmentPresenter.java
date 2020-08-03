package com.tianji.blockchain.fragment.find;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.VolleyError;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.basic.BasicPresenter;
import com.tianji.blockchain.entity.BannerEntity;
import com.tianji.blockchain.entity.DappEntity;
import com.tianji.blockchain.entity.DappTopicEntity;
import com.tianji.blockchain.entity.RecentHistoryEntity;
import com.tianji.blockchain.entity.RecommendEntity;
import com.tianji.blockchain.restful.Api;
import com.tianji.blockchain.restful.bean.BannerBean;
import com.tianji.blockchain.restful.bean.DappBean;
import com.tianji.blockchain.restful.bean.PagerBean;
import com.tianji.blockchain.restful.bean.PagerResponseBean;
import com.tianji.blockchain.restful.bean.RecommendBean;
import com.tianji.blockchain.restful.bean.ResponseBean;
import com.tianji.blockchain.restful.bean.TopicBean;
import com.tianji.blockchain.sharepreferences.RecentHistorySharedPreferences;
import com.tianji.blockchain.utils.HttpVolley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:jason
 **/
public class FindFragmentPresenter extends BasicPresenter {
    private int requestNumber = 0;

    public FindFragmentPresenter(Context context, BasicMvpInterface basicMvpInterface) {
        super(context, basicMvpInterface);
    }

    private void validateRequestNumber() {
        if (this.requestNumber <= 0) {
            return;
        }

        this.requestNumber--;
        if (this.requestNumber == 0) {
            this.basicMvpInterface.getDataSuccess(null, 6);
        }
    }

    @Override
    public void getData(Map<String, String> params) {
        String action = params.get("action");
        boolean isFromCache = params.containsKey("cache");

        if ("init".equals(action)) {
            this.requestNumber = 5;

            if (Constant.IS_MOCK_MODE) {
                this.mockBannerList();
                this.mockRecentList();
                this.mockRankingList();
                this.mockTopicList();
            } else {
                this.fetchBannerList(isFromCache, null);
                this.fetchRecentList(null);
                this.fetchRankingList(isFromCache, null);
                this.fetchTopicList(isFromCache, null);
                this.fetchRecommendList(isFromCache, null);
            }
        } else if ("fetchBannerList".equals(action)) {
            if (Constant.IS_MOCK_MODE) {
                this.mockBannerList();
            } else {
                this.fetchBannerList(isFromCache, params);
            }
        } else if ("fetchRecentList".equals(action)) {
            if (Constant.IS_MOCK_MODE) {
                this.mockRecentList();
            } else {
                this.fetchRecentList(params);
            }
        } else if ("fetchRecommendList".equals(action)) {
            if (Constant.IS_MOCK_MODE) {

            } else {
                this.fetchRecommendList(isFromCache, params);
            }
        } else if ("fetchRankingList".equals(action)) {
            if (Constant.IS_MOCK_MODE) {
                this.mockRankingList();
            } else {
                this.fetchRankingList(isFromCache, params);
            }
        } else if ("fetchTopicList".equals(action)) {
            if (Constant.IS_MOCK_MODE) {
                this.mockTopicList();
            } else {
                this.fetchTopicList(isFromCache, params);
            }
        }
    }

    private void fetchBannerList(boolean fromCache, Map<String, String> params) {
        if (params == null) {
            params = new HashMap<>();
            params.put("position", "16");
        }
        int position = Integer.parseInt(params.get("position"));

        Api.getInstance().fetchBannerList(fromCache, position, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                ResponseBean<List<BannerBean>> response = JSON.parseObject(data, new TypeReference<ResponseBean<List<BannerBean>>>() {
                });
                if (response.getStatus() == 1) {
                    List<BannerBean> result = response.getResult();
                    if (result != null && result.size() > 0) {
                        List<BannerEntity> temp = new ArrayList<>();
                        for (BannerBean item : result) {
                            temp.add(item.transfer());
                        }
                        basicMvpInterface.getDataSuccess(temp, 1);
                    }
                } else {
                    // 获取广告列表出错
                    basicMvpInterface.getDataFail(0, response.getErrMsg(), 1);
                }
                validateRequestNumber();
            }

            @Override
            public void onFail(VolleyError error) {
                validateRequestNumber();
                // 获取广告列表出错
                basicMvpInterface.getDataFail(-1, error.getMessage(), 1);
            }
        });
    }

    private void fetchRecentList(Map<String, String> params) {
        List<RecentHistoryEntity> result = RecentHistorySharedPreferences.getInstance(context).getHistory();
        basicMvpInterface.getDataSuccess(result, 2);
        validateRequestNumber();
    }

    private void fetchRecommendList(boolean fromCache, Map<String, String> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        int pageIndex = params.get("pageIndex") != null ? Integer.parseInt(params.get("pageIndex")) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize")) : 50;

        Api.getInstance().fetchRecommend(fromCache, pageIndex, pageSize, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                PagerResponseBean<RecommendBean> response = JSON.parseObject(data, new TypeReference<PagerResponseBean<RecommendBean>>() {
                });
                if (response.getStatus() == 1) {
                    PagerBean<RecommendBean> result = response.getResult();
                    List<RecommendBean> dataList = result.getData();
                    if (dataList != null && dataList.size() > 0) {
                        List<RecommendEntity> temp = new ArrayList<>();
                        for (RecommendBean item : dataList) {
                            temp.add(item.transfer());
                        }
                        basicMvpInterface.getDataSuccess(temp, 3);
                    } else {
                        basicMvpInterface.getDataSuccess(null, 3);
                    }
                } else {
                    basicMvpInterface.getDataFail(0, response.getErrMsg(), 3);
                }
                validateRequestNumber();
            }

            @Override
            public void onFail(VolleyError error) {
                validateRequestNumber();
                // 获取推荐数据失败
                basicMvpInterface.getDataFail(-1, error.getMessage(), 3);
            }
        });
    }

    private void fetchRankingList(boolean fromCache, Map<String, String> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        String currentPlatformID = params.get("currentPlatformID");
        int pageIndex = params.get("pageIndex") != null ? Integer.parseInt(params.get("pageIndex")) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize")) : 50;
        Api.getInstance().fetchDappRanking(fromCache, currentPlatformID == null ? "-1" : currentPlatformID, pageIndex, pageSize, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                PagerResponseBean<DappBean> response = JSON.parseObject(data, new TypeReference<PagerResponseBean<DappBean>>() {
                });
                if (response.getStatus() == 1) {
                    List<DappBean> resultData = response.getResult().getData();
                    if (resultData != null && resultData.size() > 0) {
                        List<DappEntity> temp = new ArrayList<>();

                        for (DappBean item : resultData) {
                            temp.add(item.transfer());
                        }
                        basicMvpInterface.getDataSuccess(temp, 4);
                    }
                } else {
                    // 获取排行榜数据失败
                    basicMvpInterface.getDataFail(0, response.getErrMsg(), 4);
                }
                validateRequestNumber();
            }

            @Override
            public void onFail(VolleyError error) {
                validateRequestNumber();
                // 获取排行榜数据失败
                basicMvpInterface.getDataFail(-1, error.getMessage(), 4);
            }
        });
    }

    private void fetchTopicList(boolean fromCache, Map<String, String> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        int pageIndex = params.get("pageIndex") != null ? Integer.parseInt(params.get("pageIndex")) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize")) : 5;

        Api.getInstance().fetchTopicList(fromCache, pageIndex, pageSize, new HttpVolley.VolleyCallBack() {
            @Override
            public void onSuccess(String data) {
                ResponseBean<List<TopicBean>> response = JSON.parseObject(data, new TypeReference<ResponseBean<List<TopicBean>>>() {
                });
                if (response.getStatus() == 1) {
                    List<TopicBean> result = response.getResult();
                    if (result != null && result.size() > 0) {
                        List<DappTopicEntity> temp = new ArrayList<>();
                        for (TopicBean item : result) {
//                            if (item.getId() == 4) {
//                                // 将ID为4的专题单独展示（本周推荐专栏）
//                                basicMvpInterface.getDataSuccess(item.transfer().getItems(), 3);
//                            } else {
//                                temp.add(item.transfer());
//                            }
                            temp.add(item.transfer());
                        }
                        basicMvpInterface.getDataSuccess(temp, 5);
                    } else {
                        basicMvpInterface.getDataSuccess(null, 5);
                    }
                } else {
                    String errorMsg = response.getErrMsg();
                    basicMvpInterface.getDataFail(0, errorMsg, 5);
                }
                validateRequestNumber();
            }

            @Override
            public void onFail(VolleyError error) {
                validateRequestNumber();
                basicMvpInterface.getDataFail(-1, error.getMessage(), 5);
            }
        });
    }

    private void mockBannerList() {
        final String bannerImg = "https://staticfiles.dapponline.io//uploads/images/20190212/e8cdc8f00744660c78b0d32feeb54c9f.png";

        List<BannerEntity> bannerList = new ArrayList<>();
        BannerEntity banner01 = new BannerEntity();
        banner01.setTitle("欢庆圣诞");
        banner01.setLink("https://dapponline.io");
        banner01.setThumbnail(bannerImg);
        bannerList.add(banner01);
        BannerEntity banner02 = new BannerEntity();
        banner02.setTitle("货币市场改革，快来围观");
        banner02.setLink("https://dapponline.io");
        banner02.setThumbnail(bannerImg);
        bannerList.add(banner02);

        this.basicMvpInterface.getDataSuccess(bannerList, 1);
    }

    private void mockRecentList() {
        final String defaultIcon = "https://staticfiles.dapponline.io//uploads/images/20200110/56780d1c8285f253158f3c4bcc9ad2c5.png";

        List<RecentHistoryEntity> recentList = new ArrayList<>();
        RecentHistoryEntity recent01 = new RecentHistoryEntity();
        recent01.setIcon(defaultIcon);
        recent01.setDescription("Hex");
        recent01.setCreateTime("2020/05/25");
        recentList.add(recent01);
        RecentHistoryEntity recent02 = new RecentHistoryEntity();
        recent02.setIcon(defaultIcon);
        recent02.setDescription("DoubleWay");
        recent02.setCreateTime("2020/05/26");
        recentList.add(recent02);
        RecentHistoryEntity recent03 = new RecentHistoryEntity();
        recent03.setIcon(defaultIcon);
        recent03.setDescription("Hex");
        recent03.setCreateTime("2020/05/25");
        recentList.add(recent03);
        RecentHistoryEntity recent04 = new RecentHistoryEntity();
        recent04.setIcon(defaultIcon);
        recent04.setDescription("DoubleWay");
        recent04.setCreateTime("2020/05/26");
        recentList.add(recent04);
        RecentHistoryEntity recent05 = new RecentHistoryEntity();
        recent05.setIcon(defaultIcon);
        recent05.setDescription("Hex");
        recent05.setCreateTime("2020/05/25");
        recentList.add(recent05);
        RecentHistoryEntity recent06 = new RecentHistoryEntity();
        recent06.setIcon(defaultIcon);
        recent06.setDescription("DoubleWay");
        recent06.setCreateTime("2020/05/26");
        recentList.add(recent06);

        this.basicMvpInterface.getDataSuccess(recentList, 2);
    }

    private void mockRankingList() {
        final String defaultIcon = "https://staticfiles.dapponline.io//uploads/images/20200110/56780d1c8285f253158f3c4bcc9ad2c5.png";

        List<DappEntity> rankingList = new ArrayList<>();
        DappEntity ranking01 = new DappEntity();
        ranking01.setIcon(defaultIcon);
        ranking01.setName("Hex");
        ranking01.setSummary("an ether platform game");
        ranking01.setCreateTime("2020/05/06");
        ranking01.setTypeName("游戏");
        ranking01.setUser24Hours(32806);
        rankingList.add(ranking01);
        DappEntity ranking02 = new DappEntity();
        ranking02.setIcon(defaultIcon);
        ranking02.setName("DoubleWay");
        ranking02.setSummary("an ether platform game");
        ranking02.setCreateTime("2020/06/02");
        ranking02.setTypeName("游戏");
        ranking02.setUser24Hours(29562);
        rankingList.add(ranking02);

        this.basicMvpInterface.getDataSuccess(rankingList, 4);
    }

    private void mockTopicList() {
        final String defaultIcon = "https://staticfiles.dapponline.io//uploads/images/20200110/56780d1c8285f253158f3c4bcc9ad2c5.png";

        List<DappEntity> recommendList = new ArrayList<>();
        DappEntity recommend01 = new DappEntity();
        recommend01.setIcon(defaultIcon);
        recommend01.setName("Hex");
        recommend01.setSummary("an ether platform game");
        recommend01.setCreateTime("2020/05/01");
        recommend01.setTypeName("游戏");
        recommend01.setUser24Hours(32806);
        recommendList.add(recommend01);
        DappEntity recommend02 = new DappEntity();
        recommend02.setIcon(defaultIcon);
        recommend02.setName("DoubleWay");
        recommend02.setSummary("an ether platform game");
        recommend02.setCreateTime("2020/04/28");
        recommend02.setTypeName("游戏");
        recommend02.setUser24Hours(29562);
        recommendList.add(recommend02);

        this.basicMvpInterface.getDataSuccess(recommendList, 3);

        List<DappTopicEntity> topicList = new ArrayList<>();
        DappTopicEntity topic01 = new DappTopicEntity();
        topic01.setTopicName("2020最热门游戏");
        List<DappEntity> topicItems = new ArrayList<>();
        DappEntity topicItem01 = new DappEntity();
        topicItem01.setIcon(defaultIcon);
        topicItem01.setName("Hex");
        topicItem01.setSummary("an ether platform game");
        topicItem01.setCreateTime("2020/03/28");
        topicItem01.setTypeName("游戏");
        topicItem01.setUser24Hours(32806);
        topicItems.add(topicItem01);
        DappEntity topicItem02 = new DappEntity();
        topicItem02.setIcon(defaultIcon);
        topicItem02.setName("DoubleWay");
        topicItem02.setSummary("an ether platform game");
        topicItem02.setCreateTime("2020/08/21");
        topicItem02.setTypeName("游戏");
        topicItem02.setUser24Hours(29562);
        topicItems.add(topicItem02);
        topic01.setItems(topicItems);
        topicList.add(topic01);

        DappTopicEntity topic02 = new DappTopicEntity();
        topic02.setTopicName("最具话题游戏");
        List<DappEntity> topic02Items = new ArrayList<>();
        DappEntity topicItem03 = new DappEntity();
        topicItem03.setIcon(defaultIcon);
        topicItem03.setName("Crypto Hero");
        topicItem03.setSummary("a very hot game on ether a very hot game on ether a very hot game on ether");
        topicItem03.setCreateTime("2020/06/01");
        topicItem03.setTypeName("游戏");
        topicItem03.setUser24Hours(48560);
        topic02Items.add(topicItem03);
        DappEntity topicItem04 = new DappEntity();
        topicItem04.setIcon(defaultIcon);
        topicItem04.setName("Secondary Forever");
        topicItem04.setSummary("an tron platform game");
        topicItem04.setCreateTime("2020/06/02");
        topicItem04.setTypeName("游戏");
        topicItem04.setUser24Hours(28654);
        topic02Items.add(topicItem04);
        topic02.setItems(topic02Items);
        topicList.add(topic02);

        this.basicMvpInterface.getDataSuccess(topicList, 5);
    }
}