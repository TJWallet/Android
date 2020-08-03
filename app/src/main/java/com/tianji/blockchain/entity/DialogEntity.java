package com.tianji.blockchain.entity;

import android.view.View;

/**
 * 通用提示dialog实体类
 */
public class DialogEntity {
    private String title;
    private String desc;
    private String positiveBtnText;
    private String negativeBtnText;
    private View.OnClickListener positiveListener;

    public DialogEntity(String title, String desc, String positiveBtnText, View.OnClickListener positiveListener) {
        this.title = title;
        this.desc = desc;
        this.positiveBtnText = positiveBtnText;
        this.negativeBtnText = negativeBtnText;
        this.positiveListener = positiveListener;
    }

    public DialogEntity(String title, String desc, String positiveBtnText, String negativeBtnText, View.OnClickListener positiveListener) {
        this.title = title;
        this.desc = desc;
        this.positiveBtnText = positiveBtnText;
        this.negativeBtnText = negativeBtnText;
        this.positiveListener = positiveListener;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPositiveBtnText() {
        return positiveBtnText;
    }

    public void setPositiveBtnText(String positiveBtnText) {
        this.positiveBtnText = positiveBtnText;
    }

    public String getNegativeBtnText() {
        return negativeBtnText;
    }

    public void setNegativeBtnText(String negativeBtnText) {
        this.negativeBtnText = negativeBtnText;
    }

    public View.OnClickListener getPositiveListener() {
        return positiveListener;
    }

    public void setPositiveListener(View.OnClickListener positiveListener) {
        this.positiveListener = positiveListener;
    }

    @Override
    public String toString() {
        return "DialogEntity{" +
                "title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", positiveBtnText='" + positiveBtnText + '\'' +
                ", negativeBtnText='" + negativeBtnText + '\'' +
                ", positiveListener=" + positiveListener +
                '}';
    }
}
