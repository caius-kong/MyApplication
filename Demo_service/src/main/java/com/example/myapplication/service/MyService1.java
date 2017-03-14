package com.example.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by kongyunhui on 2017/3/7.
 *
 * start service 生命周期： onCreate() -> onStartCommand -> onDestroy
 */

public class MyService1 extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("service: onBind");
        return null;
    }

    // 仅在创建service时执行一次！
    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("service1: onCreate (可以在onStartCommand前做一些准备工作)");
    }

    // 调用几次，执行几次
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("service1: onStartCommand (开启线程，执行耗时任务)");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("service1: onDestroy (调用了stopService或者stopself)");
    }
}
