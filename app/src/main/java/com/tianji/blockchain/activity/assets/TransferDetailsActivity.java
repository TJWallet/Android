package com.tianji.blockchain.activity.assets;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import androidx.annotation.Nullable;

import android.widget.*;

import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.basic.BasicMvpInterface;
import com.tianji.blockchain.dialog.SelectDialog;
import com.tianji.blockchain.dialog.TipsDialog;
import com.tianji.blockchain.entity.AddressEntity;
import com.tianji.blockchain.entity.DialogEntity;
import com.tianji.blockchain.entity.TransferRecode;
import com.tianji.blockchain.entity.TransferWaitEntity;
import com.tianji.blockchain.sharepreferences.AddressSharedPreferences;
import com.tianji.blockchain.sharepreferences.TransferWaitSharedPreferences;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.CompanyUtil;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.MathUtils;
import com.tianji.blockchain.utils.QrCodeUtils;
import com.tianji.blockchain.utils.TimeUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class TransferDetailsActivity extends BasicConnectShowActivity implements View.OnClickListener {
    private TransferRecode transferRecode;
    private String transferCoinName;
    private int transferType;

    private TextView tv_amount;
    private TextView tv_coin_name;
    private ImageView img_transfer_type;
    private TextView tv_transfer_type;
    private TextView tv_miner_fee;
    private TextView tv_pay_address;
    private TextView tv_collect_address;
    private TextView tv_transfer_time;
    //    private TextView tv_remarks;
    private TextView tv_transfer_hash;
    private TextView tv_block;
    private ImageView img_qrcode;
    private TextView tv_to_check;
    private TextView tv_check_info;
    private TextView tv_from_wallet_name;
    private TextView tv_to_wallet_name;
    private TextView tv_miner_fee_value;

    private WalletInfo walletInfo;
    private int decimal = 18;


    private SelectDialog selectDialog;
    private TipsDialog transferErrorTDialog;
    private TipsDialog deleteTransferRecordDialog;


    private double coinView = -1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_details);
    }

    @Override
    protected void setData() {
    }

    @Override
    protected void handleMsg(Message msg) {

    }

    @Override
    protected void initView() {
        transferCoinName = getIntent().getStringExtra("_transferCoinName");
        transferType = getIntent().getIntExtra("_transferType", -1);
        transferRecode = (TransferRecode) getIntent().getSerializableExtra("_transferRecode");
        walletInfo = (WalletInfo) getIntent().getSerializableExtra("_walletInfo");
        if (walletInfo == null) {
            walletInfo = WalletApplication.getCurrentWallet();
        }

        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.transfer_details));

        if (transferType == 4) {
            ImageView img_delete = ViewCommonUtils.buildImageView(this, R.drawable.delete_icon);
            mActionBar.addViewToRight(img_delete, 1);
            img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteTransferRecordDialog != null) {
                        deleteTransferRecordDialog.show();
                    }
                }
            });
        }
        ViewCommonUtils.buildBackImageView(this, mActionBar);

        tv_amount = findViewById(R.id.tv_amount);
        tv_coin_name = findViewById(R.id.tv_coin_name);
        img_transfer_type = findViewById(R.id.img_transfer_type);
        tv_transfer_type = findViewById(R.id.tv_transfer_type);
        tv_miner_fee = findViewById(R.id.tv_miner_fee);
        tv_pay_address = findViewById(R.id.tv_pay_address);
        tv_collect_address = findViewById(R.id.tv_collect_address);
        tv_transfer_time = findViewById(R.id.tv_transfer_time);
        tv_transfer_hash = findViewById(R.id.tv_transfer_hash);
