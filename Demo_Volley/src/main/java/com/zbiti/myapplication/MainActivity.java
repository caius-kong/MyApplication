package com.zbiti.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Volley —— 适合去进行数据量不大，但通信频繁的网络操作，而不适合去进行上传、下载等高数据量的网络操作。
 *
 * volley是封装的httpUrlConnection！！！
 *
 * http://blog.csdn.net/ysh06201418/article/details/46443235
 * 1、导包
 * 2、XXRequest基本用法：newRequestQueue()、new Request、add()
 * 3、ImageLoader基本用法：newRequestQueue()、new ImageLoader、getImageListener()、imageLoader.get()
 */
public class MainActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.imageView);

        /**
         * StringRequest
         */
        requestQueue = Volley.newRequestQueue(this);  // 实例化requestQueue
//        StringRequest request1 = createStringRequest_post(); // 创建StringRequest
//        request1.setTag("req1");
//        requestQueue.add(request1); // 加入请求队列
//
//
//        /**
//         * JsonObjectRequest
//         */
//        JsonObjectRequest request2 = createJsonObjectRequest_post();
//        request1.setTag("req2");
//        requestQueue.add(request2);
//
//
//        /**
//         *  StringRequest简单的二次回调封装
//         */
//        sendStringRequest_get();
//
//
//        /**
//         *  ImageLoader
//         */
//        loadImage();

        /**
         * 自定义自定义GsonRequest （理由：GSON处理复杂数据，可以轻松完成对象转化）
         */
        GsonRequest gsonRequest = createGsonRequest();
        requestQueue.add(gsonRequest);
    }

    private StringRequest createStringRequest_post(){
        String url = "http://192.168.191.1:3000/";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.d("TAG", s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("TAG", volleyError.getMessage(), volleyError);
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", "jack");
                map.put("password", "123456");
                return map;
            }

            /*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("apikey","f71e5f1e08cd5a7e42a7e9aa70d22458");
                return map;
            }*/
        };
        return request;
    }

    private JsonObjectRequest createJsonObjectRequest_post() {
        String url = "http://192.168.191.1:8080/myHttp/LoginServlet";
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("username", "jack");
        map.put("password", "123456");
        JSONObject params = new JSONObject(map);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("TAG", jsonObject.toString());
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("TAG", volleyError.getMessage(), volleyError);
            }
        });
        return request;
    }

    private void sendStringRequest_get(){
        String url = "http://192.168.191.1:3000/sayhello";
        String tag = "req3";
        VolleyRequest.requestGet(requestQueue, url, tag, new VolleyInterface() {
            @Override
            public void onSuccess(String s) {
                Log.d("TAG", s);
            }

            @Override
            public void onError(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
            }
        });
    }

    private void loadImage(){
        // 加载网络图片 （ImageRequest、ImageLoader、NetworkImageView）
        // 此处选用ImageLoader（new、get()）
        ImageLoader imageLoader = new ImageLoader(requestQueue, new BitmapCache());
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView, R.drawable.ic_action_picture, R.drawable.ic_action_picture);
        String url = "http://ww1.sinaimg.cn/crop.0.0.800.800.1024/735510dbjw8eoo1nn6h22j20m80m8t9t.jpg";
        imageLoader.get(url, imageListener, 100, 100);
    }

    private GsonRequest createGsonRequest() {
        String url = "http://192.168.191.1:3000/list";
        GsonRequest gsonRequest = new GsonRequest<User[]>(
                url, User[].class,
                new Response.Listener<User[]>(){
                    @Override
                    public void onResponse(User[] users) {
                        Log.d("TAGss", users[0].toString());
                    }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("TAG", volleyError.getMessage(), volleyError);
                    }
                });
        return gsonRequest;
    }

    /**
     * Volley与Activity生命周期关联
     */
    @Override
    protected void onStop() {
        super.onStop();
        requestQueue.cancelAll("req1");
        requestQueue.cancelAll("req2");
    }
}
