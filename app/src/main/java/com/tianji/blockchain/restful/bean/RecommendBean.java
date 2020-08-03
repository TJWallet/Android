package com.tianji.blockchain.restful.bean;

import com.tianji.blockchain.entity.RecommendEntity;

public class RecommendBean {
    private String title;
    private String title_en;
    private String sub_title;
    private String sub_title_en;
    private String img;
    private int game_id;
    private String url;
    private int type_id_1;
    private String type_color_1;
    private String type_name_1;
    private String type_name_en_1;
    private int add_time;
    private int chain_type;
    private String chain_name;
    private String chain_name_en;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getSub_title_en() {
        return sub_title_en;
    }

    public void setSub_title_en(String sub_title_en) {
        this.sub_title_en = sub_title_en;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getAdd_time() {
        return add_time;
    }

    public void setAdd_time(int add_time) {
        this.add_time = add_time;
    }

    public int getType_id_1() {
        return type_id_1;
    }

    public void setType_id_1(int type_id_1) {
        this.type_id_1 = type_id_1;
    }

    public String getType_color_1() {
        return type_color_1;
    }

    public void setType_color_1(String type_color_1) {
        this.type_color_1 = type_color_1;
    }

    public String getType_name_1() {
        return type_name_1;
    }

    public void setType_name_1(String type_name_1) {
        this.type_name_1 = type_name_1;
    }

    public String getType_name_en_1() {
        return type_name_en_1;
    }

    public void setType_name_en_1(String type_name_en_1) {
        this.type_name_en_1 = type_name_en_1;
    }

    public int getChain_type() {
        return chain_type;
    }

    public void setChain_type(int chain_type) {
        this.chain_type = chain_type;
    }

    public String getChain_name() {
        return chain_name;
    }

    public void setChain_name(String chain_name) {
        this.chain_name = chain_name;
    }

    public String getChain_name_en() {
        return chain_name_en;
    }

    public void setChain_name_en(String chain_name_en) {
        this.chain_name_en = chain_name_en;
    }

    public RecommendEntity transfer() {
        RecommendEntity result = new RecommendEntity();
        result.setMid(this.getGame_id() > 0 ? this.getGame_id() + "" : null);
        result.setIcon(this.getImg());
        result.setLink(this.getUrl());
        result.setTitle(this.getTitle());
        result.setTitleEn(this.getTitle_en());
        result.setSubtitle(this.getSub_title());
        result.setSubtitleEn(this.getSub_title_en());
        result.setPlatformName(this.getChain_name());
        result.setPlatformNameEn(this.getChain_name_en());
        result.setTypeName(this.getType_name_1());
        result.setTypeNameEn(this.getType_name_en_1());
        result.setTypeColor(this.getType_color_1());
        return result;
    }
}
