package com.example.rjq.myapplication.fragment;


import android.content.Context;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.view.WindowManager;
import android.widget.ImageView;

import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.rjq.myapplication.R;

import com.example.rjq.myapplication.activity.MainActivity;
import com.example.rjq.myapplication.activity.ResActivity;
import com.example.rjq.myapplication.bean.HomeDataBean;
import com.example.rjq.myapplication.bean.HomeDataBean.HomeRecResDetailBean;

import com.example.rjq.myapplication.util.GlideUtil;
import com.example.rjq.myapplication.util.HttpUtil;
import com.example.rjq.myapplication.util.permission.PermissionFragment;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhy.autolayout.AutoLinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by rjq on 2017/10/28 0028.
 */

public class OneFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "homeFragment";
    private AutoLinearLayout rootView;
    private Context mContext;

    //头部及头部控件
    private View recycleHeadView;
    private Banner mBanner;
    private AutoLinearLayout headFood;
    private AutoLinearLayout headOne;
    private AutoLinearLayout headTwo;
    private AutoLinearLayout headThree;
    private AutoLinearLayout headSweet;
    private AutoLinearLayout headDeliver;
    private AutoLinearLayout headSimple;
    private AutoLinearLayout headPrefer;
    private AutoLinearLayout headFruit;
    private AutoLinearLayout headCook;
    private ImageView headOneIv;
    private ImageView headTwoIv;
    private ImageView headThreeIv;
    private ImageView headFourIv;
    private ImageView headFiveIv;

    @BindView(R.id.one_fragment_rv)
    RecyclerView homeRecyclerView;
    @BindView(R.id.one_fragment_sml)
    SmartRefreshLayout smartRefreshLayout;

    private BaseQuickAdapter adapter;
    //Glide加载图片参数
    RequestOptions requestOptions;

    public static final String RES_DETAIL = "res_detail";
    public static final String SPECIAL_NUM = "special_num";

    //首页数据
    private HomeDataBean homeDataBean;
    private List<HomeDataBean.HomeRecResDetailBean> homeRecResDetailList;
    private List<HomeDataBean.HomeFiveBean> homeFiveBeanList;
    private List<HomeDataBean.HomeBannerBean> homeBannerBeanList;
    private List<String> homeBannerImgUrl;

    //假数据
    private List<String> imageUrl;
    private List<Integer> imageLocal;
    private List<String> fiveImgList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView one");
        if (rootView == null){
            rootView = (AutoLinearLayout) inflater.inflate(R.layout.one_fragment,container,false);
            mContext = getActivity();
            initData();
            initView();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.common_status_bar_color));
        }
        return rootView;
    }

    private void initData(){
        ButterKnife.bind(this,rootView);
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.no_banner)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) //硬盘缓存,内存缓存是自动开启的
                .dontAnimate();

