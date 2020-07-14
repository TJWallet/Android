package com.tianji.blockchain.util;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tianji.blockchain.constant.Constants;
import com.tianji.blockchain.constant.enums.Chain;
import com.tianji.blockchain.constant.enums.HDWalletAddressPath;
import com.tianji.blockchain.constant.enums.ResultCode;
import com.tianji.blockchain.constant.enums.StorageSaveType;
import com.tianji.blockchain.constant.enums.WalletCreatedType;
import com.tianji.blockchain.constant.enums.WalletDir;
import com.tianji.blockchain.constant.enums.WalletProof;
import com.tianji.blockchain.entity.WalletFileInfo;
import com.tianji.blockchain.entity.WalletInfo;
import com.tianji.blockchain.entity.WalletRemarksInfo;
import com.tianji.blockchain.wallet.IRequestListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WalletFileUtils {

    /**
     * 检查钱包文件是否存在
     *
     * @param context         上下文
     * @param storageSaveType 钱包存储位置
     * @param chain           区块链类型
     * @param address         钱包地址
     * @return 布尔值，如果钱包文件存在返回true，不存在则返回false
     */
    public static boolean exists(Context context, StorageSaveType storageSaveType, Chain chain, String address) {
        String filePath = getPath(context, storageSaveType, chain, address);
        return exists(context, storageSaveType, filePath);
    }

    /**
     * 检查钱包文件是否存在
     *
     * @param context         上下文
     * @param storageSaveType 钱包存储位置
     * @param walletFilePath  钱包文件路径
     * @return 布尔值，如果钱包文件存在返回true，不存在则返回false
     */
    public static boolean exists(Context context, StorageSaveType storageSaveType, String walletFilePath) {
        switch (storageSaveType) {
            case LOCAL:
                File file = new File(walletFilePath);
                boolean isFileExists = file.exists();
                if (isFileExists) {
                    LogUtils.d("钱包文件存在...");
                } else {
                    LogUtils.d("钱包文件不存在!!!");
                }
                return isFileExists;
        }
        return false;
    }

    /**
     * 钱包确权
     *
     * @param context         上下文
     * @param storageSaveType 钱包存储位置
     * @param chain           区块链类型
     * @param address         钱包地址
     * @param password        钱包密码
     * @return 布尔值，如果钱包确权成功返回true，确权失败返回false
     */
    public static boolean walletVerify(Context context, StorageSaveType storageSaveType, Chain chain, String address, String password) {
        // 获取文件内容
        if (!exists(context, storageSaveType, chain, address)) {
            return false;
        }

        String walletFileContent = read(context, storageSaveType, getPath(context, storageSaveType, chain, address));
        if (TextUtils.isEmpty(walletFileContent)) {
            LogUtils.d("钱包不存在，钱包文件内容为空!!!");
            return false;
        }

        try {
            JSONObject walletFileJO = JSON.parseObject(walletFileContent);
            // 加密内容
            String encWalletProofContent = walletFileJO.getString("encWalletProofContent");
            // 解密
            String walletProofContent = AESUtils.decrypt(password, encWalletProofContent);

            if (TextUtils.isEmpty(walletProofContent)) {
                LogUtils.d("密码不正确!!!");
                return false;
            }

            LogUtils.d("密码验证通过...");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d("钱包文件内容异常，无法解析为JSON格式!!!");
            return false;
        }
    }

    /**
     * 删除钱包文件
     *
     * @param context         上下文
     * @param storageSaveType 钱包存储位置
     * @param chain           区块链类型
     * @param address         钱包地址
     * @return 布尔值，删除成功返回true，删除失败返回false
     */
    public static boolean deleteWithAddress(Context context, StorageSaveType storageSaveType, Chain chain, String address) {
        String filePath = getPath(context, storageSaveType, chain, address);
        return deleteWithPath(context, storageSaveType, filePath);
    }

    /**
     * 删除钱包文件
     *
     * @param context         上下文
     * @param storageSaveType 钱包存储位置
     * @param walletFilePath  钱包文件路径
     * @return 布尔值，删除成功返回true，删除失败返回false
     */
    public static boolean deleteWithPath(Context context, StorageSaveType storageSaveType, String walletFilePath) {
        switch (storageSaveType) {
            case LOCAL:
                return StorageUtils.deleteFileFromAppLocal(walletFilePath);
        }
        return false;
    }

    /**
     * 读取文件内容
     *
     * @param context         上下文
     * @param storageSaveType 钱包存储位置
     * @param walletFilePath  钱包文件路径
     * @return 返回String类型的钱包文件内容
     */
    public static String read(Context context, StorageSaveType storageSaveType, String walletFilePath) {
        switch (storageSaveType) {
            case LOCAL:
                return StorageUtils.readFileFromAppLocal(walletFilePath);
            default:
                return null;
        }
    }

    /**
     * 获取存储位置中指定区块链下的某个钱包地址路径
     *
     * @param context         上下文
     * @param storageSaveType 钱包存储位置
     * @param chain           区块链类型
     * @param address         钱包地址
     * @return 返回String类型的钱包文件路径
     */
    public static String getPath(Context context, StorageSaveType storageSaveType, Chain chain, String address) {
        String path = "";
        switch (storageSaveType) {
            case LOCAL:
                path = getDirPath(context, storageSaveType, chain) + File.separator + createName(address);
                break;
        }
        return path;
    }

    /**
     * 获取存储位置中指定区块链的钱包文件夹路径
     *
     * @param context         上下文
     * @param storageSaveType 钱包存储位置
     * @param chain           区块链类型
     * @return 返回String类型的钱包文件夹路径
     */
    public static String getDirPath(Context context, StorageSaveType storageSaveType, Chain chain) {
        String dirPath = "";
        switch (storageSaveType) {
            case LOCAL:
                dirPath = context.getFilesDir().getAbsolutePath() + File.separator + WalletDir.NORMAL.getName() + File.separator + chain.getName();
                break;
        }
        return dirPath;
    }

    /**
     * 创建钱包文件名称
     *
     * @param address 创建钱包文件名称
     * @return 返回String类型的钱包文件名称
     */
    public static String createName(String address) {
        return address + ".json";
    }

    /**
     * 保存钱包信息，钱包文件内容经过AES加密
     * 存储格式：JSON
     *
     * @param context         上下文
     * @param storageSaveType 钱包存储位置
     * @param chain           区块链类型
     * @param walletInfo      钱包信息
     * @param password        钱包密码
     * @param walletProof     钱包凭证
     * @return 布尔值，钱包文件保存成功返回true，保存失败返回false
     */
    public static boolean save(Context context, StorageSaveType storageSaveType, Chain chain, WalletInfo walletInfo, String password, String walletProof) {

        // 钱包备注保存
        WalletRemarksInfo walletRemarksInfo = new WalletRemarksInfo();
        walletRemarksInfo.setName(walletInfo.getWalletName());
        walletRemarksInfo.setPasswordTips(walletInfo.getPasswordTips());
        walletRemarksInfo.setCreatedTypeCode(walletInfo.getWalletCreatedType().getCode());
        walletRemarksInfo.setCreatedTime(walletInfo.getCreatedTime());

        WalletRemarksUtils.insert(context, chain, walletInfo.getAddress(), walletRemarksInfo);

        //加密钱包文件内容
        String encWalletProofContent;
        if (!TextUtils.isEmpty(walletProof)) {
            encWalletProofContent = AESUtils.encrypt(password, walletProof);
            if (TextUtils.isEmpty(encWalletProofContent)) {
                LogUtils.d("钱包文件内容加密失败!!!");
                return false;
            }
        } else {
            LogUtils.d("钱包文件内容为空!!!");
            return false;
        }

        // 文件内容使用AES加密
        WalletFileInfo walletFileInfo = new WalletFileInfo();
        walletFileInfo.setWalletProof(walletInfo.getWalletProof());
        walletFileInfo.setChildAccountPos(walletInfo.getChildAccountPos());
        walletFileInfo.setHdWalletAddressPath(walletInfo.getHdWalletAddressPath());
        walletFileInfo.setEncWalletProofContent(encWalletProofContent);
        walletFileInfo.setCreateTime(walletInfo.getCreatedTime());

        // 存储钱包信息
        return save(context, storageSaveType, chain, walletInfo.getAddress(), walletFileInfo.toJSON().toString());
    }

    /**
     * 根据存储位置，将指定的钱包文件内容写入到对应区块链文件夹中
     *
     * @param context           上下文
     * @param storageSaveType   钱包存储位置
     * @param chain             区块链类型
     * @param address           钱包地址
     * @param walletFileContent 钱包文件内容
     * @return 布尔值，钱包文件保存成功返回true，保存失败返回false
     */
    public static boolean save(Context context, StorageSaveType storageSaveType, Chain chain, String address, String walletFileContent) {
        String dirPath = getDirPath(context, storageSaveType, chain);
        String walletFileName = createName(address);

        switch (storageSaveType) {
            case LOCAL: // 本地钱包存储
                return StorageUtils.writeFileToAppLocal(dirPath, walletFileName, walletFileContent);
        }
        return false;
    }

    /**
     * 获取应用内钱包列表
     *
     * @param context  上下文
     * @param listener 回调
     */
    public static void getWalletListFromAppLocal(Context context, IRequestListener<Map<Chain, List<WalletInfo>>> listener) {

        Map<Chain, List<WalletInfo>> walletMap = new HashMap<>();

        // 本地，遍历获取所有文件里以及文件夹内信息
        String folderPath = context.getFilesDir().getAbsolutePath() + File.separator + WalletDir.NORMAL.getName() + File.separator;
        File folder = new File(folderPath);
        File[] chainArray = folder.listFiles();
        if (chainArray != null && chainArray.length > 0) {
            for (File chainFolder : chainArray) {
                String chainName = chainFolder.getName();
                Chain chain = Chain.getEnumByName(chainName);

                List<WalletInfo> walletInfoList = new ArrayList<>();

                // 遍历所有钱包文件账号
                File[] walletArray = chainFolder.listFiles();

                if (walletArray == null) {
                    continue;
                }

                for (File wallet : walletArray) {

                    String address = wallet.getName().replace(".json", "");

                    // 获取钱包文件内容
                    String walletFileContent = WalletFileUtils.read(context, StorageSaveType.LOCAL, wallet.getAbsolutePath());
                    // 解析钱包文件内容
                    WalletFileInfo walletFileInfo = parseLocalWalletFileContent(walletFileContent);
                    if (walletFileInfo == null) {
                        listener.onResult(ResultCode.WALLET_FILE_CONTENT_ERROR, null);
                        return;
                    }

                    // 将钱包信息JSON格式转化为实体类
                    WalletInfo walletInfo = new WalletInfo();

                    WalletRemarksInfo walletRemarksInfo = WalletRemarksUtils.query(context, chain, address);
                    if (walletRemarksInfo == null) {
                        LogUtils.d("文件备注不存在!!!");
                        listener.onResult(ResultCode.FAIL, null);
                        return;
                    }

                    walletInfo.setChain(chain);
                    walletInfo.setAddress(address);
                    walletInfo.setStorageSaveType(StorageSaveType.LOCAL);

                    // 备注信息
                    walletInfo.setWalletName(walletRemarksInfo.getName());
                    walletInfo.setPasswordTips(walletRemarksInfo.getPasswordTips());
                    walletInfo.setWalletCreatedType(WalletCreatedType.getEnumByCode(walletRemarksInfo.getCreatedTypeCode()));
                    walletInfo.setCreatedTime(walletRemarksInfo.getCreatedTime());

                    // 钱包文件信息
                    walletInfo.setHdWalletAddressPath(walletFileInfo.getHdWalletAddressPath());
                    walletInfo.setChildAccountPos(walletFileInfo.getChildAccountPos());
                    walletInfo.setWalletProof(walletFileInfo.getWalletProof());

                    // 添加
                    walletInfoList.add(walletInfo);
                }

                try {
                    // 添加数据
                    walletMap.put(Chain.getEnumByName(chainName), walletInfoList);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.d(chainName + " 钱包中的账户解析异常!!!");
                    listener.onResult(ResultCode.WALLET_FILE_CONTENT_ERROR, walletMap);
                    return;
                }
            }
            listener.onResult(ResultCode.SUCCESS, walletMap);
            return;
        }
        LogUtils.d("未发现钱包数据...");
        listener.onResult(ResultCode.SUCCESS, walletMap);

    }

    /**
     * 更新钱包文件(安全更新)
     *
     * @param context         上下文
     * @param storageSaveType 钱包存储位置
     * @param chain           区块链类型
     * @param address         钱包地址
     * @param odlPassword     旧钱包密码
     * @param newPassword     新钱包密码
     * @param walletFileJO    钱包文件内容JSON格式
     * @param listener        回调
     */
    public static void updateWalletFile(Context context,
                                        StorageSaveType storageSaveType,
                                        Chain chain,
                                        String address,
                                        String odlPassword,
                                        String newPassword,
                                        JSONObject walletFileJO,
                                        IRequestListener<Boolean> listener) {

        // 第一步：备份钱包
        boolean isBakSuccess = createBakWalletFile(context, storageSaveType, chain, address);
        if (!isBakSuccess) {
            LogUtils.d("创建备份文件不成功，更新钱包文件失败!!!");
            listener.onResult(ResultCode.FAIL, false);
            return;
        } else {
            LogUtils.d("创建备份文件成功...");
        }

        // 第二步：校验备份的钱包，以防止备份的时候出错导致钱包无法恢复
        if (!WalletFileUtils.walletVerify(context, storageSaveType, chain, address, odlPassword)) {
            LogUtils.d("备份文件校验密码不成功，更新钱包文件失败!!!");
            // 删除备份钱包
            boolean deleteResult = deleteBakWalletFile(context, storageSaveType, getBakWalletFilePath(context, storageSaveType, chain, address));
            LogUtils.d("删除备份钱包结果 = " + deleteResult);
            listener.onResult(ResultCode.WALLET_FILE_PASSWORD_ERROR, false);
            return;
        } else {
            LogUtils.d("备份文件校验成功...");
        }

        // 第三步：删除原钱包
        if (!WalletFileUtils.deleteWithAddress(context, storageSaveType, chain, address)) {
            LogUtils.d("删除原钱包不成功，更新钱包文件失败!!!");
            // 删除备份钱包
            boolean deleteResult = deleteBakWalletFile(context, storageSaveType, getBakWalletFilePath(context, storageSaveType, chain, address));
            LogUtils.d("删除备份钱包结果 = " + deleteResult);
            listener.onResult(ResultCode.FAIL, false);
            return;
        } else {
            LogUtils.d("删除原钱包成功...");
        }

        // 第四步：保存新钱包
        boolean saveResult = WalletFileUtils.save(context, storageSaveType, chain, address, walletFileJO.toJSONString());
        if (saveResult) {
            LogUtils.d("创建新钱包成功...");
            // 第五步：校验新钱包
            if (WalletFileUtils.walletVerify(context, storageSaveType, chain, address, newPassword)) {
                LogUtils.d("新钱包校验成功...");
                // 第六步：删除备份钱包
                boolean deleteResult = deleteBakWalletFile(context, storageSaveType, getBakWalletFilePath(context, storageSaveType, chain, address));
                LogUtils.d("删除备份钱包结果 = " + deleteResult);
                listener.onResult(ResultCode.SUCCESS, true);
                return;
            } else {
                LogUtils.d("新钱包校验失败!!!");
                // 删除新钱包
                WalletFileUtils.deleteWithAddress(context, storageSaveType, chain, address);
            }
        } else {
            LogUtils.d("创建新钱包失败!!!");
        }

        // 创建新钱包失败，使用备用钱包恢复
        LogUtils.d("使用备份钱包恢复...");
        if (recoverBakWalletFile(context, storageSaveType, chain, address)) {
            // 删除备份钱包
            boolean deleteResult = deleteBakWalletFile(context, storageSaveType, getBakWalletFilePath(context, storageSaveType, chain, address));
            LogUtils.d("删除备份钱包结果 = " + deleteResult);
            LogUtils.d("创建新钱包不成功，恢复之前的备份钱包为正式文件，更新钱包文件失败!!!");
        } else {
            LogUtils.d("备份钱包恢复失败!!!");
        }
        listener.onResult(ResultCode.FAIL, false);
    }

    /**
     * 删除备份钱包文件的钱包
     *
     * @param context           上下文
     * @param storageSaveType   钱包存储位置
     * @param bakWalletFilePath 备份钱包文件路径
     * @return 返回删除结果，布尔值
     */
    private static boolean deleteBakWalletFile(Context context, StorageSaveType storageSaveType, String bakWalletFilePath) {
        if (!WalletFileUtils.exists(context, storageSaveType, bakWalletFilePath)) {
            LogUtils.d("备份文件不存在，删除失败!!!");
            return true;
        }
        return StorageUtils.deleteFileFromAppLocal(bakWalletFilePath);
    }

    /**
     * 给指定的钱包文件在应用本地文件目录下创建备份文件
     *
     * @param context         上下文
     * @param storageSaveType 钱包存储位置
     * @param chain           区块链类型
     * @param address         钱包地址
     * @return 布尔值，备份文件创建成功返回true，失败返回false，布尔值
     */
    private static boolean createBakWalletFile(Context context, StorageSaveType storageSaveType, Chain chain, String address) {
        // 第一步确认钱包文件是否存在
        boolean isWalletFileExists = WalletFileUtils.exists(context, storageSaveType, chain, address);
        if (!isWalletFileExists) {
            LogUtils.d("钱包文件不存在，无法备份!!!");
            return false;
        }

        // 第二步确认备份文件夹是否存在
        String bakDirPath = getBakWalletDirPath(context, storageSaveType, chain);
        if (!StorageUtils.isFileExists(bakDirPath)) {
            LogUtils.d("备份文件夹不存在，正在创建备份文件夹...");
            // 创建文件夹
            if (!StorageUtils.mkdirs(bakDirPath)) {
                LogUtils.d("备份文件夹创建失败，无法备份!!!");
                return false;
            } else {
                LogUtils.d("备份文件夹创建成功...");
            }
        }

        // 第三步读取钱包文件内容
        String fileContent = WalletFileUtils.read(context, storageSaveType, WalletFileUtils.getPath(context, storageSaveType, chain, address));
        if (fileContent == null) {
            LogUtils.d("读取钱包文件返回内容为空，无法备份!!!");
            return false;
        }
        // 第四步创建备份文件并写入
        boolean writeResult = StorageUtils.writeFileToAppLocal(bakDirPath, createBakWalletFileName(address), fileContent);
        if (!writeResult) {
            LogUtils.d("创建备份文件并写入时失败，无法备份!!!");
            return false;
        }
        LogUtils.d("创建备份文件成功...");
        return true;
    }

    /**
     * 恢复之前的备份钱包为正式文件
     *
     * @param context         上下文
     * @param storageSaveType 钱包存储位置
     * @param chain           区块链类型
     * @param address         钱包地址
     * @return 布尔值，恢复成功返回true，失败返回false
     */
    private static boolean recoverBakWalletFile(Context context, StorageSaveType storageSaveType, Chain chain, String address) {
        LogUtils.d("执行恢复程序 - 恢复备份钱包为正式文件");
        // 判断原钱包文件是否存在
        if (WalletFileUtils.exists(context, storageSaveType, chain, address)) {
            // 删除原钱包
            if (!WalletFileUtils.deleteWithAddress(context, storageSaveType, chain, address)) {
                LogUtils.d("原钱包无法删除，恢复之前的备份钱包为正式文件失败!!!");
                return false;
            }
        }

        String bakWalletFilePath = getBakWalletFilePath(context, storageSaveType, chain, address);
        // 文件是否存在
        if (!WalletFileUtils.exists(context, storageSaveType, bakWalletFilePath)) {
            LogUtils.d("备份文件不存在");
            return false;
        }
        // 获取备份文件内容
        String bakWalletFileContent = WalletFileUtils.read(context, StorageSaveType.LOCAL, getBakWalletFilePath(context, storageSaveType, chain, address));

        if (!WalletFileUtils.save(context, storageSaveType, chain, address, bakWalletFileContent)) {
            LogUtils.d("恢复之前的备份钱包为正式文件失败");
            return false;
        }
        LogUtils.d("恢复之前的备份钱包为正式文件成功");
        return true;
    }

    /**
     * 获取本地存储位置中指定区块链的备份钱包文件路径
     *
     * @param context         上下文
     * @param storageSaveType 钱包存储位置
     * @param chain           区块链类型
     * @param address         钱包地址
     * @return 返回String类型的钱包文件路径
     */
    protected static String getBakWalletFilePath(Context context, StorageSaveType storageSaveType, Chain chain, String address) {
        return getBakWalletDirPath(context, storageSaveType, chain) + File.separator + createBakWalletFileName(address);
    }

    /**
     * 获取本地存储位置中指定区块链的备份文件夹路径
     *
     * @param context         上下文
     * @param storageSaveType 钱包存储位置
     * @param chain           区块链类型
     * @return 返回String类型的区块链备份文件夹路径
     */
    protected static String getBakWalletDirPath(Context context, StorageSaveType storageSaveType, Chain chain) {
        String dirPath = "";
        switch (storageSaveType) {
            case LOCAL:
                dirPath = context.getFilesDir().getAbsolutePath() + File.separator + WalletDir.BAK_LOCAL.getName() + File.separator + chain.getName();
                break;
        }
        return dirPath;
    }

    /**
     * 格式化备份钱包文件名
     *
     * @param address 钱包地址
     * @return 返回String类型
     */
    protected static String createBakWalletFileName(String address) {
        // 如果传入的地址含有0x，去除
        address = address.replace("0x", "").toLowerCase();
        return address + ".json.bak";
    }

    /**
     * 将钱包文件内容解析为WalletFileInfo对象
     *
     * @param walletFileContent 钱包文件内容
     * @return 返回WalletFileInfo对象
     */
    public static WalletFileInfo parseLocalWalletFileContent(String walletFileContent) {

        WalletFileInfo walletFileInfo;

        // 文件内容非空判断
        if (TextUtils.isEmpty(walletFileContent)) {
            LogUtils.d("钱包文件内容为空");
            return null;
        }

        // 判断钱包文件内容格式
        JSONObject walletFileJO;
        try {
            walletFileJO = JSON.parseObject(walletFileContent);
        } catch (Exception e) {
            LogUtils.d("钱包文件内容异常，无法解析为jSON格式");
            e.printStackTrace();
            return null;
        }

        // 加密内容
        String encWalletProofContent = walletFileJO.getString("encWalletProofContent");
        // 文件内容非空判断
        if (TextUtils.isEmpty(encWalletProofContent)) {
            LogUtils.d("加密内容为空");
            return null;
        }

        // HD协议地址码
        Integer hdWalletAddressPathCode = walletFileJO.getInteger("hdWalletAddressPathCode");
        // 文件内容非空判断
        if (hdWalletAddressPathCode == null) {
            LogUtils.d("HD协议地址码为空");
            return null;
        }

        // 子钱包位置
        Integer childAccountPos = walletFileJO.getInteger("childAccountPos");
        // 文件内容非空判断
        if (childAccountPos == null) {
            LogUtils.d("子钱包位置为空");
            return null;
        }

        // 钱包凭证码
        Integer walletProofCode = walletFileJO.getInteger("walletProofCode");
        // 文件内容非空判断
        if (walletProofCode == null) {
            LogUtils.d("钱包凭证码为空");
            return null;
        }

        // 钱包创建时间
        Long createTime = walletFileJO.getLong("createTime");
//        // 文件内容非空判断
//        if (createTime == null) {
//            LogUtils.d("钱包创建时间为空");
//            return null;
//        }

        walletFileInfo = new WalletFileInfo();
        walletFileInfo.setEncWalletProofContent(encWalletProofContent);
        walletFileInfo.setHdWalletAddressPath(HDWalletAddressPath.getEnumByCode(hdWalletAddressPathCode));
        walletFileInfo.setChildAccountPos(childAccountPos);
        walletFileInfo.setWalletProof(WalletProof.getEnumByCode(walletProofCode));

        return walletFileInfo;
    }
}
