package com.example.rjq.myapplication.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.adapter.TabFragmentAdapter;

import com.example.rjq.myapplication.bean.GoodsListBean;
import com.example.rjq.myapplication.bean.HomeDataBean;
import com.example.rjq.myapplication.event.MessageEvent;
import com.example.rjq.myapplication.fragment.EvaluateFragment;
import com.example.rjq.myapplication.fragment.GoodsFragment;
import com.example.rjq.myapplication.fragment.ResDetailFragment;
import com.example.rjq.myapplication.util.AnimationUtil;
import com.example.rjq.myapplication.util.GlideUtil;
import com.example.rjq.myapplication.util.HttpUtil;
import com.google.gson.Gson;
import com.zhy.autolayout.AutoLinearLayout;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.rjq.myapplication.fragment.OneFragment.RES_DETAIL;

public class ResActivity extends BaseActivity {

    private HomeDataBean.HomeRecResDetailBean homeRecResDetailBean;
    @BindView(R.id.collapsing)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.res_img)
    ImageView resImgIv;
    @BindView(R.id.res_name)
    TextView resNameTv;
    @BindView(R.id.res_star)
    RatingBar ratingBar;
    @BindView(R.id.res_score)
    TextView resScore;
    @BindView(R.id.res_order_num)
    TextView resOrderNum;
    @BindView(R.id.res_deliver_time)
    TextView resDeliverTime;
    @BindView(R.id.res_description)
    TextView resDescriptionTv;
    @BindView(R.id.res_reduce_container)
    AutoLinearLayout resReduceContainer;
    @BindView(R.id.res_special_container)
    AutoLinearLayout resSpecialContainer;
    @BindView(R.id.res_new_container)
    AutoLinearLayout resNewContainer;
    @BindView(R.id.res_give_container)
    AutoLinearLayout resGiveContainer;
    @BindView(R.id.res_reduce)
    TextView resReduceTv;
    @BindView(R.id.res_special)
    TextView resSpecialTv;
    @BindView(R.id.res_new)
    TextView resNewTv;
    @BindView(R.id.res_give)
    TextView resGiveTv;
    @BindView(R.id.res_special_num)
    TextView resSpecialNum;
    @BindView(R.id.return_btn)
    ImageView returnBtn;
    @BindView(R.id.search_ll)
    LinearLayout searchLl;
    @BindView(R.id.more_iv)
    ImageView moveIv;
    @BindView(R.id.vp)
    ViewPager viewPager;
    @BindView(R.id.shop_cart_main)
    RelativeLayout shopCartMain;
    @BindView(R.id.shop_cart_num)
    TextView shopCartNum;
    @BindView(R.id.total_price)
    TextView totalPrice;
    @BindView(R.id.no_shop)
    TextView noShop;
    @BindView(R.id.how_money_to_delivery)
    TextView howMoneyToDelivery;
    @BindView(R.id.go_to_account)
    TextView goToCheckOut;

    //优惠总数
    private int specialNum;
    //fragment列表
    private List<Fragment> mFragments = new ArrayList<>();
    //tab名的列表
    private List<String> mTitles = new ArrayList<>();

    private GoodsListBean goodsListBean;

    private TabFragmentAdapter adapter;
    private ViewGroup anim_mask_layout;//动画层

    public HomeDataBean.HomeRecResDetailBean getResDetailBean(){
        return homeRecResDetailBean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);
        setStatusBarTransparent();
    }

    @Override
    protected void initView() {
        super.initView();
//        collapsingToolbarLayout.setContentScrim(getResources().getDrawable(R.mipmap.background));
        returnBtn.setOnClickListener(this);
        goToCheckOut.setOnClickListener(this);
        setViewPager();
    }

    @Override
    protected void initData() {
        super.initData();
        //请求店铺的商品列表
//        HashMap<String,String> hashMap = new HashMap<>();
//        hashMap.put("id",String.valueOf(homeRecResDetailBean.getId()));
//        HttpUtil.sendOkHttpPostRequest("http://", hashMap, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("GoodsListBean",e.toString());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                 goodsListBean = new Gson().fromJson(response.toString(),GoodsListBean.class);
//            }
//        });

        Intent intent = getIntent();
        homeRecResDetailBean = (HomeDataBean.HomeRecResDetailBean) intent.getSerializableExtra(RES_DETAIL);

        //设置商家名称
        String resName = homeRecResDetailBean.getResName();
        resNameTv.setText(resName);

        //设置起送费
        String deliverMoney = getResources().getString(R.string.res_deliver_money);
        deliverMoney = String.format(deliverMoney,homeRecResDetailBean.getResDeliverMoney());
        howMoneyToDelivery.setText(deliverMoney);

        //设置图片
        String resImg = homeRecResDetailBean.getResImg();
//        GlideUtil.load(this,resImg,resImgIv,GlideUtil.REQUEST_OPTIONS);
//        collapsingToolbarLayout.setContentScrim(getResources().getDrawable(R.mipmap.background));

        //设置月售多少单
        int monthOrder = homeRecResDetailBean.getResOrderNum();
        String monthOrderNum = getResources().getString(R.string.res_month_sell_order);
        monthOrderNum = String.format(monthOrderNum,monthOrder);
        resOrderNum.setText(monthOrderNum);

        //设置配送时间
        int deliverTime = homeRecResDetailBean.getResDeliverTime();
        String businessDeliverTime = getResources().getString(R.string.res_business_deliver_time);
        businessDeliverTime = String.format(businessDeliverTime,deliverTime);
        resDeliverTime.setText(businessDeliverTime);

        //设置星星评分
        float starNum = homeRecResDetailBean.getResStar();
        ratingBar.setRating(starNum);
        resScore.setText(starNum+"");

        //设置商家描述
        String resDescription = homeRecResDetailBean.getResDescription();
        if (!TextUtils.isEmpty(resDescription)){
            resDescriptionTv.setText(resDescription);
        }

        //设置各个活动
        String resReduce = homeRecResDetailBean.getResReduce();
        if (!TextUtils.isEmpty(resReduce)){
            specialNum ++;
            resReduceContainer.setVisibility(View.VISIBLE);
            resReduceTv.setText(resReduce);
        }
        String resSpecial = homeRecResDetailBean.getResSpecial();
        if (!TextUtils.isEmpty(resSpecial)){
            specialNum ++;
            resSpecialContainer.setVisibility(View.VISIBLE);
            resSpecialTv.setText(resSpecial);
        }
        String resNew = homeRecResDetailBean.getResNew();
        if (!TextUtils.isEmpty(resNew)){
            specialNum ++;
            resNewContainer.setVisibility(View.VISIBLE);
            resNewTv.setText(resNew);
        }
        String resGive = homeRecResDetailBean.getResGive();
        if (!TextUtils.isEmpty(resGive)){
            specialNum ++;
            resGiveContainer.setVisibility(View.VISIBLE);
            resGiveTv.setText(resGive);
        }

        //设置优惠总数
        if (specialNum > 0){
            resSpecialNum.setText(specialNum+"个优惠");
            resSpecialNum.setVisibility(View.VISIBLE);
        }

    }

    private void setViewPager() {
        GoodsFragment goodsFragment = new GoodsFragment();
        EvaluateFragment evaluateFragment = new EvaluateFragment();
        ResDetailFragment resDetailFragment = new ResDetailFragment();
        mFragments.add(goodsFragment);
        mFragments.add(evaluateFragment);
        mFragments.add(resDetailFragment);

        mTitles.add(getResources().getString(R.string.order));
        mTitles.add(getResources().getString(R.string.evaluate));
        mTitles.add(getResources().getString(R.string.restaurant));

        adapter = new TabFragmentAdapter(getSupportFragmentManager(),mFragments,mTitles);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        //设置tabLayout的下划线的宽度
        setIndicator(tabLayout,36,36);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        shopCartMain.setVisibility(View.VISIBLE);
//                        shopCartMain.startAnimation(AnimationUtil.createInAnimation(ResActivity.this, shopCartMain.getMeasuredHeight()));
                        break;
                    case 1:
                        shopCartMain.setVisibility(View.GONE);
                        break;
                    case 2:
                        shopCartMain.setVisibility(View.GONE);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.return_btn:
                finish();
                break;
            case R.id.go_to_account:
                Toast.makeText(this, "结账", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 添加 或者  删除  商品发送的消息处理
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        if(event!=null){
            if(event.num>0){
                shopCartNum.setText(String.valueOf(event.num));
                shopCartNum.setVisibility(View.VISIBLE);
                totalPrice.setVisibility(View.VISIBLE);
                noShop.setVisibility(View.GONE);
                totalPrice.setText("¥"+String.valueOf(event.price));
                if (event.price >= homeRecResDetailBean.getResDeliverMoney()){
                    howMoneyToDelivery.setVisibility(View.GONE);
                    goToCheckOut.setVisibility(View.VISIBLE);
                }else{
                    goToCheckOut.setVisibility(View.GONE);
                    howMoneyToDelivery.setVisibility(View.VISIBLE);
                    int price = (int)(homeRecResDetailBean.getResDeliverMoney()-event.price);
                    if ((homeRecResDetailBean.getResDeliverMoney()-event.price) > price){
                        howMoneyToDelivery.setText("还差￥"+(homeRecResDetailBean.getResDeliverMoney()-event.price)+"起送");
                    }else{
                        howMoneyToDelivery.setText("还差￥"+price+"起送");
                    }

                }

            }else{
                shopCartNum.setVisibility(View.GONE);
                totalPrice.setVisibility(View.GONE);
                noShop.setVisibility(View.VISIBLE);
                goToCheckOut.setVisibility(View.GONE);
                howMoneyToDelivery.setVisibility(View.VISIBLE);
                String deliverMoney = getResources().getString(R.string.res_deliver_money);
                deliverMoney = String.format(deliverMoney,homeRecResDetailBean.getResDeliverMoney());
                howMoneyToDelivery.setText(deliverMoney);
            }

            Log.d("ResActivity","添加的数量："+event.goods.size());
        }

    }

    /**
     * 设置动画（点击添加商品）
     * @param v
     * @param startLocation
     */
    public void setAnim(final View v, int[] startLocation) {
        anim_mask_layout = null;
        anim_mask_layout = createAnimLayout();
        anim_mask_layout.addView(v);//把动画小球添加到动画层
        final View view = addViewToAnimLayout(anim_mask_layout, v, startLocation);
        int[] endLocation = new int[2];// 存储动画结束位置的X、Y坐标
        shopCartNum.getLocationInWindow(endLocation);
        // 计算位移
        int endX = 0 - startLocation[0] + 50;// 动画位移的X坐标
        int endY = endLocation[1] - startLocation[1];// 动画位移的y坐标

        TranslateAnimation translateAnimationX = new TranslateAnimation(0,endX, 0, 0);
        translateAnimationX.setInterpolator(new LinearInterpolator());
        translateAnimationX.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationX.setFillAfter(true);

        TranslateAnimation translateAnimationY = new TranslateAnimation(0, 0, 0, endY);
        translateAnimationY.setInterpolator(new AccelerateInterpolator());
        translateAnimationY.setRepeatCount(0);// 动画重复执行的次数
        translateAnimationY.setFillAfter(true);

        AnimationSet set = new AnimationSet(false);
        set.setFillAfter(false);
        set.addAnimation(translateAnimationY);
        set.addAnimation(translateAnimationX);
        set.setDuration(400);// 动画的执行时间
        view.startAnimation(set);
        // 动画监听事件
        set.setAnimationListener(new Animation.AnimationListener() {
            // 动画的开始
            @Override
            public void onAnimationStart(Animation animation) {
                v.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            // 动画的结束
            @Override
            public void onAnimationEnd(Animation animation) {
                v.setVisibility(View.GONE);
            }
        });

    }

    /**
     * 初始化动画图层
     * @return
     */
    private ViewGroup createAnimLayout() {
        ViewGroup rootView = (ViewGroup) this.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setId(Integer.MAX_VALUE-1);
        animLayout.setBackgroundResource(android.R.color.transparent);
        rootView.addView(animLayout);
        return animLayout;
    }

    /**
     * 将View添加到动画图层
     * @param parent
     * @param view
     * @param location
     * @return
     */
    private View addViewToAnimLayout(final ViewGroup parent, final View view, int[] location) {
        int x = location[0];
        int y = location[1];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = x;
        lp.topMargin = y;
        view.setLayoutParams(lp);
        return view;
    }

    public GoodsListBean getGoodsListBean(){
        return goodsListBean;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
