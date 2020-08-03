package com.tianji.blockchain.restful.bean;

import com.tianji.blockchain.entity.DappEntity;
import com.tianji.blockchain.entity.DappTopicEntity;

import java.util.ArrayList;
import java.util.List;

public class TopicBean {
    private int id;
    private String intro;
    private String intro_en;
    private String logo;
    private String name;
    private String name_en;
    private List<TopicDappBean> games;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getIntro_en() {
        return intro_en;
    }

    public void setIntro_en(String intro_en) {
        this.intro_en = intro_en;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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

    public List<TopicDappBean> getGames() {
        return games;
    }

    public void setGames(List<TopicDappBean> games) {
        this.games = games;
    }

    public DappTopicEntity transfer() {
        DappTopicEntity result = new DappTopicEntity();
        List<DappEntity> items = new ArrayList<>();

        result.setMid("" + this.getId());
        result.setTopicName(this.getName());
        result.setTopicNameEN(this.getName_en());

        if (this.games != null && this.games.size() > 0) {
            for(TopicDappBean item : games) {
                items.add(item.transfer());
            }
        }
        result.setItems(items);

        return result;
    }
}
