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
import android.widget.ImageView;

import com.tianji.blockchain.R;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class EditWalletNameDialog extends Dialog {
    private Context context;

    private ImageView img_close;
    private EditText edt_wallet_name;
    private Button btn_edit_wallet_name_ok;

    private View.OnClickListener listener;

    public EditWalletNameDialog(Context context, View.OnClickListener listener) {
        super(context);
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_edit_wallet_name);
        initView();
    }

    private void initView() {
        img_close = findViewById(R.id.img_close);
        edt_wallet_name = findViewById(R.id.edt_wallet_name);
        btn_edit_wallet_name_ok = findViewById(R.id.btn_edit_wallet_name_ok);

        Window dialogWindow = this.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp); //将属性设置给窗体
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_edit_wallet_name_ok.setOnClickListener(listener);

    }

    public String getEdtText() {
        return ViewCommonUtils.getEdtString(edt_wallet_name);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        edt_wallet_name.setText("");
    }
}
