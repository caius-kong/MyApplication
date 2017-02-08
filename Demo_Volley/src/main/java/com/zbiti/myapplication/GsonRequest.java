package com.zbiti.myapplication;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import java.io.UnsupportedEncodingException;

/**
 * Created by admin on 2016/3/31.
 */
public class GsonRequest<T> extends Request<T>{
    private Gson mGson;
    private Class<T> mClass;
    private final Response.Listener<T> mListener;

    public GsonRequest(int method, String url, Class<T> clazz, Response.Listener<T> listener,
                       Response.ErrorListener errorListener){
        super(method, url, errorListener);
        mGson = new Gson();
        this.mClass = clazz;
        this.mListener = listener;
    }

    public GsonRequest(String url, Class<T> clazz, Response.Listener<T> listener,
                       Response.ErrorListener errorListener){
        this(Method.GET, url, clazz, listener, errorListener);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            // 将服务器响应的数据解析出来
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            System.out.println("-jsonString-->" + jsonString);

            // error：Expected BEGIN_OBJECT but was STRING at line 1 column 2
            // 获得jsonString不符合格式，因此处理下
//            jsonString = jsonString.substring(1, jsonString.length()-1);
//            jsonString = jsonString.replace("\\", "");
//            System.out.println("-after jsonString-->" + jsonString);

            // 调用Gson的fromJson方法将数据组装成对象
            return Response.success(mGson.fromJson(jsonString, mClass),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        // 将最终的数据进行回调
        mListener.onResponse(response);
    }
}
