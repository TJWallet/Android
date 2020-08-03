package com.tianji.blockchain.restful.bean;

import com.tianji.blockchain.entity.ArticleItemEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

// 金色财经文章
public class ArticleItemBean {
    private int id;
    private String content;
    private String content_prefix;
    private String link_name;
    private String link;
    private String ipfs_hash;
    private int grade;
    private long created_at;
    private int up_counts;
    private int down_counts;
    private int extra_type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent_prefix() {
        return content_prefix;
    }

    public void setContent_prefix(String content_prefix) {
        this.content_prefix = content_prefix;
    }

    public String getLink_name() {
        return link_name;
    }

    public String getIpfs_hash() {
        return ipfs_hash;
    }

    public void setIpfs_hash(String ipfs_hash) {
        this.ipfs_hash = ipfs_hash;
    }

    public void setLink_name(String link_name) {
        this.link_name = link_name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public int getUp_counts() {
        return up_counts;
    }

    public void setUp_counts(int up_counts) {
        this.up_counts = up_counts;
    }

    public int getDown_counts() {
        return down_counts;
    }

    public void setDown_counts(int down_counts) {
        this.down_counts = down_counts;
    }

    public int getExtra_type() {
        return extra_type;
    }

    public void setExtra_type(int extra_type) {
        this.extra_type = extra_type;
    }

    public ArticleItemEntity transfer() {
        ArticleItemEntity result = new ArticleItemEntity();
        result.setMid(this.getId() + "");
        if (this.content != null && this.content.length() > 0 && this.content.indexOf("【") != -1 && this.content.indexOf("】") != -1) {
            result.setTitle(this.content.substring(this.content.indexOf("【") + 1, this.content.indexOf("】")));
        }
        if (this.content != null && this.content.length() > 0 && this.content.indexOf("】") != -1) {
            result.setContent(this.content.substring(this.content.indexOf("】") + 1));
        } else {
            result.setContent(this.content);
        }
        long now = new Date().getTime();
        long createTime = new Date(this.created_at * 1000).getTime();

        if (now - createTime >= 1000 * 60 * 60) {
            result.setDatetime(new SimpleDateFormat("HH:mm:ss").format(new Date(this.created_at * 1000)));
        } else {
            long timeDiff = now - createTime;
            int minutes = (int) (timeDiff / (1000 * 60));
            result.setDatetime(minutes + "分钟前");
        }
        result.setSourceLink(this.getLink());
        result.setSourceLinkTitle(this.getLink_name());
        result.setIpfs_hash(this.getIpfs_hash());
        return result;
    }
}
