package com.zbiti.myapplication.model;

import android.os.Handler;
import android.util.Log;
import com.zbiti.myapplication.Bean.News;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by admin on 2016/3/29.
 *
 * 模型（Model）：负责处理数据的加载或者存储，比如从网络或本地数据库获取数据等；
 */
public class NewsModel implements INewsModel {
    private static final ExecutorService mExecutor = Executors.newFixedThreadPool(3);
    private Handler handler = new Handler();

    @Override
    public void loadNews(String url, final int pageIndex, final OnLoadNewsListListener listener) {
        Log.d("TAG", "model excute load...");
        mExecutor.execute(new Runnable() {
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
        });
    }

    @Override
    public void cancelTasks() {
        // TODO 终止线程池ThreadPool.shutDown()，AsyncTask.cancle()，或者调用框架的取消任务api
        mExecutor.shutdown();
    }

    // 回调接口
    public interface OnLoadNewsListListener {
        void onSuccess(List<News> list);
        void onFailure(String msg, Exception e);
    }
}
