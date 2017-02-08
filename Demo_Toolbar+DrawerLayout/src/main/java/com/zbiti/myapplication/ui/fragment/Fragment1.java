package com.zbiti.myapplication.ui.fragment;

import com.zbiti.myapplication.R;
import com.zbiti.myapplication.framework.http.MyHttpMethods;

import java.util.Arrays;
import java.util.List;

import rx.Subscriber;

/**
 * Created by admin on 2016/6/15.
 */
public class Fragment1 extends BaseListFragment {
    private int mPageIndex = 1; // 数据的页面索引，用于分页加载
    private Subscriber subscriber;

    @Override
    protected void onRefreshStart() {
        System.out.println("--->下拉刷新数据");
        // 模拟数据
        List<String> list = Arrays.asList(dataAdequate());
        refreshDataOnUi(list);

        // Retrofit + RxJava: 将 http处理、线程问题 交给框架完成，开发者仅关注业务逻辑 (操作分离，Fragment仅关注UI相关的逻辑)
//        subscriber = new Subscriber<List<User>>() {
//            @Override
//            public void onCompleted() {
//            }
//
//            @Override
//            public void onError(Throwable e) {
//                Toast.makeText(getActivity(), "-error-->:" + e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onNext(List<User> users) {
//                List<String> list = new ArrayList<String>();
//                if(users!=null && users.size()>0){
//                    for(User user : users){
//                        list.add(user.getName());
//                    }
//                }
//                refreshDataOnUi(list);
//            }
//        };
//        MyHttpMethods.getInstance().findUsers(subscriber, "kongyunhui");
    }

    @Override
    protected void onScrollLast() {
        System.out.println("--->上拉加载更多数据");
        // 模拟数据
//        List<String> list = getDataFromDb(mPageIndex);
        List<String> list = Arrays.asList(dataInadequate());
        loadDataOnUi(list);
        // 存储文章列表到数据库缓存
        // ...
        // 页面索引+1
        // if(list.size()>0){
        //    mPageIndex++;
        // }
    }

    @Override
    protected int emptyDataString() {
        return R.string.no_data;
    }


    /**
     * ================== 假数据 ======================
     */
    private String[] dataAdequate() {
        return new String[]{"k", "o", "n", "g", "y", "u", "n", "h", "u", "i"};
    }

    private String[] dataInadequate() {
        return new String[]{"y", "u", "n", "h", "u", "i"};
    }
}
