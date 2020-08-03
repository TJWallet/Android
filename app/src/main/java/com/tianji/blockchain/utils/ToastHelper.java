package com.tianji.blockchain.utils;

import android.content.Context;
import android.widget.Toast;

import com.tianji.blockchain.R;


public class ToastHelper {

    private Context context;
    /**
     * 单例模式
     **/
    private static ToastHelper instance;


    private ToastHelper(Context context) {
        this.context = context;
    }

    public static ToastHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ToastHelper(context);
        }
        return instance;
    }


    public void usbNotConnectToast() {
        Toast.makeText(context, context.getResources().getString(R.string.no_connect), Toast.LENGTH_SHORT).show();
    }

    public void usbDisConnectToast() {
        Toast.makeText(context, context.getResources().getString(R.string.dis_connect), Toast.LENGTH_SHORT).show();
    }
}
