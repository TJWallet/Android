package com.tianji.blockchain.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tianji.blockchainwallet.constant.enums.Chain;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.main.EditAddressActivity;
import com.tianji.blockchain.adapter.basic.OnItemClickListener;
import com.tianji.blockchain.adapter.recyclerView.RVAdapterBasicSelectDialog;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

import java.util.List;

import jnr.ffi.annotations.In;

public class SelectDialog extends Dialog {
    private TextView tv_title;
    private ImageView img_close;
    private RecyclerView recyclerView;
    private Context context;

    private RVAdapterBasicSelectDialog adapter;
    private String title;
    private OnItemClickListener listener;
    private String[] mList;
    private WalletInfo walletInfo;
    private String address;


    public SelectDialog(@NonNull Context context, int themeResId, String[] list, String title, OnItemClickListener listener) {
        super(context, themeResId);
        this.context = context;
        this.mList = list;
        this.title = title;
        this.listener = listener;
    }

    public SelectDialog(@NonNull Context context, int themeResId, String[] list, String title) {
        super(context, themeResId);
        this.context = context;
        this.mList = list;
        this.title = title;
        this.listener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position, Object obj) {
            if (walletInfo == null || address == null) return;
            switch (position) {
                case 0:
                    CommonUtils.copyContent(context, address);
                    break;
                case 1:
                    Intent intent = new Intent(context, EditAddressActivity.class);
                    intent.putExtra("_walletInfo", walletInfo);
                    intent.putExtra("_addressType", Constant.AddressType.TYPE_ADDRESS_OPERATE);
                    intent.putExtra("_saveAddress", address);
                    context.startActivity(intent);
                    break;
            }
            SelectDialog.this.dismiss();
        }
    };


    /**
     * 更新钱包对象
     *
     * @param walletInfo
     */
    public void upDateWalletInfo(WalletInfo walletInfo) {
        this.walletInfo = walletInfo;
    }

    public void upDateAddress(String address) {
        this.address = address;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_basic_select);
        initView();
    }


    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        img_close = findViewById(R.id.img_close);
        recyclerView = findViewById(R.id.recyclerView);
        ViewCommonUtils.recyclerViewNoSliding(context, recyclerView);


        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.y = 0; //设置Dialog距离底部的距离
        dialogWindow.setAttributes(lp); //将属性设置给窗体

        tv_title.setText(title);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        adapter = new RVAdapterBasicSelectDialog(context, mList);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickListener(listener);
    }
}
