package com.tianji.blockchain.entity;

import java.util.List;

public class InformationListEntity {
    private List<InformationEntity> content;
    private int totalElements;


    public List<InformationEntity> getContent() {
        return content;
    }

    public void setContent(List<InformationEntity> content) {
        this.content = content;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    @Override
    public String toString() {
        return "InformationListEntity{" +
                "content=" + content +
                ", totalElements=" + totalElements +
                '}';
    }
}
