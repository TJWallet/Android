package com.tianji.blockchain.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.tianji.blockchain.R;
import com.tianji.blockchain.utils.CommonUtils;

public class IPFSAddressListDialog extends Dialog {
    private String hash;
    private Context context;

    private ImageView img_close;
    private LinearLayout ll_ipfs_hash;
    private TextView tv_ipfs_hash;
    private LinearLayout ll_ipfs_web_1, ll_ipfs_web_2, ll_ipfs_web_3;
    private TextView tv_ipfs_web_1, tv_ipfs_web_2, tv_ipfs_web_3;

    public IPFSAddressListDialog(@NonNull Context context, int themeResId, String hash) {
        super(context, themeResId);
        this.context = context;
        this.hash = hash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_ipfs_address);
        initView();
    }

    private void initView() {
        img_close = findViewById(R.id.img_close);
        ll_ipfs_hash = findViewById(R.id.ll_ipfs_hash);
        tv_ipfs_hash = findViewById(R.id.tv_ipfs_hash);
        ll_ipfs_web_1 = findViewById(R.id.ll_ipfs_web_1);
        ll_ipfs_web_2 = findViewById(R.id.ll_ipfs_web_2);
        ll_ipfs_web_3 = findViewById(R.id.ll_ipfs_web_3);
        tv_ipfs_web_1 = findViewById(R.id.tv_ipfs_web_1);
        tv_ipfs_web_2 = findViewById(R.id.tv_ipfs_web_2);
        tv_ipfs_web_3 = findViewById(R.id.tv_ipfs_web_3);

        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.y = 0; //设置Dialog距离底部的距离
        dialogWindow.setAttributes(lp); //将属性设置给窗体

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        String webUrl1 = "https://ipfs.dapponline.io/ipfs/" + hash;
        String webUrl2 = "https://ipfs.io/ipfs/" + hash;
        String webUrl3 = "https://ipfs.infura.io/ipfs/" + hash;

        tv_ipfs_hash.setText(hash);
        tv_ipfs_web_1.setText(webUrl1);
        tv_ipfs_web_2.setText(webUrl2);
        tv_ipfs_web_3.setText(webUrl3);

        ll_ipfs_hash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.copyContent(context, hash);
            }
        });

        ll_ipfs_web_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(webUrl1);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
        ll_ipfs_web_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(webUrl2);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
        ll_ipfs_web_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(webUrl3);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            }
        });
    }
}
