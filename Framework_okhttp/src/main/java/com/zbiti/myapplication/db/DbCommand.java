package com.zbiti.myapplication.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Looper;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by kongyunhui on 2017/1/11.
 *
 * 使用AsyncTask设计原理，封装db.query(...)
 *
 * 后记：原来有类似的设计了。
 * 1、ContentProvider: 封装数据源，统一提供crud (但是这个没有封装异步请求)
 *   区别在于：ContentProvider是可以跨进程共享的
 * 2、实现LoaderManager.LoaderCallbacks<Cursor>类: 不仅提供了query，而且封装了异步请求(后台load，前台ui)
 *   区别在于：它load返回Cursor，而我这里利用泛型直接返回List<T>
 */

@SuppressWarnings("ALL")
public class DbCommand<T> {
    private static ExecutorService mExecutor = Executors.newSingleThreadExecutor(); // 单线程线程池，保证线程安全
    private final static Handler mUIHandler = new Handler(Looper.getMainLooper());
    private final static Gson mGson = new Gson();

    /**
     * 数据库查询
     * @param db
     * @param table
     * @param columns null: 默认查询所有字段
     * @param selection
     * @param selectionArgs
     * @param groupBy
     * @param having
     * @param orderBy
     * @param limit
     * @param callback 接口回调，实现 UI更新 的方法
     */
    @SuppressWarnings("FinalStaticMethod")
    public static void query(final SQLiteDatabase db, final String table, final String[] columns, final String selection, final String[] selectionArgs,
                                   final String groupBy, final String having, final String orderBy, final String limit,
                                   final ResultCallback callback){
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
                List<Map<String, String>> list = new ArrayList<Map<String, String>>(cursor.getCount());
                while(cursor.moveToNext()) {
                    int columnCount = cursor.getColumnCount();
                    Map<String, String> map = new HashMap<String, String>();
                    for(int i=0; i<columnCount; i++){
                        String key = cursor.getColumnName(i);
                        String value = cursor.getString(i);
                        map.put(key, value);
                    }
                    list.add(map);
                }
                cursor.close();
                // 此处不需要利用Gson的泛型类型处理，JSON.toJSON可以将list转化为一个序列化对象
                Object o = JSON.toJSON(list); // list --> Serializable Object （ps. Object JSON.parse(str)，此处的Object指JSONObject/JSONArray）
                postResult(o, callback);
            }
        });
    }

    /**
     * 数据库插入
     * @param db
     * @param table
     * @param nullColumnHack
     * @param values
     * @return -1: 插入失败
     */
    @SuppressWarnings("FinalStaticMethod")
    public static void insert(final SQLiteDatabase db, final String table, final String nullColumnHack, final ContentValues values, final ResultCallback callback){
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                long count = db.insert(table, nullColumnHack, values);
                postResult(count, callback);
            }
        });
    }

    /**
     * 数据库删除
     * @param db
     * @param table
     * @param whereClause
     * @param whereArgs
     */
    @SuppressWarnings("FinalStaticMethod")
    public static void delete(final SQLiteDatabase db, final String table, final String whereClause, final String[] whereArgs, final ResultCallback callback){
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                int count = db.delete(table, whereClause, whereArgs);
                postResult(count, callback);
            }
        });

    }

    /**
     * 数据库更新
     * @param db
     * @param table
     * @param values
     * @param whereClause
     * @param whereArgs
     * @return
     */
    @SuppressWarnings("FinalStaticMethod")
    public static void update(final SQLiteDatabase db, final String table, final ContentValues values, final String whereClause, final String[] whereArgs, final ResultCallback callback){
        mExecutor.execute(new Runnable() {
            @Override
            public void run() {
                int count = db.update(table, values, whereClause, whereArgs);
                postResult(count, callback);
            }
        });
    }

    /**
     * 1、使用handler将 UI更新 投递到主线程（为什么handler.post()到主线程？因为handler绑定MainLooper，因此该handler与主线程关联）
     * 2、此处使用接口回调，具体的 UI更新 会在调用query()的线程中实现
     * @param result
     * @param callback
     */
    private static void postResult(final Object result, final ResultCallback callback){
        mUIHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.updateUI(result); // 将Serializable Object强制转化成T (依据：T extends Object + Serializable)
            }
        });
    }

    /**
     * 定义接口回调方法
     * @param <T>
     */
    public static abstract class ResultCallback<T>{
        public abstract void updateUI(T result);
    }
}
