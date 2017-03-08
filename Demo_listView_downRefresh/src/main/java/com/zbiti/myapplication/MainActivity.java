package com.zbiti.myapplication;

import android.app.ListActivity;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ListActivity implements AbsListView.OnScrollListener {
    private ListView listView;
    ArrayAdapter<String> adapter;
    public List<String> data = new ArrayList<>();
    private int visibleLastIndex = 0;   //最后的可视项索引
    private int visibleItemCount;       // 当前窗口可见项总数
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View view = View.inflate(this, R.layout.load_more, null);
        listView = getListView();
        listView.addFooterView(view);  // 添加尾部视图

        loadData(0);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        setListAdapter(adapter); // 设置适配器

        listView.setOnScrollListener(this);   //添加滑动监听
    }

    private void loadData(int count) {
        for (int i = count; i < 15 + count; i++)
            data.add("" + i);
    }

    /**
     * 滑动时被调用
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.visibleItemCount = visibleItemCount;
        visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
    }

    /**
     * 滑动状态改变时被调用
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // int itemsLastIndex = adapter.getCount() - 1; // 数据集最后一项的索引
        int footorIndex = adapter.getCount(); // 加载底部的loadMoreView项
        // 当滑动状态为停止，且'可见的最后一个item'是footer时
        if (scrollState == SCROLL_STATE_IDLE && visibleLastIndex == footorIndex){
            //如果是自动加载,可以在这里放置异步加载数据的代码
            System.out.println("--->loading...");
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData(adapter.getCount());
                    adapter.notifyDataSetChanged(); //数据集变化后,通知adapter
                    listView.setSelection(visibleLastIndex - visibleItemCount + 1 + 1); //设置选中项（position位置的item到listview的最顶端）
                }
            }, 2000);
        }
    }
}
