package com.tianji.blockchain.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.aboutus.AboutUsActivity;
import com.tianji.blockchain.activity.aboutus.AboutUsPresenter;
import com.tianji.blockchain.activity.aboutus.HelpCenterActivity;
import com.tianji.blockchain.activity.aboutus.SettingActivity;
import com.tianji.blockchain.activity.main.AddressBookActivity;
import com.tianji.blockchain.activity.main.WalletManagerActivity;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.dialog.UpDateDialog;
import com.tianji.blockchain.entity.UpdateEntity;
import com.tianji.blockchain.fragment.basic.BaseFragment;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.CompanyUtil;
import com.tianji.blockchain.view.ActionBar;

import java.util.HashMap;
import java.util.Map;

import jnr.ffi.annotations.In;

public class MainFragment extends BaseFragment implements View.OnClickListener, BasicMvpInterface {
    private ViewGroup root_view;
    private ActionBar mActionBar;
    private RelativeLayout rl_wallet_manager, rl_address_book, rl_help_center, rl_about_as, rl_setting;
    private View view_check_update;

    @Override
    protected int setLayoutResouceId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionBar = new ActionBar(getActivity());
        mActionBar.setVisibility(View.GONE);
    }

    @Override
    protected void initView() {
        //设置actionbar
        root_view = findViewById(R.id.root_view);
        ViewGroup.LayoutParams LP = new ViewGroup.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, CompanyUtil.dip2px(getActivity(), 61f));
        mActionBar.setLayoutParams(LP);
        root_view.addView(mActionBar);
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.main));
        //点击空白处，收起键盘
        CommonUtils.stopKeyboard(getActivity(), root_view);

        rl_wallet_manager = findViewById(R.id.rl_wallet_manager);
        rl_address_book = findViewById(R.id.rl_address_book);
        rl_setting = findViewById(R.id.rl_setting);
        rl_help_center = findViewById(R.id.rl_help_center);
        rl_about_as = findViewById(R.id.rl_about_as);
        view_check_update = findViewById(R.id.view_check_update);

        rl_wallet_manager.setOnClickListener(this);
        rl_address_book.setOnClickListener(this);
        rl_setting.setOnClickListener(this);
        rl_help_center.setOnClickListener(this);
        rl_about_as.setOnClickListener(this);
        setData();
    }

    @Override
    protected void setData() {
        if (presenter == null) {
            presenter = new MainFragmentPresenter(getActivity(), this);
        }
        Map<String, String> params = new HashMap<>();
        presenter.getData(params);
    }

    @Override
    protected void initReceiver() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_wallet_manager:
                startActivity(WalletManagerActivity.class);
                break;
            case R.id.rl_address_book:
                Intent intent = new Intent(WalletApplication.getApp(), AddressBookActivity.class);
                intent.putExtra("_address_book_mark", Constant.AddressBookMark.TYPE_MAIN);
                startActivity(intent);
                break;
            case R.id.rl_setting:
                startActivity(new Intent(WalletApplication.getApp(), SettingActivity.class));
                break;
            case R.id.rl_help_center:
                startActivity(HelpCenterActivity.class);
                break;
            case R.id.rl_about_as:
                startActivity(AboutUsActivity.class);
                break;
        }
    }

    @Override
    public void getDataSuccess(Object object, int type) {
        UpdateEntity entity = (UpdateEntity) object;
        long code = CommonUtils.getAppVersionCode(getActivity());
        if (code < entity.getVersionCode()) {
            view_check_update.setVisibility(View.VISIBLE);
        } else {
            view_check_update.setVisibility(View.GONE);
        }
    }

    @Override
    public void getDataFail(Object error, int type) {

    }

    @Override
    public void getDataFail(int errCode, String errMsg, int type) {

    }
}
