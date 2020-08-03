package com.tianji.blockchain.utils;

import android.content.Context;

import com.tianji.blockchainwallet.WalletManager;
import com.tianji.blockchainwallet.constant.enums.Chain;
import com.tianji.blockchainwallet.constant.enums.ResultCode;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchainwallet.wallet.IRequestListener;
import com.tianji.blockchain.R;
import com.tianji.blockchain.entity.AllObWalletEntity;
import com.tianji.blockchain.entity.AssetsDetailsItemEntity;
import com.tianji.blockchain.sharepreferences.AssetsListSharedPreferences;
import com.tianji.blockchain.sharepreferences.ObserverWalletListSharedPreferences;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SharePreferencesHelper {
    public static void addDefaultAssets(Context context, String address, Chain chainType) {
        LogUtils.log("添加新得资产 == " + chainType);
        switch (chainType) {
            case ACL:
                AssetsDetailsItemEntity assetsEntityACL = new AssetsDetailsItemEntity();
                assetsEntityACL.setAssetsName("ACL");
                assetsEntityACL.setContractAddress("");
                assetsEntityACL.setAssetsIconRes(R.drawable.acl_icon_select);
                assetsEntityACL.setDecimals(18);
                String aclKey = address + chainType;
                AssetsListSharedPreferences.getInstance(context).addAssetes(aclKey, assetsEntityACL);
                break;
            case ETH:
                String ethKey = address + chainType;
                AssetsDetailsItemEntity assetsEntityETH = new AssetsDetailsItemEntity();
                assetsEntityETH.setAssetsName("ETH");
                assetsEntityETH.setId(0);
                assetsEntityETH.setContractAddress("");
                assetsEntityETH.setDecimals(18);
                assetsEntityETH.setAssetsIconRes(R.drawable.eth_icon_selected);
                AssetsListSharedPreferences.getInstance(context).addAssetes(ethKey, assetsEntityETH);
                AssetsDetailsItemEntity assetsEntityCPCT = new AssetsDetailsItemEntity();
                assetsEntityCPCT.setAssetsName("CPCT");
                assetsEntityCPCT.setId(2);
                assetsEntityCPCT.setDecimals(18);
                assetsEntityCPCT.setContractAddress("0x8fdcC30edA7E94F1c12ce0280Df6Cd531E8365c5");
                assetsEntityCPCT.setAssetsIconRes(R.drawable.cpct_icon);
                assetsEntityCPCT.setIconUrl("http://resource.tjwallet.net/icon/微信图片_20200613201907-20200613121920337.jpg");
                assetsEntityCPCT.setAssetsCompleteName("CPCT");
                AssetsListSharedPreferences.getInstance(context).addAssetes(ethKey, assetsEntityCPCT);
                AssetsDetailsItemEntity assetsEntityUSDT = new AssetsDetailsItemEntity();
                assetsEntityUSDT.setAssetsName("USDT");
                assetsEntityUSDT.setId(3);
                assetsEntityUSDT.setDecimals(6);
                assetsEntityUSDT.setAssetsIconRes(R.drawable.usdt_icon);
                assetsEntityUSDT.setContractAddress("0xdac17f958d2ee523a2206206994597c13d831ec7");
                assetsEntityUSDT.setIconUrl("http://resource.tjwallet.net/icon/830964_image20-2020061312095360.png");
                assetsEntityUSDT.setAssetsCompleteName("泰达币-ERC20");
                AssetsListSharedPreferences.getInstance(context).addAssetes(ethKey, assetsEntityUSDT);
                break;
            case BTC:
                String btcKey = address + chainType;
                AssetsDetailsItemEntity assetsEntityBTC = new AssetsDetailsItemEntity();
                assetsEntityBTC.setAssetsName("BTC");
                assetsEntityBTC.setContractAddress("");
                assetsEntityBTC.setDecimals(8);
                assetsEntityBTC.setAssetsIconRes(R.drawable.btc);
                AssetsListSharedPreferences.getInstance(context).addAssetes(btcKey, assetsEntityBTC);
                break;
        }
    }


    /**
     * 添加观察者硬件
     *
     * @param context
     * @param walletInfoList
     * @return
     */
    public static void addObHardware(Context context, List<WalletInfo> walletInfoList, String usbMac, String uuid) {
        AllObWalletEntity entity = new AllObWalletEntity();
        List<AllObWalletEntity> allObWalletEntityList = ObserverWalletListSharedPreferences.getInstance(context).getAllObWalletList();
        String hardwareName = "";
        if (allObWalletEntityList.size() > 9) {
            hardwareName = "TJW" + (allObWalletEntityList.size() + 1);
        } else {
            hardwareName = "TJW0" + (allObWalletEntityList.size() + 1);
        }
        for (int i = 0; i < allObWalletEntityList.size(); i++) {
            if (allObWalletEntityList.get(i).getHardwareName().equals(hardwareName)) {
                String regEx = "[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(allObWalletEntityList.get(allObWalletEntityList.size() - 1).getHardwareName());
                String str = m.replaceAll("").trim();
                int num = Integer.parseInt(str);
                hardwareName = "TJW0" + (num + 1);
            }
        }
        entity.setHardwareName(hardwareName);
        List<WalletInfo> newWalletInfoList = new ArrayList<>();
        newWalletInfoList.add(new WalletInfo());
        newWalletInfoList.add(new WalletInfo());
        newWalletInfoList.add(new WalletInfo());
        for (int i = 0; i < walletInfoList.size(); i++) {
            switch (walletInfoList.get(i).getChain()) {
                case BTC:
                    walletInfoList.get(i).setWalletName(hardwareName + "-BTC");
                    newWalletInfoList.remove(1);
                    newWalletInfoList.add(1, walletInfoList.get(i));
                    break;
                case ETH:
                    walletInfoList.get(i).setWalletName(hardwareName + "-ETH");
                    newWalletInfoList.remove(0);
                    newWalletInfoList.add(0, walletInfoList.get(i));
                    break;
                case ACL:
                    walletInfoList.get(i).setWalletName(hardwareName + "-ACL");
                    newWalletInfoList.remove(2);
                    newWalletInfoList.add(2, walletInfoList.get(i));
                    break;
            }
        }
        entity.setWalletInfoList(newWalletInfoList);
        entity.setUsbMacAddress(usbMac);
        entity.setUuid(uuid);
        entity.setTimeStamp(System.currentTimeMillis());
        WalletManager.getInstance().getUsbWalletCreatedTime(context, new IRequestListener<Long>() {
            @Override
            public void onResult(ResultCode resultCode, Long result) {
                switch (resultCode) {
                    case SUCCESS:
                        entity.setUsbInitTimeStamp(result);
                        break;
                    default:
                        entity.setUsbInitTimeStamp(0);
                        break;
                }
                ObserverWalletListSharedPreferences.getInstance(context).addObWallet(usbMac, entity);
                LogUtils.log(" -- 储存观察者钱包 ==" + entity.getHardwareName());
            }
        });

    }
}
