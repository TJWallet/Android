package com.tianji.blockchain.restful.cache;

import com.tianji.blockchainwallet.constant.enums.ResultCode;

public interface OnLoadDiskLruCacheListener {
    void load(ResultCode resultCode, String result);
}
