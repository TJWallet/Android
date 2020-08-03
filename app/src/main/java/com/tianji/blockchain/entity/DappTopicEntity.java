package com.tianji.blockchain.entity;

import java.io.Serializable;
import java.util.List;

/** author:jason **/
public class DappTopicEntity implements Serializable {
    private String mid;
    private String topicName;
    private String topicNameEN;
    private List<DappEntity> items;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getTopicNameEN() {
        return topicNameEN;
    }

    public void setTopicNameEN(String topicNameEN) {
        this.topicNameEN = topicNameEN;
    }

    public List<DappEntity> getItems() {
        return items;
    }

    public void setItems(List<DappEntity> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "DappTopicEntity{" +
                "mid='" + mid + '\'' +
                ", topicName='" + topicName + '\'' +
                ", topicNameEN='" + topicNameEN + '\'' +
                ", items=" + items +
                '}';
    }
}
