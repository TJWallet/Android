package com.tianji.blockchain.entity;

import java.io.Serializable;

/** author:jason **/
public class RelevantArticleEntity implements Serializable {
    private String mid;
    private String thumbnail;
    private String title;
    private String titleEN;
    private String createTime;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleEN() {
        return titleEN;
    }

    public void setTitleEN(String titleEN) {
        this.titleEN = titleEN;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "RelevantArticleEntity{" +
                "mid='" + mid + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", title='" + title + '\'' +
                ", titleEN='" + titleEN + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
