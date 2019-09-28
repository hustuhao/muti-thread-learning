package com.tuhao.mutiprogram.mutiload.service;

import com.tuhao.mutiprogram.mutiload.main.ServerSemaphore;
import com.tuhao.mutiprogram.mutiload.dao.DataDao;
import com.tuhao.mutiprogram.mutiload.semaphore.DataSemaphore;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DataService {
    private Executor executor = Executors.newFixedThreadPool(4); // 共用线程池，是为了从全局角度，叫多线程可控
    private final DataDao dataDao = new DataDao();

    /**
     * 多线程查询数据库
     * @param userId
     * @return
     * @throws Exception
     */
    public List<String> getAllData(final String userId) throws Exception {
        final DataSemaphore semaphore = new DataSemaphore(0);

        final int count = dataDao.getCount(userId); // 总记录数
        System.out.println("count=" + count);
        // 如果总记录数小于设置的阈值，就直接单线程查询
        int threadCount = ServerSemaphore.threadCount;
        if (count < ServerSemaphore.hold) {
            threadCount = 1;
        }
        int section = count / threadCount; // 区间大小： count / 4
        // 循环创建线程
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
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<String> data = dataDao.find(userId, begin, end); // 该线程获取调用Dao获取数据
                        semaphore.fillData(data); // 填充数据
                    } catch (Throwable t) {
                        t.printStackTrace();
                    } finally {
                        // 执行成功后，发放授权
                        semaphore.release();
                    }
                }
            });
        }

        semaphore.acquire(threadCount); // 等待授权数量满足条件，放行
        return semaphore.getResult();
    }
}