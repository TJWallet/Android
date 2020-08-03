package com.tianji.blockchain.utils;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

/**
 * 指纹相关方法 xgp
 */
public class FingerPrintUtils {
    private static FingerPrintUtils instance;
    private Context context;
    private FingerprintManager manager;
    private KeyguardManager mKeyManager;
    private CancellationSignal mCancellationSignal = new CancellationSignal();

    private FingerPrintUtils(Context context) {
        this.context = context;
        manager = (FingerprintManager) context.getSystemService(Context.FINGERPRINT_SERVICE);
        mKeyManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
    }

    public static FingerPrintUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (FingerPrintUtils.class) {
                if (instance == null) {
                    instance = new FingerPrintUtils(context);
                }
            }
        }
        return instance;
    }

    public boolean isFinger() {
        if (!manager.isHardwareDetected()) {
            Toast.makeText(context, "没有指纹识别模块", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!mKeyManager.isKeyguardSecure()) {
            Toast.makeText(context, "没有开启锁屏密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!manager.hasEnrolledFingerprints()) {
            Toast.makeText(context, "没有录入指纹", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void startCheckFingerPrint(FingerprintManager.CryptoObject cryptoObject, FingerprintManager.AuthenticationCallback callback) {
        //android studio 上，没有这个会报错
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "没有指纹识别权限", Toast.LENGTH_SHORT).show();
            return;
        }
        manager.authenticate(null, mCancellationSignal, 0, callback, null);
    }

    public KeyguardManager getKeyManager() {
        if (mKeyManager != null) {
            return mKeyManager;
        }
        return null;
    }

//    private void showAuthenticationScreen() {
//        KeyguardManager mKeyManager = FingerPrintUtils.getInstance(this).getKeyManager();
//        Intent intent = mKeyManager.createConfirmDeviceCredentialIntent("finger", "测试指纹识别");
//        if (intent != null) {
//            startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS) {
//            // Challenge completed, proceed with using cipher
//            if (resultCode == RESULT_OK) {
//                Toast.makeText(this, "识别成功", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "识别失败", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }


//     if (FingerPrintUtils.getInstance(MainActivity.this).isFinger()) {
//        FingerPrintUtils.getInstance(MainActivity.this).startCheckFingerPrint(null, new FingerprintManager.AuthenticationCallback() {
//            @Override
//            public void onAuthenticationError(int errorCode, CharSequence errString) {
//                //但多次指纹密码验证错误后，进入此方法；并且，不能短时间内调用指纹验证
//                Toast.makeText(MainActivity.this, errString, Toast.LENGTH_SHORT).show();
//                showAuthenticationScreen();
//            }
//
//            @Override
//            public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
//                Toast.makeText(MainActivity.this, helpString, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
//                Toast.makeText(MainActivity.this, "指纹识别成功", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAuthenticationFailed() {
//                Toast.makeText(MainActivity.this, "指纹识别失败", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

}
