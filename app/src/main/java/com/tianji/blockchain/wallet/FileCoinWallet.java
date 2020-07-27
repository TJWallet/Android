package com.tianji.blockchain.wallet;

import android.content.Context;

import com.tianji.blockchain.constant.enums.HDWalletAddressPath;
import com.tianji.blockchain.constant.enums.ResultCode;
import com.tianji.blockchain.constant.enums.WalletProof;
import com.tianji.blockchain.entity.WalletInfo;
import com.tianji.blockchain.util.HexUtils;
import com.tianji.blockchain.util.LogUtils;
import com.tianji.blockchain.util.blake2b.Blake2b;
import com.tianji.blockchain.util.blake2b.security.Blake2bProvider;

import org.apache.commons.codec.binary.Base32;
import org.bitcoinj.core.ECKey;
import org.web3j.crypto.ECKeyPair;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Map;

public class FileCoinWallet extends BaseWallet {

    @Override
    protected String getPrivateKey(ECKey ecKey) {
        return Numeric.toHexStringNoPrefix(ecKey.getPrivKey());
    }

    @Override
    protected ECKey getEcKey(String privateKey) {
        return ECKey.fromPrivate(Numeric.toBigInt(privateKey));
    }

    @Override
    protected String getAddress(ECKey ecKey, HDWalletAddressPath hdWalletAddressPath) {
        ECKeyPair ecKeyPair = ECKeyPair.create(new BigInteger(getPrivateKey(ecKey), 16));

        String hexPublicKey = ecKeyPair.getPublicKey().toString(8);
        hexPublicKey = "08" + hexPublicKey;
        String blake2b20Hash = blake2bHash(hexPublicKey, Blake2b.BLAKE2_B_384);
        if (blake2b20Hash == null) {
            return null;
        }

        blake2b20Hash = "04" + blake2b20Hash;
        String checksum = blake2bHash(blake2b20Hash, Blake2b.BLAKE2_B_160);
        if (checksum == null) {
            return null;
        }

        return blake2b20Hash + checksum;
    }

    @Override
    public WalletInfo createDefaultAccount(String mnemonic) {
        WalletInfo walletInfo = null;

        return walletInfo;
    }

    @Override
    public void signTransaction(Context context, WalletInfo walletInfo, String password, Map<String, Object> params, IRequestListener<String> listener) {

        // 获取私钥
        getWalletProof(context, WalletProof.PRIVATE_KEY, walletInfo, password, (resultCode, result) -> {
            if (resultCode == ResultCode.SUCCESS) {

            } else {
                listener.onResult(resultCode, null);
            }
        });
    }

    /***
     * blake2b计算
     * @param content 需要哈希的内容
     * @param algorithm 加密算法
     * @return 返回哈希值
     */
    private String blake2bHash(String content, String algorithm) {
        try {
            Security.addProvider(new Blake2bProvider());
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.update(HexUtils.hexToByteArray(content));
            byte[] byteArray = digest.digest();
            return HexUtils.bytesToHex(byteArray);
        } catch (NoSuchAlgorithmException e) {
            LogUtils.i("20位的blake2b计算失败!!!");
            e.printStackTrace();
            return null;
        }
    }

    /***
     * base32编码
     * @param content 需要编码的内容
     * @return 返回编码后的值（String类型）
     */
    private String base32(String content) {
        Base32 base32 = new Base32();
        byte[] contentByteArray = HexUtils.hexToByteArray(content);
        return base32.encodeAsString(contentByteArray);
    }
}
