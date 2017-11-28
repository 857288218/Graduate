package com.example.rjq.myapplication.fragment;

import android.content.Context;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.adapter.OneFragmentAdapter;
import com.example.rjq.myapplication.util.GlideImageLoader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rjq on 2017/10/28 0028.
 */

public class OneFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "life";
    private View rootView;
    private View headItemView;
    private Banner mBanner;
    @BindView(R.id.one_fragment_rv)
    RecyclerView oneRecyclerView;
    @BindView(R.id.one_fragment_sml)
    SmartRefreshLayout smartRefreshLayout;

    private List<String> imageUrl;
    private List<Integer> imageLocal;
    private List<String> bannerTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.one_fragment,container,false);
            initData();
            initView();
        }
        Log.d(TAG,"onCreateView one");
        return rootView;
    }

    private void initView(){
        headItemView = LayoutInflater.from(getActivity()).inflate(R.layout.one_fragemnt_head_item,null);
        mBanner = (Banner) headItemView.findViewById(R.id.banner);
        initRecyclerView();
        initRefresh();
    }

    private void initData(){
        ButterKnife.bind(this,rootView);
        //图片地址
        imageUrl = new ArrayList<>();
        imageUrl.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg");
        imageUrl.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
        imageUrl.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
        imageUrl.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
        imageUrl.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
        imageUrl.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
        imageUrl.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");

        //本地图片
        imageLocal = new ArrayList<>();
        imageLocal.add(R.drawable.b1);
        imageLocal.add(R.drawable.b2);
        imageLocal.add(R.drawable.b3);
        imageLocal.add(R.drawable.b2);

        //Title名称
        bannerTitle = new ArrayList<>();
        bannerTitle.add("一");
        bannerTitle.add("二");
        bannerTitle.add("三");
        bannerTitle.add("四");
    }

    private void initRecyclerView(){
        initBanner();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        oneRecyclerView.setLayoutManager(linearLayoutManager);
        final BaseQuickAdapter adapter = new BaseQuickAdapter<String,BaseViewHolder>(R.layout.one_fragment_content_item,imageUrl) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.addOnClickListener(R.id.one_content_item_iv)
                        .addOnClickListener(R.id.one_content_item_tv);
            }

        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getActivity(), ""+position, Toast.LENGTH_SHORT).show();
            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch(view.getId()){
                    case R.id.one_content_item_iv:
                        TextView tv = (TextView) adapter.getViewByPosition(oneRecyclerView,position+1,R.id.one_content_item_tv);
                        tv.setText("我是ItemChildClick");
                        break;
                    case R.id.one_content_item_tv:
                        ImageView iv = (ImageView) adapter.getViewByPosition(oneRecyclerView,position+1,R.id.one_content_item_iv);
                        Glide.with(getActivity()).load(R.drawable.b2).into(iv);
                        break;
                }
            }
        });
        adapter.addHeaderView(headItemView);
        adapter.setHeaderFooterEmpty(true,true);
        oneRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

        }
    }

    private void initRefresh(){
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
            }
        });

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
            }
        });
    }

    private void initBanner(){
        //设置样式,默认为:Banner.NOT_INDICATOR(不显示指示器和标题)
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置轮播样式（没有标题默认为右边,有标题时默认左边）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(imageLocal);
        mBanner.setBannerAnimation(Transformer.Default);
        //设置点击事件，下标是从0开始
        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Toast.makeText(getActivity(), "您点击了："+position, Toast.LENGTH_SHORT).show();
            }
        });
        mBanner.start();
    }

    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
        Log.d(TAG,"onStart one");
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
        Log.d(TAG,"onStop one");
    }

}
