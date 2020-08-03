package com.tianji.blockchain.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class ImageLoaderHelper {
    // 默认图片配置（loading状态图片、加载失败状态图片、图片路径错误状态图片）
    public static class ImageConfig {
        private int imageForEmptyUri;
        private int imageOnFail;
        private int imageOnLoading;

        public ImageConfig() {}

        public ImageConfig(int imageOnLoading) {
            this.imageOnLoading = imageOnLoading;
        }

        public ImageConfig(int imageOnLoading, int imageOnFail) {
            this.imageOnLoading = imageOnLoading;
            this.imageOnFail = imageOnFail;
        }

        public ImageConfig(int imageOnLoading, int imageOnFail, int imageForEmptyUri) {
            this.imageOnLoading = imageOnLoading;
            this.imageOnFail = imageOnFail;
            this.imageForEmptyUri = imageForEmptyUri;
        }

        public int getImageForEmptyUri() {
            return imageForEmptyUri;
        }

        public void setImageForEmptyUri(int imageForEmptyUri) {
            this.imageForEmptyUri = imageForEmptyUri;
        }

        public int getImageOnFail() {
            return imageOnFail;
        }

        public void setImageOnFail(int imageOnFail) {
            this.imageOnFail = imageOnFail;
        }

        public int getImageOnLoading() {
            return imageOnLoading;
        }

        public void setImageOnLoading(int imageOnLoading) {
            this.imageOnLoading = imageOnLoading;
        }
    }

    // 首次加载图片加过渡动画
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener{
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
        private static final boolean isOnlyFirst = false;

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (isOnlyFirst && firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                } else {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                }
            }
        }
    }

    private ImageConfig config;
    private static ImageLoaderHelper instance;
    public static ImageLoaderHelper getInstance() {
        if (instance == null) {
            synchronized (ImageLoaderHelper.class) {
                if (instance == null) {
                    instance = new ImageLoaderHelper();
                }
            }
        }
        return instance;
    }

    /***
     * 加载图片时，底部的两个角为直角，带图片高度和宽度
     *
     * @param context
     * @param imageView
     * @param url
     * @param radius
     */
    public void loadImageWithBottom90(Context context, ImageView imageView, String url, int radius, int height, int width) {
        loadImageWithBottom90(context, imageView, url, radius, 0, height, width);
    }

    /***
     * 加载图片时，底部的两个角为直角，带边宽，带图片高度和宽度
     * @param context
     * @param borderWidth
     * @param imageView
     * @param url
     * @param radius
     */
    public void loadImageWithBottom90(Context context, ImageView imageView, String url, int radius, int borderWidth, int height, int width) {
        if (context == null) {
            return;
        }
        loadImage(
                imageView,
                url,
                radius,
                borderWidth,
                context.getResources().getColor(android.R.color.background_dark),
                height,
                width,
                false,
                false,
                true,
                true);
    }

    /***
     * 加载图片时，底部的两个角为直角，带边宽
     * @param context
     * @param borderWidth
     * @param imageView
     * @param url
     * @param radius
     */
    public void loadImageWithBottom90(Context context, ImageView imageView, String url, int radius, int borderWidth) {
        if (context == null) {
            return;
        }
        loadImage(
                imageView,
                url,
                radius,
                borderWidth,
                context.getResources().getColor(android.R.color.background_dark),
                false,
                false,
                true,
                true);
    }

    /***
     * 加载图片时，顶部的两个角为直角
     *
     * @param context
     * @param imageView
     * @param url
     * @param radius
     */
    public void loadImageWithTop90(Context context, ImageView imageView, String url, int radius) {
        loadImageWithTop90(context, 0, imageView, url, radius);
    }

    /***
     * 加载图片时，顶部的两个角为直角
     * @param context
     * @param borderWidth
     * @param imageView
     * @param url
     * @param radius
     */
    public void loadImageWithTop90(Context context, int borderWidth, ImageView imageView, String url, int radius) {
        if (context == null) {
            return;
        }
        loadImage(
                imageView,
                url,
                radius,
                borderWidth,
                context.getResources().getColor(android.R.color.background_dark),
                true,
                true,
                false,
                false);
    }

    /***
     * 四个圆角图片加载
     * @param context
     * @param imageView
     * @param url
     * @param radius
     */
    public void loadImageWithCorner(Context context, ImageView imageView, String url, int radius) {
        loadImageWithCorner(context, imageView, url, radius, 0);
    }

    /***
     * 四个圆角图片加载
     * @param context
     * @param imageView
     * @param url
     * @param radius
     * @param borderWidth
     */
    public void loadImageWithCorner(Context context, ImageView imageView, String url, int radius, int borderWidth) {
        if (context == null) {
            return;
        }
        loadImage(
                imageView,
                url,
                radius,
                borderWidth,
                context.getResources().getColor(android.R.color.background_dark),
                false,
                false,
                false,
                false);
    }

    /***
     * 四个圆角图片加载
     * @param context
     * @param imageView
     * @param url
     * @param radius
     * @param borderWidth
     */
    public void loadImageWithCorner(Context context, ImageView imageView, String url, int radius, int borderWidth, int height, int width) {
        if (context == null) {
            return;
        }
        loadImage(
                imageView,
                url,
                radius,
                borderWidth,
                context.getResources().getColor(android.R.color.background_dark),
                height,
                width,
                false,
                false,
                false,
                false);
    }

    /***
     * 四个直角图片加载
     * @param context
     * @param imageView
     * @param url
     */
    public void loadImageWithRightAngle(Context context, ImageView imageView, String url) {
        loadImageWithRightAngle(context, imageView, url, 0);
    }

    /***
     * 四个直角图片加载
     * @param context
     * @param borderWidth
     * @param imageView
     * @param url
     */
    public void loadImageWithRightAngle(Context context, ImageView imageView, String url, int borderWidth) {
        loadImageWithRightAngle(
                context,
                imageView,
                url,
                borderWidth,
                0,
                0);
    }

    /***
     * 四个直角图片加载
     * @param context
     * @param imageView
     * @param url
     */
    public void loadImageWithRightAngle(Context context, ImageView imageView, String url, int height, int width) {
        loadImageWithRightAngle(context, imageView, url, 0, height, width);
    }

    /***
     * 四个直角图片加载
     * @param context
     * @param borderWidth
     * @param imageView
     * @param url
     */
    public void loadImageWithRightAngle(Context context, ImageView imageView, String url, int borderWidth, int height, int width) {
        if (context == null) {
            return;
        }
        loadImage(
                imageView,
                url,
                0,
                borderWidth,
                context.getResources().getColor(android.R.color.background_dark),
                height,
                width,
                true,
                true,
                true,
                true);
    }

    public void loadImage(Context context, ImageView imageView, String url) {
        loadImageWithCorner(context, imageView, url, 0);
    }

    /***
     * 加载图片
     * @param imageView
     * @param url
     * @param radius 圆角角度
     * @param borderWidth 边宽
     * @param isLeftTopRightAngle 左上角是否直角
     * @param isRightTopRightAngle 右上角是否直角
     * @param isLeftBottomRightAngle 左下角是否直角
     * @param isRightBottomRightAngle 右下角是否直角
     */
    public void loadImage(ImageView imageView,
                          final String url,
                          final int radius,
                          final int borderWidth,
                          final int borderColor,
                          final boolean isLeftTopRightAngle,
                          final boolean isRightTopRightAngle,
                          final boolean isLeftBottomRightAngle,
                          final boolean isRightBottomRightAngle) {

        loadImage(imageView, url, radius, borderWidth, borderColor, 0, 0, isLeftTopRightAngle, isRightTopRightAngle, isLeftBottomRightAngle, isRightBottomRightAngle);
    }

    /***
     * 加载图片
     * @param imageView
     * @param url
     * @param radius 圆角角度
     * @param borderWidth 边宽
     * @param isLeftTopRightAngle 左上角是否直角
     * @param isRightTopRightAngle 右上角是否直角
     * @param isLeftBottomRightAngle 左下角是否直角
     * @param isRightBottomRightAngle 右下角是否直角
     */
    public void loadImage(ImageView imageView,
                          String url,
                          int radius,
                          int borderWidth,
                          int borderColor,
                          int height,
                          int width,
                          boolean isLeftTopRightAngle,
                          boolean isRightTopRightAngle,
                          boolean isLeftBottomRightAngle,
                          boolean isRightBottomRightAngle) {

        try {
            //圆角图片
            DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder()
                    .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                    .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                    .cacheInMemory(true)
                    .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                    .bitmapConfig(Bitmap.Config.RGB_565)   //设置图片的解码类型
                    .displayer(new RoundedCenterBitmapDisplayer(
                            radius,
                            borderWidth,
                            borderColor,
                            height,
                            width,
                            isLeftTopRightAngle,
                            isRightTopRightAngle,
                            isLeftBottomRightAngle,
                            isRightBottomRightAngle));

            if (instance.config != null) {
                int failImageResource = instance.config.getImageOnFail();
                int loadingImageResource = instance.config.getImageOnLoading();
                int emptyImageResource = instance.config.getImageForEmptyUri();
                if (failImageResource != 0) {
                    builder.showImageOnFail(failImageResource);
                }
                if (loadingImageResource != 0) {
                    builder.showImageOnLoading(loadingImageResource);
                }
                if (emptyImageResource != 0) {
                    builder.showImageForEmptyUri(emptyImageResource);
                }
                instance.config = null;
            }

            DisplayImageOptions options = builder.build();
            ImageLoader.getInstance().displayImage(url, imageView, options, new AnimateFirstDisplayListener());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 加载圆形图片
     * @param imageView
     * @param url
     */
    public void loadImageWithCircle(final ImageView imageView, final String url) {
        try {
            final ViewTreeObserver observer = imageView.getViewTreeObserver();
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int width = imageView.getMeasuredWidth();
                    int height = imageView.getMeasuredHeight();

                    if (imageView.getHeight() > 0) {
                        // 移除监听
                        imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        //圆角图片
                        DisplayImageOptions options = new DisplayImageOptions.Builder()
                                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                                .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                                .cacheInMemory(true)
                                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                                .bitmapConfig(Bitmap.Config.ARGB_4444)   //设置图片的解码类型
                                .displayer(new CircleBitmapDisplayer())
                                .build();//构建完成

                        ImageLoader.getInstance().displayImage(url, imageView, options, new AnimateFirstDisplayListener());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 单次拼接参数
     * @param config
     * @return
     */
    public ImageLoaderHelper once(ImageConfig config) {
        instance.config = config;
        return instance;
    }

}
