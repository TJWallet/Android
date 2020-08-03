package com.tianji.blockchain.utils;

import com.tianji.blockchain.entity.AllObWalletEntity;

import java.util.List;

public class ListUtils {

    public static void hardwareSort(List<AllObWalletEntity> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                if (list.get(j).getTimeStamp() > list.get(j + 1).getTimeStamp()) {
                    AllObWalletEntity recode = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, recode);
                }
            }
        }
    }
}
