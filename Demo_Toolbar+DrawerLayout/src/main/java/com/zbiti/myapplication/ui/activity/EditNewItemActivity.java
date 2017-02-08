package com.zbiti.myapplication.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.baoyz.actionsheet.ActionSheet;
import com.zbiti.myapplication.MyApplication;
import com.zbiti.myapplication.R;
import com.zbiti.myapplication.control.NavigateManager;
import com.zbiti.myapplication.framework.dialog.CommonDialog;
import com.zbiti.myapplication.framework.eventbus.EventEntity;
import com.zbiti.myapplication.framework.eventbus.EventType;
import com.zbiti.myapplication.sharedPreference.ItemSharedPreference;
import com.zbiti.myapplication.util.DateUtil;
import com.zbiti.myapplication.util.image.CompressImageHelper;
import com.zbiti.myapplication.util.image.GetPathFromUri4kitkat;
import com.zbiti.myapplication.util.image.ImageManager;
import com.zbiti.myapplication.widget.UploadImageView;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.devland.esperandro.Esperandro;
import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.functions.Action1;

public class EditNewItemActivity extends BaseActivity implements View.OnClickListener{
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.et_item_name)
    EditText etItemName;
    @Bind(R.id.et_item_price)
    EditText etItemPrice;
    @Bind(R.id.et_item_content)
    EditText etItemContent;
    @Bind(R.id.hs_images)
    HorizontalScrollView hsImages;
    @Bind(R.id.ll_container)
    LinearLayout llContainer;
    @Bind(R.id.iv_item_image)
    ImageView ivItemImage;
    @Bind(R.id.tv_item_submit)
    TextView tvItemSubmit;

    private String takePicturePath;
    private String imagePath;
    private Uri uri;

    private ItemSharedPreference itemSharedPreference;
    private List<UploadImageView> uploadImageViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_new_item);

        ButterKnife.bind(this);
        initDate();
        initView();
        initListener();
    }

    private void initDate(){
        itemSharedPreference = Esperandro.getPreferences(ItemSharedPreference.class, mContext);
        itemSharedPreference.clear();
    }

    private void initView(){
        initToolBar(toolbar, true, "发布商品");
    }

    private void initListener(){
        ivItemImage.setOnClickListener(this);
        tvItemSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_item_image:
                showSelectImageDialog();
                break;
            case R.id.tv_item_submit:
                submitNewItem();
                break;
        }
    }

    private void submitNewItem(){
        if(TextUtils.isEmpty(etItemName.getText().toString().trim()) || TextUtils.isEmpty(etItemPrice.getText().toString().trim()) || TextUtils.isEmpty(etItemContent.getText().toString().trim())){
            Toast.makeText(EditNewItemActivity.this, "请填写完整！", Toast.LENGTH_SHORT).show();
            return;
        }
        itemSharedPreference.name(etItemName.getText().toString().trim());
        itemSharedPreference.price(Float.parseFloat(etItemPrice.getText().toString().trim()));
        itemSharedPreference.content(etItemContent.getText().toString().trim());
        // 模拟图片上传效果
        Observable.from(new String[]{"10", "30", "50", "80", "100"})
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(final String s) {
                        if(uploadImageViews!=null && uploadImageViews.size()>0) {
                            for (UploadImageView uploadImageView : uploadImageViews) {
                                uploadImageView.setProgress(Integer.parseInt(s));
                            }
                        }
                    }
                });
        // 通知Fragment2更新数据 (当fragment2处于前台，就会自动完成更新，否则不会，当然也不需要，下次切换时自然会刷新数据)
        EventBus.getDefault().post(new EventEntity(EventType.UPDATE_FRAGMENT_LIST2));
        loadingDialog.dismiss();
        CommonDialog.showSuccessDialog(this, new CommonDialog.DismissListener() {
            @Override
            public void onDismiss() {
                finish();
            }
        });
    }

    // 仿ios ActionSheet：底部弹出菜单
    private void showSelectImageDialog() {
        if (itemSharedPreference.imageList() != null && itemSharedPreference.imageList().size() >= 9) {
            Toast.makeText(EditNewItemActivity.this, "最多上传9张图片！", Toast.LENGTH_SHORT).show();
        } else {
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
                                    takePicturePath = "/" + MyApplication.APP_CACHE_DIR + "/" + DateUtil.getCurrentMillis() + ".jpg";
                                    NavigateManager.gotoTakePicture(EditNewItemActivity.this, takePicturePath);
                                    break;
                                case 1:
                                    NavigateManager.gotoChoosePicture(EditNewItemActivity.this);
                                    break;
                            }
                        }
                    }).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case NavigateManager.TAKE_PICTURE_REQUEST_CODE:
                    imagePath = Environment.getExternalStorageDirectory() + takePicturePath;
                    setImageViewWithPath(imagePath);
                    break;
                case NavigateManager.CHOOSE_PICTURE_REQUEST_CODE:
                    uri = data.getData();
                    imagePath = GetPathFromUri4kitkat.getPath(this, uri);
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
        // 压缩
        String thumbnailImagePath = CompressImageHelper.compressImageView(mContext, imagePath);
        // 模拟保存
        List<String> imageList = itemSharedPreference.imageList();
        if(imageList==null){
            imageList = new ArrayList<String>();
        }
        imageList.add(thumbnailImagePath);
        itemSharedPreference.imageList(imageList);
        // 显示
        hsImages.setVisibility(View.VISIBLE);
        updateImageLayout();
    }

    private void updateImageLayout(){
        // 先清空，然后往里面addView
        llContainer.removeAllViews();
        uploadImageViews = new ArrayList<UploadImageView>();
        List<String> imageList = itemSharedPreference.imageList();
        for(int i=0; i<imageList.size(); i++){
            final int position = i;
            View view = View.inflate(mContext, R.layout.item_image_pick_layout, null);

            UploadImageView ivSelectedImage = ButterKnife.findById(view, R.id.iv_selected_image);
            ImageManager.getInstance().loadLocalImage(mContext, imageList.get(i), ivSelectedImage);
            uploadImageViews.add(ivSelectedImage);
            ImageView ivDeleteImage = ButterKnife.findById(view, R.id.iv_delete_image);
            ivDeleteImage.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    List<String> l = itemSharedPreference.imageList();
                    l.remove(position);
                    itemSharedPreference.imageList(l);
                    updateImageLayout();
                    if(itemSharedPreference.imageList()==null || itemSharedPreference.imageList().size()==0){
                        hsImages.setVisibility(View.GONE);
                    }
                }
            });

            llContainer.addView(view);
        }

        // 最后在加载一张按钮功能的image
        View view = View.inflate(mContext, R.layout.item_image_pick_layout, null);
        UploadImageView ivSelectedImage = ButterKnife.findById(view, R.id.iv_selected_image);
        ImageView ivDeleteImage = ButterKnife.findById(view, R.id.iv_delete_image);
        ivSelectedImage.setProgressFinish();
        ivDeleteImage.setVisibility(View.INVISIBLE);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectImageDialog();
            }
        });
        ivSelectedImage.setImageResource(R.mipmap.ic_add_image);
        llContainer.addView(view);
    }
}
