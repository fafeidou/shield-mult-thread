package com.shield.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * COPYRIGHT © 2005-2018 CHARLESKEITH ALL RIGHTS RESERVED.
 *
 * @author Batman.qin
 * @create 2019-05-06 9:14
 */
public class ThreadPoolService {

    private ThreadPoolService() {

    }

    private static final int CORE_CPUS = Runtime.getRuntime().availableProcessors();


    public static ExecutorService getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    private enum Singleton {
        INSTANCE;

        private ExecutorService executor;

        Singleton() {
            executor = new ThreadPoolExecutor(
                    CORE_CPUS, 10, 60L, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(6000));
        }

        public ExecutorService getInstance() {
            return executor;
        }
    }
}
