package com.tianji.blockchain.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import androidx.annotation.Nullable;

import android.view.View;
import android.widget.*;

import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicActionBarActivity;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.entity.AddressEntity;
import com.tianji.blockchain.sharepreferences.AddressSharedPreferences;
import com.tianji.blockchain.utils.ViewCommonUtils;

public class AddressDetailsActivity extends BasicConnectShowActivity implements View.OnClickListener {
    private ImageView img_icon;
    private TextView tv_chain_type;
    private TextView tv_address;
    private TextView tv_address_name;
    private TextView tv_address_tips;
    private Button btn_edit_address;
    private AddressEntity entity;
    private int addressBooksPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_details);
    }


    @Override
    protected void initView() {
        addressBooksPosition = getIntent().getIntExtra("_position", -1);
        entity = AddressSharedPreferences.getInstance(this).getAddressList().get(addressBooksPosition);
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.address_details));
        ViewCommonUtils.buildBackImageView(this, mActionBar);

        img_icon = findViewById(R.id.img_icon);
        tv_chain_type = findViewById(R.id.tv_chain_type);
        tv_address = findViewById(R.id.tv_address);
        tv_address_name = findViewById(R.id.tv_address_name);
        tv_address_tips = findViewById(R.id.tv_address_tips);
        btn_edit_address = findViewById(R.id.btn_edit_address);

        btn_edit_address.setOnClickListener(this);

        switch (entity.getChainType()) {
            case Constant.ChainType.CHAIN_TYPE_ETH:
                img_icon.setImageResource(R.drawable.eth_icon_selected);
                tv_chain_type.setText(getResources().getString(R.string.eth));
                break;
            case Constant.ChainType.CHAIN_TYPE_ACL:
                img_icon.setImageResource(R.drawable.acl_icon_select);
                tv_chain_type.setText(getResources().getString(R.string.acl));
                break;
            case Constant.ChainType.CHAIN_TYPE_BTC:
                img_icon.setImageResource(R.drawable.btc);
                tv_chain_type.setText(getResources().getString(R.string.btc));
                break;
        }
        tv_address.setText(entity.getAddress());
        tv_address_name.setText(entity.getAddressName());
        tv_address_tips.setText(entity.getAddressTips());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_edit_address:
                Intent intent = new Intent(this, EditAddressActivity.class);
                intent.putExtra("_addressType", Constant.AddressType.TYPE_ADDRESS_EDIT);
                intent.putExtra("_position", addressBooksPosition);
                startActivity(intent);
                finish();
                break;
        }
    }
}
