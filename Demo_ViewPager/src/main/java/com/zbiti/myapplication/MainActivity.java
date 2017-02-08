package com.zbiti.myapplication;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    private ViewPager viewPager;
    private List<View> mViews = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
    }

    private void initWidgets(){
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        // 设置切换动画
        viewPager.setPageTransformer(true, new DepthPageTransformer());
        // viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        // 设置适配器
        initViews();
        viewPager.setAdapter(new ViewsAdapter());
    }

    private void initViews(){
        LayoutInflater mInflater = LayoutInflater.from(this);
        View  tab01 = mInflater.inflate(R.layout.tab01, null);
        View  tab02 = mInflater.inflate(R.layout.tab02, null);
        View  tab03 = mInflater.inflate(R.layout.tab03, null);
        mViews.add(tab01);
        mViews.add(tab02);
        mViews.add(tab03);
    }

    class ViewsAdapter extends PagerAdapter{
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mViews.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mViews.get(position));
        }

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
