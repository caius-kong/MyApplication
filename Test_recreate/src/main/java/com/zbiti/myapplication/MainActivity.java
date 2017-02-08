package com.zbiti.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * 操作A：MainActivity --> SecondActivity --> MainActivity
 * (且横竖屏幕一次或者打开不保留活动 == 模拟activity重新创建)
 *
 * 发现1：成员变量count自增，在重新创建时，又恢复了默认的100
 * 发现2：EditText会自动恢复，涉及到Activity中View级别的自我恢复机制
 */
public class MainActivity extends AppCompatActivity {
    private TextView tvName;
    private EditText etContent;
    private Button btnAddCount,btnNext;

    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvName = (TextView)findViewById(R.id.tv_name);
        btnAddCount = (Button)findViewById(R.id.btn_addCount);
        btnNext = (Button)findViewById(R.id.btn_next);

        count = 100;
        tvName.setText(count+"");
        btnAddCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count++;
                tvName.setText(count+"");
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });
    }
}
