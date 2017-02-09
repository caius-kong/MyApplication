package com.zbiti.myapplication.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.mingle.widget.LoadingView;
import com.zbiti.myapplication.R;
import com.zbiti.myapplication.adapter.RecyclerViewAdapter;
import com.zbiti.myapplication.framework.eventbus.EventEntity;
import com.zbiti.myapplication.framework.eventbus.EventType;
import com.zbiti.myapplication.widget.CircleRefreshLayout;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by admin on 2016/6/20.
 */
public abstract class BaseListFragment extends Fragment implements CircleRefreshLayout.OnCircleRefreshListener{
    @Bind(R.id.circle_refresh_layout)
    CircleRefreshLayout circleRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ll_status)
    FrameLayout llStatus;
    @Bind(R.id.loadingView)
    LoadingView loadingView;
    @Bind(R.id.tv_status)
    TextView tvStatus;

    private int lastVisibleItem;
    private LinearLayoutManager mLinearLayoutManager;

    protected List<String> data = new ArrayList<String>();
    protected RecyclerViewAdapter mAdapter;
    public static final int PAGE_SIZE_LIMIT = 10;

    private Handler handler = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        EventBus.getDefault().register(this); // 注册EventBus
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_base_list, container, false);
        ButterKnife.bind(this, rootView);

        initView();
        initListener();
        startRefresh(); // 页面首次加载，默认刷新！
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // 从数据库中加载缓存文章 （用户首先看到的是缓存文章，当网络请求返回数据，清空文章列表，加载网络文章）
        // ...
    }

    private void initData(){

    }

    private void initView(){
        loadingView.setVisibility(View.VISIBLE);

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        mAdapter = new RecyclerViewAdapter(getActivity(), data);
        recyclerView.setAdapter(mAdapter);
    }

    // 注意：如果你使用的是虚拟机，必须使用鼠标点击才能触发监听，滚轮滚动无效！
    private void initListener(){
        // 监听滑动事件 - 上拉加载数据
        recyclerView.addOnScrollListener(new PauseOnScrollListener());
        // 监听刷新事件 - 下拉刷新数据
        circleRefreshLayout.setOnRefreshListener(this);
    }

    class PauseOnScrollListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE:
                    // 滑动停止时，判断一定条件 -> 加载...
                    int size = recyclerView.getAdapter().getItemCount();
                    if (lastVisibleItem + 1 == size && mAdapter.isLoadMoreShown() &&
                            !mAdapter.getLoadMoreViewText().equals(getString(R.string.load_data_adequate))) {
                        onScrollLast();
                    }
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING:
                    break;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this); // 注销EventBus
    }

    /**
     * 重写onEventMainThread，接收消息(EventBus)
     *
     * @param event
     */
    public void onEventMainThread(EventEntity event) {
        switch (event.getType()) {
            case EventType.UPDATE_FRAGMENT_LIST:
                System.out.println("--->update fragment1 list...");
                onRefreshStart();
                break;
        }
    }

    /**
     * CircleRefreshLayout.OnCircleRefreshListener的实现方法
     * 下拉刷新
     */
    @Override
    public void startRefresh(){
        // 让子弹飞一会儿，防止刷新太快
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onRefreshStart();
            }
        },500);
    }

    // 下拉刷新完成后（网络请求响应后调用）
    private void completeRefresh() {
        if (circleRefreshLayout != null) {
            circleRefreshLayout.completeRefresh();
        }
        if (loadingView != null && loadingView.getVisibility() == View.VISIBLE) {
            loadingView.setVisibility(View.GONE);
        }
    }

    /**
     *  ============ 处理result data —— UI更新、数据更新 ==================
     */
    public void refreshDataOnUi(List<String> list){
        // UI更新
        completeRefresh();
        if (list.size() == 0) {
            updateUi4DataEmpty();
        } else if (list.size() == PAGE_SIZE_LIMIT) {
            updateUi4DataAdequate();
        } else {
            updateUi4DataInadequate();
        }
        // 数据更新
        mAdapter.clear();
        mAdapter.addItems(list);
    }

    public void loadDataOnUi(List<String> list){
        if (list.size() == 0) {
            updateUi4MoreDataEmpty();
        } else if (list.size() == PAGE_SIZE_LIMIT) {
            updateUi4MoreDataAdequate();
        } else {
            updateUi4MoreDataInadequate();
        }
        mAdapter.addItems(list);
    }

    // 数据为空
    private void updateUi4DataEmpty(){
        llStatus.setVisibility(View.VISIBLE);
        mAdapter.setLoadMoreViewVisibility(View.GONE);
        tvStatus.setVisibility(View.VISIBLE);
        tvStatus.setText(getString(emptyDataString()));
    }

    // 数据足够PAGE_SIZE
    private void updateUi4DataAdequate(){
        llStatus.setVisibility(View.GONE);
        mAdapter.setLoadMoreViewVisibility(View.VISIBLE);
        mAdapter.setLoadMoreViewText(getString(R.string.loading_data));
    }

    //数据不足PAGE_SIZE
    private void updateUi4DataInadequate(){
        llStatus.setVisibility(View.GONE);
        mAdapter.setLoadMoreViewVisibility(View.GONE);
    }

    //加载失败
    private void updateUi4DataFail() {
        llStatus.setVisibility(View.VISIBLE);
        mAdapter.setLoadMoreViewVisibility(View.GONE);
        tvStatus.setVisibility(View.VISIBLE);
        tvStatus.setText(R.string.load_data_fail);
    }

    private void updateUi4MoreDataEmpty(){
        mAdapter.setLoadMoreViewVisibility(View.GONE);
    }

    private void updateUi4MoreDataAdequate(){
        mAdapter.setLoadMoreViewVisibility(View.VISIBLE);
    }

    private void updateUi4MoreDataInadequate(){
        mAdapter.setLoadMoreViewVisibility(View.VISIBLE);
        mAdapter.setLoadMoreViewText(getString(R.string.load_data_adequate));
    }

    private void updateUi4MoreDataFail() {
        mAdapter.setLoadMoreViewVisibility(View.VISIBLE);
        mAdapter.setLoadMoreViewText(getString(R.string.load_data_fail));
    }

    /**
     *  ============== 请求列表数据的接口回调 =================
     */
    protected abstract void onRefreshStart(); //下拉刷新数据 (备注：作用于CircleRefreshLayout)
    protected abstract void onScrollLast(); //上拉加载数据   (备注：作用于RecyclerView)
    protected abstract int emptyDataString(); //数据为空时的显示文字
}
