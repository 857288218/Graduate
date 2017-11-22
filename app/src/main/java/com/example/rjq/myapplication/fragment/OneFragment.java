package com.example.rjq.myapplication.fragment;

import android.content.Context;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;


import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.util.GlideImageLoader;
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
    @BindView(R.id.banner)
    Banner mBanner;

    private List<String> imageUrl;
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
        initBanner();
    }

    private void initData(){
        ButterKnife.bind(this,rootView);
        //图片地址
        imageUrl = new ArrayList<>();
        imageUrl.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg");
        imageUrl.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
        imageUrl.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
        imageUrl.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");

        //Title名称
        bannerTitle = new ArrayList<>();
        bannerTitle.add("一");
        bannerTitle.add("二");
        bannerTitle.add("三");
        bannerTitle.add("四");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

        }
    }

    private void initBanner(){
        //设置样式,默认为:Banner.NOT_INDICATOR(不显示指示器和标题)
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置轮播样式（没有标题默认为右边,有标题时默认左边）
        mBanner.setIndicatorGravity(BannerConfig.RIGHT);
        //设置轮播要显示的标题和图片对应（如果不传默认不显示标题）
        mBanner.setBannerTitles(bannerTitle);
        //设置是否自动轮播（不设置则默认自动）
        mBanner.isAutoPlay(true);
        //设置轮播图片间隔时间（不设置默认为2000）
        mBanner.setDelayTime(3000);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(imageUrl);
        //设置banner动画效果(为ViewPager的滑动动画)
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
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG,"onAttach one");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate one");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"onActivityCreated one");
    }

    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
        Log.d(TAG,"onStart one");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume one");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause one");
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
        Log.d(TAG,"onStop one");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroyView one");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy one");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"onDetach one");
    }


}
