package com.tianji.blockchain.restful.bean;

import com.tianji.blockchain.entity.DappEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DappBean {
    private int id;
    private String name;
    private int add_time;
    private String name_en;
    private String subtitle;
    private String subtitle_en;
    private String icon_url;
    private String www;
    private String img;
    private int chain_type;
    private int type_id;
    private String type_name;
    private String type_name_en;
    private String type_color;
    private String type_color_1;
    private int type_id_1;
    private String type_name_1;
    private String type_name_en_1;
    private String balance;
    private int daily_life;
    private int tran24h;
    private String turn24h;
    private int tran7d;
    private String turn7d;
    private String daily_life_inc;
    private String tran24h_inc;
    private String turn24h_inc;
    private String tran7d_inc;
    private String turn7d_inc;
    private String ranking;
    private String coin_name;
    private String chain_name;
    private String chain_name_en;

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

    public int getAdd_time() {
        return add_time;
    }

    public void setAdd_time(int add_time) {
        this.add_time = add_time;
    }

    public String getName_en() {
        return name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
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

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getWww() {
        return www;
    }

    public void setWww(String www) {
        this.www = www;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getChain_type() {
        return chain_type;
    }

    public void setChain_type(int chain_type) {
        this.chain_type = chain_type;
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

    public String getType_color() {
        return type_color;
    }

    public void setType_color(String type_color) {
        this.type_color = type_color;
    }

    public String getType_color_1() {
        return type_color_1;
    }

    public void setType_color_1(String type_color_1) {
        this.type_color_1 = type_color_1;
    }

    public int getType_id_1() {
        return type_id_1;
    }

    public void setType_id_1(int type_id_1) {
        this.type_id_1 = type_id_1;
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

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public int getDaily_life() {
        return daily_life;
    }

    public void setDaily_life(int daily_life) {
        this.daily_life = daily_life;
    }

    public int getTran24h() {
        return tran24h;
    }

    public void setTran24h(int tran24h) {
        this.tran24h = tran24h;
    }

    public String getTurn24h() {
        return turn24h;
    }

    public void setTurn24h(String turn24h) {
        this.turn24h = turn24h;
    }

    public int getTran7d() {
        return tran7d;
    }

    public void setTran7d(int tran7d) {
        this.tran7d = tran7d;
    }

    public String getTurn7d() {
        return turn7d;
    }

    public void setTurn7d(String turn7d) {
        this.turn7d = turn7d;
    }

    public String getDaily_life_inc() {
        return daily_life_inc;
    }

    public void setDaily_life_inc(String daily_life_inc) {
        this.daily_life_inc = daily_life_inc;
    }

    public String getTran24h_inc() {
        return tran24h_inc;
    }

    public void setTran24h_inc(String tran24h_inc) {
        this.tran24h_inc = tran24h_inc;
    }

    public String getTurn24h_inc() {
        return turn24h_inc;
    }

    public void setTurn24h_inc(String turn24h_inc) {
        this.turn24h_inc = turn24h_inc;
    }

    public String getTran7d_inc() {
        return tran7d_inc;
    }

    public void setTran7d_inc(String tran7d_inc) {
        this.tran7d_inc = tran7d_inc;
    }

    public String getTurn7d_inc() {
        return turn7d_inc;
    }

    public void setTurn7d_inc(String turn7d_inc) {
        this.turn7d_inc = turn7d_inc;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getCoin_name() {
        return coin_name;
    }

    public void setCoin_name(String coin_name) {
        this.coin_name = coin_name;
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

    public DappEntity transfer() {
        DappEntity result = new DappEntity();
        result.setCreateTime(new SimpleDateFormat("yyyy-MM-dd").format(new Date(this.getAdd_time() * 1000)));
        result.setName(this.getName());
        result.setNameEn(this.getName_en());
        result.setIcon(this.getIcon_url());
        result.setUser24Hours(this.getDaily_life());
        result.setTypeName(this.getType_name_1());
        result.setTypeNameEn(this.getType_name_en_1());
        result.setTypeColor(this.getType_color_1());
        result.setPlatformName(this.getChain_name());
        result.setPlatformNameEn(this.getChain_name_en());
        result.setSummary(this.getSubtitle());
        result.setSummaryEn(this.getSubtitle_en());
        result.setMid("" + this.getId());
        return result;
    }
}
