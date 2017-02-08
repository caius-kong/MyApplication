package com.zbiti.myapplication;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener {
    private TextView topTitle;
    private LinearLayout tab1_btn, tab2_btn, tab3_btn, tab4_btn;
    private ImageView message_img, contacts_img, news_img, setting_img;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        initEvents();
    }

    private void initViews() {
        topTitle = (TextView) findViewById(R.id.topTitle);
        tab1_btn = (LinearLayout) findViewById(R.id.tab1_btn);
        tab2_btn = (LinearLayout) findViewById(R.id.tab2_btn);
        tab3_btn = (LinearLayout) findViewById(R.id.tab3_btn);
        tab4_btn = (LinearLayout) findViewById(R.id.tab4_btn);

        message_img = (ImageView) findViewById(R.id.message_img);
        contacts_img = (ImageView) findViewById(R.id.contacts_img);
        news_img = (ImageView) findViewById(R.id.news_img);
        setting_img = (ImageView) findViewById(R.id.setting_img);
    }

    private void initEvents() {
        tab1_btn.setOnClickListener(this);
        tab2_btn.setOnClickListener(this);
        tab3_btn.setOnClickListener(this);
        tab4_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mFragmentManager = getFragmentManager();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        resetUI();
        switch (v.getId()) {
            case R.id.tab1_btn:
                topTitle.setText("消息");
                message_img.setImageResource(R.drawable.message_selected);
                transaction.replace(R.id.content, new MessageFragment());
                break;
            case R.id.tab2_btn:
                topTitle.setText("联系人");
                contacts_img.setImageResource(R.drawable.contacts_selected);
                transaction.replace(R.id.content, new ContactsFragment());
                break;
            case R.id.tab3_btn:
                topTitle.setText("动态");
                news_img.setImageResource(R.drawable.news_selected);
                transaction.replace(R.id.content, new NewsFragment());
                break;
            case R.id.tab4_btn:
                topTitle.setText("设置");
                setting_img.setImageResource(R.drawable.setting_selected);
                transaction.replace(R.id.content, new SettingFragment());
                break;
        }
        transaction.commit();
    }

    private void resetUI() {
        message_img.setImageResource(R.drawable.message_unselected);
        contacts_img.setImageResource(R.drawable.contacts_unselected);
        news_img.setImageResource(R.drawable.news_unselected);
        setting_img.setImageResource(R.drawable.setting_unselected);
    }
}
