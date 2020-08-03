package com.tianji.blockchain.view;


import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.utils.CompanyUtil;


/**
 * Created by xgp on 2017/12/11.
 * actionBar控件
 */

public class ActionBar extends RelativeLayout {
    private TextView tv_action_title;
    private RelativeLayout actionBarBg;

    public ActionBar(Context context) {
        super(context);
        inflate(context, R.layout.action_bar_normal, this);
        tv_action_title = findViewById(R.id.action_bar_title);
        actionBarBg = findViewById(R.id.rl_actionbar);
    }

    /**
     * 设置actionbar的标题，最好不要超过5个字
     *
     * @param title
     */
    public void setActionTitle(String title) {
        tv_action_title.setText(title);
    }

    public String getActionTitle() {
        return (String) tv_action_title.getText();
    }

    public void setBackground(int color) {
        actionBarBg.setBackgroundColor(color);
    }

    public void addTextViewToRight(View view) {
        LayoutParams LP1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LP1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        LP1.addRule(RelativeLayout.CENTER_VERTICAL);
        LP1.setMargins(0, 0, CompanyUtil.dip2px(WalletApplication.getApp(), 10f), 0);
        view.setLayoutParams(LP1);
        this.addView(view);
    }

    /**
     * 设置标题栏靠标题右边的按钮
     *
     * @param view
     * @param position
     */
    public void addViewToRight(View view, int position) {
        if (position != 1 && position != 2 && position != 3) {
            return;
        }
        switch (position) {
            case 1:
                LayoutParams LP1 = new LayoutParams(CompanyUtil.dip2px(WalletApplication.getApp(), 30f), CompanyUtil.dip2px(WalletApplication.getApp(), 30f));
                LP1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                LP1.addRule(RelativeLayout.CENTER_VERTICAL);
                LP1.setMargins(0, 0, CompanyUtil.dip2px(WalletApplication.getApp(), 10f), 0);
                view.setLayoutParams(LP1);
                this.addView(view);
                break;
            case 2:
                LayoutParams LP2 = new LayoutParams(CompanyUtil.dip2px(WalletApplication.getApp(), 30f), CompanyUtil.dip2px(WalletApplication.getApp(), 30f));
                LP2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                LP2.addRule(RelativeLayout.CENTER_VERTICAL);
                LP2.setMargins(0, 0, CompanyUtil.dip2px(WalletApplication.getApp(), 50f), 0);
                view.setLayoutParams(LP2);
                this.addView(view);
                break;
            case 3:
                LayoutParams LP3 = new LayoutParams(CompanyUtil.dip2px(WalletApplication.getApp(), 30f), CompanyUtil.dip2px(WalletApplication.getApp(), 30f));
                LP3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                LP3.addRule(RelativeLayout.CENTER_VERTICAL);
                LP3.setMargins(0, 0, CompanyUtil.dip2px(WalletApplication.getApp(), 90f), 0);
                view.setLayoutParams(LP3);
                this.addView(view);
                break;
        }
    }

    /**
     * 设置标题栏靠标题右边的按钮
     */
    public void addViewToRightHasText(View view1, View view2) {
        LayoutParams LP1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LP1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        LP1.addRule(RelativeLayout.CENTER_VERTICAL);
        LP1.setMargins(0, 0, CompanyUtil.dip2px(WalletApplication.getApp(), 10f), 0);
        view1.setLayoutParams(LP1);
        this.addView(view1);
        LayoutParams LP2 = new LayoutParams(CompanyUtil.dip2px(WalletApplication.getApp(), 30f), CompanyUtil.dip2px(WalletApplication.getApp(), 30f));
        LP2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        LP2.addRule(RelativeLayout.CENTER_VERTICAL);
        LP2.setMargins(0, 0, CompanyUtil.dip2px(WalletApplication.getApp(), 70f), 0);
        view2.setLayoutParams(LP2);
        this.addView(view2);
    }

    /**
     * 设置标题栏靠标题左边的按钮，一般是返回按钮
     *
     * @param view
     */
    public void addViewToLeft(View view) {
        LayoutParams LP = new LayoutParams(CompanyUtil.dip2px(WalletApplication.getApp(), 30f), CompanyUtil.dip2px(WalletApplication.getApp(), 30f));
        LP.addRule(RelativeLayout.CENTER_VERTICAL);
        LP.setMargins(CompanyUtil.dip2px(WalletApplication.getApp(), 10f), 0, 0, 0);
        view.setLayoutParams(LP);
        this.addView(view);
    }

}
