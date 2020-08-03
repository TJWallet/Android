package com.tianji.blockchain.entity;

import java.io.Serializable;

/** author:jason **/
public class SocialMediaEntity implements Serializable {
    private String icon;
    private int iconResourceID;
    private String link;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getIconResourceID() {
        return iconResourceID;
    }

    public void setIconResourceID(int iconResourceID) {
        this.iconResourceID = iconResourceID;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "SocialMediaEntity{" +
                "icon='" + icon + '\'' +
                ", iconResourceID=" + iconResourceID +
                ", link='" + link + '\'' +
                '}';
    }
}
