package com.zbiti.myapplication;

import android.app.ActionBar;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.zbiti.myapplication.db.DBInfo;

public class ExpressActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express);
        getLoaderManager().initLoader(0, null, this); // 初始化Loader
        init();
    }

    private void init(){
        listView = (ListView)findViewById(R.id.listView_express);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // 通过构建并返回一个CursorLoader，该loader中包含了查询到的结果集
        String[] projection = new String[]{DBInfo.MOBILE_TABLE.ID, DBInfo.MOBILE_TABLE.ICON, DBInfo.MOBILE_TABLE.NAME, DBInfo.MOBILE_TABLE.PHONE};
        String selection = DBInfo.MOBILE_TABLE.TYPE + " = '1'";
        if(args != null){
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
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item2, data,
                new String[]{DBInfo.MOBILE_TABLE.ICON, DBInfo.MOBILE_TABLE.NAME, DBInfo.MOBILE_TABLE.PHONE},
                new int[]{R.id._icon, R.id.name, R.id.phone}, BIND_ABOVE_CLIENT);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // 重置UI
        listView.setAdapter(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this); // 当Activity重启时，来重新启动Loader
    }
}
