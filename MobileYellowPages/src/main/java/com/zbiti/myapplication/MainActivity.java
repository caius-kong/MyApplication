package com.zbiti.myapplication;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.TaskStackBuilder;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.zbiti.myapplication.db.DBInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Android Studio的Activity默认继承AppCompatActivity，以实现ActionBar
 * <p/>
 * 子页面仅仅配置android:parentActivityName=".MainActivity"
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    private GridView gridView;
    private ListView listView;
    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getLoaderManager().initLoader(0, null, this); // 初始化Loader
        init();
    }

    private void init() {
        gridView = (GridView) findViewById(R.id.gridView);
        SimpleAdapter adapter1 = new SimpleAdapter(this, getListMap(), R.layout.item1,
                new String[]{"image", "text"}, new int[]{R.id.imageButton, R.id.textView2});
        gridView.setAdapter(adapter1);
        gridView.setOnItemClickListener(this);

        listView = (ListView) findViewById(R.id.listView);
//        SimpleAdapter adapter2 = new SimpleAdapter(this, getList(), R.layout.item2,
//                new String[]{"icon", "name", "phone"}, new int[]{R.id._icon, R.id.name, R.id.phone});
//        listView.setAdapter(adapter2);

        adapter = new SimpleCursorAdapter(this, R.layout.item2, null,
                new String[]{DBInfo.MOBILE_TABLE.ICON, DBInfo.MOBILE_TABLE.NAME, DBInfo.MOBILE_TABLE.PHONE},
                new int[]{R.id._icon, R.id.name, R.id.phone}, BIND_ABOVE_CLIENT);
        listView.setAdapter(adapter);
    }

    private List<Map<String, Object>> getListMap() {
        List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
        int[] icon = {R.drawable.pic_1, R.drawable.pic_2, R.drawable.pic_3, R.drawable.pic_4,
                R.drawable.pic_5, R.drawable.pic_6, R.drawable.pic_7, R.drawable.pic_8};
        String[] iconName = {"快递", "交通出行", "酒店旅游", "医院", "银行", "公共服务", "品牌售后",
                "保险证券"};
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", icon[i]);
            map.put("text", iconName[i]);
            listMap.add(map);
        }
        return listMap;
    }

    private List<Map<String, Object>> getList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        int[] icon = {R.drawable.pic_1, R.drawable.pic_2, R.drawable.pic_3, R.drawable.pic_4,
                R.drawable.pic_5, R.drawable.pic_6, R.drawable.pic_7, R.drawable.pic_8};
        String[] name = {"中通快递", "中国人保", "圆通快递", "招商银行", "浙商银行", "小米", "农业银行",
                "中国南方航空"};
        String[] phone = new String[]{"95311", "95518", "95554", "95421", "93214", "99745", "91234", "93241"};
        for (int i = 0; i < icon.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("icon", icon[i]);
            map.put("name", name[i]);
            map.put("phone", phone[i]);
            list.add(map);
        }
        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        System.out.println("-position-->" + position);
        switch (position) {
            case 0:
                // Toast.makeText(MainActivity.this, "跳转到快递", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, ExpressActivity.class));
                break;
            case 1:
                Toast.makeText(MainActivity.this, "跳转到交通出行", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(MainActivity.this, "跳转到酒店旅游", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(MainActivity.this, "跳转到医院", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(MainActivity.this, "跳转到银行", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                Toast.makeText(MainActivity.this, "跳转到公共服务", Toast.LENGTH_SHORT).show();
                break;
            case 6:
                Toast.makeText(MainActivity.this, "跳转到品牌售后", Toast.LENGTH_SHORT).show();
                break;
            case 7:
                Toast.makeText(MainActivity.this, "跳转到保险证券", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // 通过构建并返回一个CursorLoader，该loader中包含了查询到的结果集
        String[] projection = new String[]{DBInfo.MOBILE_TABLE.ID, DBInfo.MOBILE_TABLE.ICON, DBInfo.MOBILE_TABLE.NAME, DBInfo.MOBILE_TABLE.PHONE};
        String selection = DBInfo.MOBILE_TABLE.ISHOT + " = '1'";
        if (args != null) {
            selection = args.getString("selection");
        }
        String[] selectionArgs = null;
        String sortOrder = null;
        CursorLoader loader = new CursorLoader(this, MyContentProvider.CONTENT_URI, projection, selection, selectionArgs, sortOrder);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // 根据返回的cursor,更新UI
        adapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // 重置UI
        adapter.changeCursor(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this); // 当Activity重启时，来重新启动Loader
    }
}
