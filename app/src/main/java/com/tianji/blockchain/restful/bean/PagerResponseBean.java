package com.tianji.blockchain.restful.bean;

public class PagerResponseBean<T> {
    private int Status;
    private String ErrMsg;
    private PagerBean<T> Result;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String errMsg) {
        ErrMsg = errMsg;
    }

    public PagerBean<T> getResult() {
        return Result;
    }

    public void setResult(PagerBean<T> result) {
        Result = result;
    }
}
