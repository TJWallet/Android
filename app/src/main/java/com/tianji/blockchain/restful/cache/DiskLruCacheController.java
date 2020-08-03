package com.tianji.blockchain.restful.cache;

import android.content.Context;
import android.text.TextUtils;

import com.jakewharton.disklrucache.DiskLruCache;
import com.tianji.blockchainwallet.constant.enums.ResultCode;
import com.tianji.blockchain.utils.LogUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import okhttp3.FormBody;

public class DiskLruCacheController {
    private static DiskLruCacheController instance;
    private DiskLruCache diskLruCache;

    private DiskLruCacheController(Context context) {
        if (diskLruCache == null) {
            File cacheFile = new File(context.getCacheDir().getPath(), "OkHttpCache");
            LogUtils.i(context.getCacheDir().getPath() + "/OkHttpCache");
            if (!cacheFile.exists()) {
                cacheFile.mkdirs();
            }
            try {
                diskLruCache = DiskLruCache.open(cacheFile, 1, 1, 10 * 1024 * 1024);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static DiskLruCacheController getInstance(Context context) {
        if (instance == null) {
            synchronized (DiskLruCacheController.class) {
                if (instance == null) {
                    instance = new DiskLruCacheController(context);
                }
            }
        }
        return instance;
    }

    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public void saveCache(final String key, final String result) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DiskLruCache.Editor editor;

                    editor = diskLruCache.edit(key);
                    if (editor != null) {
                        OutputStream outputStream = editor.newOutputStream(0);

                        outputStream.write(result.getBytes());

                        if (result != null && !TextUtils.isEmpty(result)) {
                            editor.commit();
                        } else {
                            editor.abort();
                        }
                    }
                    //频繁的flush
                    diskLruCache.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void getCacheString(String key, OnLoadDiskLruCacheListener onLoadDiskLruCacheListener) {
        String tempCache;
        LogUtils.i("getCacheString key = " + key);
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            if (snapshot != null) {
                tempCache = convertStreamToString(snapshot.getInputStream(0));
                if (!TextUtils.isEmpty(tempCache)) {
                    onLoadDiskLruCacheListener.load(ResultCode.SUCCESS, tempCache);
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        onLoadDiskLruCacheListener.load(ResultCode.FAIL, null);
    }

    public String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private boolean downloadUrlToStream(String urlString, OutputStream outputStream) {
        HttpURLConnection urlConnection = null;
        BufferedOutputStream out = null;
        BufferedInputStream in = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream(), 8 * 1024);
            out = new BufferedOutputStream(outputStream, 8 * 1024);
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public String createKey(String url, FormBody formBody) {
        final StringBuffer sb = new StringBuffer();
        sb.append(url);
        for (int i = 0; i < formBody.size(); i++) {
            sb.append("&&" + formBody.name(i) + "=" + formBody.value(i));
        }

        return hashKeyForDisk(sb.toString());
    }

    public String createKey(String url, Map<String, String> params) {
        final StringBuffer sb = new StringBuffer();
        sb.append(url);

        for (Map.Entry<String, String> param : params.entrySet()) {
            sb.append("&&" + param.getKey() + "=" + param.getValue());
        }

        return hashKeyForDisk(sb.toString());
    }
}
