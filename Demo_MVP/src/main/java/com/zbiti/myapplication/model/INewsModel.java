package com.zbiti.myapplication.model;

/**
 * Created by admin on 2016/3/29.
 */
public interface INewsModel {
    void loadNews(String url, int pageIndex, NewsModel.OnLoadNewsListListener listener);
}
