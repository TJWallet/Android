package com.tianji.blockchain.utils;


import android.content.Context;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadManager {
    private ThreadPoolExecutor threadPoolExecutor;
    /**
     * 单例模式
     **/
    private static ThreadManager instance;

    private ThreadManager() {
        if (threadPoolExecutor == null) {
            threadPoolExecutor = new ThreadPoolExecutor(5, 10, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(50));
        }
    }

    public static ThreadManager getInstance() {
        if (instance == null) {
            instance = new ThreadManager();
        }
        return instance;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }
}
