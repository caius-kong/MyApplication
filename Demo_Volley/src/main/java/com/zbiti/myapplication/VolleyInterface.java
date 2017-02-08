package com.zbiti.myapplication;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * Created by admin on 2016/3/28.
 */
public abstract class VolleyInterface{
    // 定义方法，返回listener，并在实现方法中回调（接口暴露）
    public Response.Listener<String> loadListener(){
        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.d("TAG", "加载中...");
                onSuccess(s); // 通过抽象方法，暴露二次回调方法
            }
        };
        return listener;
    }

    public Response.ErrorListener loadErrorListener(){
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                onError(volleyError);
                Log.d("TAG", "加载失败！");
            }
        };
        return errorListener;
    }

    // 二次回调的方法
    public abstract void onSuccess(String s);
    public abstract void onError(VolleyError error);
}
