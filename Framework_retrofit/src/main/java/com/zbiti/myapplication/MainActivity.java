package com.zbiti.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Retrofit: A type-safe HTTP client for Android and Java
 *
 * Retrofit requires at minimum Java 7 or Android 2.3.
 *
 * 1、定义服务接口 (细化每一个请求url)
 * 2、创建Retrofit对象，并创建服务对象 (所有请求的基本url)
 * 3、调用服务api，并execute/enqueue (执行请求，接收响应)
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.1:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MyService myService = retrofit.create(MyService.class);
        Call<User> call = myService.login("jack", "123456");
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                System.out.println("--->sucessed");
                User user = response.body();
                System.out.println("--->" + user);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println("--->failed");
                System.out.println("-e-->" + t.getMessage());
            }
        });
    }

    public interface MyService{
        @GET("login")
        Call<User> login(@Query("username") String username, @Query("password") String password);
    }
}
