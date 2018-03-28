package com.example.rjq.myapplication.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.example.rjq.myapplication.adapter.PopupPayWayAdapter;
import com.example.rjq.myapplication.adapter.PupupTimeAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.bean.AddressBean;
import com.example.rjq.myapplication.bean.CouponBean;
import com.example.rjq.myapplication.bean.DiscountBean;
import com.example.rjq.myapplication.bean.ResBuyCategoryNum;
import com.example.rjq.myapplication.bean.ResBuyItemNum;
import com.example.rjq.myapplication.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Handler;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AccountActivity extends BaseActivity {
    private static final int REQUEST_ADDRESS = 8080;
    private static final int REQUEST_COUPON = 8018;
    @BindView(R.id.title_middle)
    TextView middleTitle;
    @BindView(R.id.title_right)
    TextView rightTitle;
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.go_to_account)
    TextView goToAccount;
    @BindView(R.id.how_money_to_delivery)
    TextView howMoneyToDelivery;
    @BindView(R.id.no_shop)
    TextView noShop;
    @BindView(R.id.image)
    ImageView imageView;
    @BindView(R.id.tv_all_price_bottom)
    TextView allPriceBottom;
    @BindView(R.id.rl_own_taken)
    RelativeLayout rlOwnToken;
    @BindView(R.id.tv_res_name)
    TextView resName;
    @BindView(R.id.tv_deliver_money)
    TextView deliverMoney;
    @BindView(R.id.tv_all_money)
    TextView allMoneyTv;
    @BindView(R.id.ll_buy_item_container)
    AutoLinearLayout buyItemContainer;
    @BindView(R.id.tv_taken_time)
    TextView takenTime;
    @BindView(R.id.loading)
    ProgressBar progressBar;
    @BindView(R.id.address_ll)
    RelativeLayout address;
    @BindView(R.id.tv_location)
    TextView location;
    @BindView(R.id.tv_name)
    TextView name;
    @BindView(R.id.tv_phone)
    TextView phone;
    @BindView(R.id.pay_way)
    AutoRelativeLayout payWay;
    @BindView(R.id.tv_pay_selected)
    TextView paySelectedTv;
    @BindView(R.id.tv_package_money)
    TextView packageMoneyTv;
    @BindView(R.id.extra_info)
    EditText extraInfo;
    @BindView(R.id.tv_deliver_time)
    TextView deliverTime;
    @BindView(R.id.rl_immediately_deliver)
    AutoRelativeLayout rlDeliverTeime;
    @BindView(R.id.rl_red_paper)
    AutoRelativeLayout rlRedPaper;
    @BindView(R.id.tv_red_paper)
    TextView redPaperTv;
    @BindView(R.id.rl_reduce)
    RelativeLayout reduceRl;
    @BindView(R.id.tv_reduce)
    TextView reduceTv;

    private int resId;
    private String resNameText;
    //用户购买的详细数据
    private List<ResBuyItemNum> list;
    private List<AddressBean> addressList;
    private double allMoney;
    private double packageMoney;
    private List<String> payTvList;
    private List<Integer> payIvList;
    private Dialog payDialog;
    private CouponBean couponBean;
    private double reduceMoney;
    private List<DiscountBean> discountBeanList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setStateBarColor(R.color.my_fragment_status_bar_color);
    }

    @Override
    protected void initData() {
        super.initData();
        resId = getIntent().getIntExtra("res_id",-1);
        resNameText = getIntent().getStringExtra("res_name");
        reduceMoney = getIntent().getDoubleExtra("reduce_money",0);

        list = DataSupport.where("resId = ?",String.valueOf(resId)).find(ResBuyItemNum.class);
        addressList = DataSupport.where("selected = ?","1").find(AddressBean.class);
        if (addressList.size()>0){
            location.setText(addressList.get(0).getAddress());
            name.setText(addressList.get(0).getName());
            phone.setText(addressList.get(0).getPhone());
        }
        for (ResBuyItemNum resBuyItemNum : list){
            View view = initBuyItem(resBuyItemNum);
            buyItemContainer.addView(view);
            allMoney += resBuyItemNum.getBuyNum() * resBuyItemNum.getItemPrice();
            packageMoney += resBuyItemNum.getItemPackageMoney() * resBuyItemNum.getBuyNum();
        }
        allMoney += list.get(0).getResExtraMoney();
        allMoney += packageMoney;

        if (reduceMoney > 0){
            int price = (int)reduceMoney;
            reduceRl.setVisibility(View.VISIBLE);
            if (reduceMoney > price){
                reduceTv.setText("-￥"+reduceMoney);
            }else{
                reduceTv.setText(price+"");
            }
        }else{
//            HashMap<String,String> hashMap = new HashMap<>();
//            hashMap.put("res_id",String.valueOf(resId));
//            HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH, hashMap, new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    discountBeanList = new Gson().fromJson(response.body().string(),new TypeToken<List<DiscountBean>>(){}.getType());
//                    if (discountBeanList != null && discountBeanList.size() > 0){
//                        for (DiscountBean discountBean : discountBeanList){
//                            if (allMoney >= discountBean.getFilledVal()){
//                                reduceMoney = discountBean.getReduceVal();
//                            }
//                        }
//                    }
//                }
//            });
        }

        allMoney -= reduceMoney;
        deliverMoney.setText("￥"+list.get(0).getResExtraMoney());
        allMoneyTv.setText(""+allMoney);
        allPriceBottom.setText("￥"+allMoney);
        int price = (int) packageMoney;
        if (packageMoney>price){
            packageMoneyTv.setText("￥"+packageMoney);
        }else{
            packageMoneyTv.setText("￥"+price);
        }

        //初始化支付方式
        payTvList = new ArrayList<>();
        payTvList.add("支付宝");payTvList.add("银行卡支付");payTvList.add("微信支付");payTvList.add("QQ钱包");

        payIvList = new ArrayList<>();
        payIvList.add(R.mipmap.ali_pay);payIvList.add(R.mipmap.card_pay);payIvList.add(R.mipmap.v_pay);payIvList.add(R.mipmap.q_pay);

    }

    @Override
    protected void initView() {
        super.initView();
        middleTitle.setText("提交订单");
        goToAccount.setText("确认支付");
        goToAccount.setVisibility(View.VISIBLE);
        howMoneyToDelivery.setVisibility(View.GONE);
        noShop.setVisibility(View.GONE);
        rightTitle.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        allPriceBottom.setVisibility(View.VISIBLE);
        resName.setText(resNameText);
        paySelectedTv.setText(payTvList.get(0));
        goToAccount.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        rlOwnToken.setOnClickListener(this);
        address.setOnClickListener(this);
        payWay.setOnClickListener(this);
        rlDeliverTeime.setOnClickListener(this);
        rlRedPaper.setOnClickListener(this);
        initPayDialog();

        //刚进入界面选择默认收货地址
        addressList = DataSupport.where("selected = ?","1").find(AddressBean.class);
        if (addressList.size()>0){
            location.setText(addressList.get(0).getAddress());
            name.setText(addressList.get(0).getName());
            phone.setText(addressList.get(0).getPhone());
        }else{
            location.setText("请选择收货地址");
            name.setText("");
            phone.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.go_to_account:
                if (deliverTime.getText().toString().equals("选择配送时间") && takenTime.getText().toString().equals("选择堂取时间")){
                    Toast.makeText(this, "请选择配送方式!", Toast.LENGTH_SHORT).show();
                } else if (location.getText().equals("请选择收货地址")){
                    Toast.makeText(this, "请选择收货地址!", Toast.LENGTH_SHORT).show();
                } else{
                    progressBar.setVisibility(View.VISIBLE);
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            DataSupport.deleteAll(ResBuyItemNum.class,"resId = ?",list.get(0).getResId());
                            DataSupport.deleteAll(ResBuyCategoryNum.class,"resId = ?",list.get(0).getResId());
                            startActivity(new Intent(AccountActivity.this,SuccessBuyActivity.class));
                            finish();
                        }
                    }, 500);

                    String orderTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
                    int userId = PreferenceManager.getDefaultSharedPreferences(this).getInt("user_id",-1);
                    progressBar.setVisibility(View.VISIBLE);

