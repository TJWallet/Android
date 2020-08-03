package com.tianji.blockchain.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.tianji.blockchainwallet.constant.Constants;
import com.tianji.blockchain.R;
import com.tianji.blockchain.entity.UpdateEntity;
import com.tianji.blockchain.utils.FileHelper;
import com.tianji.blockchain.utils.InstallUtils;
import com.tianji.blockchain.utils.LogUtils;

import java.io.File;
import java.io.IOException;

public class UpDateDialog extends Dialog {
    private TextView tv_title;
    private TextView tv_description;
    private Button btn_update;
    private Button btn_not_update;
    private ProgressBar progress;
    private TextView tv_progress;
    private LinearLayout ll_progress_block;
    private TextView tv_close;

    private Context context;

    //下载
    private HttpHandler<File> httpHandler;
    private final HttpUtils httpUtils;

    private UpdateEntity entity;

    public UpDateDialog(@NonNull Context context, UpdateEntity entity) {
        super(context);
        this.context = context;
        this.entity = entity;
        httpUtils = new HttpUtils();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_update);
        initView();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_description = findViewById(R.id.tv_description);
        btn_update = findViewById(R.id.btn_update);
        btn_not_update = findViewById(R.id.btn_not_update);
        ll_progress_block = findViewById(R.id.ll_progress_block);
        progress = findViewById(R.id.progress);
        tv_progress = findViewById(R.id.tv_progress);
        tv_close = findViewById(R.id.tv_close);

        this.setCanceledOnTouchOutside(false);// 设置点击屏幕其他地方Dialog不消失
        this.setCancelable(false);//点击返回键不消失

        tv_description.setText(entity.getDescription());
        tv_title.setText("新版本升级 V" + entity.getVersionName());

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                downloadApk(entity.getUrl());
            }
        });

        btn_not_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        Window window = this.getWindow();
        window.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void showProgress() {
        ll_progress_block.setVisibility(View.VISIBLE);
        btn_update.setVisibility(View.GONE);
        btn_not_update.setVisibility(View.GONE);
    }

    public void showDownloadBtn() {
        ll_progress_block.setVisibility(View.GONE);
        btn_update.setVisibility(View.VISIBLE);
        btn_not_update.setVisibility(View.VISIBLE);
    }


    private void downloadApk(String downloadUrl) {
        try {
//            Runtime.getRuntime().exec("chmod 777 " + context.getFilesDir().getAbsolutePath());
            Runtime.getRuntime().exec("chmod 777 " + Environment.getDownloadCacheDirectory().getAbsolutePath());
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOWNLOADS).getPath();
//            String directory = context.getFilesDir().getAbsolutePath();
            final String savePath = directory + fileName;
            if (FileHelper.fileExist(savePath)) {
                FileHelper.deleteFile(savePath);
            }
            LogUtils.log("下载到地址==" + savePath);
            httpHandler = httpUtils.download(downloadUrl, savePath, true, true, new RequestCallBack<File>() {
                @Override
                public void onSuccess(final ResponseInfo<File> responseInfo) {
                    try {
                        Runtime.getRuntime().exec("chmod 777 " + responseInfo.result.getAbsolutePath());
                        LogUtils.log("下载成功" + responseInfo.result.getAbsolutePath());
                        if (UpDateDialog.this.isShowing()) {
                            UpDateDialog.this.dismiss();
                        }
                        InstallUtils.install(context, new File(savePath), "com.tianji.blockchain.fileprovider");
                        UpDateDialog.this.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    LogUtils.logError("下载失败:" + e.toString());
                    LogUtils.logError("下载失败:" + s);
                    if (UpDateDialog.this.isShowing()) {
                        UpDateDialog.this.dismiss();
                        showDownloadBtn();
                    }
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    LogUtils.log("下载进度 = " + total + "/" + current);
                    if (ll_progress_block.getVisibility() == View.VISIBLE) {
                        progress.setMax((int) total);
                        progress.setProgress((int) current);

                        double d = ((double) current / total);
                        int percent = (int) (d * 100);
                        LogUtils.log("下载百分比 = " + percent);
                        tv_progress.setText(percent + "%");
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
