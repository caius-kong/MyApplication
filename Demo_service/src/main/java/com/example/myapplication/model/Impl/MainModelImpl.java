package com.example.myapplication.model.Impl;

import com.example.myapplication.model.MainModel;

/**
 * Created by kongyunhui on 2017/3/8.
 */

public class MainModelImpl implements MainModel {
    @Override
    public void doSth(OnMainListener listener) {
        // 耗时操作等
        listener.onSuccess("data");
    }

    public interface OnMainListener{
        void onSuccess(String data);
        void onError();
    }
}
