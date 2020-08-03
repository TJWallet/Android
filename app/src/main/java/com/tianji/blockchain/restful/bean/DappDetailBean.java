package com.tianji.blockchain.restful.bean;

import com.tianji.blockchain.R;
import com.tianji.blockchain.entity.DappDetailEntity;
import com.tianji.blockchain.entity.DappDiagramDataEntity;
import com.tianji.blockchain.entity.DappEntity;
import com.tianji.blockchain.entity.SmartContractEntity;
import com.tianji.blockchain.entity.SocialMediaEntity;

import java.util.ArrayList;
import java.util.List;

public class DappDetailBean {
    private int id;
    private int type_id;
    private String name;
    private String name_en;
    private int country_id;
    private String subtitle;
    private String subtitle_en;
    private int chain_type;
    private String sketch;
    private String sketch_en;
    private String icon_url;
    private String img;
    private String intro;
    private String intro_en;
    private String share_url;
    private String www;
    private List<String> contract;
    private int block;
    private String twitter;
    private String telegraph;
    private String reddit;
    private String media;
    private String discord;
    private String facebook;
    private String instagram;
    private String github;
    private String youtobe;
    private String wechat;
    private String download;
    private int game_status;
    private String author;
    private String balance;
    private int daily_life;
    private int tran24h;
    private String turn24h;
    private int tran7d;
    private String turn7d;
    private String contact;
    private int is_show;
    private int language;
    private int add_time;
    private int content_update_time;
    private String type_name;
    private String type_name_en;
    private String type_color;
    private String type_color_1;
    private int type_id_1;
    private String type_name_1;
    private String type_name_en_1;
    private String daily_life_inc;
    private String tran24h_inc;
    private String turn24h_inc;
    private String tran7d_inc;
    private String turn7d_inc;
    private List<DappScreenshotBean> screenshots;
    private int is_like;
    private int zan;
    private int cai;
    private List<DappRecommendBean> recommon;
    private List<DappDiagramBean> gt24hs;
    private List<DappDiagramBean02> gt7ds;
    private List<DappDiagramBean02> gt30ds;
    private List<String> contract_enc;
    private String country_name;
    private String country_name_en;
    private String chain_type_name;
    private String chain_type_name_en;
    private String chain_url;
    private String coin_name;
    private String chain_logo;
    private int chain_is_has_data;
    private int isTaskComplete;
    private String active;
    private List<DappCustomLabelBean> customLabel;
    private String ipfs_hash;
    private String ipfs_hash_en;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public String getName() {
        return name;
    }

    public String getIpfs_hash_en() {
        return ipfs_hash_en;
    }

