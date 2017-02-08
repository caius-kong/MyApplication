package com.zbiti.myapplication.okhttp;

import android.os.Handler;
import android.os.Looper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.google.gson.internal.$Gson$Types;
import com.zbiti.myapplication.db.DbCommand;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/5/23.
 * <p/>
 * okhttp的简单封装
 */
public class OkHttpClientManager {
    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private Gson mGson;

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient();
        //cookie enabled
        mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        mHandler = new Handler(Looper.getMainLooper());
        mGson = new Gson();
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 异步的get请求
     *
     * @param url
     * @param callback
     */
    public void _getAsyn(String url, final ResultCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        handleRequest(request, callback);
    }

    /**
     *  异步的get请求（带参数）
     *  @param url
     *  @param params
     *  @param callback
     */
    public void _getAsyn(String url,  Map<String, String> params, final ResultCallback callback) {
        final Request request = new Request.Builder()
                .url(appendParams(url, params))
                .build();
        handleRequest(request, callback);
    }

    private String appendParams(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        sb.append(url + "?");
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }

        sb = sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    public void _postAsyn(String url,  Map<String, String> params, final ResultCallback callback) {
        Request request = buildSimpleFormRequest(url, params);
        handleRequest(request, callback);
    }

    private Request buildSimpleFormRequest(String url, Map<String, String> params) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (String key : params.keySet()) {
            builder.add(key, params.get(key));
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    /**
     * 异步基于post的文件上传，单文件且携带其他form参数上传 (如：用户名+头像)
     *
     * @param url
     * @param callback
     * @param file
     * @param fileKey
     * @param params
     * @throws IOException
     */
    public void _postAsyn(String url, File file, String fileKey, Map<String, String> params, ResultCallback callback) throws IOException {
        Request request = buildMultipartFormRequest(url, new File[]{file}, new String[]{fileKey}, params);
        handleRequest(request, callback);
    }

    private Request buildMultipartFormRequest(String url, File[] files, String[] fileKeys, Map<String, String> params) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM);
        for (String key : params.keySet()) {
            builder.addPart(
                    Headers.of("Content-Disposition", "form-data; username=\"" + key + "\""),
                    RequestBody.create(null, params.get(key)));
        }
        if (files != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                builder.addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        RequestBody.create(MediaType.parse(guessMimeType(fileName)), file));
            }
        }
        RequestBody requestBody = builder.build();

        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private String guessMimeType(String path) {
        // TODO 根据文件名设置contentType
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path); // 从源码来看，path不存在时，返回"text/plain"；存在时，返回null
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    /**
     * 文件下载
     * @param url
     * @param destDir 存储位置 例如：Environment.getExternalStorageDirectory().getAbsolutePath()
     * @param callback
     */
    public void _downloadAsyn(final String url, final String destDir, final ResultCallback callback){
        Request request = new Request.Builder()
                .url(url)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, IOException e) {
                sendFailedResultCallback(request, e, callback);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                // 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件
                InputStream is = null;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream(); // 文件流
                    File file = new File(destDir, getFileName(url));
                    fos = new FileOutputStream(file);
                    byte[] buffer = new byte[2048];
                    int len = 0;
                    while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    sendSuccessResultCallback(file.getAbsolutePath(), callback);
                } catch(Exception e){
                    sendFailedResultCallback(response.request(), e, callback);
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {}
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {}
                }
            }
        });
    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    private void handleRequest(Request request, final ResultCallback callback) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                // 在ui线程下回调"抽象方法"，带执行处实现方法
                sendFailedResultCallback(request, e, callback);
            }

            @Override
            public void onResponse(final Response response) {
                try {
                    final String string = response.body().string();
                    Object o = mGson.fromJson(string, callback.mType); // jsonString --> java Object
                    sendSuccessResultCallback(o, callback);
                } catch (IOException e) { // 保证response取值时，如果报错，程序也不会崩溃！
                    sendFailedResultCallback(response.request(), e, callback);
                }
            }
        });
    }

    

    private void sendFailedResultCallback(final Request request, final Exception e, final ResultCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(request, e);
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                }
            }
        });
    }

    /**
     * 定义抽象类作为回调，其中的 抽象方法 由相应的执行处进行实现。(一种典型 方法 与 方法体 分离的回调实现)(进程通信)
     *
     * 注意：如果在外部定义，不需要static
     *
     * 关于泛型：泛型类型在运行时都是Object类型，我们无法知道其具体类型。
     * 解决方案一：传入Class类型作为参数 （每次传入参数Class clz较为麻烦）
     * 解决方案二：使用Gson库提供的解决方法 （获取ParameterizedType，这个Type包含有你几乎关心的所有类型元数据）
     *
     * 知识点补充：所有的Class都实现Type接口，ParameterizedType继承于Type。
     *
     */
    public static abstract class ResultCallback<T> {
        Type mType;

        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        /**
         * 对于带有泛型Class，可以返回一个ParameterizedType对象
         * getClass().getGenericSuperclass() ==> ParameterizedType
         */
        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass(); // 返回superClass的Type/ParameterizedType
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter."); // superclass是不带泛型的Class的实例，抛出异常
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]); // 转化成指定的数据结构
        }

        public abstract void onError(Request request, Exception e);

        public abstract void onResponse(T response);
    }
}
