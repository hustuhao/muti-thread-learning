package com.tuhao.mutiprogram;
/*
利用 join 顺序打印 one two three
缺点：串行执行，失去了多线程的意义。

* */
public class PrintInOrder2 {
    public static void main(String[] args) throws InterruptedException {
        One one = new One();
        Two two = new Two();
        Three three = new Three();

        one.start();
        one.join(); //当前线程(main)等待 one 线程结束
        two.start();
        two.join(); //当前线程(main)等待 two 线程结束
        three.start();
        three.join(); //当前线程(main)等待 three 线程结束
    }
}

class One extends Thread{
    @Override
    public void run() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("One");
    }
}

class Two extends Thread{
    @Override
    public void run() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Two");
    }
}

class Three extends Thread{
    @Override
    public void run() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Three");
    }
}
