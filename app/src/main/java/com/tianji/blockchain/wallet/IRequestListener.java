package com.tianji.blockchain.wallet;

import com.tianji.blockchain.constant.enums.ResultCode;

public interface IRequestListener<T> {
    void onResult(ResultCode resultCode, T result);
}
