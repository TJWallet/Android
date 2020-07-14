package com.tianji.blockchain.util;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tianji.blockchain.constant.enums.Chain;
import com.tianji.blockchain.constant.enums.ResultCode;
import com.tianji.blockchain.entity.WalletRemarksInfo;
import com.tianji.blockchain.wallet.IRequestListener;

import java.util.HashMap;
import java.util.Map;

public class WalletRemarksUtils {

    private static final String WALLET_REMARKS_PREFERENCES_NAME = "walletRemarks";

    /**
     * 删除钱包备注
     *
     * @param context 上下文
     * @param chain   区块链类型
     * @param address 钱包地址
     * @return 布尔值，删除成功返回true，失败返回false
     */
    public static boolean delete(Context context, Chain chain, String address) {
        // 查询钱包备注
        Map<String, WalletRemarksInfo> walletRemarksMap = getRemarksList(context);
        if (walletRemarksMap == null || walletRemarksMap.isEmpty()) {
            LogUtils.d("钱包备注信息不存在，删除失败!!!");
            return false;
        }

        // 删除
        walletRemarksMap.remove(createKey(chain, address));
        JSONObject walletRemarksJO = JSONObject.parseObject(JSON.toJSONString(walletRemarksMap));

        // 写入
        return PreferencesUtils.putString(context, WALLET_REMARKS_PREFERENCES_NAME, walletRemarksJO.toString());
    }

    /**
     * 插入钱包备注
     *
     * @param context           上下文
     * @param chain             区块链类型
     * @param address           钱包地址
     * @param walletRemarksInfo 新钱包备注信息
     * @return 布尔值，修改成功返回true，失败返回false
     */
    public static boolean insert(Context context, Chain chain, String address, WalletRemarksInfo walletRemarksInfo) {
        // 查询已经存在钱包备注缓存
        Map<String, WalletRemarksInfo> walletRemarksMap = getRemarksList(context);
        if (walletRemarksMap == null || walletRemarksMap.isEmpty()) {
            // 没有则重新创建
            walletRemarksMap = new HashMap<>();
        }

        // 添加
        walletRemarksMap.put(createKey(chain, address), walletRemarksInfo);
        JSONObject walletRemarksJO = JSONObject.parseObject(JSON.toJSONString(walletRemarksMap));

        // 保存
        return PreferencesUtils.putString(context, WALLET_REMARKS_PREFERENCES_NAME, walletRemarksJO.toString());
    }

    /**
     * 修改钱包备注（原钱包备注不存在的话则返回false）
     *
     * @param context         上下文
     * @param chain           区块链类型
     * @param address         钱包地址
     * @param newWalletName   新钱包名称
     * @param newPasswordTips 新密码提示
     * @return 布尔值，修改成功返回true，失败返回false
     */
    public static boolean update(Context context, Chain chain, String address, String newWalletName, String newPasswordTips) {
        // 查询钱包备注
        WalletRemarksInfo walletRemarksInfo = query(context, chain, address);
        if (walletRemarksInfo == null) {
            LogUtils.d("未找到对应的钱包备注信息，更新失败!!!");
            return false;
        }

        if (!TextUtils.isEmpty(newWalletName)) {
            // 设置新钱包名
            walletRemarksInfo.setName(newWalletName);
        }

        if (!TextUtils.isEmpty(newPasswordTips)) {
            // 设置新密码提示
            walletRemarksInfo.setPasswordTips(newPasswordTips);
        }

        // 更新
        return insert(context, chain, address, walletRemarksInfo);
    }

    /**
     * 修改钱包备注（原钱包备注不存在的话则返回false）
     *
     * @param context       上下文
     * @param chain         区块链类型
     * @param address       钱包地址
     * @param newWalletName 新钱包名称
     * @return 布尔值，修改成功返回true，失败返回false
     */
    public static boolean update(Context context, Chain chain, String address, String newWalletName) {
        return update(context, chain, address, newWalletName, null);
    }

