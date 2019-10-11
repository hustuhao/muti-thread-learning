package com.tuhao.threadpool.two.rejectpolicy.impl;

import com.tuhao.threadpool.two.executor.MyThreadPoolExecutor;
import com.tuhao.threadpool.two.rejectpolicy.RejectPolicy;

/**
 * 丢弃当前任务
 */
public class DiscardRejectPolicy implements RejectPolicy {
    @Override
    public void reject(Runnable task, MyThreadPoolExecutor myThreadPoolExecutor) {
        // do nothing
        System.out.println("discard one task");
    }
}