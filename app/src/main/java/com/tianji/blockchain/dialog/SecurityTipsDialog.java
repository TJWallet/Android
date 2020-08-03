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

import androidx.annotation.NonNull;
import com.tianji.blockchain.R;

public class SecurityTipsDialog extends Dialog {
    private View.OnClickListener listener;

    private Context context;
    private ImageView ivClose;
    private Button btnConfirm;

    public SecurityTipsDialog(@NonNull Context context, int themeResId, View.OnClickListener onClickConfirmListener) {
        super(context, themeResId);
        this.context = context;
        this.listener = onClickConfirmListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_security_tips);
        initView();
    }

    private void initView() {
        this.ivClose = this.findViewById(R.id.ivClose);
        this.btnConfirm = this.findViewById(R.id.btnConfirm);

        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.y = 0;
        window.setAttributes(layoutParams);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnConfirm.setOnClickListener(listener);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
