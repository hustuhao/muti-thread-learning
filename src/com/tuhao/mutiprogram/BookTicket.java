package com.tuhao.mutiprogram;

/*多线程购票程序*/
public class BookTicket implements Runnable{
    //  为了让线程共享一个资源所以我们定义一个静态成员变量
    private static int tickets = 100;
    Object obj = new Object();
    public BookTicket() {
    }
    @Override
    public void run() {
        while (true) {
            synchronized (BookTicket.class) {
                try {
                    if (tickets > 0) {
                        Thread.sleep(100); //只是为了打印的效果，一行一行打印，如果不加则，所有结果一下子全都出来了
                        // getName:获取线程的名字，就是main方法里的第二个参数
                        System.out.println(Thread.currentThread().getName() + " is selling  NO." + (tickets--) + " tickets");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public static void main(String[] args){
        BookTicket ticket = new BookTicket();

        Thread t1 = new Thread(ticket,"window-1");
        Thread t2 = new Thread(ticket,"window-2");
        Thread t3 = new Thread(ticket,"window-3");

        t1.start();
        t2.start();
        t3.start();
    }

}
