package com.tianji.blockchain.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.hardware.HardwareWalletGuideActivity;
import com.tianji.blockchain.activity.pin.SetPinActivity;
import com.tianji.blockchain.dialog.HardwareTipsDialog;
import com.tianji.blockchain.dialog.ShowContentDialog;
import com.tianji.blockchain.dialog.TipsDialog;
import com.tianji.blockchain.entity.DialogEntity;

public class DialogUtils {

    public static void buildProgressDialog(Context mContext, ProgressDialog progressDialog, int id) {
        if (progressDialog == null) {
            return;
        }
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(mContext.getResources().getString(id));
        progressDialog.show();
    }

    public static void buildProgressDialog(Context mContext, ProgressDialog progressDialog, String content) {
        if (progressDialog == null) {
            return;
        }
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(content);
        progressDialog.show();
    }

    public static void cancelProgressDialog(ProgressDialog progressDialog) {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    /***创建一个初始化硬件的弹窗***/
    public static TipsDialog buildHardwareInitDialog(Context context, View.OnClickListener listener) {
        DialogEntity initDialogEntity = new DialogEntity(context.getResources().getString(R.string.hardware_init_title)
                , context.getResources().getString(R.string.hardware_init_content)
                , context.getResources().getString(R.string.hardware_init_btn_ok),
                listener);
        return new TipsDialog(context, initDialogEntity);
    }

    /***创建一个切换硬件的弹窗***/
    public static TipsDialog buildHardwareCheckDialog(Context context, View.OnClickListener listener) {
        DialogEntity checkHardwareEntity = new DialogEntity(context.getResources().getString(R.string.hardware_has_wallet_title)
                , context.getResources().getString(R.string.hardware_has_wallet_content)
                , context.getResources().getString(R.string.hardware_has_wallet_btn_ok),
                listener);
        return new TipsDialog(context, checkHardwareEntity);
    }

    /***创建一个初始化硬件的弹窗***/
    public static HardwareTipsDialog buildHardwareGifInitDialog(Context context, Activity activity, View.OnClickListener listener) {
        DialogEntity initDialogEntity = new DialogEntity(context.getResources().getString(R.string.hardware_init_title)
                , context.getResources().getString(R.string.hardware_init_content)
                , context.getResources().getString(R.string.hardware_init_btn_ok),
                listener);
        return new HardwareTipsDialog(context, R.style.Wallet_Manager_Dialog, initDialogEntity, activity);
    }

    /***创建一个切换硬件的弹窗***/
    public static HardwareTipsDialog buildHardwareGifCheckDialog(Context context, Activity activity, View.OnClickListener listener) {
        DialogEntity checkHardwareEntity = new DialogEntity(context.getResources().getString(R.string.hardware_has_wallet_title)
                , context.getResources().getString(R.string.hardware_has_wallet_content)
                , context.getResources().getString(R.string.hardware_has_wallet_btn_ok),
                listener);
        return new HardwareTipsDialog(context, R.style.Wallet_Manager_Dialog, checkHardwareEntity, activity);
    }

    /***创建一个激活流程中没有网络的弹窗***/
    public static TipsDialog buildHardwareNoNetworkDialog(Context context, View.OnClickListener listener) {
        DialogEntity dialogEntity = new DialogEntity(context.getResources().getString(R.string.tips)
                , context.getResources().getString(R.string.init_no_network_desc)
                , context.getResources().getString(R.string.ok),
                listener);
        return new TipsDialog(context, dialogEntity, false);
    }

}
