package com.tianji.blockchain.entity;

import java.util.List;

public class DiscoveryMergedEntity {
    private List<BannerEntity> bannerList;
    private List<RecentHistoryEntity> recentHistoryList;
    private List<DappEntity> recommendList;
    private List<DappEntity> rankingList;
    private List<DappTopicEntity> topicList;

    public List<BannerEntity> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<BannerEntity> bannerList) {
        this.bannerList = bannerList;
    }

    public List<RecentHistoryEntity> getRecentHistoryList() {
        return recentHistoryList;
    }

    public void setRecentHistoryList(List<RecentHistoryEntity> recentHistoryList) {
        this.recentHistoryList = recentHistoryList;
    }

    public List<DappEntity> getRecommendList() {
        return recommendList;
    }

    public void setRecommendList(List<DappEntity> recommendList) {
        this.recommendList = recommendList;
    }

    public List<DappEntity> getRankingList() {
        return rankingList;
    }

    public void setRankingList(List<DappEntity> rankingList) {
        this.rankingList = rankingList;
    }

    public List<DappTopicEntity> getTopicList() {
        return topicList;
    }

    public void setTopicList(List<DappTopicEntity> topicList) {
        this.topicList = topicList;
    }

    @Override
    public String toString() {
        return "DiscoveryMergedEntity{" +
                "bannerList=" + bannerList +
                ", recentHistoryList=" + recentHistoryList +
                ", recommendList=" + recommendList +
                ", rankingList=" + rankingList +
                ", topicList=" + topicList +
                '}';
    }
}
