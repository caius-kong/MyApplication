package com.zbiti.myapplication.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.zbiti.myapplication.R;
import com.zbiti.myapplication.control.NavigateManager;
import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends Activity implements View.OnClickListener{
    @Bind(R.id.iv_ad)
    ImageView ivAd;
    @Bind(R.id.ll_container)
    LinearLayout llContainer;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        initDate();
        initView();
        initListener();
    }

    private void initDate(){
        // getCurrentUser，判断是否免登录等

        // 更新ivAd，每天一广告

        // ...
    }

    private void initView(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                NavigateManager.gotoMainActivity(SplashActivity.this);
            }
        }, 3000);
    }

    private void initListener(){
        llContainer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_container:
                NavigateManager.gotoMainActivity(this);
                break;
        }
    }
}
