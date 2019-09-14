package com.tuhao.mutiprogram;
/* 经典生产者和消费者问题 */
class FooBar {
    private int n; // 打印 FooBar 的次数
    boolean flag = true;

    public FooBar(int n) {
        this.n = n;
    }

    public synchronized void foo(Runnable printFoo) throws InterruptedException {
        while (flag) {
            this.wait();
        }
        for (int i = 0; i < n; i++) {
            printFoo.run();
            this.notify();
            this.wait();
        }
    }

    public synchronized void bar(Runnable printBar) throws InterruptedException {
        flag = false;
        this.notify();
        for (int i = 0; i < n; i++) {
            this.wait();
            printBar.run();
            this.notify();
        }
    }
}
