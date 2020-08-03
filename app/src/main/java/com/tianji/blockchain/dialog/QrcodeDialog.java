package com.tianji.blockchain.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.tianji.blockchain.utils.CompanyUtil;
import com.tianji.blockchain.utils.QrCodeUtils;

public class QrcodeDialog extends Dialog {
    private Context context;
    private ImageView img_qrcode;
    private String qrContent;
    private String title;
    private String desc;
    private int imgRes;
    private Button btn_positive;
    private TextView tv_negative;
    private TextView tv_title;
    private TextView tv_desc;
    private View.OnClickListener listener;

    public QrcodeDialog(@NonNull Context context, String title, String desc, String qrContent, View.OnClickListener listener) {
        super(context);
        this.context = context;
        this.qrContent = qrContent;
        this.title = title;
        this.desc = desc;
        this.listener = listener;
    }

    public QrcodeDialog(@NonNull Context context, String title, String desc, int imgRes, View.OnClickListener listener) {
        super(context);
        this.context = context;
        this.title = title;
        this.desc = desc;
        this.imgRes = imgRes;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_qrcode);
        initView();
    }

    private void initView() {
        img_qrcode = findViewById(R.id.img_qrcode);
        btn_positive = findViewById(R.id.btn_positive);
        tv_negative = findViewById(R.id.tv_negative);
        tv_title = findViewById(R.id.tv_title);
        tv_desc = findViewById(R.id.tv_desc);

        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp); //将属性设置给窗体

        tv_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_positive.setOnClickListener(listener);
        tv_title.setText(title);
        tv_desc.setText(desc);
        if (imgRes != 0) {
            img_qrcode.setImageResource(imgRes);
        } else {
            Bitmap bit = QrCodeUtils.createSimpleQRCodeBitmap(qrContent, CompanyUtil.dip2px(context, 168f), CompanyUtil.dip2px(context, 168f), "UTF-8", "H", "0", Color.BLACK, Color.WHITE);
            img_qrcode.setImageBitmap(bit);
        }

    }
}
