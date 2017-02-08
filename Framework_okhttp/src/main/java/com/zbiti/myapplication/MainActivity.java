package com.zbiti.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.ArrayMap;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.zbiti.myapplication.db.DBInfos;
import com.zbiti.myapplication.db.MySQLiteOpenHelper;
import com.zbiti.myapplication.entity.User;
import com.zbiti.myapplication.entity.User2;
import com.zbiti.myapplication.db.DbCommand;
import com.zbiti.myapplication.okhttp.OkHttpClientManager;
import org.apache.commons.lang3.RandomUtils;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * okhttp优势：
 * 1、弱网情况下表现良好
 * 2、HttpClient有被遗弃的趋势，但是HttpURLConnection，因此okhttp是个很好的选择
 * 3、直接支持https
 *
 * 注意：android中url写 localhost 或者 127.0.0.1 都访问不到
 */
public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private Button btn_insert;
    public OkHttpClientManager mOkHttpClientManager = OkHttpClientManager.getInstance();
    private SQLiteDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.tv);
        btn_insert = (Button) findViewById(R.id.btn_insert);

//        sendGetDemo();
//
//        sendPostDemo();

//        mOkHttpClientManager._getAsyn("http://192.168.1.116:3000/sayhello", new OkHttpClientManager.ResultCallback<User>() {
//            @Override
//            public void onError(Request request, Exception e) {
//                System.out.println("--->failed");
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(User jsonObject) {
//                textView.setText(jsonObject.toString());//注意这里是UI线程
//            }
//        });

        db = new MySQLiteOpenHelper(getApplicationContext()).getWritableDatabase();

        DbCommand.query(db, DBInfos.USER_TABLE.TABLE_NAME, null, null, null, null, null, null, null, new DbCommand.ResultCallback<List<User2>>() {
            @Override
            public void updateUI(List<User2> list) {
                textView.setText(list.toString());
            }
        });

        btn_insert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put("id", RandomUtils.nextInt());
                values.put("name", "kyh");
                values.put("phone", "17311111111");
                DbCommand.insert(db, DBInfos.USER_TABLE.TABLE_NAME, null, values, new DbCommand.ResultCallback<Long>() {
                    @Override
                    public void updateUI(Long result) {
                        if(result > 0)
                            Toast.makeText(MainActivity.this, "插入成功", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(MainActivity.this, "插入失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    /**
     * OnTrimMemory是Android 4.0之后提供的API。
     * 在内存紧张的时候，系统会根据不同的 "内存状态" 来 "回调"，根据不同的内存状态，来响应不同的内存释放策略。
     *
     * 释放的UI资源：图片、数组、缓存等资源。
     *
     * @param level
     */
    @Override
    public void onTrimMemory(int level) {
        System.out.println(" onTrimMemory ... level:" + level);
        switch(level) {
            case TRIM_MEMORY_UI_HIDDEN:
                // 编写释放资源代码
                // releaseBitmap(mBitmap);
                // releaseList(mList);
                break;
        }
        super.onTrimMemory(level);
    }

    private void releaseBitmap(Bitmap bitmap){
        // 先判断是否已经回收
        if(bitmap != null && !bitmap.isRecycled()){
            // 回收并且置为null
            bitmap.recycle();
            bitmap = null;
        }
        System.gc();
    }

    private void releaseList(List list){
        if(list != null) {
            list.clear();
            list = null;
        }
        System.gc();
    }

    /**
     * 发送GET请求
     */
    public void sendGetDemo() {
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url("http://192.168.1.1:3000/sayhello")
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("--->failed");
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                /*
                 * 如果希望获得返回的二进制字节数组，则调用response.body().string()；
                 * 如果希望获得返回的二进制字节数组，则调用response.body().bytes()； --> 图片下载(decode成图片)
                 * 如果你想拿到返回的inputStream，则调用response.body().byteStream() --> 文件下载(写文件操作)
                 *
                 * 注意：onResponse执行的线程并不是UI线程，如果你希望操作控件，还是需要使用handler等！！！！！！！！！
                 * */
                final String str = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(str);
                    }
                });
            }
        });
    }


    /**
     * 发送POST请求
     * <p/>
     * 区别：RequestBody的构造（请求参数）
     */
    public void sendPostDemo() {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        RequestBody requestBody = new FormEncodingBuilder()
                .add("username", "jack")
                .add("password", "123456")
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.1:3000/")
                .post(requestBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("--->failed");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                final String str = response.body().string();
                System.out.println("-str-->" + str);
            }
        });
    }

    public void fileUploadDemo() {
        OkHttpClient mOkHttpClient = new OkHttpClient();

        // 向服务器传递了一个键值对username:孔昀晖和一个文件
        // 我们通过MultipartBuilder的addPart方法可以添加键值对或者文件。
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"username\""),
                        RequestBody.create(null, "孔昀晖"))
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"mFile\"; filename=\"wjd.mp4\""),
                        RequestBody.create(MediaType.parse("application/octet-stream"), new File(Environment.getExternalStorageDirectory(), "balabala.mp4")))
                .build();

        Request request = new Request.Builder()
                .url("http://192.168.1.103:8080/okHttpServer/fileUpload")
                .post(requestBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                System.out.println("--->failed");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                System.out.println("--->successed");
            }
        });
    }
}
