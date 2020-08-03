package com.tianji.blockchain.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.tianji.blockchain.R;

public class LoadingDialog extends Dialog {
    private Context context;
    private String loadingText;
    private TextView tvLoading;

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public LoadingDialog(@NonNull Context context, int themeResId, String loadingText) {
        super(context, themeResId);
        this.context = context;
        this.loadingText = loadingText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_loading);
        initView();
    }

    private void initView() {
        this.tvLoading = this.findViewById(R.id.tvLoading);

        Window window = this.getWindow();
        window.setGravity(Gravity.NO_GRAVITY);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.y = 0;
        window.setAttributes(layoutParams);

        if (this.loadingText != null) {
            tvLoading.setText(this.loadingText);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
