package com.tianji.blockchain.entity;

import java.io.Serializable;
import java.util.List;

public class QuestionColumnEntity implements Serializable {
    private String title;
    private int id;
    private long createdAt;
    private QuestionListEntity questionListEntity;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public QuestionListEntity getQuestionListEntity() {
        return questionListEntity;
    }

    public void setQuestionListEntity(QuestionListEntity questionListEntity) {
        this.questionListEntity = questionListEntity;
    }

    @Override
    public String toString() {
        return "QuestionColumnEntity{" +
                "title='" + title + '\'' +
                ", id=" + id +
                ", createdAt=" + createdAt +
                ", questionListEntity=" + questionListEntity +
                '}';
    }
}
