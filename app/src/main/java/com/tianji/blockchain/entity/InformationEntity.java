package com.tianji.blockchain.entity;

import java.io.Serializable;

public class InformationEntity implements Serializable {
    private int id;
    private String title;
    private String content;
    private long createdAt;
    private boolean isReaded;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isReaded() {
        return isReaded;
    }

    public void setReaded(boolean readed) {
        this.isReaded = readed;
    }

    @Override
    public String toString() {
        return "InformationEntity{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", isReaded=" + isReaded +
                '}';
    }
}
