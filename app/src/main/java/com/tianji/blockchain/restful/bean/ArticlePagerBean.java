package com.tianji.blockchain.restful.bean;

import com.tianji.blockchain.entity.ArticleByDateEntity;
import com.tianji.blockchain.entity.ArticlePagerEntity;

import java.util.ArrayList;
import java.util.List;

public class ArticlePagerBean {
    private int new_cnt;
    private int news;
    private int count;
    private int total;
    private int top_id;
    private int bottom_id;
    private List<ArticleByDateBean> list;

    public int getNew_cnt() {
        return new_cnt;
    }

    public void setNew_cnt(int new_cnt) {
        this.new_cnt = new_cnt;
    }

    public int getNews() {
        return news;
    }

    public void setNews(int news) {
        this.news = news;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTop_id() {
        return top_id;
    }

    public void setTop_id(int top_id) {
        this.top_id = top_id;
    }

    public int getBottom_id() {
        return bottom_id;
    }

    public void setBottom_id(int bottom_id) {
        this.bottom_id = bottom_id;
    }

    public List<ArticleByDateBean> getList() {
        return list;
    }

    public void setList(List<ArticleByDateBean> list) {
        this.list = list;
    }

    public ArticlePagerEntity transfer() {
        ArticlePagerEntity result = new ArticlePagerEntity();
        result.setUpdateCount(this.getNew_cnt());
        result.setCount(this.getCount());
        result.setFirstId(this.getTop_id() + "");
        result.setLastId(this.getBottom_id() + "");
        if (this.list != null && this.list.size() > 0) {
            List<ArticleByDateEntity> temp = new ArrayList<>();
            for (ArticleByDateBean item : this.list) {
                temp.add(item.transfer());
            }
            result.setData(temp);
        }
        return result;
    }
}
