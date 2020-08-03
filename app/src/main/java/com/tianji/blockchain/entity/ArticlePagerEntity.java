package com.tianji.blockchain.entity;

import java.io.Serializable;
import java.util.List;

public class ArticlePagerEntity implements Serializable {
    private int count;
    private int updateCount;
    private String firstId;
    private String lastId;
    private List<ArticleByDateEntity> data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getUpdateCount() {
        return updateCount;
    }

    public void setUpdateCount(int updateCount) {
        this.updateCount = updateCount;
    }

    public String getFirstId() {
        return firstId;
    }

    public void setFirstId(String firstId) {
        this.firstId = firstId;
    }

    public String getLastId() {
        return lastId;
    }

    public void setLastId(String lastId) {
        this.lastId = lastId;
    }

    public List<ArticleByDateEntity> getData() {
        return data;
    }

    public void setData(List<ArticleByDateEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ArticlePagerEntity{" +
                "count=" + count +
                ", updateCount=" + updateCount +
                ", firstId='" + firstId + '\'' +
                ", lastId='" + lastId + '\'' +
                ", data=" + data +
                '}';
    }
}
