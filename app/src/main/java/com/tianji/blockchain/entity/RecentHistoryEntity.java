package com.tianji.blockchain.entity;

import java.io.Serializable;

/**
 * author:jason
 **/
public class RecentHistoryEntity implements Serializable {
    private String mid;
    private String icon;
    private String type;
    private String description;
    private String summary;
    private String link;
    private String typeColor;
    private String platformName;
    private String createTime;
    private String summaryEn;
    private String descriptionEn;
    private String platformNameEn;
    private String typeEn;


    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTypeColor() {
        return typeColor;
    }

    public void setTypeColor(String typeColor) {
        this.typeColor = typeColor;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSummaryEn() {
        return summaryEn;
    }

    public void setSummaryEn(String summaryEn) {
        this.summaryEn = summaryEn;
    }

    public String getDescriptionEn() {
        return descriptionEn;
    }

    public void setDescriptionEn(String descriptionEn) {
        this.descriptionEn = descriptionEn;
    }

    public String getPlatformNameEn() {
        return platformNameEn;
    }

    public void setPlatformNameEn(String platformNameEn) {
        this.platformNameEn = platformNameEn;
    }

    public String getTypeEn() {
        return typeEn;
    }

    public void setTypeEn(String typeEn) {
        this.typeEn = typeEn;
    }

    @Override
    public String toString() {
        return "RecentHistoryEntity{" +
                "mid='" + mid + '\'' +
                ", icon='" + icon + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", summary='" + summary + '\'' +
                ", link='" + link + '\'' +
                ", typeColor='" + typeColor + '\'' +
                ", platformName='" + platformName + '\'' +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
