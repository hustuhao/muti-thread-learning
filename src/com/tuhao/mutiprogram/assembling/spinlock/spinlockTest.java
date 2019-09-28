package com.tuhao.mutiprogram.assembling.spinlock;

import java.util.List;

// test for AssembingTest.java
public class spinlockTest {
    public static void main(String[] args) throws Exception {
        AssembingService as = new AssembingService();
        while(true) {
            List<String> res = as.getAllData();
            System.out.println(res);
        }
    }
}
