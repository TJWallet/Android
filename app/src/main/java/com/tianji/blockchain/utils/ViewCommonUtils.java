package com.tianji.blockchain.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.view.ActionBar;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 关于控件的通用方法类
 */
public class ViewCommonUtils {

    /**
     * 切换明文密文
     *
     * @param textView
     * @param img
     */
    public static void showPwd(boolean isEdtHidden, TextView textView, ImageView img) {
        if (!isEdtHidden) {
            //editText可见
            textView.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            /**图标改变*/
            img.setImageResource(R.drawable.cansee);
        } else {
            textView.setText("****");
            /**图标改变*/
            img.setImageResource(R.drawable.cantsee);
        }
        textView.postInvalidate();
    }


    /**
     * 切换明文密文
     *
     * @param editText
     * @param img
     */
    public static void showPwd(boolean isEdtHidden, EditText editText, ImageView img) {
        if (isEdtHidden) {
            //editText可见
            editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            /**图标改变*/
            img.setImageResource(R.drawable.cansee_dark);
        } else {
            //editText不可见
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            /**图标改变*/
            img.setImageResource(R.drawable.cantsee_dark);
        }
        editText.postInvalidate();
        //切换后将EditText光标置于末尾
        CharSequence charSequence = editText.getText();
        if (charSequence instanceof Spannable) {
            Spannable spanText = (Spannable) charSequence;
            Selection.setSelection(spanText, charSequence.length());
        }
    }

    /**
     * 设置actionBar
     *
     * @param activity
     * @param mActionBar
     */
    public static void buildBackImageView(final Activity activity, ActionBar mActionBar) {
        ImageView img_back = buildImageView(activity, R.drawable.back_light);
        img_back.setScaleType(ImageView.ScaleType.CENTER);
        mActionBar.addViewToLeft(img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }

    /**
     * 设置actionBar,自定义返回点击事件
     *
     * @param activity
     * @param mActionBar
     */
    public static void buildBackImageView(final Activity activity, ActionBar mActionBar, View.OnClickListener listener) {
        ImageView img_back = buildImageView(activity, R.drawable.back_light);
        img_back.setScaleType(ImageView.ScaleType.CENTER);
        mActionBar.addViewToLeft(img_back);
        img_back.setOnClickListener(listener);
    }


    /**
     * 创建一个ImageView
     *
     * @param resources
     * @return
     */
    public static ImageView buildImageView(Context context, int resources) {
        ImageView image = new ImageView(context);
        image.setScaleType(ImageView.ScaleType.CENTER);
        image.setImageResource(resources);
        return image;
    }

    /**
     * recyclerView禁止滑动
     *
     * @param recyclerView
     */
    public static void recyclerViewNoSliding(Context context, RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * 获取输入框内容
     *
     * @param editText
     * @return
     */
    public static String getEdtString(EditText editText) {
        return editText.getText().toString().trim();
    }

    /**
     * 延时拉起软键盘
     *
     * @param context
     */
    public static void showKeyBorad(final Context context) {

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        }, 200);//这里的时间大概是自己测试的
    }

    /**
     * 创建一个默认只有返回按钮的actionbar
     *
     * @param mActionBar
     * @param activity
     * @param res
     */
    public static void createDefaultActionBar(ActionBar mActionBar, Activity activity, int res) {
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(activity.getResources().getString(res));
        ViewCommonUtils.buildBackImageView(activity, mActionBar);
    }

    /**
     * 创建一个默认只有返回按钮的actionbar
     *
     * @param mActionBar
     * @param activity
     */
    public static void createDefaultActionBar(ActionBar mActionBar, Activity activity, String title) {
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(title);
        ViewCommonUtils.buildBackImageView(activity, mActionBar);
    }

    /**
     * @param img
     * @param type 1：USB连接上了 , 2:USB拔出了 ; 3: USB已经连接上了 ; 4:USB未连接
     */
    public static void setConnectTypeImg(ImageView img, int type) {
        switch (type) {
            case 1:
                img.setVisibility(View.VISIBLE);
                break;
            case 2:
                img.setVisibility(View.GONE);
                break;
            case 3:
                img.setVisibility(View.VISIBLE);
                break;
            case 4:
                img.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * @param textView
     */
    public static void setTextViewUnderLine(TextView textView, boolean flag) {
        if (flag) {
            textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            textView.getPaint().setAntiAlias(true);//抗锯齿
        } else {
            textView.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG); //下划线
            textView.getPaint().setAntiAlias(true);//抗锯齿
        }
    }

}
