package com.zbiti.myapplication;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpRequest;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import cz.msebera.android.httpclient.Header;

/**
 * Android 开源框架 —— 网络请求库 （android-async-http）
 *
 * 封装的httpClient，而android平台不推荐用HttpClient了，所以这个库已经不适合android平台了！！！！！
 *
 * 其主要特征如下：处理异步Http请求，并通过匿名内部类处理回调结果，Http异步请求均位于非UI线程，不会阻塞UI操作,
 * 通过线程池处理并发请求处理文件上传、下载,响应结果自动打包JSON格式.
 *
 * 注意：不支持https,需要自己扩展 (okhttp支持)
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button1, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initEvent();
    }

    private void initView() {
        button1 = (Button) findViewById(R.id.btn1);
        button2 = (Button) findViewById(R.id.btn2);
    }

    private void initEvent() {
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                String url = "/";
                RequestParams params = new RequestParams();
                params.put("username", "jack");
                params.put("password", "123456");
                HttpUtils.post(url, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            String msg = response.getString("name").toString();
                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(MainActivity.this, "请求失败！", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.btn2:
                String url2 = "http://img.hb.aicdn.com/d2024a8a998c8d3e4ba842e40223c23dfe1026c8bbf3-OudiPA_fw580";
                HttpUtils.get(url2, new FileAsyncHttpResponseHandler(MainActivity.this) {
                    @Override
                    public void onSuccess(int i, Header[] headers, File file) {
                        try {
                            FileInputStream fis = new FileInputStream(file);
                            FileOutputStream fos = null;
                            File pathFile = null;
                            // 判断是否存在sdcard
                            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                                pathFile = Environment.getExternalStorageDirectory();
                                fos = new FileOutputStream(new File(pathFile, "1.jpg"));
                            } else {
                                // 文件默认会存储到/data/data/package/files中
                                fos = openFileOutput("2.jpg", Context.MODE_PRIVATE);
                            }
                            byte[] buffer = new byte[1024];
                            while (fis.read(buffer) != -1) {
                                fos.write(buffer, 0, buffer.length);
                                fos.flush();
                            }
                            Toast.makeText(MainActivity.this, "下载成功！", Toast.LENGTH_SHORT).show();

                            System.out.println("--->" + i);

                            fis.close();
                            fos.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, Throwable throwable, File file) {
                        Toast.makeText(MainActivity.this, "下载失败！", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
        }
    }
}
