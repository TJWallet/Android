package com.tianji.blockchain.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianji.blockchain.R;

public class ShowContentDialog extends Dialog {
    private Context context;

    private TextView tv_title;
    private TextView tv_desc;
    private TextView tv_content;
    private Button btn_positive;
    private TextView tv_negative;

    private String title;
    private String desc;
    private String content;
    private String btnText;
    private View.OnClickListener listener;
    private boolean isTwoBtn;

    public ShowContentDialog(@NonNull Context context, String title, String desc, String content, String btnText, View.OnClickListener listener) {
        super(context);
        this.context = context;
        this.desc = desc;
        this.content = content;
        this.btnText = btnText;
        this.listener = listener;
        this.title = title;
        this.isTwoBtn = true;
    }

    public ShowContentDialog(@NonNull Context context, String title, String desc, String content, String btnText, boolean isTwoBtn, View.OnClickListener listener) {
        super(context);
        this.context = context;
        this.desc = desc;
        this.content = content;
        this.btnText = btnText;
        this.listener = listener;
        this.title = title;
        this.isTwoBtn = isTwoBtn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_show_content);
        this.setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_desc = findViewById(R.id.tv_desc);
        tv_content = findViewById(R.id.tv_content);
        btn_positive = findViewById(R.id.btn_positive);
        tv_negative = findViewById(R.id.tv_negative);

        if (!isTwoBtn) {
            tv_negative.setVisibility(View.GONE);
        } else {
            tv_negative.setVisibility(View.VISIBLE);
        }
        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp); //将属性设置给窗体
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);

        tv_desc.setText(desc);
        tv_content.setText(content);

        if (content.equals("")) {
            tv_content.setTextColor(context.getResources().getColor(R.color.hintColor));
            tv_content.setText(context.getResources().getString(R.string.no_tips));
        } else {
            tv_content.setText(content);
        }

        tv_title.setText(title);

        tv_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_positive.setText(btnText);
        btn_positive.setOnClickListener(listener);
    }
}
