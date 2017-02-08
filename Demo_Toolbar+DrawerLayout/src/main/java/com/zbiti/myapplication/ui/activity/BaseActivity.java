package com.zbiti.myapplication.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.zbiti.myapplication.R;
import com.zbiti.myapplication.constant.GlobalParams;
import com.zbiti.myapplication.framework.dialog.LoadingDialog;
import com.zbiti.myapplication.util.DisplayUtil;
import com.zbiti.myapplication.util.theme.Selector;

/**
 * Created by admin on 2016/6/12.
 */
public class BaseActivity extends AppCompatActivity {
    protected Context mContext;
    protected Activity mActivity;

    protected LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mActivity = this;

        initSystemBarTint();
        init();
    }

    private void init() {
        loadingDialog = new LoadingDialog(this);
        GlobalParams.screenWidth = DisplayUtil.getWindowWidth(this);
        GlobalParams.screenHeight = DisplayUtil.getWindowHeight(this);
    }

    // 初始化沉浸式状态栏
    private void initSystemBarTint() {
        // 4.4及以上版本开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true); // 开启状态栏染色
            tintManager.setStatusBarTintColor(getColorPrimary());
        }
    }

    // 设置状态栏透明度
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    // 设置状态栏颜色
    public void setStatusBarTintColor(int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(getResources().getColor(resId));
        }
    }

    // 获取colorPrimary
    public int getColorPrimary() {
        TypedValue typedValue = new  TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }

    // 获取DarkColorPrimary
    public int getDarkColorPrimary() {
        TypedValue typedValue = new  TypedValue();
        getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
        return typedValue.data;
    }

    // 设置多种形状的背景
    protected void setOvalShapeViewBackground(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(Selector.createOvalShapeSelector(getColorPrimary()));
        } else {
            view.setBackgroundDrawable(Selector.createOvalShapeSelector(getColorPrimary()));
        }
    }

    protected void setRoundRectShapeViewBackground(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(Selector.createRoundRectShapeSelector(getColorPrimary()));
        } else {
            view.setBackgroundDrawable(Selector.createRoundRectShapeSelector(getColorPrimary()));
        }
    }

    protected void setRectShapeViewBackground(View view) {
        if (Build.VERSION.SDK_INT >= 16) {
            view.setBackground(Selector.createRectShapeSelector(getColorPrimary()));
        } else {
            view.setBackgroundDrawable(Selector.createRectShapeSelector(getColorPrimary()));
        }
    }

    // 初始化toolbar
    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(homeAsUpEnabled);
        }
    }

    public void initToolBar(Toolbar toolbar, boolean homeAsUpEnabled, int resTitle) {
        initToolBar(toolbar, homeAsUpEnabled, getString(resTitle));
    }

    // 开启up
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // 重写这三个方法，加入渐入渐出效果
    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (intent != null && intent.getComponent() != null && !intent.getComponent().getClassName().equals(SplashActivity.class.getName())) {
            overridePendingTransition(android.R.anim.fade_in, R.anim.hold_long);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (intent != null && intent.getComponent() != null && !intent.getComponent().getClassName().equals(SplashActivity.class.getName())) {
            overridePendingTransition(android.R.anim.fade_in, R.anim.hold_long);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (!((Object) this).getClass().equals(SplashActivity.class) && !((Object) this).getClass().equals(MainActivity.class) && !((Object) this).getClass().equals(LoginActivity.class)) {
            overridePendingTransition(R.anim.hold_long, android.R.anim.fade_out);
        }
    }
}
