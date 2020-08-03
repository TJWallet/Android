package com.tianji.blockchain.restful.bean;

import com.tianji.blockchain.entity.DappDiagramDataEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DappDiagramBean {
    private int date;
    private int active_user;
    private String time;
    private int transactions;
    private int turnover;
    private int add_time;

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getActive_user() {
        return active_user;
    }

    public void setActive_user(int active_user) {
        this.active_user = active_user;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTransactions() {
        return transactions;
    }

    public void setTransactions(int transactions) {
        this.transactions = transactions;
    }

    public int getTurnover() {
        return turnover;
    }

    public void setTurnover(int turnover) {
        this.turnover = turnover;
    }

    public int getAdd_time() {
        return add_time;
    }

    public void setAdd_time(int add_time) {
        this.add_time = add_time;
    }

    public DappDiagramDataEntity transfer() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        DappDiagramDataEntity result = new DappDiagramDataEntity();
        result.setUser(this.getActive_user());
        result.setVolume(this.getTurnover());
        result.setTx(this.getTransactions());
        result.setCreateTime(formatter.format(new Date(this.getAdd_time() * 1000)));
        result.setDatetime(formatter.format(new Date(this.getDate() * 1000)));
        return result;
    }
}
