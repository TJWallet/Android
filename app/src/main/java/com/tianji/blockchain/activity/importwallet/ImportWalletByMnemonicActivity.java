package com.tianji.blockchain.activity.importwallet;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianji.blockchainwallet.constant.enums.Chain;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.activity.basic.BasicActionBarActivity;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.activity.createwallet.CreateWalletStepOneActivity;
import com.tianji.blockchain.activity.sacn.MipcaActivityCapture;
import com.tianji.blockchain.adapter.basic.OnItemClickListener;
import com.tianji.blockchain.adapter.gridView.GVAdapterMnemonic;
import com.tianji.blockchain.adapter.recyclerView.RVAdapterMnemonicAssociation;
import com.tianji.blockchain.entity.MnemoinicButtonEntity;
import com.tianji.blockchain.utils.BIP39Dictionary;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;

import java.util.ArrayList;
import java.util.List;

public class ImportWalletByMnemonicActivity extends BasicConnectShowActivity {
    private static final int TYPE_ASS = 0;

    private static final int TYPE_UNFILLED = 0;
    private static final int TYPE_FILLED = 1;
    private Chain chainType;
    private int source;

    private EditText edt_mnemonic;
    private RecyclerView recyclerView;
    private GridView gv_mnemonic;
    private TextView tv_next;
    private TextView tv_progress;
    private int progress;
    private RelativeLayout rl_new_root;

    private GVAdapterMnemonic adapterBtn;
    private RVAdapterMnemonicAssociation adapterMne;

    private List<MnemoinicButtonEntity> btnList = new ArrayList<>();
    private List<String> associationString = new ArrayList<>();

