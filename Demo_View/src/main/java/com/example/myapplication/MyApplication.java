package com.example.myapplication;

import android.app.Application;

import com.antfortune.freeline.FreelineCore;

/**
 * Created by kongyunhui on 2016/12/29.
 *
 * Freeline默认替换你的Application类，在FreelineApplication中已经执行了FreelineCore.init(this);。默认情况下不需要调用。
 *
 * 但是，如果你在build.gradle中配置了applicationProxy false关掉了默认替换，则需要手动加上FreelineCore.init(this);.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        FreelineCore.init(this); // 默认情况下不需要调用
    }
}
