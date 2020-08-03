package com.tianji.blockchain.activity.createwallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class SelectWalletTypeActivity extends BasicConnectShowActivity implements View.OnClickListener {
    private RelativeLayout rl_filecoin;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_wallet_type);
    }

    @Override
    protected void setData() {

    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void initReceiver() {

    }

    @Override
    protected void initView() {
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.select_wallet_chain));
        ViewCommonUtils.buildBackImageView(this, mActionBar);
        rl_filecoin = findViewById(R.id.rl_filecoin);
    }

    @Override
    public void onClick(View v) {
        Intent createIntent = new Intent(this, SelectAddWalletTypeActivity.class);
        switch (v.getId()) {
            case R.id.rl_filecoin:
                createIntent.putExtra("_chainType", Chain.FILECOIN);
                startActivity(createIntent);
                break;
        }
    }
}
