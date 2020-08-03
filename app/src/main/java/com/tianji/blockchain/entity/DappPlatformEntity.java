package com.tianji.blockchain.entity;

import java.io.Serializable;

public class DappPlatformEntity implements Serializable {
    private String mid;
    private String icon;
    private String selectedIcon;
    private String name;
    private String nameEN;
    private boolean selected;

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

    public String getSelectedIcon() {
        return selectedIcon;
    }

    public void setSelectedIcon(String selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEN() {
        return nameEN;
    }

    public void setNameEN(String nameEN) {
        this.nameEN = nameEN;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "DappPlatformEntity{" +
                "mid='" + mid + '\'' +
                ", icon='" + icon + '\'' +
                ", selectedIcon='" + selectedIcon + '\'' +
                ", name='" + name + '\'' +
                ", nameEN='" + nameEN + '\'' +
                ", selected=" + selected +
                '}';
    }
}
