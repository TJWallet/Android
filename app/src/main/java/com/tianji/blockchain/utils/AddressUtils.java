package com.tianji.blockchain.utils;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.web3j.utils.Numeric;

import static org.web3j.crypto.Keys.ADDRESS_LENGTH_IN_HEX;

public class AddressUtils {

    /**
     * 验证ETH地址合法性
     *
     * @param input
     * @return
     */
    public static boolean isETHValidAddress(String input) {
        if (input.equals("") || !input.startsWith("0x"))
            return false;
        return isValidAddress(input);
    }


    private static boolean isValidAddress(String input) {
        String cleanInput = Numeric.cleanHexPrefix(input);

        try {
            Numeric.toBigIntNoPrefix(cleanInput);
        } catch (NumberFormatException e) {
            return false;
        }

        return cleanInput.length() == ADDRESS_LENGTH_IN_HEX;
    }


    /**
     * 验证BTC地址合法性
     *
     * @param input
     * @return
     */
    public static boolean isBTCValidAddress(String input) {
        try {
            NetworkParameters networkParameters = null;
            networkParameters = MainNetParams.get();
            Address address = Address.fromBase58(networkParameters, input);
            if (address != null) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }


}
