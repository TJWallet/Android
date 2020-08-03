package com.tianji.blockchain.activity.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tianji.blockchainwallet.WalletManager;
import com.tianji.blockchainwallet.constant.enums.ResultCode;
import com.tianji.blockchainwallet.constant.enums.WalletCreatedType;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchainwallet.wallet.IRequestListener;
import com.tianji.blockchain.Constant;
import com.tianji.blockchain.R;
import com.tianji.blockchain.WalletApplication;
import com.tianji.blockchain.activity.basic.BasicConnectShowActivity;
import com.tianji.blockchain.activity.createwallet.BackUpMnemonicActivity;
import com.tianji.blockchain.activity.home.HomeActivity;
import com.tianji.blockchain.dialog.EditNameDialog;
import com.tianji.blockchain.dialog.EditWalletNameDialog;
import com.tianji.blockchain.dialog.PasswordDialog;
import com.tianji.blockchain.dialog.SelectDialog;
import com.tianji.blockchain.dialog.ShowContentDialog;
import com.tianji.blockchain.dialog.TipsDialog;
import com.tianji.blockchain.entity.DialogEntity;
import com.tianji.blockchain.sharepreferences.CurrentWalletSharedPreferences;
import com.tianji.blockchain.sharepreferences.MnemonicSharedPreferences;
import com.tianji.blockchain.sharepreferences.ObserverWalletListSharedPreferences;
import com.tianji.blockchain.utils.CommonUtils;
import com.tianji.blockchain.utils.LogUtils;
import com.tianji.blockchain.utils.TimeUtils;
import com.tianji.blockchain.utils.ViewCommonUtils;
import com.tianji.blockchain.utils.WalletListHelper;

@SuppressWarnings("ALL")
public class WalletDetailsActivity extends BasicConnectShowActivity implements View.OnClickListener {
    private final static int TYPE_EXPORT_MNEMONICS = 2;
    private final static int TYPE_EXPORT_KEYSTORE = 3;
    private final static int TYPE_EXPORT_PRIVATE_KEY = 4;
    private final static int TYPE_DELETE_WALLET = 5;
    private final static int TYPE_ERROR = 6;
    private final static int TYPE_ERROR_PASSWORD = 7;
    private WalletInfo walletInfo;
    private ImageView img_chain_icon;
    private TextView tv_wallet_name;
    private RelativeLayout img_edit;
    private TextView tv_wallet_address;
    private ImageView img_copy;
    private LinearLayout ll_wallet_name;
    private LinearLayout ll_wallet_address;
    private TextView tv_wallet_create_time_content;

    private RelativeLayout rl_edit_pwd, rl_export_mnemonics, rl_export_keystore, rl_export_private_key;
    private Button btn_delete_wallet;

    private PasswordDialog passwordDialog;
    private ShowContentDialog showPrivateKeyDialog;
    private TipsDialog showDeleteDialog;
    private EditNameDialog editWalletNameDialog;
    private int verificationType;
    private int showType;

    private String privateKey;

    private boolean isBackUp;

    private String pwd;

    private SelectDialog selectDialog;

