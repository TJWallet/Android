package com.tianji.blockchain.entity;

import java.io.Serializable;

/** author:jason **/
public class RecentEntity implements Serializable {
    private String icon;
    private String title;
    private String datetime;
    private String type;

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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "RecentEntity{" +
                "icon='" + icon + '\'' +
                ", title='" + title + '\'' +
                ", datetime='" + datetime + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