//        homeDataBean = ((MainActivity)mContext).getHomeData();
//        homeRecResDetailList = homeDataBean.getHomeRecResDetailList();
//        homeFiveBeanList = homeDataBean.getHomeFiveImg();
//        homeBannerBeanList = homeDataBean.getBannerList();
//        for (HomeBannerBean homeBannerBean : homeBannerBeanList){
//            homeBannerImgUrl.add(homeBannerBean.getBannerImgUrl());
//        }

        //假数据
        homeDataBean = new HomeDataBean();
        homeRecResDetailList = new ArrayList<>();
        HomeDataBean.HomeRecResDetailBean homeRecResDetailBean2 = new HomeDataBean.HomeRecResDetailBean(2,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg",
                "稻香村",4.8f,1212,35,3,"东院食堂",30,"","","新用户下单立减20","");
        HomeDataBean.HomeRecResDetailBean homeRecResDetailBean1 = new HomeDataBean.HomeRecResDetailBean(1,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg",
                "杨国福麻辣烫",4.9f,2343,23,5,"公寓食堂三楼",20,"满25减5,满35减11","","","");
        HomeDataBean.HomeRecResDetailBean homeRecResDetailBean3 = new HomeDataBean.HomeRecResDetailBean(3,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg",
                "稻香村",4f,222,34,3,"东院食堂",30,"","暖心三人餐A","","");
        HomeDataBean.HomeRecResDetailBean homeRecResDetailBean4 = new HomeDataBean.HomeRecResDetailBean(4,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg",
                "稻香村",4.9f,23234,29,6,"东院食堂",45,"","","新用户下单立减20","满20赠送鸡蛋汤一份");
        HomeDataBean.HomeRecResDetailBean homeRecResDetailBean5 = new HomeDataBean.HomeRecResDetailBean(5,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg",
                "杨国福麻辣烫",4.7f,3212,20,6,"东院食堂",27,"","","新用户下单立减20","");
        HomeDataBean.HomeRecResDetailBean homeRecResDetailBean6 = new HomeDataBean.HomeRecResDetailBean(6,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg",
                "稻香村",4.7f,3212,20,6,"东院食堂",43,"","","新用户下单立减20","");
        HomeRecResDetailBean homeRecResDetailBean7 = new HomeRecResDetailBean(7,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg",
                "杨国福麻辣烫",4.7f,3212,20,6,"东院食堂",12,"","","新用户下单立减20","");
        HomeRecResDetailBean homeRecResDetailBean8 = new HomeRecResDetailBean(8,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg",
                "稻香村",4.7f,3212,20,6,"东院食堂",67,"","满25减5，满30减7","新用户下单立减20","满17赠奶茶一杯");
        homeRecResDetailList.add(homeRecResDetailBean1);homeRecResDetailList.add(homeRecResDetailBean2);homeRecResDetailList.add(homeRecResDetailBean3);
        homeRecResDetailList.add(homeRecResDetailBean4);homeRecResDetailList.add(homeRecResDetailBean5);homeRecResDetailList.add(homeRecResDetailBean6);
        homeRecResDetailList.add(homeRecResDetailBean7);homeRecResDetailList.add(homeRecResDetailBean8);

        fiveImgList = new ArrayList<>();
