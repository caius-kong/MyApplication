package com.zbiti.myapplication;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 2016/5/25.
 */
public class UserHttpMethods {
    public static final String BASE_URL = "http://192.168.1.1:3000";

    private static final int DEFAULT_TIMEOUT = 5;

    private static UserHttpMethods mInstance;
    private Retrofit retrofit;
    private UserService userService;

    // 构造方法私有
    private UserHttpMethods() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        /*
        // Adding header to all request with Retrofit 2
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("key", "value").build();
                return chain.proceed(request);
            }
        });
        // httpClientBuilder.addInterceptor(new UnauthorisedInterceptor(context));
        */

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //增加返回值为Gson的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                //增加返回值为Oservable<T>的支持
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        userService = retrofit.create(UserService.class);
    }

    // 获得单例
    public static UserHttpMethods getInstance() {
        if (mInstance == null) {
            synchronized (UserHttpMethods.class) {
                if (mInstance == null) {
                    mInstance = new UserHttpMethods();
                }
            }
        }
        return mInstance;
    }

    /**
     * 用于登录验证
     * @param subscriber 由调用者传过来的观察者对象（响应被观察者）
     * @param username 用户名
     * @param password 密码
     */
    public void login(Subscriber<User> subscriber, String username, String password){
        userService.login(username, password)
                .map(new HttpResultFunc<User>())
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
