package com.tianji.blockchain.restful.bean;

import com.tianji.blockchain.entity.DappPlatformEntity;

public class PlatformBean {
    private int id;
    private String name;
    private String name_en;
    private String url;
    private String coin_name;
    private int is_has_data;
    private int status;
    private int is_user_address;
    private String icon;
    private String icon_on;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCoin_name() {
        return coin_name;
    }

    public void setCoin_name(String coin_name) {
        this.coin_name = coin_name;
    }

    public int getIs_has_data() {
        return is_has_data;
    }

    public void setIs_has_data(int is_has_data) {
        this.is_has_data = is_has_data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIs_user_address() {
        return is_user_address;
    }

    public void setIs_user_address(int is_user_address) {
        this.is_user_address = is_user_address;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIcon_on() {
        return icon_on;
    }

    public void setIcon_on(String icon_on) {
        this.icon_on = icon_on;
    }

    public DappPlatformEntity transfer() {
        DappPlatformEntity result = new DappPlatformEntity();
        result.setMid(this.getId() + "");
        result.setIcon(this.getIcon());
        result.setSelectedIcon(this.getIcon_on());
        result.setName(this.getName());
        return result;
    }
}
