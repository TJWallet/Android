package com.tianji.blockchain.entity;

import java.io.Serializable;
import java.util.List;

public class ArticleByDateEntity implements Serializable {
    private String date;
    private String extraTime;
    private List<ArticleItemEntity> articleList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExtraTime() {
        return extraTime;
    }

    public void setExtraTime(String extraTime) {
        this.extraTime = extraTime;
    }

    public List<ArticleItemEntity> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<ArticleItemEntity> articleList) {
        this.articleList = articleList;
    }

    @Override
    public String toString() {
        return "ArticleByDateEntity{" +
                "date='" + date + '\'' +
                ", extraTime='" + extraTime + '\'' +
                ", articleList=" + articleList +
                '}';
    }
}
