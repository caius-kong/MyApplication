package com.example.myapplication.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by kongyunhui on 2017/3/7.
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

    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
}
