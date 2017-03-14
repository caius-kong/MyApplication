package com.example.myapplication.controller;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;
import com.example.myapplication.R;
import com.example.myapplication.model.Impl.MainModelImpl;
import com.example.myapplication.model.MainModel;
import com.example.myapplication.service.MyService1;
import com.example.myapplication.service.MyService2;

/**
 * C层：在Android中，Activity处理用户交互问题，因此可以认为Activity是控制器。
 * Activity读取V视图层的数据（如:EditText控件的数据），控制用户输入（如:EditText控件数据的输入），并向Model发送数据请求。
 *
 * 1、Service的2种创建方式：Service、IntentService
 * 2、Service的2种启动方式：startService、bindService
 */
public class MainActivity extends AppCompatActivity {
    private MainModel mainModel;
    private ToggleButton toggleButton1, toggleButton2;

    private MyService2 myService2;
    private MyService2.MyBinder mBinder;
    private boolean isConn = false;
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            // 当与服务的连接建立时调用.
            // 传入的 IBinder 将用作与服务通信 (你可以选择调用MyBinder内的方法，或者获取MyService实例进行交互)
            mBinder = (MyService2.MyBinder)iBinder;
            myService2 = mBinder.getService();
            myService2.myMethod();
            isConn = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            // 只有异常失去连接才会回调
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // 加载v层

        mainModel = new MainModelImpl();
        initView();
    }

    private void initView(){
        toggleButton1  = (ToggleButton) findViewById(R.id.toggleButton1);
        toggleButton2  = (ToggleButton) findViewById(R.id.toggleButton2);
        toggleButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyService1.class);
                if(toggleButton1.isChecked()){
                    startService(intent);
                } else {
                    stopService(intent);
                }
            }
        });
        toggleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyService2.class);
                if(toggleButton2.isChecked()){
                    bindService(intent, conn, Service.BIND_AUTO_CREATE);
                } else {
                    if(isConn) {
                        unbindService(conn);
                        System.out.println("-count-->" + mBinder.getCount());
                        isConn = false;
                    }
                }
            }
        });
    }
}
