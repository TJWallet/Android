package com.tianji.blockchain.entity;

import java.io.Serializable;

public class RatingStatisticsEntity implements Serializable {
    private String userTotal;
    private String ratingScore;

    public String getUserTotal() {
        return userTotal;
    }

    public void setUserTotal(String userTotal) {
        this.userTotal = userTotal;
    }

    public String getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(String ratingScore) {
        this.ratingScore = ratingScore;
    }

    @Override
    public String toString() {
        return "RatingStatisticsEntity{" +
                "userTotal='" + userTotal + '\'' +
                ", ratingScore='" + ratingScore + '\'' +
                '}';
    }
}
