package com.tianji.blockchain.restful.bean;

import com.tianji.blockchain.entity.BannerEntity;

public class BannerBean {
    private int id;
    private String title;
    private String title_en;
    private String sketch;
    private String sketch_en;
    private String img;
    private String url;
    private int game_id;
    private int is_activity;
    private int activity_id;
    private String bg;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getSketch() {
        return sketch;
    }

    public void setSketch(String sketch) {
        this.sketch = sketch;
    }

    public String getSketch_en() {
        return sketch_en;
    }

    public void setSketch_en(String sketch_en) {
        this.sketch_en = sketch_en;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public int getIs_activity() {
        return is_activity;
    }

    public void setIs_activity(int is_activity) {
        this.is_activity = is_activity;
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public String getBg() {
        return bg;
    }

    public void setBg(String bg) {
        this.bg = bg;
    }

    public BannerEntity transfer() {
        BannerEntity result = new BannerEntity();
        result.setMid("" + this.getId());
        result.setTitle(this.getTitle());
        result.setTitleEN(this.getTitle_en());
        result.setLink(this.getUrl());
        result.setBackground(this.getBg());
        result.setThumbnail(this.getImg());
        result.setRefID(this.getGame_id() + "");
        return result;
    }
}
