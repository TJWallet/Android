package com.tianji.blockchain.wallet;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tianji.blockchain.constant.enums.Chain;
import com.tianji.blockchain.constant.enums.HDWalletAddressPath;
import com.tianji.blockchain.constant.enums.ResultCode;
import com.tianji.blockchain.constant.enums.StorageSaveType;
import com.tianji.blockchain.constant.enums.WalletCreatedType;
import com.tianji.blockchain.constant.enums.WalletProof;
import com.tianji.blockchain.entity.WalletInfo;
import com.tianji.blockchain.util.AESUtils;
import com.tianji.blockchain.util.HDWalletUtils;
import com.tianji.blockchain.util.LogUtils;
import com.tianji.blockchain.util.WalletFileUtils;
import com.tianji.blockchain.util.WalletRemarksUtils;

import org.bitcoinj.core.ECKey;

import java.util.Map;

public abstract class BaseWallet {

    /***
     * 通过ECKey获取私钥
     * @param ecKey 通过EC加密算法生成的秘钥对
     * @return 返回String类型私钥
     */
    protected abstract String getPrivateKey(ECKey ecKey);

    /***
     * 通过私钥生成ECKey
     * @param privateKey    私钥
     * @return 返回ECKey对象
     */
    protected abstract ECKey getEcKey(String privateKey);

    /***
     * 通过ECKey和钱包地址协议来生成钱包地址，例如比特币私钥通过地址协议来可以生成普通钱包地址和隔离见证地址
     * @param ecKey 通过EC加密算法生成的秘钥对
     * @param hdWalletAddressPath   钱包地址协议
     * @return 返回String类型的地址
     */
    protected abstract String getAddress(ECKey ecKey, HDWalletAddressPath hdWalletAddressPath);

    /***
     * 创建默认账户，子地址默认使用第0位地址，比特币默认生成隔离见证地址
     * @param mnemonic  助记词
     * @return 返回String类型的地址
     */
    public abstract WalletInfo createDefaultAccount(String mnemonic);

    /***
     * 区块链交易签名，参数通过Map封装传入
     * @param context   上下文
     * @param walletInfo   钱包对象
     * @param password  钱包密码
     * @param params    交易参数封装对象
     * @param listener  回调
     */
    public abstract void signTransaction(Context context, WalletInfo walletInfo, String password, Map<String, Object> params, IRequestListener<String> listener);

    /***
     * 创建钱包
     * @param context   上下文
     * @param walletCreatedType 钱包生成方式，本地创建或者是导入
     * @param storageSaveType   保存位置，应用本地存储或者是硬件存储
     * @param chain 区块链类型
     * @param childAccountPos   HD钱包协议地址返回的钱包位置
     * @param hdWalletAddressPath   钱包HD协议地址，区块链或者是地址类型不同都有区分
     * @param content   导入时需要设置，助记词，私钥或者keystore
     * @param walletName    钱包名称
     * @param password  钱包密码
     * @param passwordTips  钱包密码提示
     * @param listener  回调
     */
    public void create(Context context, WalletCreatedType walletCreatedType, StorageSaveType storageSaveType, Chain chain, HDWalletAddressPath hdWalletAddressPath, int childAccountPos, String content, String walletName, String password, String passwordTips, IRequestListener<WalletInfo> listener) {

        WalletInfo walletInfo = new WalletInfo();

        // 地址
        String address;
        // 私钥
        String privateKey;
        // 助记词
        String mnemonic;
        // ECKEY
        ECKey ecKey;
        // 钱包凭证
        String walletProof;

        switch (walletCreatedType) {
            case LOCAL_CREATED:
                // 创建助记词
                mnemonic = HDWalletUtils.createMnemonic(16);
                // 生成ECKEY
                ecKey = HDWalletUtils.createECKeyByMnemonic(mnemonic, hdWalletAddressPath.getPath(), childAccountPos);
                // 生成地址
                address = getAddress(ecKey, hdWalletAddressPath);

                walletInfo.setWalletProof(WalletProof.MNEMONIC);
                walletProof = mnemonic;
                break;
            case IMPORTED_MNEMONIC:
                mnemonic = content;
                // 生成ECKEY
                ecKey = HDWalletUtils.createECKeyByMnemonic(content, hdWalletAddressPath.getPath(), childAccountPos);
                // 生成地址
                address = getAddress(ecKey, hdWalletAddressPath);

                walletInfo.setWalletProof(WalletProof.MNEMONIC);
                walletProof = mnemonic;
                break;
            case IMPORTED_PRIVATE_KEY:
                privateKey = content;
                // 生成ECKEY
                ecKey = getEcKey(privateKey);
                // 生成地址
                address = getAddress(ecKey, hdWalletAddressPath);

                walletInfo.setWalletProof(WalletProof.PRIVATE_KEY);
                walletProof = privateKey;
                break;
            case IMPORTED_KEYSTORE:
                LogUtils.d("非keystore支持类型!!!");
                listener.onResult(ResultCode.KEYSTORE_NOT_SUPPORT, null);
                return;
            default:
                listener.onResult(ResultCode.PARAMS_ERROR, null);
                return;
        }

        walletInfo.setAddress(address);
        walletInfo.setWalletName(walletName);
        walletInfo.setStorageSaveType(storageSaveType);
        walletInfo.setHdWalletAddressPath(hdWalletAddressPath);
        walletInfo.setChain(chain);
        walletInfo.setCreatedTime(System.currentTimeMillis() / 1000);
        walletInfo.setPasswordTips(passwordTips);
        walletInfo.setWalletCreatedType(walletCreatedType);
        walletInfo.setChildAccountPos(childAccountPos);

        // 判断钱包地址是否已经存在
        if (WalletFileUtils.exists(context, storageSaveType, chain, walletInfo.getAddress())) {
            listener.onResult(ResultCode.WALLET_FILE_ADDRESS_EXISTS, null);
            return;
        }

        // 判断钱包备注是否已经存在
        if (WalletRemarksUtils.walletRemarksExists(context, chain, address)) {
            LogUtils.d("钱包备注已经存在...");
            listener.onResult(ResultCode.WALLET_FILE_REMARKS_EXISTS, null);
            return;
        }

        // 判断钱包名称是否已经存在
        if (WalletRemarksUtils.walletNameExists(context, walletName)) {
            listener.onResult(ResultCode.WALLET_FILE_NAME_EXISTS, null);
            return;
        }

        // 保存文件
        boolean saveResult = WalletFileUtils.save(context, storageSaveType, chain, walletInfo, password, walletProof);
        if (saveResult) {
            LogUtils.d("钱包创建成功...");
            listener.onResult(ResultCode.SUCCESS, walletInfo);
        } else {
            LogUtils.d("钱包创建失败!!!");
            listener.onResult(ResultCode.FAIL, null);
        }
    }

