package com.example.rjq.myapplication.activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.adapter.PopupPayWayAdapter;
import com.example.rjq.myapplication.adapter.TabFragmentAdapter;

import com.example.rjq.myapplication.bean.DiscountBean;
import com.example.rjq.myapplication.bean.GoodsListBean;
import com.example.rjq.myapplication.bean.ResBuyItemNum;
import com.example.rjq.myapplication.bean.ResDetailBean;
import com.example.rjq.myapplication.bean.UserBean;
import com.example.rjq.myapplication.event.MessageEvent;
import com.example.rjq.myapplication.fragment.EvaluateFragment;
import com.example.rjq.myapplication.fragment.GoodsFragment;
import com.example.rjq.myapplication.fragment.ResDetailFragment;
import com.example.rjq.myapplication.util.AnimationUtil;
import com.example.rjq.myapplication.util.FileStorage;
import com.example.rjq.myapplication.util.GlideUtil;
import com.example.rjq.myapplication.util.HttpUtil;
import com.example.rjq.myapplication.view.GoodsDetailPopWin;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.autolayout.AutoLinearLayout;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.rjq.myapplication.fragment.OneFragment.RES_DETAIL;

public class ResActivity extends BaseActivity {

    public static final String RES_ID = "res_id";
    private ResDetailBean homeRecResDetailBean;
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
    @BindView(R.id.res_reduce)
    TextView resReduceTv;
    @BindView(R.id.res_special_num)
    TextView resSpecialNum;
    @BindView(R.id.return_btn)
    ImageView returnBtn;
    @BindView(R.id.search_ll)
    RelativeLayout searchLl;
    @BindView(R.id.more_iv)
    ImageView moreIv;
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
    @BindView(R.id.pop_rl)
    RelativeLayout popRl;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    //优惠总数
    private int specialNum;
    //fragment列表
    private List<Fragment> mFragments = new ArrayList<>();
    //tab名的列表
    private List<String> mTitles = new ArrayList<>();

    private double totalMoney;

    private GoodsListBean goodsListBean;
    private List<GoodsListBean.GoodsCategoryBean> categoryBeanList;

    private TabFragmentAdapter adapter;
    private ViewGroup anim_mask_layout;//动画层

    private int resId;
    private String resName;
    private String discountString;
    private List<DiscountBean> discountBeanList;

