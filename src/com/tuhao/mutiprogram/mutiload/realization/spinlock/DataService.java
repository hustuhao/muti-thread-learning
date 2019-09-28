package com.tuhao.mutiprogram.mutiload.realization.spinlock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

import com.tuhao.mutiprogram.mutiload.main.ServerSemaphore;
import com.tuhao.mutiprogram.mutiload.dao.DataDao;

/**
 *
 */
public class DataService {
    private final DataDao dataDao = new DataDao();

    /**
     * 多线程查询数据库
     * @param userId
     * @return
     * @throws Exception
     */
    public List<String> getAllData(final String userId) throws Exception {
        /**
         * 其中 第一个参数为初始空闲
         * 第二个参数为最大线程
         * 第三个参数为超时时间
         * 第四个参数是超时时间的单位
         * 第五个参数是当超过最大线程数以后，可以放在队列中的线程
         * 第六个参数
         * 第七个参数是线程池塞满时候的策略
         */
        int corePoolSize = 2;
        int maximumPoolSize = 3;
        long keepAliveTime = 0;
        TimeUnit unit = TimeUnit.NANOSECONDS;
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<Runnable>(5);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        /**
         * AbortPolicy 如果总线成熟超过maximumPoolSize + workQueue
         * ，则跑异常java.util.concurrent.RejectedExecutionException
         */
        RejectedExecutionHandler handler = new AbortPolicy();

        // ExecutorService 为线程池的接口
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        List<Future<List<String>>> futures = new ArrayList<Future<List<String>>>();

        final int count = dataDao.getCount(userId); // 总记录数
        System.out.println("count=" + count);
        // 如果总记录数小于设置的阈值，就直接单线程查询
        int threadCount = ServerSemaphore.threadCount;
        if (count < ServerSemaphore.hold) {
            threadCount = 1;
        }
        int section = count / threadCount; // 区间大小
        // 创建线程
        for (int i = 0; i < threadCount; i++) {
            final int begin = i * section;
            final int end;
            // 最后一个区间判断
            if ((i + 1) == threadCount) {
                end = count;
            } else {
                end = (i + 1) * section;
            }
            System.out.print("begin=" + begin);
            System.out.print(",end=" + end);
            System.out.println(",size=" + (end - begin));

            // 根据总记录数count和线程数Server.threadCount进行分页任务分发
            Future<List<String>> future = executor.submit(new Callable<List<String>>() {
                @Override
                public List<String> call() throws Exception {
                    List<String> data = new ArrayList<String>();
                    try {
                        data = dataDao.find(userId, begin, end);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                    return data;
                }
            });
            futures.add(future);
        }

        this.waitForDone(futures); // 自旋锁等待所有的查询工作完成

        // 结果的组装
        List<String> reuslt = new ArrayList<String>();
        for (Future<List<String>> future : futures) {
            List<String> tmp = future.get();
            reuslt.addAll(tmp);
        }
        return reuslt;
    }

    /**
     * @fuction 判断每个任务是否完成
     * @param futures
     */
    private void waitForDone(List<Future<List<String>>> futures) {
        boolean done = false;
        while (!done) {
            done = true;
            for (Future<List<String>> future : futures) {
                future.isDone();
                if (!future.isDone()) {
                    done = false;
                    try {
                        Thread.sleep(50); //当前线程sleep，在主线程中调用这个函数等待所有的数据查询完成
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
}