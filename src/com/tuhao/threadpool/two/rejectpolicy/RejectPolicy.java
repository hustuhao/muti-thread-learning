package com.tuhao.threadpool.two.rejectpolicy;

import com.tuhao.threadpool.two.executor.MyThreadPoolExecutor;

public interface RejectPolicy {
    void reject(Runnable task, MyThreadPoolExecutor myThreadPoolExecutor);
}
