package com.tianji.blockchain.entity;

/***IPFS地址对象***/
public class IpfsItemEntity {
    private int id;
    private String hash;
    private String hash_en;
    private long add_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHash_en() {
        return hash_en;
    }

    public void setHash_en(String hash_en) {
        this.hash_en = hash_en;
    }

    public long getAdd_time() {
        return add_time;
    }

    public void setAdd_time(long add_time) {
        this.add_time = add_time;
    }
}
