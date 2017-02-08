package com.zbiti.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 手势识别
 * GestureDetector
 * 原理：onTouchListener监听MotionEvent事件
 *      GestureDetector转发MotionEvent事件，通过其指定的GestureListener接口实现
 */
public class MainActivity extends AppCompatActivity {
    private RelativeLayout layout;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = (RelativeLayout)findViewById(R.id.layout);
        mGestureDetector = new GestureDetector(this, new MyGestureListener()); // 必须在img.setOnTouchListener之前创建
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        // 滑动事件 （e1: 滑动起始事件；e2: 滑动结束事件）
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if(e1.getX() - e2.getX() > 50){
                Toast.makeText(MainActivity.this, "从右向左滑动", Toast.LENGTH_LONG).show();
            } else if(e2.getX() - e1.getX() > 50){
                Toast.makeText(MainActivity.this, "从左向右滑动", Toast.LENGTH_LONG).show();
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}
