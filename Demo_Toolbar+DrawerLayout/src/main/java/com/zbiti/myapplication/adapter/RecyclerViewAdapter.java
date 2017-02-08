package com.zbiti.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zbiti.myapplication.R;
import com.zbiti.myapplication.control.NavigateManager;
import com.zbiti.myapplication.util.NetWorkUtil;

import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by admin on 2016/6/20.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private List<String> mList = new ArrayList<String>();

    private static final int TYPE_LIST = 0;
    private static final int TYPE_FOOT_VIEW = 1;

    private View footView;

    public RecyclerViewAdapter(Context context){
        this.mContext = context;
    }

    public RecyclerViewAdapter(Context context, List<String> list){
        this(context);
        this.mList = list;
    }

    /**
     *  =============  RecyclerView.Adapter重写的方法  ==============
     */
    @Override
    public int getItemCount() {
        return mList.size()+1;
    }

    // 重写该方法，区分viewType，以加载不同view
    @Override
    public int getItemViewType(int position) {
        if(position + 1 == getItemCount()){
            return TYPE_FOOT_VIEW;
        } else {
            return TYPE_LIST;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        switch (viewType){
            case TYPE_LIST:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item1, parent, false);
                viewHolder = new ListViewHolder(view);
                break;
            case TYPE_FOOT_VIEW:
                footView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_footview_layout, parent, false);
                footView.setVisibility(View.GONE);
                viewHolder = new FootViewHolder(footView);
                break;
            default:
                viewHolder = new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item1, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ListViewHolder) {
            final ListViewHolder listViewHolder = (ListViewHolder) holder;
            listViewHolder.tvItem.setText(mList.get(position));

            listViewHolder.llRootView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    String url = "http://fir.im/docs";
                    if (NetWorkUtil.isLinkAvailable(url)) {
                        NavigateManager.gotoItemDetailActivity(mContext, url);
                    }
                }
            });
        }
    }

    public void addItems(List<String> items){
        mList.clear();
        if(items != null && items.size() > 0){
            mList.addAll(items);
        }
        notifyDataSetChanged();
    }

    /**
     *  ====================  公共方法(处理footView的显示与文字)  =============================
     */
    public void setLoadMoreViewVisibility(int visibility) {
        if (footView != null) footView.setVisibility(visibility);
        notifyItemChanged(getItemCount());
    }

    public boolean isLoadMoreShown(){
        if (footView == null) return false;
        return footView.isShown();
    }

    public String getLoadMoreViewText(){
        if (footView == null) return "";
        return ((TextView)ButterKnife.findById(footView, R.id.tv_loading_more)).getText().toString();
    }

    public void setLoadMoreViewText(String text){
        if (footView != null) ((TextView)ButterKnife.findById(footView, R.id.tv_loading_more)).setText(text);
        // 注意：adapter内部更新 --> "通知"
        notifyItemChanged(getItemCount());
    }


    /**
     *  ================== RecyclerView.ViewHolder ====================
     */
    static class ListViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.ll_rootView)
        LinearLayout llRootView;
        @Bind(R.id.tv_item)
        TextView tvItem;

        public ListViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.tv_loading_more)
        TextView tvLoadingMore;

        public FootViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
