package com.tianji.blockchain.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianji.blockchain.R;
import com.tianji.blockchain.entity.DialogEntity;

public class TipsDialog extends Dialog {
    private Context context;
    private TextView tv_title;
    private TextView tv_desc;
    private Button btn_positive;
    private TextView tv_negative;
    private CheckBox checkbox;

    private DialogEntity entity;
    private boolean isTwoBottom;
    private int descColor;
    private boolean hasCheckBox;

    public TipsDialog(@NonNull Context context, DialogEntity entity) {
        super(context);
        this.context = context;
        this.entity = entity;
        this.isTwoBottom = true;
        this.descColor = context.getResources().getColor(R.color.secondary);
    }

    public TipsDialog(@NonNull Context context, DialogEntity entity, boolean isTwoBottom) {
        super(context);
        this.context = context;
        this.entity = entity;
        this.isTwoBottom = isTwoBottom;
        this.descColor = context.getResources().getColor(R.color.secondary);
    }

    public TipsDialog(@NonNull Context context, DialogEntity entity, int descColor, boolean hasCheckBox) {
        super(context);
        this.context = context;
        this.entity = entity;
        this.isTwoBottom = true;
        this.descColor = descColor;
        this.hasCheckBox = hasCheckBox;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tips);
//        this.setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_desc = findViewById(R.id.tv_desc);
        btn_positive = findViewById(R.id.btn_positive);
        tv_negative = findViewById(R.id.tv_negative);
        checkbox = findViewById(R.id.checkbox);
        tv_desc.setTextColor(descColor);
        if (!isTwoBottom) {
            tv_negative.setVisibility(View.GONE);
        } else {
            tv_negative.setVisibility(View.VISIBLE);
        }

        if (hasCheckBox) {
            checkbox.setVisibility(View.VISIBLE);
        } else {
            checkbox.setVisibility(View.GONE);
        }

        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp); //将属性设置给窗体
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);

        tv_title.setText(entity.getTitle());
        tv_desc.setText(entity.getDesc());
        btn_positive.setText(entity.getPositiveBtnText());

        if (entity.getNegativeBtnText() != null) {
            tv_negative.setText(entity.getNegativeBtnText());
            tv_negative.setOnClickListener(entity.getPositiveListener());
            btn_positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            tv_negative.setText(R.string.cancel);
            btn_positive.setOnClickListener(entity.getPositiveListener());
            tv_negative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    public CheckBox getCheckbox() {
        return checkbox;
    }
}
