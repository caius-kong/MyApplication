package com.zbiti.myapplication.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * There are some ViewGroups (ones that utilize onInterceptTouchEvent) that throw exceptions
 * when a PhotoView is placed within them, most notably ViewPager and DrawerLayout.
 *
 * 意思是：尤其在ViewPager和DrawerLayout内使用photoView，会抛出异常! 目前的解决方案就是重写onInterceptTouchEvent(), 并手动cache异常！
 */
public class ViewPagerFixed extends android.support.v4.view.ViewPager {

    public ViewPagerFixed(Context context) {
        super(context);
    }

    public ViewPagerFixed(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}