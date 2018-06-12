package com.example.rjq.myapplication.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
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
    DecimalFormat df = new DecimalFormat("#0.0");
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
        allMoney += packageMoney;

        //网络请求店铺满减信息并设置
        progressBar.setVisibility(View.VISIBLE);
        goToAccount.setClickable(false);
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("shop_id",String.valueOf(resId));
        HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH+HttpUtil.OBTAIN_SHOP_ACCOUNT, hashMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                discountBeanList = new Gson().fromJson(response.body().string(),new TypeToken<List<DiscountBean>>(){}.getType());
                for (DiscountBean discountBean : discountBeanList){
                    if (allMoney >= discountBean.getFilledVal()){
                        reduceMoney = discountBean.getReduceVal();
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (reduceMoney>0){
                            int price = (int)reduceMoney;
                            reduceRl.setVisibility(View.VISIBLE);
                            if (reduceMoney > price){
                                reduceTv.setText("-￥"+reduceMoney);
                            }else{
                                reduceTv.setText("-￥"+price);
                            }
                        }
                        allMoney += list.get(0).getResExtraMoney();
                        allMoneyTv.setText(df.format(allMoney-reduceMoney));
                        allPriceBottom.setText("￥"+df.format(allMoney-reduceMoney));
                        goToAccount.setText("确认支付");
                        goToAccount.setOnClickListener(AccountActivity.this);
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

        deliverMoney.setText("￥"+list.get(0).getResExtraMoney());
        //设置包装费
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
        goToAccount.setVisibility(View.VISIBLE);
        howMoneyToDelivery.setVisibility(View.GONE);
        noShop.setVisibility(View.GONE);
        rightTitle.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        allPriceBottom.setVisibility(View.VISIBLE);
        resName.setText(resNameText);
        paySelectedTv.setText(payTvList.get(0));
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
                    final long orderTimeId =  System.currentTimeMillis();
                    final int userId = PreferenceManager.getDefaultSharedPreferences(this).getInt("user_id",-1);
                    progressBar.setVisibility(View.VISIBLE);

                    //用户使用红包后需要在数据库中将该用户的红包使用状态改变
                    if (couponBean != null){
                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("buyer_id",String.valueOf(userId));
                        hashMap.put("red_packet_id",String.valueOf(couponBean.getRedPaperId()));
                        HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH+HttpUtil.ALTER_USER_RED_PACKET, hashMap, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.d("AccountActivity",response.body().string());
                            }
                        });
                    }

                    //存到订单表
                    HashMap<String,String> hashMap = new HashMap<>();
                    //orderId为当前时间戳+用户Id
                    hashMap.put("order_id",String.valueOf(orderTimeId)+userId);
                    hashMap.put("buyer_id",String.valueOf(userId));
                    hashMap.put("shop_id",list.get(0).getResId());
                    hashMap.put("order_address",location.getText().toString());
                    hashMap.put("pay_way",paySelectedTv.getText().toString());

                    //2代表待接单
                    hashMap.put("order_state",String.valueOf(2));
//                    if (couponBean == null){
//                        hashMap.put("pay_amount",String.valueOf(allMoney-reduceMoney));
//                    }else{
//                        hashMap.put("pay_amount",String.valueOf(allMoney-reduceMoney-couponBean.getPrice()));
//                    }
                    hashMap.put("order_remark",extraInfo.getText().toString());

                    if (!deliverTime.getText().toString().equals("选择配送时间")){
                        hashMap.put("isdeliver",String.valueOf(1));   //外送则为1
                        hashMap.put("order_amount",String.valueOf(allMoney));//外送则加上配送费
                        if (!deliverTime.getText().toString().equals("立即配送")){
                            //预约时间
                            hashMap.put("servicetime",deliverTime.getText().toString());
                        }
                        if (couponBean == null){
                            hashMap.put("pay_amount",String.valueOf(allMoney-reduceMoney));
                        }else{
                            hashMap.put("pay_amount",String.valueOf(allMoney-reduceMoney-couponBean.getPrice()));
                        }
                    }else{
                        hashMap.put("isdeliver",String.valueOf(0));
                        hashMap.put("order_amount",String.valueOf(allMoney-list.get(0).getResExtraMoney()));//堂取减去配送费
                        //预约时间
                        hashMap.put("servicetime",takenTime.getText().toString());
                        if (couponBean == null){
                            hashMap.put("pay_amount",String.valueOf(allMoney-reduceMoney-list.get(0).getResExtraMoney()));
                        }else{
                            hashMap.put("pay_amount",String.valueOf(allMoney-reduceMoney-couponBean.getPrice()-list.get(0).getResExtraMoney()));
                        }
                    }

                    //订单明细表json数据
                    hashMap.put("data",new Gson().toJson(list));

                    HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH+HttpUtil.SAVE_ORDER, hashMap, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            String responseText = response.body().string();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(AccountActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    //删除本地数据库购物车信息
                                    DataSupport.deleteAll(ResBuyItemNum.class,"resId = ?",list.get(0).getResId());
                                    DataSupport.deleteAll(ResBuyCategoryNum.class,"resId = ?",list.get(0).getResId());
                                    startActivity(new Intent(AccountActivity.this,SuccessBuyActivity.class));
                                    finish();
                                }
                            });
                        }
                    });
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
                        allMoneyTv.setText(df.format(allMoney-reduceMoney));
                        allPriceBottom.setText("￥"+df.format(allMoney-reduceMoney));
                    }else{
                        allMoneyTv.setText(df.format((allMoney-couponBean.getPrice()-reduceMoney)));
                        allPriceBottom.setText("￥"+df.format((allMoney-couponBean.getPrice()-reduceMoney)));
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
                        allMoneyTv.setText(df.format((allMoney - list.get(0).getResExtraMoney()-reduceMoney)));
                        allPriceBottom.setText("￥"+df.format((allMoney - list.get(0).getResExtraMoney()-reduceMoney)));
                    }else{
                        allMoneyTv.setText(df.format((allMoney - list.get(0).getResExtraMoney()-couponBean.getPrice()-reduceMoney)));
                        allPriceBottom.setText("￥"+df.format((allMoney - list.get(0).getResExtraMoney()-couponBean.getPrice()-reduceMoney)));
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
                        allMoneyTv.setText(df.format((allMoney - couponBean.getPrice()-reduceMoney)));
                        allPriceBottom.setText("￥"+df.format((allMoney - couponBean.getPrice()-reduceMoney)));
                    }else{
                        allMoneyTv.setText(df.format((allMoney - list.get(0).getResExtraMoney()-couponBean.getPrice()-reduceMoney)));
                        allPriceBottom.setText("￥"+df.format((allMoney - list.get(0).getResExtraMoney()-couponBean.getPrice()-reduceMoney)));
                    }
                }
                break;
        }
    }

}
