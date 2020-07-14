package com.tianji.blockchain.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tianji.blockchain.constant.enums.ResultCode;
import com.tianji.blockchain.wallet.IRequestListener;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletFile;
import org.web3j.utils.Numeric;

public class KeystoreUtils {

    /**
     * 通过私钥生成keystore
     *
     * @param privateKey 私钥(必须是整型)
     * @param password   keystore密码
     * @return 返回JSON对象
     */
    public static JSONObject createKeystoreByPrivateKey(String privateKey, String password) {
        try {
            Credentials credentials = Credentials.create(privateKey);
            ECKeyPair ecKeyPair = credentials.getEcKeyPair();

            // 通过密码和钥匙对生成WalletFile也就是keystore的bean类
            WalletFile walletFile = org.web3j.crypto.Wallet.createLight(password, ecKeyPair);
            // 生成keystore
            JSONObject keystoreJO = new JSONObject();
            keystoreJO.put("address", walletFile.getAddress());
            keystoreJO.put("id", walletFile.getId());
            keystoreJO.put("version", walletFile.getVersion());
            keystoreJO.put("crypto", walletFile.getCrypto());
            return keystoreJO;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d("私钥不符合规范，请检查是否为空或者带有非法字符，通过私钥生成keystore失败!!!");
            return null;
        }
    }

    /**
     * 通过keystore和密码还原出私钥
     *
     * @param keystore keystore
     * @param password keystore密码
     * @return 返回String对象
     */
    public static String createPrivateKeyByKeystore(String keystore, String password) {
        try {
            Credentials credentials = createCredentialsByKeystore(keystore, password);
            if (credentials == null) {
                return null;
            }
            return Numeric.toHexStringNoPrefix(credentials.getEcKeyPair().getPrivateKey());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d("通过keystore和密码还原私钥失败!!!");
            return null;
        }
    }

    /**
     * 通过keystore和密码生成地址
     *
     * @param keystore keystore
     * @param password keystore密码
     * @return 返回String对象
     */
    public static String createAddressByKeystore(String keystore, String password) {
        try {
            Credentials credentials = createCredentialsByKeystore(keystore, password);
            if (credentials == null) {
                return null;
            }
            return credentials.getAddress();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d("通过keystore和密码生成地址失败");
            return null;
        }
    }

    /**
     * 通过keystore和密码生成credentials
     *
     * @param keystore keystore
     * @param password keystore密码
     * @return 返回Credentials对象
     */
    public static Credentials createCredentialsByKeystore(String keystore, String password) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            WalletFile walletFile = objectMapper.readValue(keystore, WalletFile.class);
            return Credentials.create(org.web3j.crypto.Wallet.decrypt(password, walletFile));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d("keystore解密失败!!!");
        }
        return null;
    }

    /**
     * keystore密码校验
     *
     * @param keystore keystore
     * @param password keystore密码
     * @param listener 回调
     */
    public static void keystoreVerify(String keystore, String password, IRequestListener<Boolean> listener) {
        if (createCredentialsByKeystore(keystore, password) != null) {
            listener.onResult(ResultCode.SUCCESS, true);
        } else {
            listener.onResult(ResultCode.FAIL, false);
        }
    }
}
