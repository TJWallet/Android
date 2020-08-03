package com.tianji.blockchain.restful.bean;

import com.tianji.blockchain.entity.RatingStatisticsEntity;

public class DappRatingStatisticsBean {
    private int totle_user;
    private float aver_totle;
    private DappRatingScoreBean score;

    public int getTotle_user() {
        return totle_user;
    }

    public void setTotle_user(int totle_user) {
        this.totle_user = totle_user;
    }

    public float getAver_totle() {
        return aver_totle;
    }

    public void setAver_totle(float aver_totle) {
        this.aver_totle = aver_totle;
    }

    public DappRatingScoreBean getScore() {
        return score;
    }

    public void setScore(DappRatingScoreBean score) {
        this.score = score;
    }

    public RatingStatisticsEntity transfer() {
        RatingStatisticsEntity entity = new RatingStatisticsEntity();
        entity.setRatingScore(this.getAver_totle() + "");
        entity.setUserTotal(this.getTotle_user() + "");
        return entity;
    }
}
