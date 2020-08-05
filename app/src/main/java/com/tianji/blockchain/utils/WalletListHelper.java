package com.tianji.blockchain.utils;


import android.content.Context;
import android.os.Build;

import com.tianji.blockchainwallet.WalletManager;
import com.tianji.blockchainwallet.constant.enums.Chain;
import com.tianji.blockchainwallet.constant.enums.ResultCode;
import com.tianji.blockchainwallet.constant.enums.StorageSaveType;
import com.tianji.blockchainwallet.entity.WalletInfo;
import com.tianji.blockchainwallet.wallet.IRequestListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WalletListHelper {
    private Context context;
    /**
     * 单例模式
     **/
    private static WalletListHelper instance;

    private WalletListHelper(Context context) {
        this.context = context;
    }

    /*** 存储在本地的钱包 ***/
    private Map<Chain, List<WalletInfo>> walletLocalMap;

    /*** 软件钱包ALL ***/
    private List<WalletInfo> softwareWalletInfoListAll = new ArrayList<>();

    /*** 软件钱包FileCoin***/
    private List<WalletInfo> softwareWalletInfoListFIL = new ArrayList<>();


    public static WalletListHelper getInstance(Context context) {
        if (instance == null) {
            instance = new WalletListHelper(context);
        }
        return instance;
    }

    /**
     * 更新钱包列表数据
     */
    private void refreshSoftwareData() {
        clearAllWalletList();
        //获取两个存储地址的map
        WalletManager.getInstance().getWalletList(context, StorageSaveType.LOCAL, "", new IRequestListener<Map<Chain, List<WalletInfo>>>() {
            @Override
            public void onResult(ResultCode resultCode, Map<Chain, List<WalletInfo>> result) {
                if (resultCode == ResultCode.SUCCESS) {
                    walletLocalMap = result;
                    //所有软件钱包
                    if (walletLocalMap != null) {
                        Iterator<Map.Entry<Chain, List<WalletInfo>>> localIterator = walletLocalMap.entrySet().iterator();
                        while (localIterator.hasNext()) {
                            Map.Entry<Chain, List<WalletInfo>> entry = localIterator.next();
                            softwareWalletInfoListAll.addAll(entry.getValue());
                            srotWalletList(softwareWalletInfoListAll);
                        }
                        if (walletLocalMap.get(Chain.FIL) != null) {
                            //获取FileCoin软件钱包
                            softwareWalletInfoListFIL.addAll(walletLocalMap.get(Chain.FIL));
                            srotWalletList(softwareWalletInfoListFIL);
                        }
                    }
                }
            }
        });
    }

    /***获取软件全部钱包***/
    public List<WalletInfo> getSoftwareWalletInfoListAll() {
        refreshSoftwareData();
        return softwareWalletInfoListAll;
    }


    /***获取软件BTC钱包***/
    public List<WalletInfo> getSoftwareWalletInfoListFIL() {
        refreshSoftwareData();
        return softwareWalletInfoListFIL;
    }


    /**
     * 清除所有钱包数据
     */
    private void clearAllWalletList() {
        softwareWalletInfoListAll.clear();
        softwareWalletInfoListFIL.clear();
    }

    /**
     * 通过创建时间排序
     *
     * @param walletInfoList
     */
    private void srotWalletList(List<WalletInfo> walletInfoList) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            walletInfoList.sort(new Comparator<WalletInfo>() {
                @Override
                public int compare(WalletInfo o1, WalletInfo o2) {
                    Long long1 = o1.getCreatedTime();
                    Long long2 = o2.getCreatedTime();
                    return long2.compareTo(long1);
                }
            });
        }
    }
}
