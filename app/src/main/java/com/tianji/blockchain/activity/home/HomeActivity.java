package com.tianji.blockchain.activity.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.basic.BasicDataActivity;
import com.tianji.blockchain.activity.createwallet.ServiceAgreementActivity;
import com.tianji.blockchain.utils.CommonUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BasicDataActivity implements View.OnClickListener {
    private RelativeLayout rl_create_wallet;
    private Banner banner;
    private TextView tv_version;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void setData() {

    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void initReceiver() {

    }

    @Override
    protected void initView() {
        rl_create_wallet = findViewById(R.id.rl_create_wallet);
        banner = findViewById(R.id.banner);
        tv_version = findViewById(R.id.tv_version);

        tv_version.setText("V" + CommonUtils.getAppVersionName(this));

        List<Integer> bannerImgList = new ArrayList<>();
        if (WalletApplication.lang.equals("")) {
            bannerImgList.add(R.drawable.home_banner_1);
            bannerImgList.add(R.drawable.home_banner_2);
            bannerImgList.add(R.drawable.home_banner_3);
            bannerImgList.add(R.drawable.home_banner_4);
        } else {
            bannerImgList.add(R.drawable.home_banner_1_en);
            bannerImgList.add(R.drawable.home_banner_2_en);
            bannerImgList.add(R.drawable.home_banner_3_en);
            bannerImgList.add(R.drawable.home_banner_4_en);
        }


        bannerViewInitWithData(banner, bannerImgList);

        rl_create_wallet.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_create_wallet:
                Intent createIntent = new Intent(this, ServiceAgreementActivity.class);
                createIntent.putExtra("_source", Constant.CreateWalletSource.SOURCE_HOME_ACTIVITY);
                startActivity(createIntent);
                break;
        }
    }

    /***
     * 带数据广告图
     * @param banner
     * @param list
     */
    private void bannerViewInitWithData(Banner banner, final List<Integer> list) {
        MyImageLoader mMyImageLoader = new MyImageLoader();
        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器，图片加载器在下方
//        banner.setImageLoader(new GlideImageLoader((int) getResources().getDimension(R.dimen.tab_home_top_banner_height), DisplayUtils.getScreenWidth(DTExchangeActivity.this)));
        banner.setImageLoader(mMyImageLoader);
        //设置图片网址或地址的集合
        banner.setImages(list);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
//        //设置轮播图的标题集合
//        banner.setBannerTitles(titles);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.start();
    }

    /**
     * 重写banner图片加载
     */
    private class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }

        /**
         * 自定义圆角类
         *
         * @param context
         * @return
         */
        @Override
        public ImageView createImageView(Context context) {
            ImageView img = new ImageView(context);
            return img;
        }
    }
}
