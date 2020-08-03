package com.tianji.blockchain.entity;

import java.io.Serializable;

/** author:jason **/
public class SmartContractEntity implements Serializable {
    private String mid;
    private String display;
    private String real;
    private String link;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getReal() {
        return real;
    }

    public void setReal(String real) {
        this.real = real;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "SmartContractEntity{" +
                "mid='" + mid + '\'' +
                ", display='" + display + '\'' +
                ", real='" + real + '\'' +
                ", link='" + link + '\'' +
                '}';
    }
}
