package com.tianji.blockchain.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

public class ShareHelper {
    /**
     * 单例模式
     **/
    private static ShareHelper instance;
    private Context context;

    private ShareHelper(Context context) {
        this.context = context;

    }

    public static ShareHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ShareHelper(context);
        }
        return instance;
    }

    public void shareImage(Bitmap bitmap) {
        //将mipmap中图片转换成Uri
        Uri imgUri = CommonUtils.bitmapToUri(context, bitmap);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        //其中imgUri为图片的标识符
        shareIntent.putExtra(Intent.EXTRA_STREAM, imgUri);
        shareIntent.setType("image/*");
        //切记需要使用Intent.createChooser，否则会出现别样的应用选择框，您可以试试
        shareIntent = Intent.createChooser(shareIntent, "Here is the title of Select box");
        context.startActivity(shareIntent);
    }

}
