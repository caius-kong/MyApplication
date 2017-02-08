package com.zbiti.myapplication.view;

import com.zbiti.myapplication.Bean.News;

import java.util.List;

/**
 * Created by admin on 2016/3/29.
 */
public interface INewsView {
    /**
     * 加载成功后，将加载得到的数据填充到RecyclerView展示给用户
     * @param newsList
     */
    void addNews(List<News> newsList);

    /**
     * 若加载数据失败，如无网络连接，则需要给用户提示信息
     */
    void showLoadFailMsg();
}