    /**
     * 查询指定钱包备注信息
     *
     * @param context 上下文
     * @param chain   区块链类型
     * @param address 钱包地址
     * @return 返回钱包备注对象 WalletRemarksInfo
     */
    public static WalletRemarksInfo query(Context context, Chain chain, String address) {

        Map<String, WalletRemarksInfo> walletRemarksMap = getRemarksList(context);

        if (walletRemarksMap != null && !walletRemarksMap.isEmpty()) {
            return walletRemarksMap.get(createKey(chain, address));
        }
        return null;
    }

    /**
     * 查询指定钱包备注信息是否存在
     *
     * @param context 上下文
     * @param chain   区块链类型
     * @param address 钱包地址
     * @return 如果存在返回true，不存在返回false
     */
    public static boolean walletRemarksExists(Context context, Chain chain, String address) {
        WalletRemarksInfo walletRemarksInfo = query(context, chain, address);
        return walletRemarksInfo != null;
    }

    /**
     * 钱包名称是否已经存在
     *
     * @param context    上下文
     * @param walletName 钱包名称
     * @return 如果存在返回true，不存在返回false
     */
    public static boolean walletNameExists(Context context, String walletName) {
        Map<String, WalletRemarksInfo> walletRemarksMap = getRemarksList(context);

        if (walletRemarksMap == null) {
            return false;
        }

        for (WalletRemarksInfo walletRemarksInfo : walletRemarksMap.values()) {
            if (walletRemarksInfo != null) {
                if (walletRemarksInfo.getName().equals(walletName)) {
                    LogUtils.d("查询的钱包名称已经存在...");
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 获取所有钱包备注信息
     *
     * @param context 上下文
     * @return 返回钱包备注键值对 Map<String, WalletRemarksInfo>
     */
    public static Map<String, WalletRemarksInfo> getRemarksList(Context context) {
        String walletRemarksStr = PreferencesUtils.getString(context, WALLET_REMARKS_PREFERENCES_NAME, "");
        if (!TextUtils.isEmpty(walletRemarksStr)) {
            try {
                JSONObject walletRemark = JSON.parseObject(walletRemarksStr);
                Map<String, WalletRemarksInfo> walletRemarksMap = new HashMap<>();

                // 遍历所有钱包备注信息
                for (Map.Entry<String, Object> entry : walletRemark.entrySet()) {
                    String address = entry.getKey();
                    WalletRemarksInfo walletRemarksInfo = JSON.toJavaObject((JSONObject) entry.getValue(), WalletRemarksInfo.class);
                    walletRemarksMap.put(address, walletRemarksInfo);
                }

                return walletRemarksMap;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    /**
     * 修改钱包备注名
     *
     * @param context       上下文
     * @param chain         区块链类型
     * @param address       钱包地址
     * @param newWalletName 新钱包名称
     * @param listener      回调，修改成功返回true，修改失败返回false
     */
    public static void renameWalletRemarks(Context context, Chain chain, String address, String newWalletName, IRequestListener<Boolean> listener) {

        // 判断钱包名称是否已经存在
        if (WalletRemarksUtils.walletNameExists(context, newWalletName)) {
            listener.onResult(ResultCode.WALLET_FILE_NAME_EXISTS, null);
            return;
        }

        try {
            // 更新
            boolean updateResult = WalletRemarksUtils.update(context, chain, address, newWalletName);

            if (updateResult) {
                LogUtils.d("更新钱包文件成功...");
                listener.onResult(ResultCode.SUCCESS, true);
            } else {
                LogUtils.d("更新钱包文件失败!!!");
                listener.onResult(ResultCode.FAIL, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d("修改钱包备注名时的钱包文件内容异常，无法解析!!!");
            listener.onResult(ResultCode.FAIL, null);
        }
    }

    /**
     * 创建钱包备注key
     *
     * @param chain   区块链类型
     * @param address 钱包地址
     * @return 返回String类型的钱包备注key
     */
    public static String createKey(Chain chain, String address) {
        return chain + " - " + address;
    }
}
