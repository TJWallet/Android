package com.tianji.blockchain.entity;

import java.io.Serializable;

/** author:jason **/
public class DappEntity implements Serializable {
    private String mid;
    private String icon;
    private String name;
    private String nameEn;
    private String summary;
    private String summaryEn;
    private String typeName;
    private String typeNameEn;
    private String typeColor;
    private String platformName;
    private String platformNameEn;
    private int user24Hours;
    private String createTime;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummaryEn() {
        return summaryEn;
    }

    public void setSummaryEn(String summaryEn) {
        this.summaryEn = summaryEn;
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

    public String getPlatformNameEn() {
        return platformNameEn;
    }

    public void setPlatformNameEn(String platformNameEn) {
        this.platformNameEn = platformNameEn;
    }

    public int getUser24Hours() {
        return user24Hours;
    }

    public void setUser24Hours(int user24Hours) {
        this.user24Hours = user24Hours;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "DappEntity{" +
                "mid='" + mid + '\'' +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", summary='" + summary + '\'' +
                ", summaryEn='" + summaryEn + '\'' +
                ", typeName='" + typeName + '\'' +
                ", typeNameEn='" + typeNameEn + '\'' +
                ", typeColor='" + typeColor + '\'' +
                ", platformName='" + platformName + '\'' +
                ", platformNameEn='" + platformNameEn + '\'' +
                ", user24Hours=" + user24Hours +
                ", createTime='" + createTime + '\'' +
                '}';
    }
}
