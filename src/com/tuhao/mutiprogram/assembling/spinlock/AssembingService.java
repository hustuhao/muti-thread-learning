package com.tuhao.mutiprogram.assembling.spinlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.*;

public class AssembingService {
    public final int N = 3;
    //创建线程池
    private Executor executor = Executors.newFixedThreadPool(N);
    //任务
    //创建任务集合
    List<Future<String>> taskList = new ArrayList();
    //创建线程池
    public List<String> getAllData() throws Exception {
        int threadCount = N;
        //记录结果,注意这里需要使用线程安全的集合！
        List<String> resList = new Vector<>();
        FutureTask<String> ftk1 = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                resList.add("A");
                return "A";
            }
        });

        FutureTask<String> ftk2 = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                resList.add("B");
                return "B";
            }
        });

        FutureTask<String> ftk3 = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                resList.add("C");
                return "C";
            }
        });

        taskList.add(ftk1);
        taskList.add(ftk2);
        taskList.add(ftk3);

        executor.execute(ftk1);
        executor.execute(ftk2);
        executor.execute(ftk3);

        waitForDone(taskList);

        return resList;
    }



    /**
     * @fuction 判断每个任务是否完成
     * @param futures
     */
    private void waitForDone(List<Future<String>> futures) {
        boolean done = false;
        while (!done) {
            done = true;
            for (Future<String> future : futures) {
                future.isDone();
                if (!future.isDone()) {//任务是否完成
                    done = false;
                    try {
                        //休眠50秒之后继续判断任务是否完成
                        Thread.sleep(50); //自旋锁：当前线程sleep，在主线程中调用这个函数等待所有的数据查询完成
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }
}
