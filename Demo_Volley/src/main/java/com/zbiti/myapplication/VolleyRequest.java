package com.zbiti.myapplication;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;

/**
 * Created by admin on 2016/3/28.
 *
 * Volley简单的二次回调封装
 */
public class VolleyRequest {
    public static void requestGet(RequestQueue requestQueue, String url, String tag, VolleyInterface vif){
        requestQueue.cancelAll(tag);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                vif.loadListener(), vif.loadErrorListener());
        request.setTag(tag);
        requestQueue.add(request);
    }

    public static void requestPost(){

    }
}
