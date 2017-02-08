package com.zbiti.myapplication;

import android.test.ActivityUnitTestCase;
import android.test.InstrumentationTestCase;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by admin on 2016/5/18.
 */
public class TestClass extends InstrumentationTestCase {
    public void test1() throws Exception{
        assertEquals(3,3);
    }

    public void test2() throws Exception{
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url("https://github.com/hongyangAndroid")
                .build();
        //new call
        com.squareup.okhttp.Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("--->failed");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String htmlStr = response.body().string();
                System.out.println("-htmlStr-->" + htmlStr);
            }
        });
    }
}
