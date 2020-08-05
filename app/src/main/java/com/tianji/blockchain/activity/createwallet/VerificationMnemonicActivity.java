package com.tianji.blockchain.activity.createwallet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.adapter.gridView.GVAdapterMnemonic;
import com.tianji.blockchain.adapter.gridView.GVAdapterSimpleText;
import com.tianji.blockchain.entity.MnemoinicButtonEntity;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VerificationMnemonicActivity extends BasicConnectShowActivity {
    private static final int TYPE_UNFILLED = 0;
    private static final int TYPE_FILLED = 1;

    private GridView gv_mnemonic_one, gv_mnemonic_two;
    private TextView tv_next;
    private GVAdapterMnemonic adapterBtn;
    private GVAdapterSimpleText adapterText;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String mnemonic;
    private List<MnemoinicButtonEntity> btnList = new ArrayList<>();
    private List<MnemonicSelectorEntity> textList = new ArrayList<>();
    private int pageType;

    private WalletInfo walletInfo;
    private boolean createLock = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_mnemonic);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
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
    protected void onResume() {
        super.onResume();
        createLock = true;
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void initView() {
        mnemonic = getIntent().getStringExtra("_mnemonic");
        pageType = getIntent().getIntExtra("_pageType", -1);
        walletInfo = (WalletInfo) getIntent().getSerializableExtra("_walletInfo");
        if (walletInfo != null) {
            LogUtils.log(className + "-- getIntent 钱包地址是" + walletInfo.getAddress());
        }

        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.verification_mnemonic));
        ViewCommonUtils.buildBackImageView(this, mActionBar);

        gv_mnemonic_one = findViewById(R.id.gv_mnemonic_one);
        gv_mnemonic_two = findViewById(R.id.gv_mnemonic_two);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setEnabled(false);
        tv_next = findViewById(R.id.tv_next);

        if (!mnemonic.equals("")) {
            String[] mnemonics = mnemonic.split(" ");
            LogUtils.log("助记词的长度是" + mnemonics.length);
            btnList.clear();
            textList.clear();
            for (int i = 0; i < mnemonics.length; i++) {
                MnemoinicButtonEntity entity = new MnemoinicButtonEntity();
                entity.setTextContent("");
                entity.setType(0);
                btnList.add(entity);
            }
            for (int i = 0; i < mnemonics.length; i++) {
                MnemonicSelectorEntity entity = new MnemonicSelectorEntity();
                entity.setMnemonic(mnemonics[i]);
                entity.setSelected(false);
                textList.add(entity);
            }
            //随机打乱集合内容顺序
            Collections.shuffle(textList);

            adapterBtn = new GVAdapterMnemonic(this, btnList);
            adapterText = new GVAdapterSimpleText(this, textList);
            gv_mnemonic_one.setAdapter(adapterBtn);
            gv_mnemonic_two.setAdapter(adapterText);
            gv_mnemonic_two.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    if (!textList.get(position).isSelected) {
                        for (int i = 0; i < btnList.size(); i++) {
                            if (btnList.get(i).getType() == TYPE_UNFILLED) {
                                TextView textView = (TextView) view;
                                btnList.get(i).setTextContent(textList.get(position).getMnemonic());
                                btnList.get(i).setType(TYPE_FILLED);
                                textList.get(position).setSelected(true);

                                textView.setTextColor(getResources().getColor(R.color.tertiary));
                                break;
                            }
                        }
                        checkNextShow();
                        adapterBtn.notifyDataSetChanged();
                        adapterText.notifyDataSetChanged();
                    }
                }
            });

            gv_mnemonic_one.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (btnList.get(position).getType() == TYPE_FILLED) {
                        for (int i = 0; i < textList.size(); i++) {
                            if (btnList.get(position).getTextContent().equals(textList.get(i).getMnemonic())) {
                                textList.get(i).setSelected(false);
                                break;
                            }
                        }
                        btnList.get(position).setType(TYPE_UNFILLED);
                        btnList.get(position).setTextContent("");
                        checkNextShow();
                    }
                    adapterBtn.notifyDataSetChanged();
                    adapterText.notifyDataSetChanged();
                }
            });
        }


        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!createLock) return;
                createLock = false;
                swipeRefreshLayout.setRefreshing(true);
                String verification = "";
                for (int i = 0; i < btnList.size(); i++) {
                    if (i == btnList.size() - 1) {
                        verification += btnList.get(i).getTextContent();
                    } else {
                        verification += btnList.get(i).getTextContent() + " ";
                    }
                }
                if (verification.equals(mnemonic)) {
                    LogUtils.log(className + "-- 助记词验证成功");
                    Intent intent = new Intent(VerificationMnemonicActivity.this, VerificationSuccessActivity.class);
                    intent.putExtra("_walletInfo", walletInfo);
                    intent.putExtra("_pageType", pageType);
                    startActivity(intent);
                } else {
                    showToast(R.string.mnemonic_wrong);
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    LogUtils.log(className + "-- 获取到的助记词" + mnemonic);
                    LogUtils.log(className + "-- 拼接的助记词" + verification);
                    createLock = true;
                }
            }
        });
    }


    public class MnemonicSelectorEntity {
        private String mnemonic;
        private boolean isSelected;

        public String getMnemonic() {
            return mnemonic;
        }

        public void setMnemonic(String mnemonic) {
            this.mnemonic = mnemonic;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            this.isSelected = selected;
        }
    }

    private void checkNextShow() {
        boolean canNext = false;
        for (int i = 0; i < btnList.size(); i++) {
            if (btnList.get(i).getType() == TYPE_FILLED) {
                canNext = true;
            } else {
                canNext = false;
                break;
            }
        }
        if (canNext) {
            tv_next.setBackgroundResource(R.drawable.btn_collect_copy);
        } else {
            tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
        }
    }
}
