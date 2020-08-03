package com.tianji.blockchain.entity;

import java.io.Serializable;
import java.util.List;

public class QuestionListEntity implements Serializable {
    private List<QuestionItemEntity> content;
    private int totalElements;


    public List<QuestionItemEntity> getContent() {
        return content;
    }

    public void setContent(List<QuestionItemEntity> content) {
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
        return "QuestionListEntity{" +
                "content=" + content +
                ", totalElements=" + totalElements +
                '}';
    }
}
