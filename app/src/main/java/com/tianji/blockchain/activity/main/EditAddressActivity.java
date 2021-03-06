package com.tianji.blockchain.activity.main;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.activity.sacn.MipcaActivityCapture;
import com.tianji.blockchain.entity.AddressEntity;
import com.tianji.blockchain.sharepreferences.AddressSharedPreferences;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

import java.util.List;

public class EditAddressActivity extends BasicConnectShowActivity implements View.OnClickListener {
    private ImageView img_icon;
    private TextView tv_chain_type;
    private EditText edt_address;
    private RelativeLayout rl_scan;
    private EditText edt_address_name;
    private EditText edt_address_tips;
    private Button btn_delete_address;
    private Button btn_save;

    private int type;

    private AddressEntity addressEntity;
    private int addressBooksPosition;

    private int addressChainType;
    private WalletInfo walletInfo;
    private String saveAddress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
    }

    @Override
    protected void initView() {
        type = getIntent().getIntExtra("_addressType", -1);
        addressBooksPosition = getIntent().getIntExtra("_position", -1);
        walletInfo = (WalletInfo) getIntent().getSerializableExtra("_walletInfo");
        saveAddress = getIntent().getStringExtra("_saveAddress");
        if (AddressSharedPreferences.getInstance(this).getAddressList().size() > 0) {
            if (addressBooksPosition != -1) {
                addressEntity = AddressSharedPreferences.getInstance(this).getAddressList().get(addressBooksPosition);
            }
        }
        img_icon = findViewById(R.id.img_icon);
        tv_chain_type = findViewById(R.id.tv_chain_type);
        edt_address = findViewById(R.id.edt_address);
        rl_scan = findViewById(R.id.rl_scan);
        edt_address_name = findViewById(R.id.edt_address_name);
        edt_address_tips = findViewById(R.id.edt_address_tips);
        btn_delete_address = findViewById(R.id.btn_delete_address);
        btn_save = findViewById(R.id.btn_save);

        mActionBar.setVisibility(View.VISIBLE);
        if (type == Constant.AddressType.TYPE_ADDRESS_CREATE) {
            mActionBar.setActionTitle(getResources().getString(R.string.new_address));
            btn_delete_address.setVisibility(View.GONE);
            img_icon.setImageResource(R.drawable.file_coin_select);
            tv_chain_type.setText(getResources().getString(R.string.file_coin));
            addressChainType = Constant.ChainType.CHAIN_TYPE_FIL;
        } else if (type == Constant.AddressType.TYPE_ADDRESS_EDIT) {
            mActionBar.setActionTitle(getResources().getString(R.string.edit_address));
            btn_delete_address.setVisibility(View.VISIBLE);
            switch (addressEntity.getChainType()) {
                case Constant.ChainType.CHAIN_TYPE_FIL:
                    img_icon.setImageResource(R.drawable.file_coin_select);
                    tv_chain_type.setText(getResources().getString(R.string.file_coin));
                    addressChainType = Constant.ChainType.CHAIN_TYPE_FIL;
                    break;
            }
            edt_address.setText(addressEntity.getAddress());
            edt_address_name.setText(addressEntity.getAddressName());
            edt_address_tips.setText(addressEntity.getAddressTips());
        } else if (type == Constant.AddressType.TYPE_ADDRESS_OPERATE) {
            mActionBar.setActionTitle(getResources().getString(R.string.new_address));
            btn_delete_address.setVisibility(View.GONE);
            switch (walletInfo.getChain()) {
                case FIL:
                    img_icon.setImageResource(R.drawable.file_coin_select);
                    tv_chain_type.setText(getResources().getString(R.string.file_coin));
                    addressChainType = Constant.ChainType.CHAIN_TYPE_FIL;
                    break;
            }
            edt_address.setText(saveAddress);
        }
        ViewCommonUtils.buildBackImageView(this, mActionBar);


        rl_scan.setOnClickListener(this);
        btn_delete_address.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_scan:
                CommonUtils.openScan(this);
                break;
            case R.id.btn_delete_address:
                List<AddressEntity> deleteList = AddressSharedPreferences.getInstance(this).getAddressList();
                deleteList.remove(addressBooksPosition);
                AddressSharedPreferences.getInstance(this).removeAddress(deleteList);
                showToast(R.string.delete_success);
                finish();
                break;
            case R.id.btn_save:
                String addressName = ViewCommonUtils.getEdtString(edt_address_name);
                String address = ViewCommonUtils.getEdtString(edt_address);
                String addressTips = ViewCommonUtils.getEdtString(edt_address_tips);
                if (type == Constant.AddressType.TYPE_ADDRESS_CREATE || type == Constant.AddressType.TYPE_ADDRESS_OPERATE) {
                    if (!addressName.equals("") && !address.equals("")) {
                        switch (addressChainType) {
                            case Constant.ChainType.CHAIN_TYPE_FIL:
                                setAddressSuccess(0, address, addressName, addressTips);
                                break;
                        }
                    } else {
                        showToast(R.string.address_addressname_isempty);
                    }
                } else if (type == Constant.AddressType.TYPE_ADDRESS_EDIT) {
                    if (!addressName.equals("") && !address.equals("")) {
                        List<AddressEntity> list = AddressSharedPreferences.getInstance(this).getAddressList();
                        list.remove(addressBooksPosition);
                        AddressSharedPreferences.getInstance(this).removeAddress(list);

                        switch (addressChainType) {
                            case Constant.ChainType.CHAIN_TYPE_FIL:
                                setAddressSuccess(1, address, addressName, addressTips);
                                break;
                        }
                    } else {
                        showToast(R.string.address_addressname_isempty);
                    }
                }
                break;
        }
    }

    /**
     * 设置地址对象成功
     *
     * @param type
     * @param address
     * @param addressName
     * @param addressTips
     */
    private void setAddressSuccess(int type, String address, String addressName, String addressTips) {
        AddressEntity entity = new AddressEntity();
        entity.setChainType(addressChainType);
        entity.setAddress(address);
        entity.setAddressName(addressName);
        entity.setAddressTips(addressTips);
        AddressSharedPreferences.getInstance(this).saveAddress(entity);
        if (type == 0) {
            showToast(R.string.address_entity_create_success);
        } else {
            showToast(R.string.address_entity_edit_success);
        }
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == this.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    edt_address.setText(bundle.getString("result"));
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case Constant.PermissionsCode.CAMERA_PERMISSIONS_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, MipcaActivityCapture.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, 1);
                } else {
                    LogUtils.log("相机权限申请失败");
                }
                break;
        }
    }
}


