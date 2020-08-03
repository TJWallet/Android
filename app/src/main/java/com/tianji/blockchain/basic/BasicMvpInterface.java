package com.tianji.blockchain.basic;



public interface BasicMvpInterface {
    void getDataSuccess(Object object, int type);

    void getDataFail(Object error, int type);

    void getDataFail(int errCode, String errMsg, int type);
}
