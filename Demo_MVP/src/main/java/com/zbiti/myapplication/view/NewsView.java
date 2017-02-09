package com.zbiti.myapplication.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.zbiti.myapplication.Bean.News;
import com.zbiti.myapplication.R;
import com.zbiti.myapplication.adapter.NewsAdapter;
import com.zbiti.myapplication.presenter.INewsPresenter;
import com.zbiti.myapplication.presenter.NewsPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * 视图（View）：负责界面数据的展示，与用户进行交互
 * <p/>
 * MVP模式的整个核心过程：
 * 1、当View层某个界面需要展示某些数据的时候，首先会调用Presenter层的某个接口，
 * 2、然后Presenter层会调用Model层请求数据
 * 3、当Model层数据加载成功之后会调用Presenter层的回调方法通知Presenter层数据加载完毕，
 * 4、最后Presenter层通过回调接口获得数据，再调用View层的接口将加载后的数据展示给用户。
 *
 * 总结：View调用PresenterI的接口方法请求数据，Presenter实现该接口方法并调用ViewI的接口方法返回数据，View实现ViewI的接口方法响应数据。（Presenter与Model的交互同理）
 * 1、其实就是使用了接口回调，只不过他们是双向的
 * 2、Presenter作为一个中介显而易见，他同时拥有ViewI和ModelI
 */
public class NewsView extends AppCompatActivity implements INewsView, AbsListView.OnScrollListener {
    private ListView listView;
    private NewsAdapter adapter;
    private List<News> data;
    private int pageIndex = 1;
    private int visibleLastIndex = 0;
    private int visibleItemCount;

    private INewsPresenter newsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        newsPresenter = new NewsPresenter(this); // Presenter初始化
        newsPresenter.onCreate();   // 将生命周期回调传给Presenter

        initView();
        initEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 视图退出时，销毁相关持有
        newsPresenter.onDestroy();
        newsPresenter = null;
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.news_listview);

        View footer = View.inflate(this, R.layout.load_more, null);
        listView.addFooterView(footer);

        adapter = new NewsAdapter(this);
        listView.setAdapter(adapter);

        onRefresh();
    }

    private void onRefresh() {
        pageIndex = 1;
        if (data != null) {
            data.clear();
        }
        newsPresenter.loadNews(pageIndex); // 默认加载第一页
    }

    private void initEvent() {
        listView.setOnScrollListener(this);
    }

    /**
     * 负责界面数据的显示（如何去渲染页面）
     */
    @Override
    public void addNews(List<News> newsList) {
        adapter.addItems(newsList);
        pageIndex++;
    }

    @Override
    public void showLoadFailMsg() {
        Toast.makeText(NewsView.this, "网络不给力啊！", Toast.LENGTH_LONG).show();
    }

    /**
     * 下拉加载
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        int footorIndex = adapter.getCount();
        if (scrollState == SCROLL_STATE_IDLE && visibleLastIndex == footorIndex) {
            Log.d("TAG", "loading more data...");
            // 将请求Model数据的方法交给Presenter处理
            newsPresenter.loadNews(pageIndex);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.visibleItemCount = visibleItemCount;
        visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
    }
}
