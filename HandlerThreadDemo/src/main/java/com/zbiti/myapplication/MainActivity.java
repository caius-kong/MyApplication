package com.zbiti.myapplication;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView tv1, tv2, tv3;
//    private Handler mHandler;
//    private HandlerThread mHandlerThread;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    System.out.println("--->from message");
                    tv1.setText("one");
                    break;
                case 2:
                    tv2.setText("two");
                    break;
                case 3:
                    tv3.setText("three");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 访问耗时任务
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 有4种方式完成子线程与主线程的交互 - 更新UI
                // 1、sendMessage
                handler.sendEmptyMessage(1);
                handler.sendEmptyMessage(2);
                handler.sendEmptyMessage(3);

                // 2、post
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("--->from post");
                        tv1.setText("four");
                    }
                });

                // 3、runOnUiThread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv2.setText("five");
                    }
                });

                // 4、view.post
                tv3.post(new Runnable() {
                    @Override
                    public void run() {
                        tv3.setText("six");
                    }
                });
            }
        }).start();

//        mHandlerThread = new HandlerThread("my handlerthread 1");
//        mHandlerThread.start();
//        mHandler = new Handler(mHandlerThread.getLooper()){
//            @Override
//            public void handleMessage(Message msg) {
//                switch (msg.what){
//                    case 1:
//                        Toast.makeText(MainActivity.this, "ok", Toast.LENGTH_SHORT).show();
//                        tv1.setText("1");
//                        break;
//                    case 2:
//                        tv2.setText("2");
//                        break;
//                    case 3:
//                        tv3.setText("3");
//                        break;
//                }
//            }
//        };
    }
}
