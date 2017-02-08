package com.zbiti.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 2016/3/13.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    public MySQLiteOpenHelper(Context context){
        super(context, DBInfo.DB.DB_NAME, null, DBInfo.DB.VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBInfo.MOBILE_TABLE.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 注意：这样原有数据会丢失
        db.execSQL(DBInfo.MOBILE_TABLE.DROP_TABLE);
        onCreate(db);
    }
}
