package com.tianji.blockchain.restful.bean;

import com.tianji.blockchain.entity.DappEntity;

public class DappRecommendBean {
    private int id;
    private String icon_url;
    private String name;
    private String name_en;
    private String author;
    private int chain_type;
    private String subtitle;
    private String subtitle_en;
    private int type_id;
    private String type_name;
    private String type_name_en;
    private String chain_type_name;
    private String chain_type_name_en;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getChain_type() {
        return chain_type;
    }

    public void setChain_type(int chain_type) {
        this.chain_type = chain_type;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getSubtitle_en() {
        return subtitle_en;
    }

    public void setSubtitle_en(String subtitle_en) {
        this.subtitle_en = subtitle_en;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getType_name_en() {
        return type_name_en;
    }

    public void setType_name_en(String type_name_en) {
        this.type_name_en = type_name_en;
    }

    public String getChain_type_name() {
        return chain_type_name;
    }

    public void setChain_type_name(String chain_type_name) {
        this.chain_type_name = chain_type_name;
    }

    public String getChain_type_name_en() {
        return chain_type_name_en;
    }

    public void setChain_type_name_en(String chain_type_name_en) {
        this.chain_type_name_en = chain_type_name_en;
    }

    public DappEntity transfer() {
        DappEntity result = new DappEntity();
        result.setMid(this.getId() + "");
        result.setSummary(this.getSubtitle());
        result.setSummaryEn(this.getSubtitle_en());
        result.setTypeName(this.getType_name());
        result.setTypeNameEn(this.getType_name_en());
        result.setName(this.getName());
        result.setNameEn(this.getName_en());
        result.setIcon(this.getIcon_url());
        return result;
    }
}
