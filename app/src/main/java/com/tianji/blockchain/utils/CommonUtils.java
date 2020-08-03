package com.tianji.blockchain.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.aboutus.LanguageActivity;
import com.tianji.blockchain.activity.home.WelcomeActivity;
import com.tianji.blockchain.activity.importwallet.ImportWalletByMnemonicActivity;
import com.tianji.blockchain.activity.sacn.MipcaActivityCapture;
import com.tianji.blockchain.dialog.SelectDialog;

import java.io.UnsupportedEncodingException;

public class CommonUtils {


    /**
     * 点击收起软键盘
     *
     * @param context
     * @param view
     */
    public static void stopKeyboard(final Context context, final View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });
    }

    /**
     * 点击收起键盘
     *
     * @param activity
     */
    public static void stopKeyBoard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && activity.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow((activity).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void copyContent(Context context, String content) {
        //复制内容到剪切板
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", content);
        cm.setPrimaryClip(mClipData);
        Toast.makeText(context, context.getResources().getString(R.string.copy_success), Toast.LENGTH_SHORT).show();
    }

    /**
     * 重新写入一个walletinfo
     *
     * @param info
     */
    public static WalletInfo writeWalletInfo(WalletInfo info) {
        WalletInfo walletInfo = new WalletInfo();
        walletInfo.setAddress(info.getAddress());
        walletInfo.setChain(info.getChain());
        walletInfo.setCreatedTime(info.getCreatedTime());
        walletInfo.setPasswordTips(info.getPasswordTips());
        walletInfo.setWalletCreatedType(info.getWalletCreatedType());
        walletInfo.setWalletName(info.getWalletName());
        return walletInfo;
    }

    /**
     * 打开扫一扫
     *
     * @param activity
     */
    public static void openScan(Activity activity) {
        //扫一扫
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            CommonUtils.cameraPermission(activity);
        } else {
            Intent intent = new Intent(activity, MipcaActivityCapture.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            activity.startActivityForResult(intent, 1);
        }
    }

    /**
     * 相机权限
     *
     * @param activity
     */
    public static void cameraPermission(Activity activity) {
        if (activity == null) {
            return;
        }
        String[] permission = new String[]{Manifest.permission.CAMERA};

        ActivityCompat.requestPermissions(activity, permission, Constant.PermissionsCode.CAMERA_PERMISSIONS_CODE);
    }

    /**
     * 更改字符串编码
     *
     * @param str
     * @return
     */
    public static String toUtf8(String str) {
        String result = null;
        try {
            result = new String(str.getBytes("UTF-8"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 获取当前app version code
     */
    public static long getAppVersionCode(Context context) {
        long appVersionCode = 0;
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                appVersionCode = packageInfo.getLongVersionCode();
            } else {
                appVersionCode = packageInfo.versionCode;
            }
        } catch (PackageManager.NameNotFoundException e) {
        }
        return appVersionCode;
    }

    /**
     * 获取当前app version name
     */
    public static String getAppVersionName(Context context) {
        String appVersionName = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            appVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return appVersionName;
    }

    /**
     * 格式化助记词
     *
     * @param editText
     * @return
     */
    public static String formatMnemonic(String editText) {
        String[] mneList = editText.split("\\s+");
        if (mneList.length == 12) {
            String mnemonic = "";
            for (int i = 0; i < mneList.length; i++) {
                mnemonic += mneList[i] + " ";
            }
            return mnemonic.trim();
        } else {
            return null;
        }
    }


    /**
     * 字符串转换成十六进制字符串
     *
     * @param str 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String str2HexStr(String str) {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * 十六进制转换字符串
     *
     * @return String 对应的字符串
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }


    public static Uri bitmapToUri(Context context, Bitmap bitmap) {
        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, null, null));
        return uri;
    }


    public static String replaceEnter(String str) {
        String result = str.replace("\n", "");
        return result;
    }

    public static String replaceEnterAndSpace(String str) {
        String result = str.replace("\n", "");
        result = result.replace(" ", "");
        return result;
    }

    /**
     * 获取当前屏幕高度
     *
     * @param activity
     * @return
     */
    public static int getWindowHeight(Activity activity) {
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int x = point.x;
        int y = point.y;

        return y;
    }

    /***显示点击地址的操作弹窗***/
    public static void showOperateSelectDialog(SelectDialog selectDialog, WalletInfo walletInfo, String saveAddress) {
        selectDialog.upDateWalletInfo(walletInfo);
        selectDialog.upDateAddress(saveAddress);
        selectDialog.show();
    }

    /***杀死进程***/
    public static void killApp(Context context) {
        Intent intent = new Intent(context, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        // 杀掉进程
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    /***切换货币显示***/
    public static void setCurrency(TextView textView, String amount, boolean hasBrackets) {
        if (hasBrackets) {
            if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_CNY) {
                textView.setText("(≈¥ " + amount + ")");
            } else if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_USD) {
                textView.setText("(≈$ " + amount + ")");
            }
        } else {
            if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_CNY) {
                textView.setText("≈¥ " + amount);
            } else if (WalletApplication.getApp().getCurrentCurrency() == Constant.CurrencyType.TYPE_USD) {
                textView.setText("≈$ " + amount);
            }
        }

    }
}
