package com.zbiti.myapplication.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.zbiti.myapplication.ui.activity.EditNewItemActivity;
import com.zbiti.myapplication.ui.activity.ItemDetailActivity;
import com.zbiti.myapplication.ui.activity.MainActivity;
import com.zbiti.myapplication.ui.activity.ProfileActivity;

import java.io.File;
import java.io.IOException;


/**
 * Created by admin on 2016/6/15.
 */
public class NavigateManager {
    public static final int PROFILE_REQUEST_CODE = 100;
    public static final int TAKE_PICTURE_REQUEST_CODE = 101;
    public static final int CHOOSE_PICTURE_REQUEST_CODE = 102;

    public static void gotoMainActivity(Activity activity){
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        activity.startActivity(intent);
        activity.finish();
    }

    // 个人中心
    public static void gotoProfileActivity(Activity activity, Boolean isGotoMain) {
        Intent intent = new Intent(activity, ProfileActivity.class);
        intent.putExtra("isGotoMain", isGotoMain);
        activity.startActivityForResult(intent, PROFILE_REQUEST_CODE);
    }

    // 拍照
    public static void gotoTakePicture(Activity activity) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            activity.startActivityForResult(intent, TAKE_PICTURE_REQUEST_CODE);
        } else {
            Toast.makeText(activity, "SD卡异常！", Toast.LENGTH_LONG).show();
        }
    }

    // 拍照 (指定保存路径) ps. picName需要不同，不然每次都是第一张 （貌似delete有点无力~）
    public static void gotoTakePicture(Activity activity, String takePicturePath) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 创建文件路径
            String directoryPath = Environment.getExternalStorageDirectory() + takePicturePath.substring(0, takePicturePath.lastIndexOf("/"));
            File fileDirectory = new File(directoryPath);
            if (!fileDirectory.exists()) {
                fileDirectory.mkdirs();
            }
            // 创建文件
            File mPhotoFile = null;
            try {
                mPhotoFile = new File(Environment.getExternalStorageDirectory() + takePicturePath);
                if (!mPhotoFile.exists()) {
                    mPhotoFile.createNewFile();
                }
            } catch (IOException e) {
                Toast.makeText(activity, "找不到文件或文件目录！", Toast.LENGTH_LONG).show();
            }
            // 写入uri，并返回
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPhotoFile));
            activity.startActivityForResult(intent, TAKE_PICTURE_REQUEST_CODE);
        } else {
            Toast.makeText(activity, "SD卡异常！", Toast.LENGTH_LONG).show();
        }
    }

    // 从相册选择
    public static void gotoChoosePicture(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        activity.startActivityForResult(intent, CHOOSE_PICTURE_REQUEST_CODE);
    }

    public static void gotoItemDetailActivity(Context context, String url){
        Intent intent = new Intent(context, ItemDetailActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    public static void gotoSystemExplore(Context context, String url){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        context.startActivity(intent);
    }

    public static void gotoEditNewItemActivity(Activity activity){
        activity.startActivity(new Intent(activity, EditNewItemActivity.class));
    }
}
