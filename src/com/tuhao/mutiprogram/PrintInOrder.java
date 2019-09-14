package com.tuhao.mutiprogram;
/*
顺序调用函数 first second third
类似题目：多线程顺序打印 A B C
*/
public class PrintInOrder {
    //线程共享变量
    private volatile int i = 3;
    public void first(Runnable printFirst) throws InterruptedException {
        while (i == 3) {
            printFirst.run();//打印 first
            i=i-1;
        }

    }

    public void second(Runnable printSecond) throws InterruptedException {
        //阻塞等待逻辑，核心部分
        while (i != 2) {
            continue;
        }
        printSecond.run();//打印 second
        i= i-1;
    }

    public void third(Runnable printThird) throws InterruptedException {
        while (i != 1) {
            continue;
        }
        printThird.run();//打印 third
        i= i-1;
    }

    public static void main(String[] args) throws InterruptedException {
        PrintInOrder pio = new PrintInOrder();
        pio.first(new Runnable() {
            @Override
            public void run() {
                System.out.println("first");
            }
        });
        pio.second(new Runnable() {
            @Override
            public void run() {
                System.out.println("second");
            }
        });
        pio.third(new Runnable() {
            @Override
            public void run() {
                System.out.println("third");
            }
        });
    }
}
