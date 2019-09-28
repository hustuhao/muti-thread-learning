package com.tuhao.mutiprogram.mutiload.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 数据访问层
 * piaohailin
 * 2014-3-22
 */
public class DataDao {

    /**
     * 根据条件查询记录总数
     * @param condition
     * @return
     */
    public int getCount(String condition) {
        // 模拟数据库返回结果
        return Math.abs(new Random().nextInt()) / 1000000;
    }

    /**
     * 根据条件和范围参数，查询记录
     *  begin <= 数据 < end
     * @param condition
     * @param begin
     * @param end
     */
    public List<String> find(String condition, int begin, int end) {
        // 模拟数据库返回结果
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < end - begin; i++) {
            result.add(Thread.currentThread().getName() + ",id=" + Math.abs(new Random().nextInt()));
        }
        return result;
    }
}
