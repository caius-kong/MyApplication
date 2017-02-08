package com.zbiti.myapplication.ui.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.zbiti.myapplication.R;
import com.zbiti.myapplication.util.image.ImageManager;
import com.zbiti.myapplication.widget.ViewPagerFixed;
import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

public class ImageActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.vpf_image_viewpager)
    ViewPagerFixed viewPager;

    private int index;
    private String[] picUrls;
    private int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ButterKnife.bind(this);

        initDate();
        initView();
        initListener();
    }

    private void initDate(){
        index = getIntent().getIntExtra("index", 0);
        picUrls = getIntent().getStringArrayExtra("picUrls");
        size = picUrls.length;
    }

    private void initView(){
        initToolBar(toolbar, true, index + 1 + "/" + size);
        viewPager.setAdapter(new CheckImageAdapter(picUrls));
        viewPager.setCurrentItem(index);
    }

    private void initListener(){
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                toolbar.setTitle(position + 1 + "/" + size);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    static class CheckImageAdapter extends PagerAdapter{
        private String[] picUrls;

        public CheckImageAdapter(String[] picUrls){
            this.picUrls = picUrls;
        }

        @Override
        public int getCount() {
            return picUrls.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // 原有的imageview  替换为  uk.co.senab.photoview.PhotoView
            PhotoView photoView = new PhotoView(container.getContext());
            ImageManager.getInstance().loadUrlImage(container.getContext(), picUrls[position], photoView);
            container.addView(photoView);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
    }
}
