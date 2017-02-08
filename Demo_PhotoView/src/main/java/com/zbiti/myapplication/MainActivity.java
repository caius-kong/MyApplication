package com.zbiti.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
//    ImageView mImageView;
//    PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        // Any implementation of ImageView can be used!
//        mImageView = (ImageView) findViewById(R.id.imageview1);
//
//        // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
//        // (not needed unless you are going to change the drawable later)
//        mAttacher = new PhotoViewAttacher(mImageView);
    }

    // If you later call mImageView.setImageDrawable/setImageBitmap/setImageResource/etc then you just need to call
//    mAttacher.update();
}
