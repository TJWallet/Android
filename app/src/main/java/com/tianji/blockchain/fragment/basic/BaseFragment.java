package com.tianji.blockchain.fragment.basic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tianji.blockchainwallet.WalletManager;
import com.tianji.blockchainwallet.constant.Constants;
import com.tianji.blockchainwallet.usb.IUsbAttachListener;
import com.tianji.blockchainwallet.wallet.IRequestListener;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.basic.BasicAppActivity;
import com.tianji.blockchain.activity.basic.UsbCallbackListener;
import com.tianji.blockchain.basic.BasicPresenter;
import com.tianji.blockchain.utils.LogUtils;

import java.util.Locale;

public abstract class BaseFragment extends Fragment {
    protected Handler mHandler;
    private View rootView;
    protected BasicPresenter presenter;
    protected String className;
    /**
     * UBS标识
     */
    protected boolean usbIsConnected;
    protected boolean usbConnect;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        className = getActivity().getLocalClassName();
        rootView = inflater.inflate(setLayoutResouceId(), container, false);
        setHandler();
        initView();
        initReceiver();
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @SuppressWarnings("unchecked")
    protected <T extends View> T findViewById(int id) {
        if (rootView == null) {
            return null;
        }

        return (T) rootView.findViewById(id);
    }

    /**
     * 设置根布局资源id
     *
     * @return
     * @author 漆可
     * @date 2016-5-26 下午3:57:09
     */
    protected abstract int setLayoutResouceId();

    protected void setHandler() {
        if (mHandler != null) {
            return;
        }
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                handleMsg(msg);
            }
        };
    }

    protected View getRootView() {
        return rootView;
    }

    /**
     * 子类需要实现的抽象方法
     *
     * @param msg
     */
    protected abstract void handleMsg(Message msg);

    protected abstract void initView();

    protected abstract void setData();

    protected abstract void initReceiver();


    /**
     * 弹出toast提示
     */
    protected void showToast(String msg) {
        Toast.makeText(WalletApplication.getApp(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 弹出toast提示
     */
    protected void showToast(int strId) {
        Toast.makeText(getActivity(), getString(strId), Toast.LENGTH_SHORT).show();
    }

    /**
     * 跳转到指定的Activity
     *
     * @param targetActivity 要跳转的目标Activity
     */
    protected final void startActivity(Class<?> targetActivity) {
        startActivity(new Intent(getActivity(), targetActivity));
    }

    /**
     * 跳转到指定的Activity
     *
     * @param flags          intent flags
     * @param targetActivity 要跳转的目标Activity
     */
    protected final void startActivity(int flags, Class<?> targetActivity) {
        final Intent intent = new Intent(getActivity(), targetActivity);
        intent.setFlags(flags);
        startActivity(new Intent(WalletApplication.getApp(), targetActivity));
    }
}
