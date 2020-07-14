package com.tianji.blockchain.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class StorageUtils {

    /**
     * 根据指定路径在APP本地创建文件并写入
     *
     * @param dirPath     文件夹路径
     * @param fileName    文件名
     * @param fileContent 文件内容
     * @return 布尔值，文件写入成功返回true，失败返回false
     */
    public static boolean writeFileToAppLocal(String dirPath, String fileName, String fileContent) {
        String filePath = dirPath + File.separator + fileName;
        try {

            // 创建目录
            File folder = new File(dirPath);
            if (!folder.exists()) {
                // 文件夹不存在则创建
                boolean folderCreatedResult = folder.mkdirs();
                if (!folderCreatedResult) {
                    LogUtils.d("文件夹创建失败!!!");
                    return false;
                }
            }

            // 创建文件
            File file = new File(filePath);
            if (!file.exists()) {

                // 文件不存在则创建
                boolean fileCreatedResult = file.createNewFile();
                if (!fileCreatedResult) {
                    LogUtils.d("文件创建失败!!!");
                    return false;
                }
            }

            // 写入
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write(fileContent);
            fileWriter.flush();
            fileWriter.close();

            LogUtils.d("本地创建文件 " + filePath + " 成功...");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogUtils.d("本地创建文件 " + filePath + " 失败!!!");
        return false;
    }

    /**
     * 根据指定路径删除文件
     *
     * @param filePath 文件路径
     * @return 布尔值，删除成功返回true，失败返回false
     */
    public static boolean deleteFileFromAppLocal(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                LogUtils.d("文件不存在!!!");
                return false;
            } else {
                if (file.delete()) {
                    LogUtils.d("文件删除成功...");
                    return true;
                } else {
                    LogUtils.d("文件删除失败!!!");
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d("文件删除失败，发生异常...");
            return false;
        }
    }

    /**
     * 根据指定路径从APP本地读取文件
     *
     * @param filePath 文件路径
     * @return 返回String类型的文件内容，否则返回null
     */
    public static String readFileFromAppLocal(String filePath) {

        try {
            // 创建文件
            File file = new File(filePath);
            if (!file.exists()) {
                LogUtils.d("文件不存在，读取失败!!!");
                return null;
            }

            // 读取
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String readLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((readLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(readLine);
            }
            bufferedReader.close();
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogUtils.d("文件读取失败!!!");
        return null;
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件路径
     * @return 布尔值，文件存在返回true，不存在返回false
     */
    public static boolean isFileExists(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * 创建文件夹
     *
     * @param dirPath 文件夹路径
     * @return 布尔值，文件夹存在返回true，不存在返回false
     */
    public static boolean mkdirs(String dirPath) {
        return new File(dirPath).mkdirs();
    }
}
