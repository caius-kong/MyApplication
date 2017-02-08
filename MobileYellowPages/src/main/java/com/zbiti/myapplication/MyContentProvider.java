package com.zbiti.myapplication;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import com.zbiti.myapplication.db.DBInfo;
import com.zbiti.myapplication.db.MySQLiteOpenHelper;

/**
 * Created by admin on 2016/3/13.
 */
public class MyContentProvider extends ContentProvider{
    public static final Uri CONTENT_URI = Uri.parse("content://com.zbiti.myapplication.MyContentProvider/mobiles");

    private static final int ALLROWS = 1;
    private static final int SINGLE_ROW = 2;
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // 以'mobiles'结尾的URI对应请求全部数据;
        uriMatcher.addURI("com.zbiti.myapplication.MyContentProvider", "mobiles", ALLROWS);
        // 以'mobiles/[rowID]'结尾的URI对应请求单行数据
        uriMatcher.addURI("com.zbiti.myapplication.MyContentProvider", "mobiles/#", SINGLE_ROW);
    }

    private MySQLiteOpenHelper myOpenHelper;
    @Override
    public boolean onCreate() {
        myOpenHelper = new MySQLiteOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = myOpenHelper.getReadableDatabase();
        //基于行查询的便利辅助类
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DBInfo.MOBILE_TABLE.TABLE_NAME);
        // 如果是行查询，需要添加where条件
        switch (uriMatcher.match(uri)){
            case SINGLE_ROW :
                String rowID = uri.getPathSegments().get(1);
                queryBuilder.appendWhere(DBInfo.MOBILE_TABLE.ID + "=" + rowID);
            default:break;
        }
        String groupBy = null;
        String having = null;
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, groupBy, having, sortOrder);
        // 注册当游标结果集改变时将通知的上下文ContentResolver
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        long id = db.insert(DBInfo.MOBILE_TABLE.TABLE_NAME, null, values);
        if(id > -1){
            // 给CONTENT_URI附加新的行ID
            Uri insertUri = ContentUris.withAppendedId(CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(insertUri, null);
            return insertUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        // 如果是行URI，需要限定删除的行
        switch (uriMatcher.match(uri)) {
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                selection = DBInfo.MOBILE_TABLE.ID + "=" + rowID
                        + (!TextUtils.isEmpty(selection)?
                        " and (" + selection + ')' : "");
            default:break;
        }
        // 想要返回deleteCount，必须指定一条where子句。—— 删除所有的行并返回一个值，同时传入"1"
        if(selection == null){
            selection = "1";
        }
        int deleteCount = db.delete(DBInfo.MOBILE_TABLE.TABLE_NAME, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = myOpenHelper.getWritableDatabase();
        // 如果是行URI，需要限定更新的行
        switch (uriMatcher.match(uri)) {
            case SINGLE_ROW:
                String rowID = uri.getPathSegments().get(1);
                selection = DBInfo.MOBILE_TABLE.ID + "=" + rowID
                        + (!TextUtils.isEmpty(selection)?
                        " and (" + selection + ')' : "");
            default:break;
        }
        int updateCount = db.update(DBInfo.MOBILE_TABLE.TABLE_NAME, values, selection, selectionArgs);
        // 通知所有的观察者，数据集已经改变
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        // 为一个Content Provider URI返回一个字符串，它标识了MIME类型
        switch (uriMatcher.match(uri)) {
            case ALLROWS:
                return "vnd.android.cursor.dir/vnd.zbiti.elemental";
            case SINGLE_ROW:
                return "vnd.android.cursor.item/vnd.zbiti.elemental";
            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
    }
}
