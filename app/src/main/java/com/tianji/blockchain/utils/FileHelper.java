package com.tianji.blockchain.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;

public class FileHelper {
    public static String configRootPath;
    public static String generalModelPath;
    public static String specialModelPath;

    public static String rootPath;

    public final static String rootConfigFileName = "rootConfig.json";
    public final static String recommendJsonFileName = "recommend.json";
    public final static String localMyAppFileName = "localMyApp.json";
    public final static String remoteGamesFileName = "remoteGames.json";
    public final static String remoteAllAppsFileName = "remoteAllApps.json";
    public final static String totalRanklistFileName = "totalRanklist.json";

    private static final String TAG = FileHelper.class.getSimpleName();

    private static String testFilePath;

    public enum ConfigFileType {
        ROOT_CONFIG, GENERAL_MODEL, SPECIAL_MODEL
    }

    public static void init(Context context) {
        File rootPath = context.getFilesDir();

        FileHelper.rootPath = rootPath.getAbsolutePath();
        //FileHelper.rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();

        configRootPath = FileHelper.rootPath + File.separator + "config";
        File configFile = new File(configRootPath);
        if (!configFile.exists()) {
            configFile.mkdir();
        }

        generalModelPath = configRootPath + File.separator + "generalModel";
        File generalModelFile = new File(generalModelPath);
        if (!generalModelFile.exists()) {
            generalModelFile.mkdir();
        }

        specialModelPath = configRootPath + File.separator + "specialModel";
        File specialModelFile = new File(specialModelPath);
        if (!specialModelFile.exists()) {
            specialModelFile.mkdir();
        }


        //每次初始化时，都判断data/data/...目录下时候有配置根文件，判断是否存在通模
        copyAssets(context);

        //testFilePath = context.getExternalFilesDir("tinyData").getAbsolutePath() + File.separator + "testData.txt";
        //deleteFile(testFilePath);
    }

    public static void appendStringToTestDataFile(String s) {
        File file = new File(testFilePath);
        writeFile(file, s, true);
    }

