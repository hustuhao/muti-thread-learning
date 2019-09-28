package com.tuhao.mutiprogram.mutiload.main;

import com.tuhao.mutiprogram.mutiload.service.DataService;

import java.util.List;

/**
 * 信号量
 * 信号量组装多线程结果，性能比较好，推荐使用，代码简单
 * piaohailin
 * 2014-3-22
 *
 */
public class ServerSemaphore {

    /**
     * 线程数
     */
    public static final int threadCount = 4;
    /**
     * 单线程的分片阈值
     */
    public static final int hold = 1000;

    public static void main(String[] args) throws Exception {
        DataService dataService = new DataService();
        List<String> result = dataService.getAllData("001");
        System.out.println(result.size());
        System.exit(-1);
    }
}