package com.zbiti.myapplication.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zbiti.myapplication.R;
import com.zbiti.myapplication.framework.eventbus.EventEntity;
import com.zbiti.myapplication.framework.eventbus.EventType;
import com.zbiti.myapplication.sharedPreference.ItemSharedPreference;
import com.zbiti.myapplication.widget.GroupImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.devland.esperandro.Esperandro;
import de.greenrobot.event.EventBus;

/**
 * Created by admin on 2016/6/15.
 */
public class Fragment2 extends Fragment {
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.tv_describe)
    TextView tvDescribe;
    @Bind(R.id.giv_image_group)
    GroupImageView givImageGroup;

    private ItemSharedPreference itemSharedPreference;
    private String name;
    private float price;
    private String content;
    private List<String> imageList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_2, container, false);
        ButterKnife.bind(this, rootView);

        initView();
        initListener();
        return rootView;
    }

    private void initData() {
        itemSharedPreference = Esperandro.getPreferences(ItemSharedPreference.class, getActivity());
        name = itemSharedPreference.name();
        price = itemSharedPreference.price();
        content = itemSharedPreference.content();
        imageList = itemSharedPreference.imageList();
        System.out.println("-name-->" + name);
        System.out.println("-price-->" + price);
        System.out.println("-content-->" + content);
        System.out.println("-imageList-->" + imageList);
    }

    private void initView() {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(price + "") && !TextUtils.isEmpty(content)) {
            tvName.setText(name);
            tvPrice.setText(price + "");
            tvContent.setText(content);
        }
        if (imageList != null && imageList.size() > 0) {
            givImageGroup.setVisibility(View.VISIBLE);
            givImageGroup.setPics(imageList);
        } else {
            tvDescribe.setVisibility(View.GONE);
            givImageGroup.setVisibility(View.GONE);
        }
    }

    private void initListener() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(EventEntity event) {
        switch (event.getType()) {
            case EventType.UPDATE_FRAGMENT_LIST2:
                System.out.println("--->update fragment2 list...");
                initData();
                initView();
                break;
        }
    }
}
