package com.tianji.blockchain.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.adapter.recyclerView.TransferAddressListInputAdapter;
import com.tianji.blockchain.adapter.recyclerView.TransferAddressListOutputAdapter;
import com.tianji.blockchain.entity.TransferRecode;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class TransferInfoAddressListDialog extends Dialog {
    private TransferRecode transferRecode;
    private Context context;

    private TextView tv_input_amount;
    private TextView tv_output_amount;
    private ImageView img_close;
    private RecyclerView recycler_input;
    private RecyclerView recycler_output;

    public TransferInfoAddressListDialog(@NonNull Context context, int themeResId, TransferRecode transferRecode) {
        super(context, themeResId);
        this.context = context;
        this.transferRecode = transferRecode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_transfer_info_address_list);
        initView();
    }

    private void initView() {
        tv_input_amount = findViewById(R.id.tv_input_amount);
        tv_output_amount = findViewById(R.id.tv_output_amount);
        img_close = findViewById(R.id.img_close);
        recycler_input = findViewById(R.id.recycler_input);
        recycler_output = findViewById(R.id.recycler_output);
        ViewCommonUtils.recyclerViewNoSliding(WalletApplication.getApp(), recycler_input);
        ViewCommonUtils.recyclerViewNoSliding(WalletApplication.getApp(), recycler_output);

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
        tv_input_amount.setText(transferRecode.getInputList().size() + "");
        tv_output_amount.setText(transferRecode.getOutputList().size() + "");

        TransferAddressListInputAdapter inputAdapter = new TransferAddressListInputAdapter(context, transferRecode.getInputList());
        recycler_input.setLayoutManager(new LinearLayoutManager(context));
        recycler_input.setAdapter(inputAdapter);
        TransferAddressListOutputAdapter outputAdapter = new TransferAddressListOutputAdapter(context, transferRecode.getOutputList());
        recycler_output.setLayoutManager(new LinearLayoutManager(context));
        recycler_output.setAdapter(outputAdapter);
    }
}
