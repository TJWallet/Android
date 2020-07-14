package com.tianji.blockchain.util;

import com.tianji.blockchain.constant.enums.Chain;
import com.tianji.blockchain.entity.WalletInfo;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.web3j.crypto.MnemonicUtils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HDWalletUtils {

    /**
     * 创建助记词128位-256位（128位则字节数组长度为16个字节，256位则字节数组长度为32个字节）
     *
     * @param byteArrayLength
     * @return
     */
    public static String createMnemonic(int byteArrayLength) {
        // 创建熵
        byte[] initialEntropy = new byte[byteArrayLength];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(initialEntropy);

        // 通过bip39生成助记词
        return MnemonicUtils.generateMnemonic(initialEntropy);
    }

    /**
     * 通过助记词生成秘钥对，需要指定生成路径，例如：以太坊"M/44H/60H/0H/0/0"，比特币普通地址"M/44H/0H/0H/0/0"，比特币隔离地址"M/49H/60H/0H/0/0"
     *
     * @param mnemonic
     * @param chainPath
     * @param childAccountPos
     * @return
     */
    public static ECKey createECKeyByMnemonic(String mnemonic, String chainPath, int childAccountPos) {
        try {
            DeterministicSeed deterministicSeed = new DeterministicSeed(mnemonic, null, "", 0);
            DeterministicKeyChain deterministicKeyChain = DeterministicKeyChain.builder().seed(deterministicSeed).build();
            // 普通地址
            List<ChildNumber> keyPath = HDUtils.parsePath(chainPath + childAccountPos);
            BigInteger privKey = deterministicKeyChain.getKeyByPath(keyPath, true).getPrivKey();
            return ECKey.fromPrivate(privKey);
        } catch (UnreadableWalletException e) {
            e.printStackTrace();
            LogUtils.d("导入的助记词不正确，无法生成秘钥对!!!");
        }
        return null;
    }

    /**
     * 种子通过HD方式生成多种钱包
     *
     * @param mnemonic
     * @return
     */
    public static Map<Chain, List<WalletInfo>> createChainWallet(String mnemonic) {
        Map<Chain, List<WalletInfo>> walletMap = new HashMap<>();
        // 遍历生成对应账户
        for (Chain chain : Chain.getChainList()) {
            WalletInfo walletInfo = chain.getWallet().createDefaultAccount(mnemonic);
            if (walletInfo != null) {
                List<WalletInfo> walletInfoList = new ArrayList<>();
                walletInfoList.add(walletInfo);
                walletMap.put(chain, walletInfoList);
            }
        }
        return walletMap;
    }
}