    public ResDetailBean getResDetailBean(){
        return homeRecResDetailBean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res);
//        setTheme(R.style.AppTheme_Fullscreen);
        setStatusBarTransparent();
        Log.d("process test","main process :"+android.os.Process.myPid());
        Log.d("process test","main process current thread:"+android.os.Process.myTid());
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("process test","main process son thread:"+android.os.Process.myTid());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("process test","main process main thread:"+android.os.Process.myTid());
                    }
                });
            }
        }).start();

    }

    @Override
    protected void initView() {
        super.initView();
        returnBtn.setOnClickListener(this);
        goToCheckOut.setOnClickListener(this);
        searchLl.setOnClickListener(this);
        popRl.setOnClickListener(this);
        moreIv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        goodsListBean = new GoodsListBean();
        Intent intent = getIntent();
        homeRecResDetailBean = (ResDetailBean) intent.getSerializableExtra(RES_DETAIL);
        if (homeRecResDetailBean == null){

            resId = Integer.parseInt(intent.getStringExtra(RES_ID));
            resName = intent.getStringExtra("res_name");

            //请求店铺信息
            progressBar.setVisibility(View.VISIBLE);
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("shop_id",intent.getStringExtra(RES_ID));
            HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH+HttpUtil.OBTAIN_SHOP_BY_ID, hashMap, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("homeRecResDetail fail",e.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ResActivity.this, "网络连接超时，请检查网络!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    try{
                        JSONObject jsonObject = new JSONObject(responseText);
                        int status = jsonObject.getInt("status");
                        if (status == 1){
                            homeRecResDetailBean = new Gson().fromJson(jsonObject.getJSONObject("data").toString(),ResDetailBean.class);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    setResDetail();
                                    requestResGoods();
                                }
                            });
                        }
                    }catch(JSONException e){

                    }
                }
            });
        }else{
            setResDetail();
            resId = homeRecResDetailBean.getResId();
            resName = homeRecResDetailBean.getResName();
            requestResGoods();
        }
    }

    private void requestResGoods(){
        //请求server端的店铺商品列表
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("shop_id",String.valueOf(resId));
        HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH+HttpUtil.OBTAIN_SHOP_GOODS, hashMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("GoodsListBean",e.toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ResActivity.this, "网络连接超时，请检查网络!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                categoryBeanList = new Gson().fromJson(response.body().string(),new TypeToken<List<GoodsListBean.GoodsCategoryBean>>(){}.getType());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            goodsListBean.setData(categoryBeanList);
                            goodsListBean.setResName(resName);
                            goodsListBean.setResId(resId);
                            progressBar.setVisibility(View.GONE);
                            setViewPager();
                        }catch(Exception e){
                            Toast.makeText(ResActivity.this, "网络连接超时，请检查网络!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });

            }
        });
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
//        setTabIndicator(tabLayout,36,36);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        shopCartMain.setVisibility(View.VISIBLE);
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

    public void setTabIndicator(TabLayout tabs, int leftDip, int rightDip) {
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
            case R.id.search_ll:
                //应该启动店铺内搜索界面，只搜索该店铺内的商品;而不是启动SearchActivity去搜索店铺
//                Intent intent = new Intent(this,SearchActivity.class);
//                startActivity(intent);
                Intent intent = new Intent(this,OtherProcessActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.pop_rl:
                showSelectedDetailDialog();
                break;
            case R.id.more_iv:
                break;
            case R.id.go_to_account:
                if (DataSupport.findAll(UserBean.class).size() > 0){
                    Intent accountIntent = new Intent(this,AccountActivity.class);
                    accountIntent.putExtra("res_id",resId);
                    accountIntent.putExtra("res_name",resName);
//                    if (discountBeanList != null && discountBeanList.size() > 0){
//                        double reduceMoney = 0;
//                        for (DiscountBean discountBean : discountBeanList){
//                            if (totalMoney >= discountBean.getFilledVal()){
//                                reduceMoney = discountBean.getReduceVal();
//                            }
//                        }
//                        accountIntent.putExtra("reduce_money",reduceMoney);
//                    }
                    startActivity(accountIntent);
                }else{
                    Intent loginIntent = new Intent(this,LoginActivity.class);
                    startActivity(loginIntent);
                }
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
                //double类型保留小数点后一位
                DecimalFormat df = new DecimalFormat("#0.0");
                shopCartNum.setText(String.valueOf(event.num));
                shopCartNum.setVisibility(View.VISIBLE);
                totalPrice.setVisibility(View.VISIBLE);
                noShop.setVisibility(View.GONE);
                //设置购买的总价钱
                int price2 = (int)event.price;
                totalMoney = event.price;
                if (event.price > price2){
                    totalPrice.setText("¥"+df.format(event.price));
                }else{
                    totalPrice.setText("¥"+price2);
                }

                if (event.price >= homeRecResDetailBean.getResDeliverMoney()){
                    howMoneyToDelivery.setVisibility(View.GONE);
                    goToCheckOut.setVisibility(View.VISIBLE);
                    goToCheckOut.setText(getString(R.string.go_to_account));
                }else{
                    goToCheckOut.setVisibility(View.GONE);
                    howMoneyToDelivery.setVisibility(View.VISIBLE);
                    //设置还差多少钱起送
                    int price = (int)(homeRecResDetailBean.getResDeliverMoney()-event.price);
                    if ((homeRecResDetailBean.getResDeliverMoney()-event.price) > price){
                        howMoneyToDelivery.setText("还差￥"+df.format((homeRecResDetailBean.getResDeliverMoney()-event.price))+"起送");
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

//            Log.d("ResActivity","添加的数量："+event.goods.size());
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
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
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

    private void showSelectedDetailDialog(){
        List<ResBuyItemNum> list = DataSupport.where("resId = ?",String.valueOf(resId)).find(ResBuyItemNum.class);
        if (list.size() > 0){
            double packageMoney = 0;
            final Dialog dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
            //填充对话框的布局
            View view = LayoutInflater.from(this).inflate(R.layout.popup_goods_detail, null);
            AutoLinearLayout itemLl = (AutoLinearLayout) view.findViewById(R.id.item_ll);
            TextView packageMoneyTv = (TextView) view.findViewById(R.id.good_package_money);
            RelativeLayout packageMoneyRl = (RelativeLayout) view.findViewById(R.id.package_money_rl);
            for (ResBuyItemNum resBuyItemNum : list){
                itemLl.addView(initGoodDetailItemView(resBuyItemNum.getItemName(),resBuyItemNum.getBuyNum(),resBuyItemNum.getItemPrice()*resBuyItemNum.getBuyNum()));
                packageMoney += resBuyItemNum.getItemPackageMoney() * resBuyItemNum.getBuyNum();
            }

            if (packageMoney > 0){
                packageMoneyRl.setVisibility(View.VISIBLE);
                int price = (int) packageMoney;
                if (packageMoney > price){
                    packageMoneyTv.setText(""+packageMoney);
                }else{
                    packageMoneyTv.setText(""+price);
                }
            }else{
                packageMoneyRl.setVisibility(View.GONE);
            }
            dialog.setCancelable(true);
            //将布局设置给Dialog
            dialog.setContentView(view);
            //获取当前Activity所在的窗体
            Window dialogWindow = dialog.getWindow();
            //设置Dialog从窗体底部弹出
            dialogWindow.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.y = getResources().getDimensionPixelOffset(R.dimen.dimen_54dp);//设置Dialog距离底部的距离
            //设置dialog宽度满屏
            WindowManager m = dialogWindow.getWindowManager();
            Display d = m.getDefaultDisplay();
            lp.width = d.getWidth();
            //将属性设置给窗体
            dialogWindow.setAttributes(lp);
            dialog.show();
        }
    }

    private View initGoodDetailItemView(String goodNameText, int num, double goodPriceText){
        View view = LayoutInflater.from(this).inflate(R.layout.goods_detail_item,null);
        TextView goodName = (TextView) view.findViewById(R.id.good_name);
        TextView goodNum = (TextView) view.findViewById(R.id.good_num);
        TextView goodPrice = (TextView) view.findViewById(R.id.good_price);
        goodName.setText(goodNameText);
        goodNum.setText("×"+num);
        int price = (int) goodPriceText;
        if (goodPriceText > price){
            goodPrice.setText(""+goodPriceText);
        }else{
            goodPrice.setText(""+price);
        }
        return view;
    }

    private void setResDetail(){
        //设置满减活动
        discountBeanList = homeRecResDetailBean.getDiscountList();
        if (discountBeanList != null && discountBeanList.size() > 0){
            StringBuffer sb = new StringBuffer();
            for (DiscountBean discountBean : homeRecResDetailBean.getDiscountList()){
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
        }

        if (!TextUtils.isEmpty(discountString)){
            resReduceContainer.setVisibility(View.VISIBLE);
            resReduceTv.setText(discountString);
            specialNum = 1;
        }

        //设置优惠总数
        if (specialNum > 0){
            resSpecialNum.setText(specialNum+"个优惠");
            resSpecialNum.setVisibility(View.VISIBLE);
        }

        //设置商家名称
        String resName = homeRecResDetailBean.getResName();
        resNameTv.setText(resName);

        //设置起送费
        String deliverMoney = getResources().getString(R.string.res_deliver_money);
        deliverMoney = String.format(deliverMoney,homeRecResDetailBean.getResDeliverMoney());
        howMoneyToDelivery.setText(deliverMoney);

        //设置店铺顶部图片
        final String resImg = homeRecResDetailBean.getShopPic();
        if (!TextUtils.isEmpty(resImg)){
            GlideUtil.load(this,resImg,resImgIv,GlideUtil.REQUEST_OPTIONS);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //该方法必须在子线程中执行
                    final Drawable drawable = FileStorage.loadImageFromNetwork(resImg);
                    //回到主线程更新ui
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            collapsingToolbarLayout.setContentScrim(drawable);
                        }
                    });

                }
            }).start();
        }

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
        if (starNum>0){
            ratingBar.setRating(starNum);
            resScore.setText(starNum+"");
            ratingBar.setVisibility(View.VISIBLE);
        }

        //设置商家描述
        String resDescription = homeRecResDetailBean.getResDescription();
        if (!TextUtils.isEmpty(resDescription)){
            resDescriptionTv.setText(resDescription);
        }

    }
}
