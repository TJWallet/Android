package com.tianji.blockchain.entity;

import java.io.Serializable;

public class RecommendEntity implements Serializable {
    private String mid;
    private String icon;
    private String title;
    private String titleEn;
    private String subtitle;
    private String subtitleEn;
    private String link;
    private String typeName;
    private String typeNameEn;
    private String typeColor;
    private String platformName;
    private String platformNameEn;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitleEn() {
        return titleEn;
    }

    public void setTitleEn(String titleEn) {
        this.titleEn = titleEn;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSubtitleEn() {
        return subtitleEn;
    }

    public void setSubtitleEn(String subtitleEn) {
        this.subtitleEn = subtitleEn;
    }

    public String getTypeColor() {
        return typeColor;
    }

    public void setTypeColor(String typeColor) {
        this.typeColor = typeColor;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeNameEn() {
        return typeNameEn;
    }

    public void setTypeNameEn(String typeNameEn) {
        this.typeNameEn = typeNameEn;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformNameEn() {
        return platformNameEn;
    }

    public void setPlatformNameEn(String platformNameEn) {
        this.platformNameEn = platformNameEn;
    }

    @Override
    public String toString() {
        return "RecommendEntity{" +
                "mid='" + mid + '\'' +
                ", icon='" + icon + '\'' +
                ", title='" + title + '\'' +
                ", titleEn='" + titleEn + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", subtitleEn='" + subtitleEn + '\'' +
                ", link='" + link + '\'' +
                ", typeName='" + typeName + '\'' +
                ", typeNameEn='" + typeNameEn + '\'' +
                ", platformName='" + platformName + '\'' +
                ", platformNameEn='" + platformNameEn + '\'' +
                '}';
    }

    public DappEntity toDappEntity() {
        DappEntity result = new DappEntity();
        result.setTypeColor(this.getTypeColor());
        result.setMid(this.getMid());
        result.setName(this.getTitle());
        result.setNameEn(this.getTitleEn());
        result.setTypeName(this.getTypeName());
        result.setTypeNameEn(this.getTypeNameEn());
        result.setSummary(this.getSubtitle());
        result.setSummaryEn(this.getSubtitleEn());
        result.setIcon(this.getIcon());
        result.setPlatformName(this.getPlatformName());
        result.setPlatformNameEn(this.getPlatformNameEn());
        return result;
    }

    public RecentHistoryEntity toRecentHistoryEntity() {
        RecentHistoryEntity result = new RecentHistoryEntity();
        result.setIcon(this.getIcon());
        result.setDescription(this.getTitle());
        result.setSummary(this.getSubtitle());
        return result;
    }
}
