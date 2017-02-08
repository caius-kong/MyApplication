package com.zbiti.myapplication;

import android.app.Application;
import android.content.Context;

import com.zbiti.myapplication.sharedPreference.FastJsonSerial;

import java.io.File;

import de.devland.esperandro.Esperandro;

/**
 * Created by admin on 2016/6/15.
 */
public class MyApplication extends Application {
    public static final String APP_ROOT_DIR = "DEMO_k";
    public static final String APP_CACHE_DIR = APP_ROOT_DIR + File.separator + "cache";
    private static MyApplication mAppInstance;
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppInstance = this;
        mAppContext = getApplicationContext();

        // Serialized object preferences
        Esperandro.setSerializer(new FastJsonSerial());
    }

    public static MyApplication getInstance() {
        return mAppInstance;
    }

    public static Context getContext() {
        return mAppContext;
    }
}
