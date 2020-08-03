package com.tianji.blockchain.activity.basic;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.basic.BasicPresenter;
import com.tianji.blockchain.utils.ContextUtils;
import com.tianji.blockchain.utils.LogUtils;

import java.util.Locale;


public abstract class BasicAppActivity extends AppCompatActivity {
    public BasicPresenter presenter;
    protected String className;
    protected Vibrator vibrator;

    /**
     * UBS标识
     */
    protected boolean usbIsConnected;
    protected boolean usbConnect;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //强制竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        } else {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        className = this.getLocalClassName();
        vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.log("onResume()   " + this.getLocalClassName());

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    /**
     * 弹出toast提示
     */
    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出toast提示
     */
    protected void showToast(int strId) {
        Toast.makeText(this, getString(strId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 跳转到指定的Activity
     *
     * @param targetActivity 要跳转的目标Activity
     */
    protected final void startActivity(Class<?> targetActivity) {
        startActivity(new Intent(this, targetActivity));
    }

    /**
     * 跳转到指定的Activity
     *
     * @param flags          intent flags
     * @param targetActivity 要跳转的目标Activity
     */
    protected final void startActivity(int flags, Class<?> targetActivity) {
        final Intent intent = new Intent(this, targetActivity);
        intent.setFlags(flags);
        startActivity(new Intent(this, targetActivity));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        // 6.0系统语言跟随系统语言，7.0（含7.0）以上，APP内部切换语言
        LogUtils.i("当前系统版本 = " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            // 设置语言
            super.attachBaseContext(ContextUtils.attachBaseContext(newBase, WalletApplication.getSetLocale(newBase)));
        } else {
            super.attachBaseContext(newBase);
            String locale = Locale.getDefault().getLanguage();
            // 获取当前的系统语言
            LogUtils.log("当前系统语言 = " + locale);
            if (locale.equals(Locale.CHINESE.getLanguage())) {
                WalletApplication.lang = "";
            } else {
                WalletApplication.lang = Constant.LANG_ENGLISH;
            }
        }
    }


    protected abstract void initView();

}
