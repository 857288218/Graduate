package com.example.rjq.myapplication.fragment;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
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
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.activity.AddressActivity;
import com.example.rjq.myapplication.activity.ClassifyResActivity;
import com.example.rjq.myapplication.activity.MainActivity;
import com.example.rjq.myapplication.activity.ResActivity;
import com.example.rjq.myapplication.activity.SearchActivity;
import com.example.rjq.myapplication.bean.AddressBean;

import com.example.rjq.myapplication.bean.DiscountBean;
import com.example.rjq.myapplication.bean.ResBuyCategoryNum;
import com.example.rjq.myapplication.bean.ResDetailBean;
import com.example.rjq.myapplication.bean.UserBean;
import com.example.rjq.myapplication.util.GlideUtil;
import com.example.rjq.myapplication.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import com.zhy.autolayout.AutoRelativeLayout;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static com.example.rjq.myapplication.activity.AddressActivity.SELECTED_ADDRESS;

/**
 * Created by rjq on 2017/10/28 0028.
 */

public class OneFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "homeFragment";
    public static final int ADDRESS_REQUEST = 1010;
    public static final String DELICIOUS = "delicious";
    public static final String ONE_FLOUR = "one_flour";
    public static final String TWO_FLOUR = "two_flour";
    public static final String THREE_FLOUR = "three_flour";
    public static final String SWEET = "sweet";
    public static final String DELIVER = "deliver";
    public static final String SIMPLE = "simple";
    public static final String FAVOUR = "favour";
    public static final String FRUIT = "fruit";
    public static final String COOK = "cook";
    public static final String RES_TITLE = "res_title";

    private AutoRelativeLayout rootView;
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
    @BindView(R.id.home_search)
    AutoLinearLayout searchLl;
    @BindView(R.id.home_address)
    TextView address;
    @BindView(R.id.first_load)
    ProgressBar progressBar;

    private BaseQuickAdapter adapter;
    public static final String RES_DETAIL = "res_detail";
    public static final String SPECIAL_NUM = "special_num";

    //首页数据
    private ResDetailBean homeDataBean;
    private List<ResDetailBean> homeRecResDetailList;
    private List<String> homeBannerImgUrl;
    private int userId;
    String discountString;

    //假数据
    private List<String> imageUrl;
    private List<Integer> imageLocal;
    private List<String> fiveImgList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null){
            rootView = (AutoRelativeLayout) inflater.inflate(R.layout.one_fragment,container,false);
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
        //网络请求数据
//        progressBar.setVisibility(View.VISIBLE);
//        homeRecyclerView.setVisibility(View.GONE);
//        HttpUtil.sendOkHttpGetRequest(HttpUtil.HOME_PATH+HttpUtil.HOME_DATA_API, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("oneFragment",e.getMessage());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String responseText = response.body().string();
//                Log.d("oneFragment",responseText.toString());
//                homeRecResDetailList = new Gson().fromJson(responseText,new TypeToken<List<ResDetailBean>>(){}.getType());
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        progressBar.setVisibility(View.GONE);
//                        homeRecyclerView.setVisibility(View.VISIBLE);
//                    }
//                });
//            }
//        });

        //假数据
        homeDataBean = new ResDetailBean();
        homeRecResDetailList = new ArrayList<>();
        List<DiscountBean> discountBeanList = new ArrayList<>();
        DiscountBean discountBean1 = new DiscountBean(25.0,5.5);DiscountBean discountBean2 = new DiscountBean(35.0,10.5);
        discountBeanList.add(discountBean1);discountBeanList.add(discountBean2);
        ResDetailBean homeRecResDetailBean2 = new ResDetailBean(2,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg",
                "瓦罐汤",4.8f,1212,35,3,"东院食堂",30,discountBeanList);
        ResDetailBean homeRecResDetailBean1 = new ResDetailBean(1,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic21363tj30ci08ct96.jpg",
                "杨国福麻辣烫",4.9f,2343,23,5,"公寓三楼",20,null);
        ResDetailBean homeRecResDetailBean3 = new ResDetailBean(4,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic259ohaj30ci08c74r.jpg",
                "杭州小笼包",4f,222,34,3,"公寓一楼",30,discountBeanList);
        ResDetailBean homeRecResDetailBean4 = new ResDetailBean(3,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg",
                "土家掉渣烧饼",4.9f,23234,29,6,"公寓一楼",45,discountBeanList);
        ResDetailBean homeRecResDetailBean5 = new ResDetailBean(5,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg",
                "北李妈妈菜",4.7f,3212,20,6,"东院食堂",27,discountBeanList);
        ResDetailBean homeRecResDetailBean6 = new ResDetailBean(8,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg",
                "重庆小面",4.7f,3212,20,6,"公寓二楼",43,null);
        ResDetailBean homeRecResDetailBean7 = new ResDetailBean(7,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg",
                "兰州拉面",4.7f,3212,20,6,"民族餐厅",12,discountBeanList);
        ResDetailBean homeRecResDetailBean8 = new ResDetailBean(6,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg",
                "三楼自助",4.7f,3212,20,6,"公寓三楼",67,discountBeanList);
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
        searchLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchActivity.class);
                startActivity(intent);
            }
        });

