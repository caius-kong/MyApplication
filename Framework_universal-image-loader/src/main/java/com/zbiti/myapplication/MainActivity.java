package com.zbiti.myapplication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 使用最普遍的图片加载库
 */
public class MainActivity extends Activity {
    @Bind(R.id.listView) ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();
    }

    private void initData(){
        ImageAdapter imageAdapter = new ImageAdapter(this, configUrls());
        listView.setAdapter(imageAdapter);
        // 控制滑动是否暂停加载
        // @param (imageLoader, 是否在滑动过程中暂停加载图片, 是否在猛的滑动界面的时候加载图片)
        listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
    }

    private List<String> configUrls(){
        List<String> list = new ArrayList<String>();
        list.add("http://ww1.sinaimg.cn/crop.0.0.800.800.1024/735510dbjw8eoo1nn6h22j20m80m8t9t.jpg");
        list.add("http://img.hb.aicdn.com/d2024a8a998c8d3e4ba842e40223c23dfe1026c8bbf3-OudiPA_fw580");
        list.add("http://ww1.sinaimg.cn/crop.0.0.800.800.1024/735510dbjw8eoo1nn6h22j20m80m8t9t.jpg");
        list.add("http://img.hb.aicdn.com/d2024a8a998c8d3e4ba842e40223c23dfe1026c8bbf3-OudiPA_fw580");
        list.add("http://ww1.sinaimg.cn/crop.0.0.800.800.1024/735510dbjw8eoo1nn6h22j20m80m8t9t.jpg");
        list.add("http://img.hb.aicdn.com/d2024a8a998c8d3e4ba842e40223c23dfe1026c8bbf3-OudiPA_fw580");
        return list;
    }
}
