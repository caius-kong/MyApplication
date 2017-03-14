package com.example.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by kongyunhui on 2017/3/7.
 * bind service 生命周期： onCreate() -> onBind -> onUnbind -> onDestroy
 */

public class MyService2 extends Service {
    private int count = 0;
    private boolean quit = false;

    public class MyBinder extends Binder{
        // 我们可以通过这个方法告知绑定的组件 count 是多少.
        public int getCount(){
            return count;
        }

        public MyService2 getService(){
            return MyService2.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("service2: onBind");
        return new MyBinder(); // 将代理对象通过 onBind() 方法传给 conn
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("service2: onCreate");

        new Thread(new Runnable() {
            @Override
            public void run() {
                // quit被修改前持续执行（记录myService2运行时长）
                while(!quit){
                    try {
                        Thread.sleep(1000);
                    }catch(Exception e){e.printStackTrace();}
                    count++;
                }
            }
        }).start();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("service2: onUnbind (unbindService时调用)");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("service2: onDestroy (所有绑定的client都unbindService后执行)");
        // Service被关闭前回调该方法, 将quit改为true, 停止递增count
        this.quit = true;
    }

    public void myMethod(){
        System.out.println("service: myMethod");
    }
}
