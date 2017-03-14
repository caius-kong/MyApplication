package com.zbiti.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;

/**
 * http://gank.io/post/56e80c2c677659311bed9841
 *
 * 用Retrofit就可以完成基本的Http请求，为什么加入Rxjava呢？
 * 答：让Rxjava来控制线程、结果过滤、转换数据格式等等，最终让Retrofit专注于http请求
 *
 * 步骤：
 * 1、定义服务接口（区别在于返回Observable）
 * 2、定义对应的http方法封装类
 *    1) 构造方法中封装retrofit；
 *    2) 扩展api，直接传入subscriber/observer作为参数，接收响应；
 *    3) 使用map统一数据格式
 * 3、activity中进行调用，并传入相应的subscriber
 */
public class MainActivity extends AppCompatActivity {
    @Bind(R.id.button) // 使用butterknife注解的属性或方法不能是 private 或 static
    Button clickMeBtn;
    @Bind(R.id.textView)
    TextView resultTV;

    private Subscriber subscriber;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.button)
    public void clickMe(){
        login();
    }

    // 进行网络请求
    private void login(){
        subscriber = new Subscriber<User>() {
            @Override
            public void onStart() {
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("加载中...");
                progressDialog.show();
            }
            @Override
            public void onCompleted() {
                Toast.makeText(MainActivity.this, "Login Completed", Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
            }
            @Override
            public void onError(Throwable e) {
                resultTV.setText(e.getMessage());
            }
            @Override
            public void onNext(User user) {
                resultTV.setText(user.toString());
            }
        };

        UserHttpMethods.getInstance().login(subscriber, "jack", "123456");
    }
}
