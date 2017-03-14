package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * 自定义View
 */
public class MainActivity extends AppCompatActivity{
    private MyImageView myImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
    }

    private void initWidgets(){
        myImageView = (MyImageView) findViewById(R.id.picIV);
//        setupValueAnimator();
//        setupObjectAnimator();
        setupAnimatorSet();
    }

    /**
     * valueAnimator是属性动画的核心类: 高度自由定制，因此显得比较繁琐
     */
    private void setupValueAnimator(){
        ValueAnimator valueAnimator = (ValueAnimator) AnimatorInflater.loadAnimator(getApplicationContext(), R.animator.value_animator);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                myImageView.setAlpha((float)valueAnimator.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    /**
     * ValueAnimator的子类，能够直接操作任意对象的任意属性
     */
    private void setupObjectAnimator(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(myImageView, "translationX", 0, 50, 0);
        objectAnimator.setDuration(2000);
        objectAnimator.start();
    }

    private void setupAnimatorSet(){
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(myImageView, "alpha", 1.0f, 0.5f, 1.0f).setDuration(2000);
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(myImageView, "translationX", 0, 50, 0).setDuration(2000);
        ObjectAnimator anim3 = ObjectAnimator.ofFloat(myImageView, "scaleX", 1, 2, 1).setDuration(2000);

        AnimatorSet animSet = new AnimatorSet();
        // after很怪。。。表示anim3执行之后再执行其他的
        animSet.play(anim1).with(anim2).after(anim3);
        animSet.setDuration(6000);
        animSet.start();
    }
}
