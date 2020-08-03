package com.tianji.blockchain.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {
    public static NetworkStatus getNetWorkState(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NetworkStatus.NETWORK_WIFI;
            } else if (networkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NetworkStatus.NETWORK_MOBILE;
            }
        }
        return NetworkStatus.NETWORK_NONE;
    }

    public enum NetworkStatus {
        NETWORK_NONE, NETWORK_MOBILE, NETWORK_WIFI
    }


}
