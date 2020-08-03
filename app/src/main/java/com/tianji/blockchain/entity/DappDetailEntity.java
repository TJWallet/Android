package com.tianji.blockchain.entity;

import java.io.Serializable;
import java.util.List;

public class DappDetailEntity implements Serializable {
    private String mid;
    private String icon;
    private String name;
    private String nameEN;
    private String thumbnail;
    private String summary;
    private String summaryEN;
    private String platformName;
    private String platformNameEN;
    private String typeName;
    private String typeNameEN;
    private String typeColor;
    private int followNumber;
    private String link;
    private String description;
    private String descriptionEN;
    private int isFollow;
    private int user24H;
    private String volume24H;
    private int tx24H;
    private String volume7D;
    private int tx7D;
    private String user24HPercent;
    private String volume24HPercent;
    private String tx24HPercent;
    private String volume7DPercent;
    private String tx7DPercent;
    private String assets;
    private List<SocialMediaEntity> socialList;
    private List<DappDiagramDataEntity> diagramData24H;
    private List<DappDiagramDataEntity> diagramData7D;
    private List<String> screenshotList;
    private List<SmartContractEntity> smartContractList;
    private List<RelevantArticleEntity> relevantArticleList;
    private List<DappEntity> recommendDappList;
    private String ipfs_hash;
    private String ipfs_hash_en;

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

    public String getIpfs_hash_en() {
        return ipfs_hash_en;
    }

    public void setIpfs_hash_en(String ipfs_hash_en) {
        this.ipfs_hash_en = ipfs_hash_en;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummaryEN() {
        return summaryEN;
    }

    public void setSummaryEN(String summaryEN) {
        this.summaryEN = summaryEN;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformNameEN() {
        return platformNameEN;
    }

    public void setPlatformNameEN(String platformNameEN) {
        this.platformNameEN = platformNameEN;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeNameEN() {
        return typeNameEN;
    }

    public void setTypeNameEN(String typeNameEN) {
        this.typeNameEN = typeNameEN;
    }

    public String getTypeColor() {
        return typeColor;
    }

    public void setTypeColor(String typeColor) {
        this.typeColor = typeColor;
    }

    public int getFollowNumber() {
        return followNumber;
    }

    public void setFollowNumber(int followNumber) {
        this.followNumber = followNumber;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionEN() {
        return descriptionEN;
    }

    public void setDescriptionEN(String descriptionEN) {
        this.descriptionEN = descriptionEN;
    }

    public int getIsFollow() {
        return isFollow;
    }

    public void setIsFollow(int isFollow) {
        this.isFollow = isFollow;
    }

    public int getUser24H() {
        return user24H;
    }

    public void setUser24H(int user24H) {
        this.user24H = user24H;
    }

    public String getVolume24H() {
        return volume24H;
    }

    public void setVolume24H(String volume24H) {
        this.volume24H = volume24H;
    }

    public int getTx24H() {
        return tx24H;
    }

    public void setTx24H(int tx24H) {
        this.tx24H = tx24H;
    }

    public String getVolume7D() {
        return volume7D;
    }

    public void setVolume7D(String volume7D) {
        this.volume7D = volume7D;
    }

    public int getTx7D() {
        return tx7D;
    }

    public void setTx7D(int tx7D) {
        this.tx7D = tx7D;
    }

    public String getUser24HPercent() {
        return user24HPercent;
    }

    public void setUser24HPercent(String user24HPercent) {
        this.user24HPercent = user24HPercent;
    }

    public String getVolume24HPercent() {
        return volume24HPercent;
    }

    public void setVolume24HPercent(String volume24HPercent) {
        this.volume24HPercent = volume24HPercent;
    }

    public String getTx24HPercent() {
        return tx24HPercent;
    }

    public void setTx24HPercent(String tx24HPercent) {
        this.tx24HPercent = tx24HPercent;
    }

    public String getVolume7DPercent() {
        return volume7DPercent;
    }

    public void setVolume7DPercent(String volume7DPercent) {
        this.volume7DPercent = volume7DPercent;
    }

    public String getTx7DPercent() {
        return tx7DPercent;
    }

    public void setTx7DPercent(String tx7DPercent) {
        this.tx7DPercent = tx7DPercent;
    }

    public String getAssets() {
        return assets;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }

    public List<SocialMediaEntity> getSocialList() {
        return socialList;
    }

    public void setSocialList(List<SocialMediaEntity> socialList) {
        this.socialList = socialList;
    }

    public List<DappDiagramDataEntity> getDiagramData24H() {
        return diagramData24H;
    }

    public void setDiagramData24H(List<DappDiagramDataEntity> diagramData24H) {
        this.diagramData24H = diagramData24H;
    }

    public List<DappDiagramDataEntity> getDiagramData7D() {
        return diagramData7D;
    }

    public void setDiagramData7D(List<DappDiagramDataEntity> diagramData7D) {
        this.diagramData7D = diagramData7D;
    }

    public List<String> getScreenshotList() {
        return screenshotList;
    }

    public void setScreenshotList(List<String> screenshotList) {
        this.screenshotList = screenshotList;
    }

    public List<SmartContractEntity> getSmartContractList() {
        return smartContractList;
    }

    public void setSmartContractList(List<SmartContractEntity> smartContractList) {
        this.smartContractList = smartContractList;
    }

    public List<RelevantArticleEntity> getRelevantArticleList() {
        return relevantArticleList;
    }

    public void setRelevantArticleList(List<RelevantArticleEntity> relevantArticleList) {
        this.relevantArticleList = relevantArticleList;
    }

    public List<DappEntity> getRecommendDappList() {
        return recommendDappList;
    }

    public void setRecommendDappList(List<DappEntity> recommendDappList) {
        this.recommendDappList = recommendDappList;
    }

    public String getIpfs_hash() {
        return ipfs_hash;
    }

    public void setIpfs_hash(String ipfs_hash) {
        this.ipfs_hash = ipfs_hash;
    }

    @Override
    public String toString() {
        return "DappDetailEntity{" +
                "mid='" + mid + '\'' +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", nameEN='" + nameEN + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", summary='" + summary + '\'' +
                ", summaryEN='" + summaryEN + '\'' +
                ", platformName='" + platformName + '\'' +
                ", platformNameEN='" + platformNameEN + '\'' +
                ", typeName='" + typeName + '\'' +
                ", typeNameEN='" + typeNameEN + '\'' +
                ", typeColor='" + typeColor + '\'' +
                ", followNumber=" + followNumber +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", descriptionEN='" + descriptionEN + '\'' +
                ", isFollow=" + isFollow +
                ", user24H=" + user24H +
                ", volume24H='" + volume24H + '\'' +
                ", tx24H=" + tx24H +
                ", volume7D='" + volume7D + '\'' +
                ", tx7D=" + tx7D +
                ", user24HPercent='" + user24HPercent + '\'' +
                ", volume24HPercent='" + volume24HPercent + '\'' +
                ", tx24HPercent='" + tx24HPercent + '\'' +
                ", volume7DPercent='" + volume7DPercent + '\'' +
                ", tx7DPercent='" + tx7DPercent + '\'' +
                ", assets='" + assets + '\'' +
                ", socialList=" + socialList +
                ", diagramData24H=" + diagramData24H +
                ", diagramData7D=" + diagramData7D +
                ", screenshotList=" + screenshotList +
                ", smartContractList=" + smartContractList +
                ", relevantArticleList=" + relevantArticleList +
                ", recommendDappList=" + recommendDappList +
                '}';
    }
}