    private String mnemonic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setRightImgPosition(2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_by_mnemonic);
    }

    @Override
    protected void setData() {

    }

    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case TYPE_ASS:
                String content = (String) msg.obj;
                int contentLength = content.length();
                associationString.clear();
                String[] dictionary = BIP39Dictionary.getInstance().getDictionary();
                if (contentLength == 0) {

                } else {
                    for (int i = 0; i < dictionary.length; i++) {
                        try {
                            String str = dictionary[i].substring(0, contentLength);
                            if (str.equals(content)) {
                                associationString.add(dictionary[i]);
                            }
                        } catch (Exception e) {

                        }
                    }
                }
                adapterMne.updateData(associationString);
                break;
        }
    }

    @Override
    protected void initReceiver() {

    }

    @Override
    protected void initView() {
        chainType = (Chain) getIntent().getSerializableExtra("_chainType");
        source = getIntent().getIntExtra("_source", -1);

        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.import_wallet));
        ViewCommonUtils.buildBackImageView(this, mActionBar);
        ImageView img_qrcode = ViewCommonUtils.buildImageView(this, R.drawable.scan);
        mActionBar.addViewToRight(img_qrcode, 1);
        img_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.openScan(ImportWalletByMnemonicActivity.this);
            }
        });


        edt_mnemonic = findViewById(R.id.edt_mnemonic);
        recyclerView = findViewById(R.id.recyclerView);
        rl_new_root = findViewById(R.id.rl_new_root);
        gv_mnemonic = findViewById(R.id.gv_mnemonic);
        tv_next = findViewById(R.id.tv_next);
        tv_progress = findViewById(R.id.tv_progress);
        //点击空白处，收起键盘
        CommonUtils.stopKeyboard(this, rl_new_root);
        //默认输入框获得焦点
        edt_mnemonic.requestFocus();

        progress = 0;
        setProgress();
        for (int i = 0; i < 12; i++) {
            MnemoinicButtonEntity entity = new MnemoinicButtonEntity();
            entity.setTextContent("");
            entity.setType(0);
            if (i == 0) {
                entity.setHasFocus(true);
            } else {
                entity.setHasFocus(false);
            }

            btnList.add(entity);
        }

        adapterBtn = new GVAdapterMnemonic(this, btnList);
        gv_mnemonic.setAdapter(adapterBtn);

        gv_mnemonic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < btnList.size(); i++) {
                    btnList.get(i).setHasFocus(false);

                }
                if (btnList.get(position).getType() == TYPE_FILLED) {
                    edt_mnemonic.setText(btnList.get(position).getTextContent());
                    edt_mnemonic.setSelection(btnList.get(position).getTextContent().length());
                    btnList.get(position).setType(TYPE_UNFILLED);
                    if (progress > 0) {
                        progress -= 1;
                    }
                    if (progress == 12) {
                        tv_next.setBackgroundResource(R.drawable.btn_collect_copy);
                    } else {
                        tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                    }
                    setProgress();
                    btnList.get(position).setTextContent("");
                }
                //找到第一个空位
                for (int i = 0; i < btnList.size(); i++) {
                    if (btnList.get(i).getType() == TYPE_UNFILLED) {
                        btnList.get(i).setHasFocus(true);
                        break;
                    }
                }

                adapterBtn.notifyDataSetChanged();
            }
        });

        adapterMne = new RVAdapterMnemonicAssociation(this, associationString);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        adapterMne.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object obj) {
                String content = (String) obj;
                CommonUtils.stopKeyBoard(ImportWalletByMnemonicActivity.this);

                for (int i = 0; i < btnList.size(); i++) {
                    if (btnList.get(i).getType() == TYPE_UNFILLED) {
                        btnList.get(i).setTextContent(content);
                        btnList.get(i).setType(TYPE_FILLED);
                        if (progress < 13) {
                            progress += 1;
                        }
                        if (progress == 12) {
                            tv_next.setBackgroundResource(R.drawable.btn_collect_copy);
                        } else {
                            tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                        }
                        setProgress();
                        btnList.get(i).setHasFocus(false);
                        edt_mnemonic.setText("");
                        for (int j = i; j < btnList.size(); j++) {
                            if (j < btnList.size() - 1) {
                                if (btnList.get(j + 1).getType() == TYPE_UNFILLED) {
                                    btnList.get(j + 1).setHasFocus(true);
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
                adapterBtn.notifyDataSetChanged();
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterMne);

        edt_mnemonic.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mHandler.removeMessages(TYPE_ASS);
                if (s.toString().length() == 1) return;
                if (s.toString().contains("\n") || s.toString().contains(" ")) return;

                Message message = Message.obtain();
                message.what = TYPE_ASS;
                message.obj = s.toString();
                mHandler.sendMessageDelayed(message, 300);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isOver = true;
                mnemonic = "";
                for (int i = 0; i < btnList.size(); i++) {
//                    LogUtils.log("12个列表中词语是 == " + btnList.get(i).getTextContent());
                    if (btnList.get(i).getType() == TYPE_UNFILLED) {
                        isOver = false;
                        break;
                    } else {
                        mnemonic += btnList.get(i).getTextContent() + " ";
                    }

                }
                if (isOver) {
                    Intent createIntent = new Intent(ImportWalletByMnemonicActivity.this, CreateWalletStepOneActivity.class);
                    createIntent.putExtra("_chainType", chainType);
                    createIntent.putExtra("_source", source);
                    createIntent.putExtra("_addSource", Constant.AddWalletSource.ADD_WALLET_BY_IMPORT_MNEMONIC);
                    createIntent.putExtra("_mnemonic", mnemonic.trim());
//                    LogUtils.log("传过去的助记词是 == " + mnemonic.trim());
                    startActivity(createIntent);
                } else {
                    LogUtils.log(className + " -- 助记词没有输入完整");
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    LogUtils.log("首页扫描内容" + bundle.getString("result"));
                    String result = bundle.getString("result");
                    String[] strings = result.split(" ");
                    if (strings.length == 12) {
                        for (int i = 0; i < btnList.size(); i++) {
                            btnList.get(i).setTextContent(strings[i]);
                            btnList.get(i).setType(TYPE_FILLED);
                            btnList.get(i).setHasFocus(false);
                        }
                        adapterBtn.notifyDataSetChanged();
                        progress = 12;
                        setProgress();
                        tv_next.setBackgroundResource(R.drawable.btn_collect_copy);
                    } else {
                        tv_next.setBackgroundResource(R.drawable.radius_btn_noenabled);
                        showToast(R.string.mnemonic_format_error);
                        LogUtils.log("助记词格式不对");
                    }
                }
                break;
        }
    }

    private void setProgress() {
        tv_progress.setText("(" + progress + "/12)");
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
                    LogUtils.logError("相机权限申请失败");
                }
                break;
        }
    }
}
