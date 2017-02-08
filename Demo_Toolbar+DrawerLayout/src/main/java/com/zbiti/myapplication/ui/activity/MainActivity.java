package com.zbiti.myapplication.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.shamanland.fab.FloatingActionButton;
import com.zbiti.myapplication.R;
import com.zbiti.myapplication.sharedPreference.UserSharedPreferences;
import com.zbiti.myapplication.ui.fragment.Fragment1;
import com.zbiti.myapplication.ui.fragment.Fragment2;
import com.zbiti.myapplication.ui.fragment.Fragment3;
import com.zbiti.myapplication.util.ShareUtil;
import com.zbiti.myapplication.util.image.ImageManager;
import com.zbiti.myapplication.control.NavigateManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.devland.esperandro.Esperandro;

public class MainActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fl_container)
    FrameLayout flContainer;
    @Bind(R.id.floating_action_button)
    FloatingActionButton floatingActionButton;
    @Bind(R.id.main_navigation_layout)
    NavigationView mainNavigationLayout;
    @Bind(R.id.main_drawer_layout)
    DrawerLayout mainDrawerLayout;

    private ImageView ivUserAvatar;
    private TextView tvNickName;
    private TextView tvUserSign;

    private String[] titles;
    private long lastTime;

    private UserSharedPreferences userSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();
        initView();
        initListener();
    }

    private void initData(){
        titles = new String[3];
        titles[0] = getString(R.string.menu_square_bingo);
        titles[1] = getString(R.string.menu_my_bingo);
        titles[2] = getString(R.string.menu_my_favorite);
    }

    private void initView(){
        initToolBar(toolbar, false, "DEMO");
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mainDrawerLayout, toolbar, 0, 0);
        drawerToggle.syncState();

        controlShowFragment(0);
        setOvalShapeViewBackground(floatingActionButton);
        View view = mainNavigationLayout.getHeaderView(0);
        ivUserAvatar = ButterKnife.findById(view, R.id.iv_user_avatar);
        tvNickName = ButterKnife.findById(view, R.id.tv_nick_name);
        tvUserSign = ButterKnife.findById(view, R.id.tv_user_sign);

        // 获得用户信息，更新上面3个组件的值
        // ...
    }

    private void initListener(){
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateManager.gotoEditNewItemActivity(MainActivity.this);
            }
        });

        mainNavigationLayout.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_square_bingo:
                        controlShowFragment(0);
                        break;
                    case R.id.nav_my_bingo:
                        controlShowFragment(1);
                        break;
                    case R.id.nav_favorite_bingo:
                        controlShowFragment(2);
                        break;
                    case R.id.nav_switch_theme:
                        Toast.makeText(MainActivity.this, "目前还没有编写该功能！", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_feedback:
                        ShareUtil.feedback(MainActivity.this);
                        break;
                    case R.id.nav_send_to_friend:
                        ShareUtil.sendToFriend(MainActivity.this);
                        break;
                    case R.id.nav_share:
                        ShareUtil.share(MainActivity.this);
                        break;
                }
                menuItem.setChecked(true);
                return true;
            }
        });

        ivUserAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigateManager.gotoProfileActivity(MainActivity.this, true);
            }
        });
    }

    private void controlShowFragment(int position){
        // 改变fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_container, getFragment(position));
        fragmentTransaction.commit();
        // 关闭DrawerLayout
        if (mainDrawerLayout.isShown()) {
            mainDrawerLayout.closeDrawers();
        }
        // 更新toolbar的标题
        toolbar.setTitle(titles[position]);
    }

    private Fragment getFragment(int position){
        Fragment fragment;
        switch (position){
            case 0:
                fragment = new Fragment1();
                break;
            case 1:
                fragment = new Fragment2();
                break;
            case 2:
                fragment = new Fragment3();
                break;
            default:
                fragment = new Fragment1();
                break;
        }
        return fragment;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_menu_app:
                Toast.makeText(MainActivity.this, "这是一个DEMO", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item_menu_author:
                Toast.makeText(MainActivity.this, "作者：XXX", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mainDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mainDrawerLayout.closeDrawers();
            return ;
        }
        if (System.currentTimeMillis() - lastTime < 2000) {
            super.onBackPressed();
        } else {
            lastTime = System.currentTimeMillis();
            Toast.makeText(MainActivity.this, getString(R.string.toast_exit_tip), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case NavigateManager.PROFILE_REQUEST_CODE:
                // 模拟数据库
                userSharedPreferences = Esperandro.getPreferences(UserSharedPreferences.class, this);
                String thumbnailImagePath = userSharedPreferences.userAvatarPath();
                if(thumbnailImagePath != null){
                    ImageManager.getInstance().loadCircleLocalImage(mContext, thumbnailImagePath, ivUserAvatar);
                }
                break;
        }
    }
}
