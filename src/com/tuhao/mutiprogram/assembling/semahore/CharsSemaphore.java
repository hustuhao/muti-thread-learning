package com.tuhao.mutiprogram.assembling.semahore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class CharsSemaphore extends Semaphore {
    public CharsSemaphore(int permits) {
        super(permits);
    }
    private List<String> result = new ArrayList<String>();
    public CharsSemaphore(int permits, boolean fair) {
        super(permits, fair);
    }

    /**
     * 填充数据,线程安全
     * @param data
     */
    public synchronized void fillData(String data) {
        result.add(data);
    }

    /**
     * 返回填充好的数据，线程安全
     * @return
     */
    public synchronized List<String> getResult() {
        return result;
    }
}
