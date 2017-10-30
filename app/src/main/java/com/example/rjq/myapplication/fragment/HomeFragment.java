package com.example.rjq.myapplication.fragment;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.example.rjq.myapplication.Bean.HomeFragmentContentItemListBean;
import com.example.rjq.myapplication.Bean.HomeFragmentHeadItemListBean;
import com.example.rjq.myapplication.adapter.HomeFragmentListAdapter;
import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.activity.SearchActivity;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/7/20 0020.
 */

public class HomeFragment extends Fragment implements View.OnClickListener{
    private Activity activity;
    private View homeFragment;
    private View homeFragmentHeadItemList;
    private Toolbar mToolbar;
    private AutoLinearLayout saoYiSao;

    private RecyclerView outRecyclerView;
    private HomeFragmentListAdapter homeFragmentListAdapter;

    private LinearLayoutManager innerLinearLayoutManager;

    private List<HomeFragmentContentItemListBean> contentData;
    private HomeFragmentHeadItemListBean headData;

    private AutoLinearLayout homeFragmentSearchLayout;
    private OnItemClickListener bannerItemClickListener;
    private HomeFragmentListAdapter.OnHeadItemClickListener onHeadItemClickListener;

    private RefreshLayout refreshLayout;

    private List<String> bannerImgs = new ArrayList<>();
    private List<String> innerListItemTitle;
    private List<String> innerListItemImg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (homeFragment == null) {
            homeFragment = inflater.inflate(R.layout.home_fragment,container,false);
            homeFragmentHeadItemList = inflater.inflate(R.layout.home_fragment_head_item_list,container,false);
            activity = getActivity();
            initData();
            initView();
            initListener();
        }
        ViewGroup group = (ViewGroup) homeFragment.getRootView();
        if (null != group) {
            group.removeView(homeFragment);
        }
        return homeFragment;
    }

    private void refreshHeadData(){
        bannerImgs.set(0,"http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg");
        bannerImgs.set(1,"http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg");
        bannerImgs.set(2,"http://d.3987.com/sqmy_131219/001.jpg");
        bannerImgs.set(3,"http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg");
        bannerImgs.set(4,"http://d.3987.com/sqmy_131219/001.jpg");
        bannerImgs.set(5,"http://d.3987.com/sqmy_131219/001.jpg");
        bannerImgs.set(6,"http://d.3987.com/sqmy_131219/001.jpg");
        headData.setBannerImgs(bannerImgs);
        homeFragmentListAdapter.notifyDataSetChanged();
    }

    private void initView(){
        homeFragmentSearchLayout = (AutoLinearLayout)homeFragment.findViewById(R.id.home_fragment_search_ll);
        mToolbar = (Toolbar) homeFragment.findViewById(R.id.toolBar);
        saoYiSao = (AutoLinearLayout) homeFragment.findViewById(R.id.sao_yi_sao);

        refreshLayout = (RefreshLayout) homeFragment.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshHeadData();
                refreshlayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
            }
        });

        bannerItemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(activity, "点击了"+position, Toast.LENGTH_SHORT).show();
            }
        };

        onHeadItemClickListener = new HomeFragmentListAdapter.OnHeadItemClickListener() {
            @Override
            public void onHeadItemClick(View view) {
                Toast.makeText(activity, view.getId()+"", Toast.LENGTH_SHORT).show();
            }
        };

        outRecyclerView = (RecyclerView) homeFragment.findViewById(R.id.home_fragment_out_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
        outRecyclerView.setLayoutManager(linearLayoutManager);
        homeFragmentListAdapter = new HomeFragmentListAdapter(headData,innerLinearLayoutManager,contentData,bannerItemClickListener,onHeadItemClickListener);
        outRecyclerView.setAdapter(homeFragmentListAdapter);

    }

    private void initData(){
         String[] images = {"http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg",
                "http://img2.3lian.com/2014/f2/37/d/40.jpg",
                "http://d.3987.com/sqmy_131219/001.jpg",
                "http://img2.3lian.com/2014/f2/37/d/39.jpg",
                "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
                "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
                "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
        };
        String[] images1 = {"http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg",
                "http://img2.3lian.com/2014/f2/37/d/40.jpg",
                "http://d.3987.com/sqmy_131219/001.jpg",
                "http://img2.3lian.com/2014/f2/37/d/39.jpg",
                "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
                "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg"
        };
        //图片加载地址的集合
        for (String t : images){
            bannerImgs.add(t);
        }
        innerListItemTitle = new ArrayList<>();
        for (int i=0;i<images1.length;i++){
            innerListItemTitle.add("食肉系列"+i);
        }
        innerListItemImg = Arrays.asList(images1);
        headData = new HomeFragmentHeadItemListBean();
        headData.setBannerImgs(bannerImgs);
        headData.setInnerListItemImg(innerListItemImg);
        headData.setInnerListItemTitle(innerListItemTitle);

        contentData = new ArrayList<>();

        for (int i=0;i<images1.length;i++){
            HomeFragmentContentItemListBean homeFragmentContentItemListBean= new HomeFragmentContentItemListBean("食肉系列","http://img2.3lian.com/2014/f2/37/d/40.jpg");
            contentData.add(homeFragmentContentItemListBean);
        }

        innerLinearLayoutManager = new LinearLayoutManager(activity);
        innerLinearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
    }

    private void initListener(){
        homeFragmentSearchLayout.setOnClickListener(this);
        saoYiSao.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_fragment_search_ll:
                Intent intent = new Intent(activity,SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.sao_yi_sao:
                break;

        }
    }

}
