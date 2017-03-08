package com.example.myapplication.model;

import com.example.myapplication.model.Impl.MainModelImpl;

/**
 * Created by kongyunhui on 2017/3/8.
 * M层：适合做一些业务逻辑处理，比如数据库存取操作，网络操作，复杂的算法，耗时的任务等都在model层处理。
 */

public interface MainModel {
    void doSth(MainModelImpl.OnMainListener listener);
}