//        fiveImgList.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg");
//        fiveImgList.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
//        fiveImgList.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
//        fiveImgList.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");
//        fiveImgList.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg");

        //banner图片地址
        imageUrl = new ArrayList<>();
        imageUrl.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg");
        imageUrl.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg");
        imageUrl.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg");
        imageUrl.add("http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg");

        //banner本地图片
        imageLocal = new ArrayList<>();
        imageLocal.add(R.mipmap.b1);
        imageLocal.add(R.mipmap.b2);
        imageLocal.add(R.mipmap.b3);
        imageLocal.add(R.mipmap.b2);
    }

    private void initView(){
        recycleHeadView = LayoutInflater.from(getActivity()).inflate(R.layout.one_fragemnt_head_item,null);
        mBanner = (Banner) recycleHeadView.findViewById(R.id.banner);
        headFood = (AutoLinearLayout) recycleHeadView.findViewById(R.id.head_icon_food);
        headOne = (AutoLinearLayout) recycleHeadView.findViewById(R.id.head_icon_one);
        headTwo = (AutoLinearLayout) recycleHeadView.findViewById(R.id.head_icon_two);
        headThree = (AutoLinearLayout) recycleHeadView.findViewById(R.id.head_icon_three);
        headSweet = (AutoLinearLayout) recycleHeadView.findViewById(R.id.head_icon_sweet);
        headDeliver = (AutoLinearLayout) recycleHeadView.findViewById(R.id.head_icon_deliver);
        headSimple = (AutoLinearLayout) recycleHeadView.findViewById(R.id.head_icon_simple);
        headPrefer = (AutoLinearLayout) recycleHeadView.findViewById(R.id.head_icon_prefer);
        headFruit = (AutoLinearLayout) recycleHeadView.findViewById(R.id.head_icon_fruit);
        headCook = (AutoLinearLayout) recycleHeadView.findViewById(R.id.head_icon_cook);
        headOneIv = (ImageView) recycleHeadView.findViewById(R.id.head_one_iv);
        headTwoIv = (ImageView) recycleHeadView.findViewById(R.id.head_two_iv);
        headThreeIv = (ImageView) recycleHeadView.findViewById(R.id.head_three_iv);
        headFourIv = (ImageView) recycleHeadView.findViewById(R.id.head_four_iv);
        headFiveIv = (ImageView) recycleHeadView.findViewById(R.id.head_five_iv);

        initRecyclerView();
        initRefresh();
    }

    private void initRecyclerView(){
        initBanner();
        initFiveImg();
        initClassify();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        homeRecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new BaseQuickAdapter<HomeRecResDetailBean,BaseViewHolder>(R.layout.one_fragment_content_item,homeRecResDetailList) {
            @Override
            protected void convert(BaseViewHolder helper, HomeRecResDetailBean item) {
                helper.setGone(R.id.one_fragment_item_reduce_container,false);
                helper.setGone(R.id.one_fragment_item_special_container,false);
                helper.setGone(R.id.one_fragment_item_new_container,false);
                helper.setGone(R.id.one_fragment_item_give_container,false);

                ImageView iv = helper.getView(R.id.one_content_item_iv);
                GlideUtil.load(mContext,item.getResImg(),iv,requestOptions);
                helper.setText(R.id.one_fragment_content_item_name,item.getResName());
                RatingBar ratingBar = helper.getView(R.id.one_fragment_star);
                ratingBar.setRating(item.getResStar());
                helper.setText(R.id.one_fragment_score,item.getResStar()+"");
                //月售订单
                String orderNum = mContext.getResources().getString(R.string.res_month_sell_order);
                orderNum = String.format(orderNum,item.getResOrderNum());
                helper.setText(R.id.one_fragment_order_num,orderNum);

                //起送
                String deliverMoney = mContext.getResources().getString(R.string.res_deliver_money);
                deliverMoney = String.format(deliverMoney,item.getResDeliverMoney());
                helper.setText(R.id.one_fragment_deliver,deliverMoney);

                //配送费
                String extraMoney = mContext.getResources().getString(R.string.res_extra_money);
                extraMoney = String.format(extraMoney,item.getResExtraMoney());
                helper.setText(R.id.one_fragment_extra,extraMoney);

                helper.setText(R.id.one_fragment_address,item.getResAddress());
                //配送时间
                String deliverTime = mContext.getResources().getString(R.string.res_deliver_time);
                deliverTime = String.format(deliverTime,item.getResDeliverTime());
                helper.setText(R.id.one_fragment_deliver_time,deliverTime);
                if (!TextUtils.isEmpty(item.getResReduce())){
                    helper.setVisible(R.id.one_fragment_item_reduce_container,true);
                    helper.setText(R.id.one_fragment_item_reduce,item.getResReduce());
                }
                if (!TextUtils.isEmpty(item.getResSpecial())){
                    helper.setVisible(R.id.one_fragment_item_special_container,true);
                    helper.setText(R.id.one_fragment_item_special,item.getResSpecial());
                }
                if (!TextUtils.isEmpty(item.getResNew())){
                    helper.setVisible(R.id.one_fragment_item_new_container,true);
                    helper.setText(R.id.one_fragment_item_new,item.getResNew());
                }
                if (!TextUtils.isEmpty(item.getResGive())){
                    helper.setVisible(R.id.one_fragment_item_give_container,true);
                    helper.setText(R.id.one_fragment_item_give,item.getResGive());
                }

            }

        };
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, ResActivity.class);
                intent.putExtra(RES_DETAIL,homeRecResDetailList.get(position));
                //启动具体店铺页面
                startActivity(intent);
            }
        });
        adapter.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.one_fragment_empty_view,null));
        adapter.addHeaderView(recycleHeadView);
        //默认出现了头部就不会显示Empty，和尾部，配置以下方法也支持同时显示
        adapter.setHeaderFooterEmpty(true,true);
        homeRecyclerView.setAdapter(adapter);

    }

    private void initRefresh(){
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mBanner.stopAutoPlay();
                //在这里进行网络请求，更新数据
//                HttpUtil.sendOkHttpGetRequest(HttpUtil.home_path + "", new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Log.d(TAG,e.toString());
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        String responseText = response.body().string();
//                        Gson gson = new Gson();
//                        homeDataBean = gson.fromJson(responseText,HomeDataBean.class);
//                        homeRecResDetailList = homeDataBean.getHomeRecResDetailList();
//                        homeBannerBeanList = homeDataBean.getBannerList();
//                        for (HomeBannerBean homeBannerBean : homeBannerBeanList){
//                            homeBannerImgUrl.add(homeBannerBean.getBannerImgUrl());
//                        }
//                        //根据得到的新数据更新主线程ui
//                        ((MainActivity)mContext).runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                adapter.notifyDataSetChanged();
//                                mBanner.update(homeBannerImgUrl);
//                                mBanner.startAutoPlay();
//                            }
//                        });
//                    }
//                });

                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mBanner.update(imageUrl);
                        mBanner.startAutoPlay();
                        adapter.notifyDataSetChanged();
                    }
                },1000);
                refreshlayout.finishRefresh(1000);
            }

        });

        smartRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(1000);
            }
        });
    }

    private void initBanner(){
        //设置样式,默认为:Banner.NOT_INDICATOR(不显示指示器和标题)
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置轮播样式（没有标题默认为右边,有标题时默认左边）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片加载器
        mBanner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                GlideUtil.load(context,path,imageView);
            }
        });
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

    private void initClassify(){
        headFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "美食", Toast.LENGTH_SHORT).show();
            }
        });

        headOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "公寓一楼", Toast.LENGTH_SHORT).show();
            }
        });

        headTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "公寓二楼", Toast.LENGTH_SHORT).show();
            }
        });

        headThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "公寓三楼", Toast.LENGTH_SHORT).show();
            }
        });

        headSweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "甜品饮品", Toast.LENGTH_SHORT).show();
            }
        });

        headDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "deliver", Toast.LENGTH_SHORT).show();
            }
        });

        headSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "simple", Toast.LENGTH_SHORT).show();
            }
        });

        headPrefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "prefer", Toast.LENGTH_SHORT).show();
            }
        });

        headFruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "fruit", Toast.LENGTH_SHORT).show();
            }
        });

        headCook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "cook", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initFiveImg(){
        GlideUtil.load(mContext,R.mipmap.five_one,headOneIv,requestOptions);
        GlideUtil.load(mContext,R.mipmap.five_two,headTwoIv,requestOptions);
        GlideUtil.load(mContext,R.mipmap.five_three,headThreeIv,requestOptions);
        GlideUtil.load(mContext,R.mipmap.five_four,headFourIv,requestOptions);
        GlideUtil.load(mContext,R.mipmap.five_five,headFiveIv,requestOptions);
        headOneIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "one", Toast.LENGTH_SHORT).show();
            }
        });
        headTwoIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "two", Toast.LENGTH_SHORT).show();
            }
        });
        headThreeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "three", Toast.LENGTH_SHORT).show();
            }
        });
        headFourIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "four", Toast.LENGTH_SHORT).show();
            }
        });
        headFiveIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "five", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

        }
    }

    @Override
    public void onResume() {
        Log.d(TAG,"onResume one");
        super.onResume();
        if (imageUrl.isEmpty()){
            smartRefreshLayout.setEnableLoadmore(false);
        }
    }

}
