package com.tianji.blockchain.entity;

import java.io.Serializable;

public class ArticleItemEntity implements Serializable {
    private String mid;
    private String title;
    private String content;
    private String datetime;
    private String sourceLink;
    private String sourceLinkTitle;
    private String ipfs_hash;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public String getSourceLinkTitle() {
        return sourceLinkTitle;
    }

    public void setSourceLinkTitle(String sourceLinkTitle) {
        this.sourceLinkTitle = sourceLinkTitle;
    }

    public String getIpfs_hash() {
        return ipfs_hash;
    }

    public void setIpfs_hash(String ipfs_hash) {
        this.ipfs_hash = ipfs_hash;
    }

    @Override
    public String toString() {
        return "ArticleItemEntity{" +
                "mid='" + mid + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", datetime='" + datetime + '\'' +
                ", sourceLink='" + sourceLink + '\'' +
                ", sourceLinkTitle='" + sourceLinkTitle + '\'' +
                '}';
    }
}
