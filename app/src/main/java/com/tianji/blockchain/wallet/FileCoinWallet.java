package com.tianji.blockchain.wallet;

import android.content.Context;
import android.text.TextUtils;

import com.tianji.blockchain.constant.enums.Chain;
import com.tianji.blockchain.constant.enums.HDWalletAddressPath;
import com.tianji.blockchain.constant.enums.ResultCode;
import com.tianji.blockchain.constant.enums.TransferType;
import com.tianji.blockchain.constant.enums.WalletProof;
import com.tianji.blockchain.entity.WalletInfo;
import com.tianji.blockchain.util.HDWalletUtils;
import com.tianji.blockchain.util.LogUtils;

import org.bitcoinj.core.ECKey;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
        return Keys.getAddress(ecKeyPair.getPublicKey());
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
}
