package com.zbiti.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.zbiti.myapplication.Bean.News;
import com.zbiti.myapplication.utils.MyApplication;
import com.zbiti.myapplication.R;
import com.zbiti.myapplication.utils.BitmapCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/3/29.
 */
public class NewsAdapter extends BaseAdapter{
    private Context context;
    private List<News> list = new ArrayList<News>();

    public NewsAdapter(Context context){
        this.context = context;
        list = new ArrayList<>();
    }

    public void setData(List<News> list){
        this.list = list;
    }

    public void addItems(List<News> newsList){
        if(newsList != null){
            list.addAll(newsList);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final News news = (News)getItem(position);
        ViewHolder viewHolder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.news_item, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView)convertView.findViewById(R.id.title);
            viewHolder.createAt = (TextView)convertView.findViewById(R.id.createAt);
            viewHolder.icon = (ImageView)convertView.findViewById(R.id.icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        ImageLoader imageLoader = new ImageLoader(MyApplication.getHttpQueue(), new BitmapCache());
        String url = "http://ww1.sinaimg.cn/crop.0.0.800.800.1024/735510dbjw8eoo1nn6h22j20m80m8t9t.jpg";
        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(viewHolder.icon, R.drawable.ic_action_picture, R.drawable.ic_action_picture);
        imageLoader.get(url, imageListener, 100, 100);

        viewHolder.title.setText(news.getTitle());
        viewHolder.createAt.setText(news.getCreateAt());
        return convertView;
    }

    public static class ViewHolder{
        ImageView icon;
        TextView title;
        TextView createAt;
    }
}