    private TipsDialog deleteWalletDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_details);
    }

    @Override
    protected void setData() {

    }

    @Override
    protected void handleMsg(Message msg) {
        switch (msg.what) {
            case TYPE_EXPORT_MNEMONICS:
                String mnemonics = (String) msg.obj;
                Intent mnemonicsIntent = new Intent(WalletDetailsActivity.this, BackUpMnemonicActivity.class);
                mnemonicsIntent.putExtra("_mnemonic", mnemonics);
                mnemonicsIntent.putExtra("_pageType", Constant.StartPageType.TYPE_WALLETDETAILS);
                mnemonicsIntent.putExtra("_walletInfo", walletInfo);
                startActivity(mnemonicsIntent);
                passwordDialog.dismiss();
                break;
            case TYPE_EXPORT_KEYSTORE:
                String keystore = (String) msg.obj;
                LogUtils.log("keystore ==" + keystore);
                LogUtils.log("pwd == " + pwd);
                Intent keystoreIntent = new Intent(WalletDetailsActivity.this, ExportKeystoreActivity.class);
                keystoreIntent.putExtra("_keystore", keystore);
                keystoreIntent.putExtra("_pwd", pwd);
                startActivity(keystoreIntent);
                passwordDialog.dismiss();
                break;
            case TYPE_EXPORT_PRIVATE_KEY:
                privateKey = (String) msg.obj;
                showPrivateKeyDialog = new ShowContentDialog(WalletDetailsActivity.this, getResources().getString(R.string.export_private_key), getResources().getString(R.string.export_private_key_explanation), privateKey, getResources().getString(R.string.copy), WalletDetailsActivity.this);
                showPrivateKeyDialog.show();
                passwordDialog.dismiss();
                break;
            case TYPE_DELETE_WALLET:
                showToast("删除钱包成功");
                showDeleteDialog.dismiss();
                passwordDialog.dismiss();
                if (WalletListHelper.getInstance(this).getSoftwareWalletInfoListAll().size() > 0) {
                    WalletApplication.setCurrentWallet(WalletListHelper.getInstance(this).getSoftwareWalletInfoListAll().get(0));
                    CurrentWalletSharedPreferences.getInstance(this).changeCurrentWallet(WalletListHelper.getInstance(this).getSoftwareWalletInfoListAll().get(0));
                    WalletDetailsActivity.this.finish();
                } else {
                    if (ObserverWalletListSharedPreferences.getInstance(WalletDetailsActivity.this).getAllObWalletList().size() > 0) {
                        WalletApplication.setCurrentWallet(ObserverWalletListSharedPreferences.getInstance(WalletDetailsActivity.this).getAllObWalletList().get(0).getWalletInfoList().get(0));
                        CurrentWalletSharedPreferences.getInstance(this).changeCurrentWallet(ObserverWalletListSharedPreferences.getInstance(WalletDetailsActivity.this).getAllObWalletList().get(0).getWalletInfoList().get(0));
                        WalletDetailsActivity.this.finish();
                    } else {
                        Intent intent = new Intent(WalletDetailsActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
                break;
            case TYPE_ERROR:
                String errContent = (String) msg.obj;
                showToast(errContent + "失败");
                passwordDialog.dismiss();
                break;
            case TYPE_ERROR_PASSWORD:
                showToast("密码验证失败");
                passwordDialog.dismiss();
                break;
        }
    }

    @Override
    protected void initView() {
        walletInfo = (WalletInfo) getIntent().getSerializableExtra("_walletInfo");
        mActionBar.setVisibility(View.VISIBLE);
        mActionBar.setActionTitle(getResources().getString(R.string.wallet_manager));
        ViewCommonUtils.buildBackImageView(this, mActionBar);

        isBackUp = MnemonicSharedPreferences.getInstance(this).isBackUpMnemonic(walletInfo.getAddress());

        img_chain_icon = findViewById(R.id.img_chain_icon);
        tv_wallet_name = findViewById(R.id.tv_wallet_name);
        img_edit = findViewById(R.id.img_edit);
        tv_wallet_address = findViewById(R.id.tv_wallet_address);
        img_copy = findViewById(R.id.img_copy);
        rl_edit_pwd = findViewById(R.id.rl_edit_pwd);
        rl_export_mnemonics = findViewById(R.id.rl_export_mnemonics);
        rl_export_keystore = findViewById(R.id.rl_export_keystore);
        rl_export_private_key = findViewById(R.id.rl_export_private_key);
        btn_delete_wallet = findViewById(R.id.btn_delete_wallet);
        ll_wallet_name = findViewById(R.id.ll_wallet_name);
        ll_wallet_address = findViewById(R.id.ll_wallet_address);
        tv_wallet_create_time_content = findViewById(R.id.tv_wallet_create_time_content);

        switch (walletInfo.getChain()) {
            case ACL:
                img_chain_icon.setImageResource(R.drawable.acl_icon_select);
                break;
            case BTC:
                img_chain_icon.setImageResource(R.drawable.btc);
                break;
            case ETH:
                img_chain_icon.setImageResource(R.drawable.eth_icon_selected);
                break;
        }
        tv_wallet_name.setText(walletInfo.getWalletName());
        tv_wallet_address.setText(walletInfo.getAddress());
        tv_wallet_create_time_content.setText(TimeUtils.timeStamp2Date(walletInfo.getCreatedTime() * 1000, "yyyy-MM-dd HH:mm:ss"));

        ll_wallet_address.setOnClickListener(this);
        rl_edit_pwd.setOnClickListener(this);
        rl_export_mnemonics.setOnClickListener(this);
        rl_export_keystore.setOnClickListener(this);
        rl_export_private_key.setOnClickListener(this);
        btn_delete_wallet.setOnClickListener(this);
        ll_wallet_name.setOnClickListener(this);

        String[] strArray = getResources().getStringArray(R.array.select_address);
        selectDialog = new SelectDialog(this, R.style.Wallet_Manager_Dialog, strArray, getResources().getString(R.string.operate));

        DialogEntity deleteUsbDialogEntity = new DialogEntity(getResources().getString(R.string.tips),
                getResources().getString(R.string.software_wallet_delete_desc),
                getResources().getString(R.string.cancel),
                getResources().getString(R.string.ok),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (deleteWalletDialog.getCheckbox() != null) {
                            if (deleteWalletDialog.getCheckbox().isChecked()) {
                                showPasswordDialog(R.id.btn_delete_wallet);
                                deleteWalletDialog.dismiss();
                            } else {
                                showToast(R.string.understand_risk_toast);
                            }
                        }
                    }
                });
        deleteWalletDialog = new TipsDialog(this, deleteUsbDialogEntity, getResources().getColor(R.color.warning_red), true);

        passwordDialog = new PasswordDialog(this, R.style.Wallet_Manager_Dialog, 1, false, this);
        DialogEntity showDeleteDialogEntity = new DialogEntity(getResources().getString(R.string.tips),
                getResources().getString(R.string.delete_explanation),
                getResources().getString(R.string.goto_backup),
                this);
        showDeleteDialog = new TipsDialog(this, showDeleteDialogEntity);
        editWalletNameDialog = new EditNameDialog(this, 1, walletInfo.getWalletName(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editWalletNameDialog.getEdtText().equals("")) {
                    showToast(R.string.edit_wallet_name_isempty);
                    return;
                }
                String newName = editWalletNameDialog.getEdtText();
                editWalletName(walletInfo, newName, new IRequestListener<Boolean>() {
                    @Override
                    public void onResult(ResultCode resultCode, Boolean result) {
                        switch (resultCode) {
                            case SUCCESS:
                                tv_wallet_name.setText(newName);
                                walletInfo.setWalletName(newName);
                                WalletApplication.setCurrentWallet(walletInfo);
                                CurrentWalletSharedPreferences.getInstance(WalletDetailsActivity.this).changeCurrentWallet(walletInfo);
                                break;
                            case WALLET_FILE_NAME_EXISTS:
                                showToast(R.string.WALLET_FILE_NAME_EXISTS);
                                break;
                            default:
                                showToast(R.string.edit_wallet_name_fail);
                                break;
                        }
                        editWalletNameDialog.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_wallet_name:
                editWalletNameDialog.show();
                break;
            case R.id.ll_wallet_address:
                CommonUtils.showOperateSelectDialog(selectDialog, walletInfo, walletInfo.getAddress());
//                CommonUtils.copyContent(this, walletInfo.getAddress());
                break;
            case R.id.rl_edit_pwd:
                Intent editpwdIntent = new Intent(WalletDetailsActivity.this, EditPasswordActivity.class);
                editpwdIntent.putExtra("_walletInfo", walletInfo);
                startActivityForResult(editpwdIntent, Constant.StartActivityResultCode.START_EDIT_WALLET_PASSWORD);
                break;
            case R.id.rl_export_mnemonics:
                if (walletInfo.getWalletCreatedType() == WalletCreatedType.IMPORTED_KEYSTORE || walletInfo.getWalletCreatedType() == WalletCreatedType.IMPORTED_PRIVATE_KEY) {
                    showToast(R.string.has_not_mnemonics);
                } else {
                    showPasswordDialog(R.id.rl_export_mnemonics);
                }
                break;
            case R.id.rl_export_keystore:
                showPasswordDialog(R.id.rl_export_keystore);
                break;
            case R.id.rl_export_private_key:
                showType = R.id.rl_export_private_key;
                showPasswordDialog(R.id.rl_export_private_key);
                break;
            case R.id.btn_delete_wallet:
                if (isBackUp) {
                    deleteWalletDialog.show();
                } else {
                    showType = R.id.btn_delete_wallet;
                    showDeleteDialog.show();
                }
                break;
            case R.id.btn_ok:
                switch (verificationType) {
                    case R.id.rl_export_mnemonics:
                        LogUtils.log(className + " -- 导出助记词");
                        getMnemonic(new IRequestListener<String>() {
                            @Override
                            public void onResult(ResultCode resultCode, String result) {
                                if (resultCode == ResultCode.SUCCESS) {
                                    Message message = Message.obtain();
                                    message.what = TYPE_EXPORT_MNEMONICS;
                                    message.obj = result;
                                    mHandler.sendMessage(message);
                                } else if (resultCode == ResultCode.WALLET_FILE_PASSWORD_ERROR) {
                                    showToast(R.string.password_wrong);
                                } else {
                                    showToast(R.string.export_mnemonics_failed);
                                    LogUtils.logError(className + " -- 导出助记词失败" + resultCode);
                                }
                            }
                        });
                        break;
                    case R.id.rl_export_keystore:
                        LogUtils.log(className + " -- 导出keystore");
                        getKeystore(new IRequestListener<String>() {
                            @Override
                            public void onResult(ResultCode resultCode, String result) {
                                switch (resultCode) {
                                    case SUCCESS:
                                        Message message = Message.obtain();
                                        message.what = TYPE_EXPORT_KEYSTORE;
                                        message.obj = result;
                                        mHandler.sendMessage(message);
                                        break;
                                    case KEYSTORE_NOT_SUPPORT:
                                        showToast(R.string.KEYSTORE_NOT_SUPPORT);
                                        break;
                                    case WALLET_FILE_PASSWORD_ERROR:
                                        showToast(R.string.password_wrong);
                                        break;
                                    default:
                                        LogUtils.logError("导出keystore失败 =" + resultCode);
                                        showToast(R.string.export_keystore_failed);
                                        break;
                                }
                            }
                        });
                        passwordDialog.dismiss();

                        break;
                    case R.id.rl_export_private_key:
                        LogUtils.log(className + " -- 导出私钥");
                        getPrivateKey(new IRequestListener<String>() {
                            @Override
                            public void onResult(ResultCode resultCode, String result) {
                                if (resultCode == ResultCode.SUCCESS) {
                                    Message message = Message.obtain();
                                    message.what = TYPE_EXPORT_PRIVATE_KEY;
                                    message.obj = result;
                                    mHandler.sendMessage(message);
                                } else if (resultCode == ResultCode.WALLET_FILE_PASSWORD_ERROR) {
                                    showToast("密码验证失败");
                                    passwordDialog.dismiss();
                                } else {
                                    showToast("导出失败");
                                    passwordDialog.dismiss();
                                }
                            }
                        });
                        break;
                    case R.id.btn_delete_wallet:
                        WalletManager.getInstance().deleteWallet(WalletDetailsActivity.this, walletInfo, passwordDialog.getEdtText(), new IRequestListener() {
                            @Override
                            public void onResult(ResultCode resultCode, Object result) {
                                switch (resultCode) {
                                    case SUCCESS:
                                        mHandler.sendEmptyMessage(TYPE_DELETE_WALLET);
                                        break;
                                    case WALLET_FILE_PASSWORD_ERROR:
                                        LogUtils.logError(className + " -- 删除钱包失败");
                                        showToast(R.string.password_wrong);
                                        break;
                                    case FAIL:
                                        LogUtils.logError(className + " -- 删除钱包失败");
                                        showToast(R.string.delete_wallet_fail);
                                        break;
                                    default:
                                        throw new IllegalStateException("Unexpected value: " + resultCode);
                                }

                            }
                        });
                        break;
                    case 0x25:
                        LogUtils.log(className + " -- 验证助记词");
                        getMnemonic(new IRequestListener<String>() {
                            @Override
                            public void onResult(ResultCode resultCode, String result) {
                                if (resultCode == ResultCode.SUCCESS) {
                                    Message message = Message.obtain();
                                    message.what = TYPE_EXPORT_MNEMONICS;
                                    message.obj = result;
                                    mHandler.sendMessage(message);
                                } else if (resultCode == ResultCode.WALLET_FILE_PASSWORD_ERROR) {
                                    showToast(R.string.password_wrong);
                                } else {
                                    showToast(R.string.mnemonic_wrong);
                                    LogUtils.logError(className + " -- 验证助记词失败" + resultCode);
                                }
                            }
                        });
                        passwordDialog.dismiss();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + verificationType);
                }
                break;
            case R.id.btn_positive:
                switch (showType) {
                    case R.id.btn_delete_wallet:
                        showPasswordDialog(0x25);
                        if (showDeleteDialog.isShowing()) {
                            showDeleteDialog.dismiss();
                        }
                        break;
                    case R.id.rl_export_private_key:
                        CommonUtils.copyContent(this, privateKey);
                        if (showPrivateKeyDialog.isShowing()) {
                            showPrivateKeyDialog.dismiss();
                        }
                        break;
                }
                break;
        }
    }

    private void showPasswordDialog(int viewId) {
        verificationType = viewId;
        if (!passwordDialog.isShowing()) {
            passwordDialog.show();
        }
        ViewCommonUtils.showKeyBorad(this);
    }

    /**
     * 获取keystore
     */
    private void getKeystore(IRequestListener<String> listener) {
        pwd = passwordDialog.getEdtText();
        WalletManager.getInstance().getKeystore(this, walletInfo, pwd, listener);
    }

    /**
     * 获取私钥
     */
    private void getPrivateKey(IRequestListener<String> listener) {
        pwd = passwordDialog.getEdtText();
        WalletManager.getInstance().getPrivateKey(this, walletInfo, pwd, listener);
    }

    /**
     * 获取助记词
     */
    public void getMnemonic(IRequestListener<String> listener) {
        pwd = passwordDialog.getEdtText();
        WalletManager.getInstance().getMnemonic(this, walletInfo, pwd, listener);
    }

    /**
     * 修改钱包名称
     *
     * @param walletInfo
     * @param newWalletName
     * @param listener
     */
    private void editWalletName(WalletInfo walletInfo, String newWalletName, IRequestListener<Boolean> listener) {
        WalletManager.getInstance().renameWallet(this, walletInfo, newWalletName, listener);
    }

//    /**
//     * USB监听
//     */
//    @Override
//    public void onUsbConnect() {
//        LogUtils.usbConnectTypeLog(className, 1);
//        usbConnect = true;
//    }
//
//    @Override
//    public void onUsbDisconnect() {
//        LogUtils.usbConnectTypeLog(className, 2);
//        usbIsConnected = false;
//        usbConnect = false;
//    }
//
//    @Override
//    public void usbIsConnected() {
//        LogUtils.usbConnectTypeLog(className, 3);
//        usbIsConnected = true;
//    }
//
//    @Override
//    public void noUsbConnected() {
//        LogUtils.usbConnectTypeLog(className, 4);
//        usbIsConnected = false;
//        usbConnect = false;
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.StartActivityResultCode.START_EDIT_WALLET_PASSWORD) {
            if (resultCode == 0x20) {
                Bundle bundle = data.getExtras();
                walletInfo = (WalletInfo) bundle.getSerializable("_walletInfo");
            }
        }
    }
}