//        tv_remarks = findViewById(R.id.tv_remarks);
        tv_block = findViewById(R.id.tv_block);
        img_qrcode = findViewById(R.id.img_qrcode);
        tv_to_check = findViewById(R.id.tv_to_check);
        tv_check_info = findViewById(R.id.tv_check_info);
        tv_from_wallet_name = findViewById(R.id.tv_from_wallet_name);
        tv_to_wallet_name = findViewById(R.id.tv_to_wallet_name);
        tv_miner_fee_value = findViewById(R.id.tv_miner_fee_value);

        tv_pay_address.setOnClickListener(this);
        tv_collect_address.setOnClickListener(this);
        tv_transfer_hash.setOnClickListener(this);
        tv_to_check.setOnClickListener(this);
        tv_check_info.setOnClickListener(this);

        String[] strArray = getResources().getStringArray(R.array.select_address);
        selectDialog = new SelectDialog(this, R.style.Wallet_Manager_Dialog, strArray, getResources().getString(R.string.operate));
        DialogEntity transferErrorEntity = new DialogEntity(getResources().getString(R.string.transfer_maybe_has_error), getResources().getString(R.string.transfer_maybe_has_error_desc5), getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (transferErrorTDialog != null) {
                    if (transferErrorTDialog.isShowing()) {
                        transferErrorTDialog.dismiss();
                    }
                }
            }
        });
        transferErrorTDialog = new TipsDialog(this, transferErrorEntity, false);

        DialogEntity deleteRecordEntity = new DialogEntity(getResources().getString(R.string.delete_transfer_record_title), getResources().getString(R.string.delete_transfer_record_desc), getResources().getString(R.string.ok), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferWaitSharedPreferences.getInstance(TransferDetailsActivity.this).removeRecord(walletInfo.getAddress(), transferCoinName, transferRecode.getHash());
                TransferDetailsActivity.this.finish();
            }
        });
        deleteTransferRecordDialog = new TipsDialog(this, deleteRecordEntity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (transferType) {
            case 1:
                img_transfer_type.setImageResource(R.drawable.transfer_success);
                tv_transfer_type.setText(getResources().getString(R.string.transfer_success));
                tv_transfer_type.setTextColor(getResources().getColor(R.color.textMajor));
                ViewCommonUtils.setTextViewUnderLine(tv_transfer_type, false);
//                tv_remarks.setText(CommonUtils.hexStr2Str(transferRecode.getInput().substring(1)));
                break;
            case 2:
                img_transfer_type.setImageResource(R.drawable.transfer_failed);
                tv_transfer_type.setText(getResources().getString(R.string.transfer_fail));
                tv_transfer_type.setTextColor(getResources().getColor(R.color.textMajor));
                ViewCommonUtils.setTextViewUnderLine(tv_transfer_type, false);
//                tv_remarks.setText(CommonUtils.hexStr2Str(transferRecode.getInput().substring(1)));
                break;
            case 3:
                img_transfer_type.setImageResource(R.drawable.transfer_wait);
                tv_transfer_type.setText(getResources().getString(R.string.transfer_wait));
                tv_transfer_type.setTextColor(getResources().getColor(R.color.textMajor));
                ViewCommonUtils.setTextViewUnderLine(tv_transfer_type, false);
//                tv_remarks.setText(transferRecode.getInput());
                //添加交易等待记录
                if (!transferRecode.isWait()) {
                    transferRecode.setWait(true);
                    TransferWaitEntity entity = new TransferWaitEntity();
                    entity.setCoinName(transferCoinName);
                    entity.setTransferRecode(transferRecode);
                    entity.setStatus(0);
                    LogUtils.log(className + " -- 添加交易等待记录");
                    TransferWaitSharedPreferences.getInstance(this).addTransferWait(walletInfo.getAddress(), entity);
                }
                tv_transfer_time.setText("");
                break;
            case 4:
                img_transfer_type.setImageResource(R.drawable.maybe_error);
                ViewCommonUtils.setTextViewUnderLine(tv_transfer_type, true);
                tv_transfer_type.setTextColor(getResources().getColor(R.color.warning_red));
                tv_transfer_type.setText(getResources().getString(R.string.transfer_maybe_has_error));
                tv_transfer_time.setText("");
                tv_transfer_type.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        transferErrorTDialog.show();
                    }
                });
                break;
        }


        tv_coin_name.setText(transferCoinName);

        String gasPrice = transferRecode.getGasPrice();
        if (transferRecode.getTokenDecimal() != null) {
            decimal = Integer.parseInt(transferRecode.getTokenDecimal());
        }
        tv_to_check.setVisibility(View.VISIBLE);
        tv_to_check.setText(R.string.to_check_filecoin);
        tv_miner_fee.setText(MathUtils.getETHUsedFee(transferRecode.getGasPrice(), transferRecode.getGas()) + " FIL");
        tv_amount.setText(MathUtils.doubleKeep4(new BigDecimal(transferRecode.getValue()).divide(new BigDecimal(Math.pow(10, decimal))).doubleValue()));
        tv_check_info.setVisibility(View.GONE);

        /***设置付款地址***/
        if (walletInfo.getAddress().equals(transferRecode.getFrom())) {
//            tv_pay_address.setText(transferRecode.getFrom() + "(" + walletInfo.getWalletName() + ")");
            tv_from_wallet_name.setVisibility(View.VISIBLE);
            tv_pay_address.setText(transferRecode.getFrom());
            tv_from_wallet_name.setText("(" + walletInfo.getWalletName() + ")");
        } else {
            List<AddressEntity> addressEntityList = AddressSharedPreferences.getInstance(this).getAddressList();
            if (addressEntityList.size() > 0) {
                for (int i = 0; i < addressEntityList.size(); i++) {
                    if (transferRecode.getFrom().equals(addressEntityList.get(i).getAddress())) {
                        tv_from_wallet_name.setVisibility(View.VISIBLE);
                        tv_pay_address.setText(transferRecode.getFrom());
                        tv_from_wallet_name.setText("(" + addressEntityList.get(i).getAddressName() + ")");
                        break;
                    } else {
                        tv_from_wallet_name.setVisibility(View.GONE);
                        tv_pay_address.setText(transferRecode.getFrom());
                    }
                }
            } else {
                tv_from_wallet_name.setVisibility(View.GONE);
                tv_pay_address.setText(transferRecode.getFrom());
            }

        }
        /***设置收款地址***/
        if (walletInfo.getAddress().equals(transferRecode.getTo())) {
//            tv_collect_address.setText(transferRecode.getTo() + "(" + walletInfo.getWalletName() + ")");
            tv_to_wallet_name.setVisibility(View.VISIBLE);
            tv_collect_address.setText(transferRecode.getTo());
            tv_to_wallet_name.setText("(" + walletInfo.getWalletName() + ")");
        } else {
            List<AddressEntity> addressEntityList = AddressSharedPreferences.getInstance(this).getAddressList();
            if (addressEntityList.size() > 0) {
                for (int i = 0; i < addressEntityList.size(); i++) {
                    if (transferRecode.getTo().equals(addressEntityList.get(i).getAddress())) {
                        tv_to_wallet_name.setVisibility(View.VISIBLE);
                        tv_collect_address.setText(transferRecode.getTo());
                        tv_to_wallet_name.setText("(" + addressEntityList.get(i).getAddressName() + ")");
                        break;
                    } else {
                        tv_to_wallet_name.setVisibility(View.GONE);
                        tv_collect_address.setText(transferRecode.getTo());
                    }
                }
            } else {
                tv_to_wallet_name.setVisibility(View.GONE);
                tv_collect_address.setText(transferRecode.getTo());
            }
        }

        if (transferType != 3 && transferType != 4) {
            tv_transfer_time.setText(TimeUtils.timeStamp2Date(transferRecode.getTimestamp() * 1000, "yyyy-MM-dd HH:mm"));
        }
        tv_transfer_hash.setText(transferRecode.getHash());
        tv_block.setText(transferRecode.getBlockNumber());
        Bitmap bit = QrCodeUtils.createSimpleQRCodeBitmap(transferRecode.getHash(), CompanyUtil.dip2px(this, 106f), CompanyUtil.dip2px(this, 106f), "UTF-8", "H", "2", Color.BLACK, Color.WHITE);
        img_qrcode.setImageBitmap(bit);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_pay_address:
                String fromAddress = tv_pay_address.getText().toString().trim();
                CommonUtils.showOperateSelectDialog(selectDialog, walletInfo, fromAddress);
                break;
            case R.id.tv_collect_address:
                String toAddress = tv_collect_address.getText().toString().trim();
                CommonUtils.showOperateSelectDialog(selectDialog, walletInfo, toAddress);
                break;
            case R.id.tv_transfer_hash:
                String hash = tv_transfer_hash.getText().toString().trim();
                CommonUtils.copyContent(this, hash);
                break;
            case R.id.tv_to_check:
                String filecoinHash = tv_transfer_hash.getText().toString().trim();
                String url = "https://filscan.io/#/message/detail?cid=" + filecoinHash;
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
        }
    }


}