    public void setIpfs_hash_en(String ipfs_hash_en) {
        this.ipfs_hash_en = ipfs_hash_en;
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

    public int getCountry_id() {
        return country_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
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

    public int getChain_type() {
        return chain_type;
    }

    public void setChain_type(int chain_type) {
        this.chain_type = chain_type;
    }

    public String getSketch() {
        return sketch;
    }

    public void setSketch(String sketch) {
        this.sketch = sketch;
    }

    public String getSketch_en() {
        return sketch_en;
    }

    public void setSketch_en(String sketch_en) {
        this.sketch_en = sketch_en;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getIpfs_hash() {
        return ipfs_hash;
    }

    public void setIpfs_hash(String ipfs_hash) {
        this.ipfs_hash = ipfs_hash;
    }

    public String getIntro_en() {
        return intro_en;
    }

    public void setIntro_en(String intro_en) {
        this.intro_en = intro_en;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getWww() {
        return www;
    }

    public void setWww(String www) {
        this.www = www;
    }

    public List<String> getContract() {
        return contract;
    }

    public void setContract(List<String> contract) {
        this.contract = contract;
    }

    public int getBlock() {
        return block;
    }

    public void setBlock(int block) {
        this.block = block;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public String getTelegraph() {
        return telegraph;
    }

    public void setTelegraph(String telegraph) {
        this.telegraph = telegraph;
    }

    public String getReddit() {
        return reddit;
    }

    public void setReddit(String reddit) {
        this.reddit = reddit;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getDiscord() {
        return discord;
    }

    public void setDiscord(String discord) {
        this.discord = discord;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getYoutobe() {
        return youtobe;
    }

    public void setYoutobe(String youtobe) {
        this.youtobe = youtobe;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getDownload() {
        return download;
    }

    public void setDownload(String download) {
        this.download = download;
    }

    public int getGame_status() {
        return game_status;
    }

    public void setGame_status(int game_status) {
        this.game_status = game_status;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getIs_show() {
        return is_show;
    }

    public void setIs_show(int is_show) {
        this.is_show = is_show;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public int getAdd_time() {
        return add_time;
    }

    public void setAdd_time(int add_time) {
        this.add_time = add_time;
    }

    public int getContent_update_time() {
        return content_update_time;
    }

    public void setContent_update_time(int content_update_time) {
        this.content_update_time = content_update_time;
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

    public List<DappScreenshotBean> getScreenshots() {
        return screenshots;
    }

    public void setScreenshots(List<DappScreenshotBean> screenshots) {
        this.screenshots = screenshots;
    }

    public int getIs_like() {
        return is_like;
    }

    public void setIs_like(int is_like) {
        this.is_like = is_like;
    }

    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public int getCai() {
        return cai;
    }

    public void setCai(int cai) {
        this.cai = cai;
    }

    public List<DappRecommendBean> getRecommon() {
        return recommon;
    }

    public void setRecommon(List<DappRecommendBean> recommon) {
        this.recommon = recommon;
    }

    public List<DappDiagramBean> getGt24hs() {
        return gt24hs;
    }

    public void setGt24hs(List<DappDiagramBean> gt24hs) {
        this.gt24hs = gt24hs;
    }

    public List<DappDiagramBean02> getGt7ds() {
        return gt7ds;
    }

    public void setGt7ds(List<DappDiagramBean02> gt7ds) {
        this.gt7ds = gt7ds;
    }

    public List<DappDiagramBean02> getGt30ds() {
        return gt30ds;
    }

    public void setGt30ds(List<DappDiagramBean02> gt30ds) {
        this.gt30ds = gt30ds;
    }

    public List<String> getContract_enc() {
        return contract_enc;
    }

    public void setContract_enc(List<String> contract_enc) {
        this.contract_enc = contract_enc;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getCountry_name_en() {
        return country_name_en;
    }

    public void setCountry_name_en(String country_name_en) {
        this.country_name_en = country_name_en;
    }

    public String getChain_type_name() {
        return chain_type_name;
    }

    public void setChain_type_name(String chain_type_name) {
        this.chain_type_name = chain_type_name;
    }

    public String getChain_type_name_en() {
        return chain_type_name_en;
    }

    public void setChain_type_name_en(String chain_type_name_en) {
        this.chain_type_name_en = chain_type_name_en;
    }

    public String getChain_url() {
        return chain_url;
    }

    public void setChain_url(String chain_url) {
        this.chain_url = chain_url;
    }

    public String getCoin_name() {
        return coin_name;
    }

    public void setCoin_name(String coin_name) {
        this.coin_name = coin_name;
    }

    public String getChain_logo() {
        return chain_logo;
    }

    public void setChain_logo(String chain_logo) {
        this.chain_logo = chain_logo;
    }

    public int getChain_is_has_data() {
        return chain_is_has_data;
    }

    public void setChain_is_has_data(int chain_is_has_data) {
        this.chain_is_has_data = chain_is_has_data;
    }

    public int getIsTaskComplete() {
        return isTaskComplete;
    }

    public void setIsTaskComplete(int isTaskComplete) {
        this.isTaskComplete = isTaskComplete;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public List<DappCustomLabelBean> getCustomLabel() {
        return customLabel;
    }

    public void setCustomLabel(List<DappCustomLabelBean> customLabel) {
        this.customLabel = customLabel;
    }

    public DappDetailEntity transfer() {
        DappDetailEntity result = new DappDetailEntity();
        result.setMid(this.getId() + "");
        result.setName(this.getName());
        result.setNameEN(this.getName_en());
        result.setThumbnail(this.getImg());
        result.setIcon(this.getIcon_url());
        result.setSummary(this.getSubtitle());
        result.setSummaryEN(this.getSubtitle_en());
        result.setPlatformName(this.getChain_type_name());
        result.setPlatformNameEN(this.getChain_type_name_en());
        result.setTypeColor(this.getType_color_1());
        result.setTypeName(this.getType_name_1());
        result.setTypeNameEN(this.getType_name_en_1());
        result.setFollowNumber(this.getZan());
        result.setIsFollow(this.getIs_like());
        result.setLink(this.getWww());
        result.setDescription(this.getSketch());
        result.setDescriptionEN(this.getSketch_en());
        result.setUser24H(this.getDaily_life());
        result.setVolume24H(this.getTurn24h());
        result.setTx24H(this.getTran24h());
        result.setVolume7D(this.getTurn7d());
        result.setTx7D(this.getTran7d());
        result.setAssets(this.getBalance());
        result.setUser24HPercent(this.getDaily_life_inc());
        result.setVolume24HPercent(this.getTurn24h_inc());
        result.setTx24HPercent(this.getTran24h_inc());
        result.setVolume7DPercent(this.getTurn7d_inc());
        result.setTx7DPercent(this.getTran7d_inc());
        result.setIpfs_hash(this.getIpfs_hash());
        result.setIpfs_hash_en(this.getIpfs_hash_en());
        List<SocialMediaEntity> socialList = new ArrayList<>();

        String facebook = this.getFacebook();
        String twitter = this.getTwitter();
        String github = this.getGithub();
        String reddit = this.getReddit();
        String medium = this.getMedia();
        String telegram = this.getTelegraph();
        String discord = this.getDiscord();
        String instagram = this.getInstagram();
        String youtube = this.getYoutobe();
        String wechat = this.getWechat();

        if (facebook != null && facebook.length() > 0) {
            SocialMediaEntity social01 = new SocialMediaEntity();
            social01.setIconResourceID(R.drawable.icon_facebook);
            social01.setLink(facebook);
            socialList.add(social01);
        }
        if (twitter != null && twitter.length() > 0) {
            SocialMediaEntity social02 = new SocialMediaEntity();
            social02.setIconResourceID(R.drawable.icon_twitter);
            social02.setLink(twitter);
            socialList.add(social02);
        }
        if (github != null && github.length() > 0) {
            SocialMediaEntity social03 = new SocialMediaEntity();
            social03.setIconResourceID(R.drawable.icon_github);
            social03.setLink(github);
            socialList.add(social03);
        }
        if (reddit != null && reddit.length() > 0) {
            SocialMediaEntity social04 = new SocialMediaEntity();
            social04.setIconResourceID(R.drawable.icon_reddit);
            social04.setLink(reddit);
            socialList.add(social04);
        }
        if (medium != null && medium.length() > 0) {
            SocialMediaEntity social05 = new SocialMediaEntity();
            social05.setIconResourceID(R.drawable.icon_medium);
            social05.setLink(medium);
            socialList.add(social05);
        }
        if (youtube != null && youtube.length() > 0) {
            SocialMediaEntity social06 = new SocialMediaEntity();
            social06.setIconResourceID(R.drawable.icon_youtube);
            social06.setLink(youtube);
            socialList.add(social06);
        }
        if (telegram != null && telegram.length() > 0) {
            SocialMediaEntity social07 = new SocialMediaEntity();
            social07.setIconResourceID(R.drawable.icon_telegram);
            social07.setLink(telegram);
            socialList.add(social07);
        }
        if (discord != null && discord.length() > 0) {
            SocialMediaEntity social08 = new SocialMediaEntity();
            social08.setIconResourceID(R.drawable.icon_discord);
            social08.setLink(discord);
            socialList.add(social08);
        }
        if (instagram != null && instagram.length() > 0) {
            SocialMediaEntity social09 = new SocialMediaEntity();
            social09.setIconResourceID(R.drawable.icon_instagram);
            social09.setLink(instagram);
            socialList.add(social09);
        }
        if (wechat != null && wechat.length() > 0) {
            SocialMediaEntity social10 = new SocialMediaEntity();
            social10.setIconResourceID(R.drawable.icon_wechat);
            social10.setLink(wechat);
            socialList.add(social10);
        }
        result.setSocialList(socialList);

        List<DappDiagramDataEntity> diagramData24H = new ArrayList<>();
        if (this.getGt24hs() != null && this.getGt24hs().size() > 0) {
            for (DappDiagramBean item : this.getGt24hs()) {
                diagramData24H.add(item.transfer());
            }
        }
        result.setDiagramData24H(diagramData24H);

        List<DappDiagramDataEntity> diagramData7D = new ArrayList<>();
        if (this.getGt7ds() != null && this.getGt7ds().size() > 0) {
            for (DappDiagramBean02 item : this.getGt7ds()) {
                diagramData7D.add(item.transfer());
            }
        }
        result.setDiagramData7D(diagramData7D);

        List<String> screenshotList = new ArrayList<>();
        if (this.getScreenshots() != null && this.getScreenshots().size() > 0) {
            for (DappScreenshotBean item : this.getScreenshots()) {
                screenshotList.add(item.getImg());
            }
        }
        result.setScreenshotList(screenshotList);

        List<SmartContractEntity> smartContractList = new ArrayList<>();
        if (this.getContract() != null && this.getContract().size() > 0) {
            for (int i = 0; i < this.getContract().size(); i++) {
                String display = this.getContract().get(i);
                String real = "";
                if (this.getContract_enc() != null && this.getContract_enc().size() > i) {
                    real = this.getContract_enc().get(i);
                }
                SmartContractEntity temp = new SmartContractEntity();
                temp.setDisplay(display);
                temp.setReal(real);
                temp.setLink(this.getChain_url() + real);
                smartContractList.add(temp);
            }
        }
        result.setSmartContractList(smartContractList);

        List<DappEntity> recommendDappList = new ArrayList<>();
        if (this.getRecommon() != null && this.getRecommon().size() > 0) {
            for (DappRecommendBean item : this.getRecommon()) {
                recommendDappList.add(item.transfer());
            }
        }
        result.setRecommendDappList(recommendDappList);
        return result;
    }
}
