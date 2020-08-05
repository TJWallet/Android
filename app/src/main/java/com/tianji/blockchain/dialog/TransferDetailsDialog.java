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

import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.entity.TransferEntity;
import com.tianji.blockchain.entity.TransferRecode;
import com.tianji.blockchain.entity.TransferResuletInfoEntity;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.MathUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

public class TransferDetailsDialog extends Dialog {
    private ImageView img_close;
    private TextView tv_amount;
    private TextView tv_chain_type;
    private TextView tv_transfer_type;
    private TextView tv_transfer_into_address;
    private TextView tv_transfer_out_address;
    private TextView tv_miners_fee;
    private Button btn_ok;
    private Context context;
    private TransferRecode entity;
    private View.OnClickListener listener;
    private String transferCoinName;
    private WalletInfo walletInfo;
    private TextView tv_miners_fee_value;
    private TextView tv_amount_value;

    public TransferDetailsDialog(@NonNull Context context, int themeResId, String transferCoinName, TransferRecode entity, WalletInfo walletInfo, View.OnClickListener listener) {
        super(context, themeResId);
        this.entity = entity;
        this.listener = listener;
        this.transferCoinName = transferCoinName;
        this.walletInfo = walletInfo;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_transfer_details);
        initView();
    }

    private void initView() {
        img_close = findViewById(R.id.img_close);
        tv_amount = findViewById(R.id.tv_amount);
        tv_chain_type = findViewById(R.id.tv_chain_type);
        tv_transfer_type = findViewById(R.id.tv_transfer_type);
        tv_transfer_into_address = findViewById(R.id.tv_transfer_into_address);
        tv_transfer_out_address = findViewById(R.id.tv_transfer_out_address);
        tv_miners_fee = findViewById(R.id.tv_miners_fee);
        tv_miners_fee_value = findViewById(R.id.tv_miners_fee_value);
        tv_amount_value = findViewById(R.id.tv_amount_value);
        btn_ok = findViewById(R.id.btn_ok);
        tv_amount_value.setVisibility(View.GONE);
        tv_miners_fee_value.setVisibility(View.GONE);

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

        tv_chain_type.setText(transferCoinName);
        tv_transfer_type.setText(transferCoinName + " " + context.getResources().getString(R.string.transfer));

        tv_miners_fee.setText(MathUtils.doubleKeep10(new BigDecimal(Double.parseDouble(entity.getCumulativeGasUsed())).divide(new BigDecimal(Math.pow(10, 9))).setScale(10, RoundingMode.HALF_UP).doubleValue()) + " FIL");
        tv_amount.setText(MathUtils.doubleKeep4(new BigDecimal(entity.getValue()).divide(new BigDecimal(Math.pow(10, Integer.parseInt(entity.getTokenDecimal())))).doubleValue()));

        tv_transfer_into_address.setText(entity.getTo());
        tv_transfer_out_address.setText(entity.getFrom());
        btn_ok.setOnClickListener(listener);
    }
}
