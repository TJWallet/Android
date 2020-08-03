package com.tianji.blockchain.dialog;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DialogWalletManager extends Dialog {
    private Context context;

    public DialogWalletManager(@NonNull Context context) {
        super(context);

    }

    public DialogWalletManager(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected DialogWalletManager(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
