package com.example.myapplication.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by kongyunhui on 2017/3/7.
 * 1、为什么不建议通过 bindService() 启动 IntentService？ IntentService 源码中的 onBind() 默认返回 null.
 * 2、IntentService会自动结束
 */

public class MyIntentService extends IntentService {
    public MyIntentService(){
        super("com.example.myapplication.service.MyIntentService");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // 自动开启一个工作线程来执行耗时任务
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
}
