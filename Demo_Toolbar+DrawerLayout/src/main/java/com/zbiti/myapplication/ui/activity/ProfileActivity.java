package com.zbiti.myapplication.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.baoyz.actionsheet.ActionSheet;
import com.zbiti.myapplication.MyApplication;
import com.zbiti.myapplication.R;
import com.zbiti.myapplication.framework.eventbus.EventEntity;
import com.zbiti.myapplication.framework.eventbus.EventType;
import com.zbiti.myapplication.sharedPreference.UserSharedPreferences;
import com.zbiti.myapplication.util.image.CompressImageHelper;
import com.zbiti.myapplication.util.image.GetPathFromUri4kitkat;
import com.zbiti.myapplication.util.image.ImageManager;
import com.zbiti.myapplication.control.NavigateManager;
import com.zbiti.myapplication.util.image.ImageTools;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.devland.esperandro.Esperandro;
import de.greenrobot.event.EventBus;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.rl_user_avatar)
    RelativeLayout rlUserAvatar;
    @Bind(R.id.iv_user_avatar)
    ImageView ivUserAvatar;
    @Bind(R.id.rl_nick_name)
    RelativeLayout rlNickName;
    @Bind(R.id.tv_nick_name)
    TextView tvNickName;
    @Bind(R.id.rl_user_sign)
    RelativeLayout rlUserSign;
    @Bind(R.id.tv_user_sign)
    TextView tvUserSign;
    @Bind(R.id.tv_logout)
    TextView tvLogout;

//    private String takePicturePath = "/" + MyApplication.APP_CACHE_DIR + "/avatar.jpg";
    private String imagePath;
    private Uri uri;

    private boolean isGotoMain = false;

    private final int NICK_NAME = 1;
    private final int USER_SIGN = 2;

    private UserSharedPreferences userSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
        initData();
        initView();
        initListener();
    }

    private void initData() {
        if (getIntent() != null && getIntent().hasExtra("isGotoMain")) {
            isGotoMain = getIntent().getBooleanExtra("isGotoMain", isGotoMain);
        }
    }

    private void initView() {
        initToolBar(toolbar, true, "个人中心");

        // 获得用户信息，更新上面3个组件的值 (用本地数据模拟)
        userSharedPreferences = Esperandro.getPreferences(UserSharedPreferences.class,this);
        imagePath = userSharedPreferences.userAvatarPath();
        setImageViewWithPath(imagePath);
    }

    private void initListener() {
        rlUserAvatar.setOnClickListener(this);
        rlNickName.setOnClickListener(this);
        rlUserSign.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_user_avatar:
                showSelectAvatarDialog();
                break;
            case R.id.rl_nick_name:
                showEditTextDialog("昵称", NICK_NAME, tvNickName.getText()==null?"":tvNickName.getText().toString());
                break;
            case R.id.rl_user_sign:
                showEditTextDialog("个人签名", USER_SIGN, tvUserSign.getText()==null?"":tvUserSign.getText().toString());
                break;
            case R.id.tv_logout:
                logout();
                break;
        }
    }

    // 仿ios ActionSheet：底部弹出菜单
    private void showSelectAvatarDialog() {
        ActionSheet.createBuilder(this, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("拍照", "从相册选择")
                .setCancelableOnTouchOutside(true)
                .setListener(new ActionSheet.ActionSheetListener() {
                    @Override
                    public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                    }

                    @Override
                    public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                        switch (index) {
                            case 0:
                                NavigateManager.gotoTakePicture(ProfileActivity.this);
                                break;
                            case 1:
                                NavigateManager.gotoChoosePicture(ProfileActivity.this);
                                break;
                        }
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case NavigateManager.TAKE_PICTURE_REQUEST_CODE:
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap)bundle.get("data");
                    uri = ImageTools.bitmapToUri(bitmap);
                    imagePath = GetPathFromUri4kitkat.getPath(this, uri);
                    setImageViewWithPath(imagePath);
                    break;
                case NavigateManager.CHOOSE_PICTURE_REQUEST_CODE:
                    uri = data.getData();
                    imagePath = GetPathFromUri4kitkat.getPath(this, uri);
                    // 如果imagePath没有获取到，则寻找媒体库中的第一张照片
                    if (TextUtils.isEmpty(imagePath)) {
                        String[] proj = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                            cursor.close();
                        }
                    }
                    setImageViewWithPath(imagePath);
                    break;
            }
        }
    }

    private void setImageViewWithPath(String imagePath) {
        ImageManager.getInstance().loadCircleLocalImage(mContext, imagePath, ivUserAvatar);
        uploadAvatar(imagePath);
    }

    private void uploadAvatar(String imagePath){
        // 大于500k的图片进行压缩再上传
        String thumbnailImagePath = CompressImageHelper.compressImageView(mContext, imagePath);
        // 模拟上传
        userSharedPreferences = Esperandro.getPreferences(UserSharedPreferences.class, this);
        userSharedPreferences.userAvatarPath(thumbnailImagePath);
        // 通知Fragment1更新数据
        EventBus.getDefault().post(new EventEntity(EventType.UPDATE_FRAGMENT_LIST));
        // 通知更新头像
        setResult(NavigateManager.PROFILE_REQUEST_CODE);
    }

    private EditText editText;

    private void showEditTextDialog(final String title, final int type, final String content){
        MaterialDialog materialDialog = new MaterialDialog.Builder(this)
                .title(title)
                .customView(R.layout.material_dialog_input_layout, true)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .negativeColor(getResources().getColor(R.color.font_black_3))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        switch (type){
                            case NICK_NAME:
                                tvNickName.setText(editText.getText().toString().trim());
                                // 上传数据
                                // 通知fragment更新数据
                                // 通知MainActivity更新
                                // ...
                                break;
                            case USER_SIGN:
                                tvUserSign.setText(editText.getText().toString().trim());
                                // ...
                                break;
                        }
                    }
                }).build();
        // 注意：需要在show之前设置view中的数据
        View view = materialDialog.getCustomView();
        editText = ButterKnife.findById(view, R.id.et_dialog_input);
        if (!TextUtils.isEmpty(content)) {
            editText.setText(content);
            editText.setSelection(content.length());
        } else {
            editText.setHint("请输入" + title);
        }

        materialDialog.show();
    }

    private void logout(){
        new MaterialDialog.Builder(this)
                .content("确认退出登录？")
                .contentColor(getResources().getColor(R.color.font_black_3))
                .positiveText(R.string.ok)
                .negativeText(R.string.cancel)
                .negativeColor(getResources().getColor(R.color.font_black_3))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        Toast.makeText(ProfileActivity.this, "注销->该功能未编写！", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }
}
