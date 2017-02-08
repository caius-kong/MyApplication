package com.zbiti.myapplication.model;

import android.os.Handler;
import android.util.Log;
import com.zbiti.myapplication.Bean.News;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/3/29.
 *
 * 模型（Model）：负责处理数据的加载或者存储，比如从网络或本地数据库获取数据等；
 */
public class NewsModel implements INewsModel {
    private Handler handler = new Handler();

    @Override
    public void loadNews(String url, final int pageIndex, final OnLoadNewsListListener listener) {
        Log.d("TAG", "model excute load...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 模拟数据
                final List<News> list = new ArrayList<>();
                int n = (pageIndex-1)*10;
                for(int i=n; i<n+10; i++){
                    News news = new News(i+"", "title" + i, "iconUrl", "1分钟前");
                    list.add(news);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSuccess(list); // 此处是UI更新，必须在UI线程中处理
                    }
                }, 2000);
            }
        }).start();
    }

    // 回调接口
    public interface OnLoadNewsListListener {
        void onSuccess(List<News> list);
        void onFailure(String msg, Exception e);
    }
}
