package com.tianji.blockchain.restful.bean;

import com.tianji.blockchain.entity.ArticleByDateEntity;
import com.tianji.blockchain.entity.ArticleItemEntity;

import java.util.ArrayList;
import java.util.List;

public class ArticleByDateBean {
    private String date;
    private List<ArticleItemBean> lives;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ArticleItemBean> getLives() {
        return lives;
    }

    public void setLives(List<ArticleItemBean> lives) {
        this.lives = lives;
    }

    public ArticleByDateEntity transfer() {
        ArticleByDateEntity result = new ArticleByDateEntity();
        result.setDate(this.getDate());
        if (this.lives != null && this.lives.size() > 0) {
            List<ArticleItemEntity> temp = new ArrayList<>();

            for (ArticleItemBean item : this.lives) {
                temp.add(item.transfer());
            }
            result.setArticleList(temp);
        }
        return result;
    }
}