    /**
     * 从assets目录下拷贝文件到data目录
     */
    private static void copyAssets(Context context) {
        try {
            // 若推荐页缓存不存在，从assets/jsonData目录下拷贝
            if (!fileExist(FileHelper.rootPath + File.separator + recommendJsonFileName)) {
                copyAssetsPath(context, "jsonData", FileHelper.rootPath);
            }

            String rootFilePath = configRootPath + File.separator + rootConfigFileName;

            if (!fileExist(rootFilePath)) {
                copyAssetsPath(context, "configRoot", configRootPath);
            }

            //获取通用手模文件夹下的手模数
            int generalFileNum = getFileNumber(generalModelPath);
            if (generalFileNum == 0) {
                copyAssetsPath(context, "configGeneral", generalModelPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定目录下文件数目
     *
     * @param path
     * @return
     */
    public static int getFileNumber(String path) {
        File f = new File(path);
        File[] files = f.listFiles();

        if (files == null) {
            return 0;
        } else {
            return files.length;
        }
    }

    /**
     * 复制单个文件
     *
     * @param oldFilePath String 原文件路径如：c:/fqf.txt
     * @param newFilePath String 复制后路径如：f:/fqf.txt
     * @param isDelete    boolean 是否删除原文件
     */
    public static boolean copyFile(String oldFilePath, String newFilePath, boolean isDelete) {
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            input = new FileInputStream(oldFilePath);
            output = new FileOutputStream(newFilePath);
            FileChannel inputFileChannecl = input.getChannel();
            inputFileChannecl.transferTo(0, inputFileChannecl.size(), output.getChannel());
            if (isDelete) {
                new File(oldFilePath).delete();
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != input) {
                    input.close();
                }
                if (null != output) {
                    output.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 复制单个文件
     *
     * @param oldPath     String 原文件路径如：c:/fqf.txt
     * @param newFilePath String 复制后路径如：f:/fqf.txt
     */
    public static boolean copyFile(InputStream input, String newFilePath) {
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(newFilePath);
            byte bytes[] = new byte[1024 * 10];
            int temp = 0;
            while ((temp = input.read(bytes)) != -1) {
                output.write(bytes, 0, temp);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != output) {
                    output.close();
                }
                if (null != input) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    public static void copyAssetsPath(Context context, String oldFilePath, String newFilePath) {
        // 创建要复制到的目标目录
        createPath(newFilePath);
        AssetManager am = context.getAssets();
        try {
            String[] fileOrDirName = am.list(oldFilePath);
            for (int i = 0; i < fileOrDirName.length; i++) {
                String currentFileName = fileOrDirName[i];
                if (!fileExist(newFilePath + File.separator + currentFileName))
                    copyFile(am.open(oldFilePath + File.separator + currentFileName), newFilePath + File.separator + currentFileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件绝对路径
     * @return
     */
    public static boolean fileExist(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            return false;
        }
        boolean bool = file.exists();
        return bool;
    }

    /**
     * 删除单个文件
     *
     * @param path 文件路径
     * @return {@code false}删除文件失败，{@code true}成功删除文件
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (!file.isDirectory()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 创建目录
     */
    public static void createPath(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String readFile(String path) {
        String content = "";
        File file = new File(path);
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            content = readInputStream(fin);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (null != fin) {
                try {
                    fin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return content;
    }

    public static void writeFileToFilesDir(String content, String fileName) {
        String path = FileHelper.rootPath + File.separator + fileName;
        writeFile(new File(path), content);
    }

    public static String readFileFromFilesDir(String fileName) {
        return readFile(FileHelper.rootPath + File.separator + fileName);
    }

    public static void saveRecommendJsonToFile(String content) {
        String path = FileHelper.rootPath + File.separator + recommendJsonFileName;
        writeFile(new File(path), content);
    }

    public static String getRecommendJsonFromFile() {
        String path = FileHelper.rootPath + File.separator + recommendJsonFileName;
        return readFile(path);
    }

    public static void saveLocalMyAppJsonToFile(String content) {
        String path = FileHelper.rootPath + File.separator + localMyAppFileName;
        writeFile(new File(path), content);
    }

    public static String getLocalMyAppJsonFromFile() {
        String path = FileHelper.rootPath + File.separator + localMyAppFileName;
        return readFile(path);
    }

    public static void saveRemoteGamesJsonToFile(String content) {
        String path = FileHelper.rootPath + File.separator + remoteGamesFileName;
        writeFile(new File(path), content);
    }

    public static String getRemoteGamesJsonFromFile() {
        String path = FileHelper.rootPath + File.separator + remoteGamesFileName;
        return readFile(path);
    }

    public static void saveRemoteAllAppsJsonToFile(String content) {
        String path = FileHelper.rootPath + File.separator + remoteAllAppsFileName;
        writeFile(new File(path), content);
    }

    public static String getRemoteAllAppsJsonFromFile() {
        String path = FileHelper.rootPath + File.separator + remoteAllAppsFileName;
        return readFile(path);
    }

    public static void saveTotalRanklistJsonToFile(String content) {
        String path = FileHelper.rootPath + File.separator + totalRanklistFileName;
        writeFile(new File(path), content);
    }

    public static String getTotalRanklistJsonFromFile() {
        String path = FileHelper.rootPath + File.separator + totalRanklistFileName;
        return readFile(path);
    }

    public static void writeFile(File file, String content, boolean isAppend) {
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file, isAppend);
            fout.write(content.getBytes());
            fout.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeFile(File file, String content) {
        writeFile(file, content, false);
    }

    public static void writeFile(String fileName, String content, ConfigFileType type) {
        if (null == configRootPath || null == generalModelPath || null == specialModelPath) {
            return;
        }

        File file = null;
        if (type == ConfigFileType.GENERAL_MODEL) {
            file = new File(generalModelPath + File.separator + fileName);
        } else if (type == ConfigFileType.SPECIAL_MODEL) {
            file = new File(specialModelPath + File.separator + fileName);
        } else {
            file = new File(configRootPath + File.separator + fileName);
        }

        writeFile(file, content);
    }

    /**
     * 得到输入流中的数据
     *
     * @param input
     * @return
     */
    public static String readInputStream(InputStream input) {
        StringBuffer result = new StringBuffer();
        try {
            InputStreamReader read = new InputStreamReader(input, "utf-8");
            BufferedReader reader = new BufferedReader(read);
            String temp;
            while ((temp = reader.readLine()) != null) {
                result.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static String getModeFileName(String relativePath) {
        int index = relativePath.lastIndexOf('/');
        if (index != -1) {
            return relativePath.substring(index + 1);
        }
        return null;
    }
}
