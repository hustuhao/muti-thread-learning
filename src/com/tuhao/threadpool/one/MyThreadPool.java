package com.tuhao.threadpool.one;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/*
线程池 = 有界/无界队列<Runable> + List<Thread> + 饱和策略
原文地址： https://my.oschina.net/u/4129361/blog/3056289
         https://my.oschina.net/u/4108008/blog/3114587
*/
public class MyThreadPool {

    /*阻塞队列，线程安全*/
    private BlockingQueue<Runnable> blockingQueue = null;

    /*线程数组*/
    private ArrayList<Thread> allThreads = null;

    /*线程池的工作状态*/
    private boolean isWorking = true;

    /**
     *
     * @param poolSize   线程池大小：线程数量
     * @param queueSize  阻塞队列大小：任务数量
     */
    MyThreadPool(int poolSize,int queueSize){
        blockingQueue = new LinkedBlockingQueue(queueSize);
        allThreads = new ArrayList<>(poolSize);
        /*初始化线程池，装入线程*/
        for(int i = 0 ; i < poolSize; i++){
            Worker thread = new Worker(this);
            /*加入线程数组*/
            allThreads.add(thread);
            /*开启线程，让线程处于就绪状态*/
            thread.start();
        }
    }

    /**
     * 自定义线程
     */
    private static class Worker extends Thread{
        MyThreadPool pool;
        Worker(MyThreadPool pool){
            this.pool = pool;
        }

        @Override
        public void run() {
            //判断线程池是否在工作状态 以及阻塞队列中是否有任务待执行
            while (pool.isWorking || pool.blockingQueue.size() > 0) {
                try {
                    //出列：取任务
                    Runnable runnable = pool.blockingQueue.take();
                    //执行任务
                    runnable.run();
                } catch (InterruptedException e) {
                    System.out.println("中断异常：队列为空");
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将任务提交至队列
     * @param runnable
     * @return
     * @throws InterruptedException
     */
    public boolean subumit(Runnable runnable) throws InterruptedException {
        //任务入列，如果队列满了，就丢弃任务
        return  blockingQueue.offer(runnable);
    }

    /**两个问题：
     * 1.阻塞在take地方的怎么把他停掉？捕获 InterruptedException
     * 2.保证已经在queue里面的Runable要执行完成.  pool.blockingQueue.size() > 0
     */
    public void shutDown(){
        isWorking = false;
        for(Thread thread : allThreads){
            thread.interrupt();
        }
    }
    /*测试*/
    public static void main(String[] argv) throws InterruptedException {
        int threadCount = 100;
        //CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        //5个线程执行100个任务
        MyThreadPool myThreadPool = new MyThreadPool(5,100);

        for(int i=0 ; i < threadCount; i++){
            //提交任务
            boolean isAccept = myThreadPool.subumit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10);//时间调整成1000，会使得任务执行速度变慢，导致列队爆满，丢弃之后的队列
                    } catch (InterruptedException e) {
                            e.printStackTrace();
                    }
                    System.out.println("thread " + Thread.currentThread().getName() + " run");
                    //  countDownLatch.countDown();
                }
            });
            if(isAccept){
                System.out.println("Thread " + i + " has been Submit");
            }
        }

        //  countDownLatch.await();
        Thread.sleep(1000);

        for(int i=threadCount ; i < threadCount*2; i++){
            boolean isAccept = myThreadPool.subumit(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("thread " + Thread.currentThread().getName() + " run");
                    //countDownLatch.countDown();
                }
            });
            if(isAccept){
                System.out.println("Thread " + i + " has been Submit");
            }
        }

        //释放资源，不然任务执行完成后，主程序会“卡住”
        myThreadPool.shutDown();

    }
}
