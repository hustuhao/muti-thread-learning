package com.tuhao.mutiprogram;

import java.util.function.IntConsumer;
/*
打印 0 和 奇偶数
a 线程打印 0
b 线程打印 奇数
c 线程打印 偶数
input：4 (必须是2*n)
output：01020304
*/
public class ZeroEvenOdd {
    private int n;
    private boolean zero;// 打印 0
    private boolean eo;// 打印偶数或者奇数

    public ZeroEvenOdd(int n) {
        this.n = n;
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        synchronized(this){
            for(int i = 0;i < n;i++){
                while(zero){
                    this.wait();
                }
                printNumber.accept(0);
                zero = true;
                this.notifyAll();
            }
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        for(int i = 1;i <= n;i += 2){
            synchronized(this){
                while(!(zero==true&&eo==false)){
                    this.wait();
                }
                printNumber.accept(i);
                zero = false;
                eo = true;
                this.notifyAll();
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        for(int i = 2;i <= n;i += 2){
            synchronized(this){
                while(!(zero==true&&eo==true)){
                    this.wait();
                }
                printNumber.accept(i);
                zero = false;
                eo = false;
                this.notifyAll();
            }
        }
    }

    public static void main(String[] args) {
        IntConsumer printNumber = new IntConsumer() {
            @Override
            public void accept(int value) {
                System.out.print(value);
            }
        };

        ZeroEvenOdd zeo = new ZeroEvenOdd(15);
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    zeo.zero(printNumber);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread b = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    zeo.even(printNumber);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread c = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    zeo.odd(printNumber);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        a.start();
        b.start();
        c.start();
    }
}