//                    HashMap<String,String> hashMap = new HashMap<>();
//                    hashMap.put("user_id",String.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getInt("user_id",-1)));
//                    hashMap.put("coupon_id",String.valueOf(couponBean.getRedPaperId()));
//                    HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH, hashMap, new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//
//                        }
//                    });
                    //存到订单明细表
//                    for (int i=0;i<list.size();i++){
//                        HashMap<String,String> hash = new HashMap<>();
//                        hash.put("order_detail_id",orderTime+userId);
//                        hash.put("good_id",list.get(i).getGoodId());
//                        hash.put("order_detail_order",i+"");
//                        hash.put("order_detail_goods_num",list.get(i).getBuyNum()+"");
//                        hash.put("order_detail_goods_price",list.get(i).getBuyNum()*list.get(i).getItemPrice()+"");
//                        HttpUtil.sendOkHttpPostRequest("", hash, new Callback() {
//                            @Override
//                            public void onFailure(Call call, IOException e) {
//
//                            }
//
//                            @Override
//                            public void onResponse(Call call, Response response) throws IOException {
//
//                            }
//                        });
//                    }
//
//                    //存到订单表
//                    HashMap<String,String> hashMap = new HashMap<>();
//                    hashMap.put("order_id",orderTime+userId);
//                    hashMap.put("user_id",String.valueOf(userId));
//                    hashMap.put("res_id",list.get(0).getResId());
//                    hashMap.put("order_time",orderTime);
//                    hashMap.put("order_address",location.getText().toString());
//                    hashMap.put("order_price",String.valueOf(allMoney));
//                    hashMap.put("order_buy_way",paySelectedTv.getText().toString());
//                    hashMap.put("order_extra_info",extraInfo.getText().toString());
//                    String orderDescription = list.get(0).getItemName();
//                    if (list.size()>1){
//                        orderDescription = orderDescription + "等" +list.size()+"件商品";
//                    }else{
//                        orderDescription = orderDescription + "1件商品";
//                    }
//                    hashMap.put("order_description",list.get(0).getItemName()+orderDescription);
//                    if (!deliverTime.getText().toString().equals("选择配送时间")){
//                        hashMap.put("order_deliver",String.valueOf(1));   //外送则为1
//                    }else{
//                        hashMap.put("order_deliver",String.valueOf(0));
//                        //堂取时间
//                        hashMap.put("order_taken_time",takenTime.getText().toString());
//                    }
//
//                    HttpUtil.sendOkHttpPostRequest("http://", hashMap, new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//                            Toast.makeText(AccountActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
//                            //删除本地数据库购物车信息
//                            DataSupport.deleteAll(ResBuyItemNum.class,"res_id = ? and user_id = ?",list.get(0).getResId(),
//                                    String.valueOf(PreferenceManager.getDefaultSharedPreferences(AccountActivity.this).getInt("user_id",-1)));
//                            DataSupport.deleteAll(ResBuyCategoryNum.class,"res_id = ? and user_id = ?",list.get(0).getResId(),
//                                    String.valueOf(PreferenceManager.getDefaultSharedPreferences(AccountActivity.this).getInt("user_id",-1)));
//                            Intent intent = new Intent(AccountActivity.this,SuccessBuyActivity.class);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
                }

                break;
            case R.id.rl_own_taken:
                showTimeSelectedDialog(false);
                break;
            case R.id.rl_immediately_deliver:
                showTimeSelectedDialog(true);
                break;
            case R.id.address_ll:
                Intent intent = new Intent(this,AddressActivity.class);
                startActivityForResult(intent,REQUEST_ADDRESS);
                break;
            case R.id.pay_way:
                payDialog.show();
                break;
            case R.id.rl_red_paper:
                Intent couponIntent = new Intent(this,CouponActivity.class);
                couponIntent.putExtra("res_id",resId+"");
                couponIntent.putExtra("res_name",resNameText);
                couponIntent.putExtra("all_money",allMoney-list.get(0).getResExtraMoney());
                startActivityForResult(couponIntent,REQUEST_COUPON);
                break;

        }
    }

    private View initBuyItem(ResBuyItemNum resBuyItemNum){
        View view = LayoutInflater.from(this).inflate(R.layout.buy_list_item,null);
        TextView name = (TextView) view.findViewById(R.id.account_item_name);
        TextView price = (TextView) view.findViewById(R.id.account_item_price);
        TextView num = (TextView) view.findViewById(R.id.account_item_num);
        name.setText(resBuyItemNum.getItemName());
        price.setText("￥"+resBuyItemNum.getBuyNum() * resBuyItemNum.getItemPrice());
        num.setText("×"+resBuyItemNum.getBuyNum()+"");
        return view;
    }

    private void initPayDialog(){
        payDialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View view = LayoutInflater.from(this).inflate(R.layout.popup_pay_bottom, null);
        RecyclerView payRecyclerView = (RecyclerView) view.findViewById(R.id.pay_recycler);
        ImageButton close = (ImageButton) view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payDialog.dismiss();
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        payRecyclerView.setLayoutManager(linearLayoutManager);
        final PopupPayWayAdapter adapter = new PopupPayWayAdapter(this,payTvList,payIvList);
        adapter.setOnItemClickListener(new PopupPayWayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                payDialog.dismiss();
                adapter.setSelected(position);
                progressBar.setVisibility(View.VISIBLE);
                new android.os.Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      progressBar.setVisibility(View.GONE);
                        paySelectedTv.setText(payTvList.get(position));
                    }
                },500);

            }
        });
        payRecyclerView.setAdapter(adapter);

        payDialog.setCancelable(false);
        //将布局设置给Dialog
        payDialog.setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = payDialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
        //设置dialog宽度满屏
        WindowManager m = dialogWindow.getWindowManager();
        Display d = m.getDefaultDisplay();
        lp.width = d.getWidth();
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
    }

    private void showTimeSelectedDialog(boolean isDeliver){
        //初始化堂取时间
        final List<String> timeList = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("HH:mm");
        //开始时间
        long currentTime = System.currentTimeMillis();
        currentTime += 20*60*1000;
        //结束时间
        Calendar c1 = new GregorianCalendar();
        c1.set(Calendar.HOUR_OF_DAY, 22);
        long nineTime = c1.getTimeInMillis();
        if (isDeliver){
            timeList.add("立即配送");
        }
        for (long i = currentTime;i<nineTime;i+=20*60*1000){
            Date date = new Date(i);
            timeList.add(format.format(date));
        }

        final Dialog dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View view = LayoutInflater.from(this).inflate(R.layout.popup_time_from_bottom, null);
        TextView time = (TextView) view.findViewById(R.id.time);
        if (isDeliver){
            time.setText("选择配送时间");
        }
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.time_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        PupupTimeAdapter adapter = new PupupTimeAdapter(this,timeList);
        if (isDeliver){
            adapter.setOnItemClickListener(new PupupTimeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    takenTime.setText("选择堂取时间");
                    deliverMoney.setText("￥"+list.get(0).getResExtraMoney());
                    if (couponBean == null){
                        allMoneyTv.setText(allMoney+"");
                        allPriceBottom.setText("￥"+allMoney);
                    }else{
                        allMoneyTv.setText((allMoney-couponBean.getPrice())+"");
                        allPriceBottom.setText("￥"+(allMoney-couponBean.getPrice()));
                    }
                    deliverTime.setText(timeList.get(position));
                    dialog.dismiss();
                }
            });
        }else{
            adapter.setOnItemClickListener(new PupupTimeAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    takenTime.setText(timeList.get(position));
                    deliverTime.setText("选择配送时间");
                    deliverMoney.setText("免");
                    if (couponBean == null){
                        allMoneyTv.setText((allMoney - list.get(0).getResExtraMoney())+"");
                        allPriceBottom.setText("￥"+(allMoney - list.get(0).getResExtraMoney()));
                    }else{
                        allMoneyTv.setText((allMoney - list.get(0).getResExtraMoney()-couponBean.getPrice())+"");
                        allPriceBottom.setText("￥"+(allMoney - list.get(0).getResExtraMoney()-couponBean.getPrice()));
                    }

                    dialog.dismiss();
                }
            });
        }
        recyclerView.setAdapter(adapter);

        //将布局设置给Dialog
        dialog.setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
        //设置dialog宽度满屏
        WindowManager m = dialogWindow.getWindowManager();
        Display d = m.getDefaultDisplay();
        lp.width = d.getWidth();
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_ADDRESS:
                if (resultCode == RESULT_OK){
                    progressBar.setVisibility(View.VISIBLE);
                    new android.os.Handler(getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AddressBean addressBean = (AddressBean) data.getSerializableExtra("address");
                            location.setText(addressBean.getAddress());
                            name.setText(addressBean.getName());
                            phone.setText(addressBean.getPhone());
                            progressBar.setVisibility(View.GONE);
                        }
                    },700);
                }
                break;
            case REQUEST_COUPON:
                if (resultCode == RESULT_OK){
                    couponBean = (CouponBean) data.getSerializableExtra("coupon");
                    int price = (int) couponBean.getPrice();
                    if (couponBean.getPrice() > price){
                        redPaperTv.setText("-￥"+couponBean.getPrice());
                    }else{
                        redPaperTv.setText("-￥"+price);
                    }

                    redPaperTv.setTextColor(getResources().getColor(R.color.colorRed));
                    redPaperTv.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                    if (takenTime.getText().toString().equals("选择堂取时间")){
                        allMoneyTv.setText((allMoney - couponBean.getPrice())+"");
                        allPriceBottom.setText("￥"+(allMoney - couponBean.getPrice()));
                    }else{
                        allMoneyTv.setText((allMoney - list.get(0).getResExtraMoney()-couponBean.getPrice())+"");
                        allPriceBottom.setText("￥"+(allMoney - list.get(0).getResExtraMoney()-couponBean.getPrice()));
                    }
                }
                break;
        }
    }
}
