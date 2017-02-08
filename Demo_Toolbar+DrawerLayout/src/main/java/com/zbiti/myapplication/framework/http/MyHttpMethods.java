package com.zbiti.myapplication.framework.http;

import com.zbiti.myapplication.entity.User;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 2016/6/21.
 *
 * 1、初始化retrofit
 * 2、通过retrofit创建xxService
 * 3、定义httpMethod。调用xxService的方法，对其返回的Observable对象进行Rxjava处理
 */
public class MyHttpMethods {
    private static final String BASE_URL = "http://192.168.2.20:3000";
    private static final int DEFAULT_TIMEOUT = 5;

    private static MyHttpMethods mInstance;
    private MyService myService;
    private Retrofit retrofit;

    MyHttpMethods(){
        // 设置连接超时
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        // 设置请求拦截器
        // ...

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        myService = retrofit.create(MyService.class);
    }

    // 获得单例
    public static MyHttpMethods getInstance() {
        if (mInstance == null) {
            synchronized (MyHttpMethods.class) {
                if (mInstance == null) {
                    mInstance = new MyHttpMethods();
                }
            }
        }
        return mInstance;
    }

    /**
     * 模糊查询所有用户
     *
     * @param subscriber
     * @param name
     */
    public void findUsers(Subscriber<List<User>> subscriber, String name){
        myService.findUsers(name)
                .map(new HttpResultFunc<List<User>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * 对象：HttpResult<T> --> T
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T>{

        @Override
        public T call(HttpResult<T> httpResult){
            if (httpResult.getResultCode() != 0) {
                // 错误码加工，并交给subscriber的onError()
                throw new ApiException(httpResult.getResultCode());
            }
            return httpResult.getData();
        }
    }
}