//        address.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (userId>0){
//                    Intent intent = new Intent(mContext, AddressActivity.class);
//                    startActivityForResult(intent,ADDRESS_REQUEST, ActivityOptions.makeSceneTransitionAnimation((Activity) mContext).toBundle());
//                }else{
//                    Toast.makeText(mContext, "登录后查看编辑收货地址", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
        initRecyclerView();
        initRefresh();
    }

    private void initRecyclerView(){
        initBanner();
        initFiveImg();
        initClassify();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        homeRecyclerView.setLayoutManager(linearLayoutManager);

        adapter = new BaseQuickAdapter<ResDetailBean,BaseViewHolder>(R.layout.one_fragment_content_item,homeRecResDetailList) {
            @Override
            protected void convert(BaseViewHolder helper, ResDetailBean item) {
                helper.setGone(R.id.one_fragment_item_reduce_container,false);

                //设置添加到购物车的数量，红点显示
                if (item.getBuyNum() > 0){
                    helper.setText(R.id.one_content_item_buy_num,item.getBuyNum()+"");
                    helper.setVisible(R.id.one_content_item_buy_num,true);
                }else{
                    helper.setVisible(R.id.one_content_item_buy_num,false);
                }

                //设置img
                ImageView iv = helper.getView(R.id.one_content_item_iv);
                GlideUtil.load(mContext,item.getResImg(),iv,GlideUtil.REQUEST_OPTIONS);
                //店名
                helper.setText(R.id.one_fragment_content_item_name,item.getResName());
                //评分
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

                helper.setGone(R.id.divider,false);
                if (item.getDiscountList() != null && item.getDiscountList().size() > 0){
                    helper.setVisible(R.id.one_fragment_item_reduce_container,true);
                    helper.setVisible(R.id.divider,true);
                    StringBuffer sb = new StringBuffer();
                    for (DiscountBean discountBean : item.getDiscountList()){
                        int fillPrice = (int) discountBean.getFilledVal();
                        int reducePrice = (int) discountBean.getReduceVal();
                        if (discountBean.getFilledVal()>fillPrice){
                            sb.append("满"+discountBean.getFilledVal());
                        }else{
                            sb.append("满"+fillPrice);
                        }
                        if (discountBean.getReduceVal() > reducePrice){
                            sb.append("减"+discountBean.getReduceVal()+",");
                        }else{
                            sb.append("减"+reducePrice+",");
                        }
                    }
                    discountString = sb.toString().substring(0,sb.length()-1);
                    helper.setText(R.id.one_fragment_item_reduce,discountString);
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
        //默认出现了头部就不会显示EmptyView和尾部，配置以下方法也支持同时显示
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
                Intent intent = new Intent(mContext, ClassifyResActivity.class);
                intent.putExtra(RES_TITLE,getResources().getString(R.string.head_icon_food));
                intent.putExtra(ClassifyResActivity.RES_CLASSIFY,DELICIOUS);
                startActivity(intent);
            }
        });

        headOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClassifyResActivity.class);
                intent.putExtra(RES_TITLE,getResources().getString(R.string.head_icon_one));
                intent.putExtra(ClassifyResActivity.RES_CLASSIFY,ONE_FLOUR);
                startActivity(intent);
            }
        });

        headTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClassifyResActivity.class);
                intent.putExtra(RES_TITLE,getResources().getString(R.string.head_icon_two));
                intent.putExtra(ClassifyResActivity.RES_CLASSIFY,TWO_FLOUR);
                startActivity(intent);
            }
        });

        headThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClassifyResActivity.class);
                intent.putExtra(RES_TITLE,getResources().getString(R.string.head_icon_three));
                intent.putExtra(ClassifyResActivity.RES_CLASSIFY,THREE_FLOUR);
                startActivity(intent);
            }
        });

        headSweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClassifyResActivity.class);
                intent.putExtra(RES_TITLE,getResources().getString(R.string.head_icon_sweet));
                intent.putExtra(ClassifyResActivity.RES_CLASSIFY,SWEET);
                startActivity(intent);
            }
        });

        headDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClassifyResActivity.class);
                intent.putExtra(RES_TITLE,getResources().getString(R.string.head_icon_deliver));
                intent.putExtra(ClassifyResActivity.RES_CLASSIFY,DELIVER);
                startActivity(intent);
            }
        });

        headSimple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClassifyResActivity.class);
                intent.putExtra(RES_TITLE,getResources().getString(R.string.head_icon_simple));
                intent.putExtra(ClassifyResActivity.RES_CLASSIFY,SIMPLE);
                startActivity(intent);
            }
        });

        headPrefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClassifyResActivity.class);
                intent.putExtra(RES_TITLE,getResources().getString(R.string.head_icon_prefer));
                intent.putExtra(ClassifyResActivity.RES_CLASSIFY,FAVOUR);
                startActivity(intent);
            }
        });

        headFruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClassifyResActivity.class);
                intent.putExtra(RES_TITLE,getResources().getString(R.string.head_icon_fruit));
                intent.putExtra(ClassifyResActivity.RES_CLASSIFY,FRUIT);
                startActivity(intent);
            }
        });

        headCook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClassifyResActivity.class);
                intent.putExtra(RES_TITLE,getResources().getString(R.string.head_icon_cook));
                intent.putExtra(ClassifyResActivity.RES_CLASSIFY,COOK);
                startActivity(intent);
            }
        });
    }

    private void initFiveImg(){
        GlideUtil.load(mContext,R.mipmap.five_one,headOneIv,GlideUtil.REQUEST_OPTIONS);
        GlideUtil.load(mContext,R.mipmap.five_two,headTwoIv,GlideUtil.REQUEST_OPTIONS);
        GlideUtil.load(mContext,R.mipmap.five_three,headThreeIv,GlideUtil.REQUEST_OPTIONS);
        GlideUtil.load(mContext,R.mipmap.five_four,headFourIv,GlideUtil.REQUEST_OPTIONS);
        GlideUtil.load(mContext,R.mipmap.five_five,headFiveIv,GlideUtil.REQUEST_OPTIONS);
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
        super.onResume();
//        userId = PreferenceManager.getDefaultSharedPreferences(mContext).getInt("user_id",-1);
//        List<AddressBean> addressBeanList = DataSupport.where("user_id = ? and selected = ?",String.valueOf(userId),"1").find(AddressBean.class);
        //设置默认收货地址
//        if (userId>0 && addressBeanList.size()>0){
//            address.setText("送至:"+addressBeanList.get(0).getAddress());
//        }else{
//            address.setText("选择收货地址");
//        }
        //刷新店铺红点
        new Thread(new NotifyResBuyNumRunnable()).start();
        mBanner.startAutoPlay();

    }

    class NotifyResBuyNumRunnable implements Runnable{
        @Override
        public void run() {
            List<ResBuyCategoryNum> resBuyCategoryNumList = DataSupport.findAll(ResBuyCategoryNum.class);
            if (resBuyCategoryNumList.size() > 0){
                Hashtable<String,Integer> resBuyNumTable = new Hashtable<>();
                //将resBuyCategoryNumList中的添加到购物车的数量按resId设置给resBuyNumTable
                for (int i=0;i<homeRecResDetailList.size();i++){
                    resBuyNumTable.put(String.valueOf(homeRecResDetailList.get(i).getResId()),0);
                }
                for (ResBuyCategoryNum resBuyCategoryNum : resBuyCategoryNumList){
                    int num = resBuyNumTable.get(resBuyCategoryNum.getResId()) + resBuyCategoryNum.getBuyNum();
                    resBuyNumTable.put(resBuyCategoryNum.getResId(),num);
                }
                //得到resBuyNumTable中的keyList
                List<String> keyList = new ArrayList<>();
                Iterator<String> itr = resBuyNumTable.keySet().iterator();
                while (itr.hasNext()){
                    String str = itr.next();
                    keyList.add(str);
                }
                //遍历设置homeRecResDetailList各项的buyNum
                for (int i=0;i<homeRecResDetailList.size();i++){
                    for (int j=0;j<keyList.size();j++){
                        if (homeRecResDetailList.get(i).getResId() == Integer.parseInt(keyList.get(j))){
                            homeRecResDetailList.get(i).setBuyNum(resBuyNumTable.get(keyList.get(j)));
                        }
                    }
                }

            }else {
                for (int i=0;i<homeRecResDetailList.size();i++){
                    homeRecResDetailList.get(i).setBuyNum(0);
                }
            }

            //主线程刷新推荐商家列表，显示红点数量
            ((MainActivity)mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }
}
