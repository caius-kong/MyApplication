package com.zbiti.myapplication.presenter;

import android.util.Log;

import com.zbiti.myapplication.Bean.News;
import com.zbiti.myapplication.model.INewsModel;
import com.zbiti.myapplication.model.NewsModel;
import com.zbiti.myapplication.view.INewsView;
import com.zbiti.myapplication.view.NewsView;
import java.util.List;

/**
 * Created by admin on 2016/3/29.
 *
 * 主持人（Presenter）：相当于协调者，是模型与视图之间的桥梁，将模型与视图分离开来。
 *
 * Presenter中同时持有Viwe层以及Model层的Interface的引用，而View层持有Presenter层Interface的引用。（依赖倒置）
 */
public class NewsPresenter implements INewsPresenter{
    private INewsView newsView;
    private INewsModel newsModel;

    public NewsPresenter(NewsView newsView){
        this.newsView = newsView;
        this.newsModel = new NewsModel();
    }

    @Override
    public void onCreate() {
        // ...
    }

    @Override
    public void onDestroy() {
        // 视图退出时，销毁相关持有
        newsView = null;
        if(newsModel != null){
            newsModel.cancelTasks();
            newsModel = null;
        }
    }

    @Override
    public void loadNews(int pageIndex) {
        String url = "";
        Log.d("TAG", "presenter do load...");
        // NewsModel.OnLoadNewsListListener 以及 newsView.addNews(list) 完成了Model层数据向View传递的过程
        newsModel.loadNews(url, pageIndex, new NewsModel.OnLoadNewsListListener() {
            @Override
            public void onSuccess(List<News> list) {
                Log.d("TAG", "presenter return data: " + list);
                newsView.addNews(list);
            }

            @Override
            public void onFailure(String msg, Exception e) {
                newsView.showLoadFailMsg();
            }
        });
    }
}
