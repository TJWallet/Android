package com.tianji.blockchain.broadcast;

import com.tianji.blockchain.utils.NetUtils;

public interface NetChangeListener {
    void onNetChange(NetUtils.NetworkStatus status);
}