    /***
     * 获取钱包的助记词，私钥或者是keystore（keystore需要看区块链的账户格式是否支持）
     * @param context   上下文
     * @param reqWalletProof    钱包凭证的类型（助记词，私钥或者是keystore）
     * @param walletInfo   钱包对象
     * @param password  钱包密码
     * @param listener  回调
     */
    public void getWalletProof(Context context, WalletProof reqWalletProof, WalletInfo walletInfo, String password, IRequestListener<String> listener) {

        StorageSaveType storageSaveType = walletInfo.getStorageSaveType();
        Chain chain = walletInfo.getChain();
        String walletFilePath = WalletFileUtils.getPath(context, storageSaveType, chain, walletInfo.getAddress());

        // 获取钱包文件内容
        String walletFileContent = WalletFileUtils.read(context, storageSaveType, walletFilePath);
        if (TextUtils.isEmpty(walletFileContent)) {
            listener.onResult(ResultCode.WALLET_FILE_NOT_EXISTS, null);
            return;
        }

        // 判断钱包文件内容格式
        JSONObject walletFileJO;
        try {
            walletFileJO = JSON.parseObject(walletFileContent);
        } catch (Exception e) {
            e.printStackTrace();
            listener.onResult(ResultCode.WALLET_FILE_CONTENT_ERROR, null);
            return;
        }

        // 钱包文件内容转JSON格式
        String encWalletProofContent = walletFileJO.getString("encWalletProofContent");
        HDWalletAddressPath hdWalletAddressPath = walletInfo.getHdWalletAddressPath();
        int childAccountPos = walletInfo.getChildAccountPos();

        if (TextUtils.isEmpty(encWalletProofContent)) {
            listener.onResult(ResultCode.WALLET_FILE_CONTENT_ERROR, null);
            return;
        }

        // 解密
        String walletProofContent = AESUtils.decrypt(password, encWalletProofContent);
        if (TextUtils.isEmpty(walletProofContent)) {
            listener.onResult(ResultCode.WALLET_FILE_PASSWORD_ERROR, null);
            return;
        }


        // 助记词
        String mnemonic;
        // 私钥
        String privateKey;
        // keystore
        String keystore;
        // keystore的JSON格式
        JSONObject keystoreJO;

        WalletProof walletProof = walletInfo.getWalletProof();
        if (walletProof == null) {
            LogUtils.d("钱包文件内的凭证类型异常!!! walletInfo = " + walletInfo.toJSONObject());
            listener.onResult(ResultCode.PARAMS_ERROR, null);
            return;
        }

        switch (reqWalletProof) {
            case MNEMONIC: // 助记词请求
                LogUtils.d("助记词请求...");
                switch (walletProof) {
                    case MNEMONIC:
                        mnemonic = walletProofContent;
                        listener.onResult(ResultCode.SUCCESS, mnemonic);
                        return;
                    case PRIVATE_KEY:
                    case KEYSTORE:
                        LogUtils.d("当前钱包不支持助记词返回!!!");
                        listener.onResult(ResultCode.KEYSTORE_NOT_SUPPORT, null);
                        return;
                    default:
                        listener.onResult(ResultCode.PARAMS_ERROR, null);
                        return;
                }

            case PRIVATE_KEY: // 私钥请求
                LogUtils.d("私钥请求...");
                switch (walletProof) {
                    case MNEMONIC:
                        // 生成ECKEY
                        ECKey ecKey = HDWalletUtils.createECKeyByMnemonic(walletProofContent, hdWalletAddressPath.getPath(), childAccountPos);
                        privateKey = getPrivateKey(ecKey);
                        // 生成私钥
                        listener.onResult(ResultCode.SUCCESS, privateKey);
                        return;
                    case PRIVATE_KEY:
                        privateKey = walletProofContent;
                        listener.onResult(ResultCode.SUCCESS, privateKey);
                        return;
                    case KEYSTORE:
                        LogUtils.d("非keystore支持类型!!!");
                        listener.onResult(ResultCode.KEYSTORE_NOT_SUPPORT, null);
                        return;
                    default:
                        listener.onResult(ResultCode.PARAMS_ERROR, null);
                        return;
                }

            case KEYSTORE: // keystore请求
                LogUtils.d("keystore请求...");
                LogUtils.d("非keystore支持类型!!!");
                listener.onResult(ResultCode.KEYSTORE_NOT_SUPPORT, null);
                return;
            default:
                listener.onResult(ResultCode.PARAMS_ERROR, null);
        }
    }
}