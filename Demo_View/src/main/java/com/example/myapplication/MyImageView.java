package com.example.myapplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by kongyunhui on 2016/12/27.
 * <p>
 * 自定义View （核心步骤：尺寸测量、绘制）
 * <p>
 * 基本步骤：
 * 1、extends View
 * 2、自定义View属性，并在构造方法中初始化属性 (可选)(values/attrs.xml中定义 + view.xml中引入属性[需要先引入命名空间p37])
 * 3、onMeasure()
 *      理解MeasureSpec，利用SpecMode+SpecSize ==> mWidth/mHeight
 *      a). match_parent/具体数值: SpecMode=EXACTLY ==> mWidth/mHeight = SpecSize
 *      b). wrap_content        : SpecMode=AT_MOST ==> 可以不重写onMeasure
 * 4、onDraw()
 *      根据onMeasure获得的measuredWidth/measuredHeight，绘制View
 */

public class MyImageView extends View {
    private Paint mBitmapPaint;
    private Drawable mDrawable;
    private Bitmap mBitmap;
    // view宽高
    private int mWidth;
    private int mHeight;

    public MyImageView(Context context) {
        this(context, null);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 初始化画笔
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);
        // 初始化属性
        initAttrs(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 获得specSize、specMode
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        // 设置View宽高
        setMeasuredDimension(measureWidth(widthMode, width), measureHeight(heightMode, height));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap == null) {
            // 根据测量的图片宽高获得bitmap
            mBitmap = Bitmap.createScaledBitmap(ImageUtils.drawable2Bitmap(mDrawable), getMeasuredWidth(), getMeasuredHeight(), true);
        }
        // 绘制图片
        canvas.drawBitmap(mBitmap, getLeft(), getTop(), mBitmapPaint);
    }

    /*=========================== 自定义方法 ==================================*/
    private void initAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = null;
            try {
                // 读取MyImageView的自定义属性集
                array = getContext().obtainStyledAttributes(attrs, R.styleable.MyImageView);
                // 根据图片id获得drawable
                mDrawable = array.getDrawable(R.styleable.MyImageView_src);
                // 测量mDrawable宽高
                if (mDrawable == null) throw new RuntimeException("图片不能为空");
                mWidth = mDrawable.getIntrinsicWidth();
                mHeight = mDrawable.getIntrinsicHeight();
            } finally {
                if (array != null) array.recycle();
            }
        }
    }

    private int measureWidth(int mode, int width) {
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                mWidth = width;
                break;
        }
        return mWidth;
    }

    private int measureHeight(int mode, int height) {
        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                break;
            case MeasureSpec.EXACTLY:
                mHeight = height;
                break;
        }
        return mHeight;
    }
}
