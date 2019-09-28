package com.tuhao.mutiprogram.assembling.semahore;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 三个线程并行分别获取 ABC，最后组装 A B C
 *
 */
public class AssembingService {
    private final int N = 3;
    private Executor executor = Executors.newFixedThreadPool(N);
    private CharsSemaphore chars = new CharsSemaphore(0);

    public List<String> getAllData() throws Exception {
        int threadCount = N;
        // 创建线程
        //线程 1
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(1000);//模拟生成时间
                    chars.fillData("A");
                } catch (Throwable t) {
                    t.printStackTrace();
                } finally {
                    // 执行成功后，发放授权
                    chars.release();
                }
            }
        });
        //线程 2
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(10);//模拟生成时间
                    chars.fillData("B");
                } catch (Throwable t) {
                    t.printStackTrace();
                } finally {
                    // 执行成功后，发放授权
                    chars.release();
                }
            }
        });
        // 线程 3
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.currentThread().sleep(100);//模拟生成时间
                    chars.fillData("C");
                } catch (Throwable t) {
                    t.printStackTrace();
                } finally {
                    // 执行成功后，发放授权
                    chars.release();
                }
            }
        });

        chars.acquire(threadCount); // 等待授权数量满足条件，放行
        return chars.getResult();
    }
    public static void main(String[] args) throws Exception {
        AssembingService test = new AssembingService();
        List<String> list = test.getAllData(); //结果
    }
}
