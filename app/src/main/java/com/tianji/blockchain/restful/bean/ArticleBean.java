package com.tianji.blockchain.restful.bean;

import com.tianji.blockchain.entity.RelevantArticleEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

// DAPP新闻
public class ArticleBean {
    private int id;
    private int user_id;
    private String title;
    private int type;
    private String sub_title;
    private String img;
    private String url;
    private int is_top;
    private long add_time;
    private int game_id;
    private int author_id;
    private int is_video;
    private String game_id_str;
    private String author_name;
    private String author_img;
    private String content;
    private int isGames;
    private int traffics;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
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

    public int getIs_top() {
        return is_top;
    }

    public void setIs_top(int is_top) {
        this.is_top = is_top;
    }

    public long getAdd_time() {
        return add_time;
    }

    public void setAdd_time(long add_time) {
        this.add_time = add_time;
    }

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public int getIs_video() {
        return is_video;
    }

    public void setIs_video(int is_video) {
        this.is_video = is_video;
    }

    public String getGame_id_str() {
        return game_id_str;
    }

    public void setGame_id_str(String game_id_str) {
        this.game_id_str = game_id_str;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_img() {
        return author_img;
    }

    public void setAuthor_img(String author_img) {
        this.author_img = author_img;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIsGames() {
        return isGames;
    }

    public void setIsGames(int isGames) {
        this.isGames = isGames;
    }

    public int getTraffics() {
        return traffics;
    }

    public void setTraffics(int traffics) {
        this.traffics = traffics;
    }

    public RelevantArticleEntity transfer() {
        RelevantArticleEntity result = new RelevantArticleEntity();
        result.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(this.getAdd_time() * 1000)));
        result.setMid(this.getId() + "");
        result.setThumbnail(this.getImg());
        result.setTitle(this.getTitle());
        return result;
    }
}
