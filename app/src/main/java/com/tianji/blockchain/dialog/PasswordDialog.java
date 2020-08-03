package com.tianji.blockchain.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianji.blockchain.R;
import com.tianji.blockchain.utils.ViewCommonUtils;


public class PasswordDialog extends Dialog {
    private Context context;
    private TextView tv_title;
    private ImageView img_back;
    private ImageView img_close;
    private EditText edt_password;
    private RelativeLayout rl_pwd_eye;
    private ImageView img_pwd_eye;
    private Button btn_ok;

    private int type; //1 ：密码 2：PIN码
    private boolean isShowBack;
    private View.OnClickListener listener;

    private boolean edtIsHid = true;

    public PasswordDialog(@NonNull Context context, int themeResId, int type, boolean isShowBack, View.OnClickListener listener) {
        super(context, themeResId);
        this.context = context;
        this.type = type;
        this.isShowBack = isShowBack;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_basic_password);
        initView();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        img_back = findViewById(R.id.img_back);
        img_close = findViewById(R.id.img_close);
        edt_password = findViewById(R.id.edt_password);
        rl_pwd_eye = findViewById(R.id.rl_pwd_eye);
        img_pwd_eye = findViewById(R.id.img_pwd_eye);
        btn_ok = findViewById(R.id.btn_ok);

        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.y = 0; //设置Dialog距离底部的距离
        dialogWindow.setAttributes(lp); //将属性设置给窗体

        if (type == 1) {
            tv_title.setText(context.getResources().getString(R.string.dialog_password_1));
        } else if (type == 2) {
            tv_title.setText(context.getResources().getString(R.string.dialog_password_2));
        }

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        edt_password.setFocusable(true);
        edt_password.setFocusableInTouchMode(true);
        edt_password.requestFocus();
        edt_password.setTransformationMethod(PasswordTransformationMethod.getInstance());


        if (isShowBack) {
            img_back.setVisibility(View.VISIBLE);
        } else {
            img_back.setVisibility(View.GONE);
        }
        rl_pwd_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewCommonUtils.showPwd(edtIsHid, edt_password, img_pwd_eye);
                edtIsHid = !edtIsHid;
            }
        });

        btn_ok.setOnClickListener(listener);
        img_back.setOnClickListener(listener);
    }

    public String getEdtText() {
        return ViewCommonUtils.getEdtString(edt_password);
    }

    public void setType(int type) {
        if (tv_title != null) {
            if (type == 1) {
                tv_title.setText(context.getResources().getString(R.string.dialog_password_1));
            } else if (type == 2) {
                tv_title.setText(context.getResources().getString(R.string.dialog_password_2));
            }
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        edt_password.setText("");
    }

}
