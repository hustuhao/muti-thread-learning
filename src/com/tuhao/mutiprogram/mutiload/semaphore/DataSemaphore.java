package com.tuhao.mutiprogram.mutiload.semaphore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class DataSemaphore extends Semaphore {
    private List<String> result = new ArrayList<String>();

    public synchronized List<String> getResult() {
        return result;
    }

    private static final long serialVersionUID = 1L;

    public DataSemaphore(int permits) {
        super(permits);
    }

    public DataSemaphore(int permits, boolean fair) {
        super(permits, fair);
    }

    /**
     * 填充数据
     * @param data
     */
    public synchronized void fillData(List<String> data) {
        result.addAll(data);
    }
}