package com.example.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 参考：http://blog.csdn.net/javacainiao931121/article/details/51704672
 *
 * RecyclerView优势：布局灵活，代码结构清晰
 * 1、通过设置布局，切换布局方式很简单
 * 2、适配器配置中，重写getItemViewType()，区分viewType，以加载不同view （这就很厉害了，ListView这些，其item肯定是一样的，而RecyclerView的item可以变化）
 */
public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initWidgets();
    }

    private void initWidgets(){
        mRecyclerView = (RecyclerView) findViewById(R.id.mRecycleView);
        // 设置布局
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        // 设置adapter
        mRecyclerView.setAdapter(new RecycleAdapter(loadDataSet()));
    }

    /**
     * 设置RecyclerView的适配器
     */
    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.RecycleViewHolder>{
        private List<Map<String, String>> mDataSet = new ArrayList<Map<String, String>>();
        
        RecycleAdapter(List<Map<String, String>> list){
            this.mDataSet = list;
        }

        @Override
        public int getItemCount() {
            return mDataSet.size();
        }

        @Override
        public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            // 创建ViewHolder(设置itemView的布局)
            return new RecycleViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.itemview, parent, false));
        }

        @Override
        public void onBindViewHolder(RecycleViewHolder holder, int position) {
            // 绑定数据（设置itemView各组件的数据）
            holder.idTv.setText(mDataSet.get(position).get("id"));
            holder.nameTv.setText(mDataSet.get(position).get("name"));
        }

        /**
         * ViewHolder：itemView各组件的持有者，完成initWidgets
         */
        class RecycleViewHolder extends RecyclerView.ViewHolder{
            private TextView idTv;
            private TextView nameTv;

            public RecycleViewHolder(View itemView){
                super(itemView);
                idTv = (TextView) itemView.findViewById(R.id.idTv);
                nameTv = (TextView) itemView.findViewById(R.id.nameTv);
            }
        }
    }
    
    private List<Map<String, String>> loadDataSet(){
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("id", "1");
        map1.put("name", "孔昀晖");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("id", "2");
        map2.put("name", "杨青");
        list.add(map1);
        list.add(map2);
        return list;
    }
}
