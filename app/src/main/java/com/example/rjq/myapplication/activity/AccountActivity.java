package com.example.rjq.myapplication.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.example.rjq.myapplication.adapter.PupupTimeAdapter;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.bean.ResBuyCategoryNum;
import com.example.rjq.myapplication.bean.ResBuyItemNum;
import com.example.rjq.myapplication.util.HttpUtil;
import com.zhy.autolayout.AutoLinearLayout;

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
import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AccountActivity extends BaseActivity {
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
    @BindView(R.id.cb_immediately_deliver)
    CheckBox checkBox;
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

    private int resId;
    private String resNameText;
    //用户购买的详细数据
    private List<ResBuyItemNum> list;
    private double allMoney;
    private List<String> timeList;
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
        for (ResBuyItemNum resBuyItemNum : list){
            View view = initBuyItem(resBuyItemNum);
            buyItemContainer.addView(view);
            allMoney += resBuyItemNum.getBuyNum() * resBuyItemNum.getItemPrice();
        }
        allMoney += list.get(0).getResExtraMoney();
        deliverMoney.setText("￥"+list.get(0).getResExtraMoney());
        allMoneyTv.setText(""+allMoney);
        allPriceBottom.setText("￥"+allMoney);

        //初始化堂取时间
        timeList = new ArrayList<>();
        DateFormat format = new SimpleDateFormat("HH:mm");
        //开始时间
        long currentTime = System.currentTimeMillis();
        currentTime += 20*60*1000;
        //结束时间
        Calendar c1 = new GregorianCalendar();
        c1.set(Calendar.HOUR_OF_DAY, 21);
        long nineTime = c1.getTimeInMillis();
        for (long i = currentTime;i<nineTime;i+=20*60*1000){
            Date date = new Date(i);
            timeList.add(format.format(date));
        }

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

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    takenTime.setText("选择堂取时间");
                    deliverMoney.setText("￥"+list.get(0).getResExtraMoney());
                    allMoneyTv.setText(allMoney+"");
                }
            }
        });
        goToAccount.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        rlOwnToken.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.go_to_account:
                if (!checkBox.isChecked() || takenTime.equals("选择堂取时间")){
                    Toast.makeText(this, "请选择配送方式后付款!", Toast.LENGTH_SHORT).show();
                }else{
//                    progressBar.setVisibility(View.VISIBLE);
//                    int num = 0;
//                    HashMap<String,String> hashMap = new HashMap<>();
//                    hashMap.put("user_id",list.get(0).getUserId());
//                    hashMap.put("res_id",list.get(0).getResId());
//                    hashMap.put("order_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())));
//                    hashMap.put("order_price",String.valueOf(allMoney));
//                    for (ResBuyItemNum resBuyItemNum : list){
//                        num += resBuyItemNum.getBuyNum();
//                    }
//                    hashMap.put("order_description",list.get(0).getItemName()+"等"+num+"件商品");
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
//                            DataSupport.where("res_id = ? and user_id = ?",list.get(0).getResId(),list.get(0).getUserId()).find(ResBuyItemNum.class);
//                            DataSupport.where("res_id = ? and user_id = ?",list.get(0).getResId(),list.get(0).getUserId()).find(ResBuyCategoryNum.class);
//                            finish();
//                        }
//                    });
                }
                break;
            case R.id.rl_own_taken:
                showTimeSelectedDialog();
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

    private void showTimeSelectedDialog(){
        final Dialog dialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View view = LayoutInflater.from(this).inflate(R.layout.popup_time_from_bottom, null);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.time_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        PupupTimeAdapter adapter = new PupupTimeAdapter(this,timeList);
        adapter.setOnItemClickListener(new PupupTimeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                takenTime.setText(timeList.get(position));
                checkBox.setChecked(false);
                deliverMoney.setText("免");
                allMoneyTv.setText((allMoney - list.get(0).getResExtraMoney())+"");
                dialog.dismiss();
            }
        });
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
}
