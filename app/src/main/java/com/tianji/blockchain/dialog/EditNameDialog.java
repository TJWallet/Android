package com.tianji.blockchain.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianji.blockchain.R;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class EditNameDialog extends Dialog {
    /***1：修改钱包名称 ； 2：修改硬件钱包名称***/
    private int type;
    private Context context;

    private TextView tv_title;
    private EditText edt_name;
    private Button btn_positive;
    private TextView tv_negative;
    private String name;

    private View.OnClickListener listener;

    public EditNameDialog(@NonNull Context context, int type, String name, View.OnClickListener listener) {
        super(context);
        this.context = context;
        this.type = type;
        this.listener = listener;
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_name);
        initView();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        edt_name = findViewById(R.id.edt_name);
        btn_positive = findViewById(R.id.btn_positive);
        tv_negative = findViewById(R.id.tv_negative);

        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp); //将属性设置给窗体
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);


        switch (type) {
            case 1:
                tv_title.setText(R.string.edit_wallet_name);
                edt_name.setHint(R.string.edit_wallet_name_hint);
                break;
            case 2:
                tv_title.setText(R.string.edit_hardware_name);
                edt_name.setHint(R.string.edit_hardware_name_hint);
                break;
        }
        edt_name.setText(name);
        tv_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_positive.setOnClickListener(listener);
    }

    public String getEdtText() {
        return ViewCommonUtils.getEdtString(edt_name);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        edt_name.setText("");
    }
}
