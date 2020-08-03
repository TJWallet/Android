package com.tianji.blockchain.entity;


import java.io.Serializable;

/** author:jason **/
public class BannerEntity implements Serializable {
    private String mid;
    private String title;
    private String titleEN;
    private String thumbnail;
    private String link;
    private String refID;
    private String background;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public String getLink() {
        return link;
    }

    public String getTitleEN() {
        return titleEN;
    }

    public void setTitleEN(String titleEN) {
        this.titleEN = titleEN;
    }

    public String getRefID() {
        return refID;
    }

    public void setRefID(String refID) {
        this.refID = refID;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "BannerEntity{" +
                "mid='" + mid + '\'' +
                ", title='" + title + '\'' +
                ", titleEN='" + titleEN + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", link='" + link + '\'' +
                ", refID='" + refID + '\'' +
                ", background='" + background + '\'' +
                '}';
    }
}
