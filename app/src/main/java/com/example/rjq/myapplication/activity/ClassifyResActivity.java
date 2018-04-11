package com.example.rjq.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.adapter.ClassifyResActivityAdapter;
import com.example.rjq.myapplication.adapter.MultipleOrderPopWinAdapter;
import com.example.rjq.myapplication.bean.ResBuyCategoryNum;
import com.example.rjq.myapplication.bean.ResDetailBean;
import com.example.rjq.myapplication.fragment.OneFragment;
import com.example.rjq.myapplication.util.HttpUtil;
import com.example.rjq.myapplication.view.MultipleOrderPopupWindow;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.autolayout.AutoLinearLayout;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ClassifyResActivity extends BaseActivity {
    private static String TAG = "ClassifyResActivity";
    public static final String RES_CLASSIFY = "res_classify";
    public static final String RES_ORDER_MODE = "res_order_mode";

    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.classify_title)
    TextView title;
    @BindView(R.id.search)
    ImageButton searchBtn;
    @BindView(R.id.classify_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.first_load)
    ProgressBar progressBar;
    @BindView(R.id.order)
    AutoLinearLayout order;
    @BindView(R.id.order_multiple_order)
    TextView multipleOrder;
    @BindView(R.id.order_good_common)
    TextView goodCommon;
    @BindView(R.id.order_short_distance)
    TextView shortDistance;
    @BindView(R.id.order_filter)
    TextView filter;
    @BindView(R.id.divider)
    View view;
    @BindView(R.id.empty_view)
    ImageView emptyView;

    private ClassifyResActivityAdapter adapter;
    private List<ResDetailBean> list;
    private LinearLayoutManager linearLayoutManager;
    private String resClassify;
    private MultipleOrderPopupWindow popWin;
    private int selectedFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_res);
        setStateBarColor(R.color.my_fragment_status_bar_color);
    }

    @Override
    protected void initData() {
        super.initData();
        resClassify = getIntent().getStringExtra(RES_CLASSIFY);

        list = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(ClassifyResActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new ClassifyResActivityAdapter(ClassifyResActivity.this,list);
        recyclerView.setAdapter(adapter);
        //刚进入界面请求商家列表数据
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("label_name",resClassify);
        notifyResList(hashMap);

        final List<String> list = new ArrayList<>();
        list.add(getResources().getString(R.string.multiple_order));list.add(getResources().getString(R.string.sale_highest));
        list.add(getResources().getString(R.string.deliver_price_lowest));list.add(getResources().getString(R.string.deliver_time_lowest));
        list.add(getResources().getString(R.string.extra_price_lowest));
        popWin = new MultipleOrderPopupWindow(this, list, new MultipleOrderPopWinAdapter.OnMultipleOrderItemClickListener() {
            @Override
            public void onMultipleOrderItemClick(int position) {
                popWin.setSelectedPosition(position);
                popWin.dismiss();
                multipleOrder.setText(list.get(position));
                //重新请求商家列表数据
//                HashMap<String,String> hashMap = new HashMap<>();
//                hashMap.put(RES_CLASSIFY,resClassify);
//                hashMap.put(RES_ORDER_MODE,list.get(position));
//                notifyResList(hashMap);
            }
        });

        popWin.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                int selectedPosition = popWin.getSelectedPosition();
                if (selectedPosition == -1){
                    multipleOrder.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.down_grey),null);
                    multipleOrder.setTextColor(getResources().getColor(R.color.color_666));
                    if (selectedFlag == 1){
                        goodCommon.setTextColor(getResources().getColor(R.color.black));
                        shortDistance.setTextColor(getResources().getColor(R.color.color_666));
                    }else if (selectedFlag == 2){
                        goodCommon.setTextColor(getResources().getColor(R.color.color_666));
                        shortDistance.setTextColor(getResources().getColor(R.color.black));
                    }
                }else{
                    multipleOrder.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.down_black),null);
                    multipleOrder.setTextColor(getResources().getColor(R.color.black));
                    goodCommon.setTextColor(getResources().getColor(R.color.color_666));
                    shortDistance.setTextColor(getResources().getColor(R.color.color_666));
                }
            }
        });

    }

    @Override
    protected void initView() {
        super.initView();
        backBtn.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
        multipleOrder.setOnClickListener(this);
        shortDistance.setOnClickListener(this);
        goodCommon.setOnClickListener(this);

        title.setText(getIntent().getStringExtra(OneFragment.RES_TITLE));
        searchBtn.setVisibility(View.VISIBLE);

//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        adapter = new ClassifyResActivityAdapter(this,list);
//        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        notifyResBuyNum();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.search:
                Intent intent = new Intent(this,SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.order_good_common:
//                HashMap<String,String> hashMap = new HashMap<>();
//                hashMap.put(RES_CLASSIFY,resClassify);
//                hashMap.put(RES_ORDER_MODE,"order_by_comment");
//                notifyResList(hashMap);
                goodCommon.setTextColor(getResources().getColor(R.color.black));
                shortDistance.setTextColor(getResources().getColor(R.color.color_666));
                multipleOrder.setText(getResources().getString(R.string.multiple_order));
                multipleOrder.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.down_grey),null);
                multipleOrder.setTextColor(getResources().getColor(R.color.color_666));
                popWin.setSelectedPosition(-1);
                selectedFlag = 1;
                break;
            case R.id.order_short_distance:
//                HashMap<String,String> disHashMap = new HashMap<>();
//                disHashMap.put(RES_CLASSIFY,resClassify);
//                disHashMap.put(RES_ORDER_MODE,"order_by_distance");
//                notifyResList(disHashMap);
                goodCommon.setTextColor(getResources().getColor(R.color.color_666));
                shortDistance.setTextColor(getResources().getColor(R.color.black));
                multipleOrder.setText(getResources().getString(R.string.multiple_order));
                multipleOrder.setTextColor(getResources().getColor(R.color.color_666));
                multipleOrder.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.down_grey),null);
                popWin.setSelectedPosition(-1);
                selectedFlag = 2;
                break;
            case R.id.order_multiple_order:
                popWin.showAsDropDown(view);
                multipleOrder.setTextColor(getResources().getColor(R.color.bottom_tab_text_selected_color));
                multipleOrder.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.down_selected),null);
                goodCommon.setTextColor(getResources().getColor(R.color.color_666));
                shortDistance.setTextColor(getResources().getColor(R.color.color_666));
                break;
        }
    }

    private void notifyResList(HashMap<String,String> hashMap){
        progressBar.setVisibility(View.VISIBLE);
        order.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.GONE);
        HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH+HttpUtil.OBTAIN_SHOP_BY_LABEL, hashMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        emptyView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ClassifyResActivity.this, "连接超时，请检查网络设置!", Toast.LENGTH_SHORT).show();
                    }
                });
                Log.d(TAG,e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                list.clear();
                list.addAll((List)new Gson().fromJson(response.body().string(), new TypeToken<List<ResDetailBean>>(){}.getType()));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //请求完数据在UI线程更新ui
                        progressBar.setVisibility(View.GONE);
                        if (list.size() ==0 || list == null){
                            order.setVisibility(View.GONE);
                            emptyView.setVisibility(View.VISIBLE);
                        }else{
                            notifyResBuyNum();
                            order.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
    }

    private void notifyResBuyNum(){
        List<ResBuyCategoryNum> resBuyCategoryNumList = DataSupport.findAll(ResBuyCategoryNum.class);
        List<String> resIdList = new ArrayList<>();
        if (list != null){
            if (resBuyCategoryNumList != null && resBuyCategoryNumList.size() > 0){
                Hashtable<String,Integer> resBuyNumTable = new Hashtable<>();
                //将resBuyCategoryNumList中的添加到购物车的数量按resId设置给resBuyNumTable
                for (int i=0;i<list.size();i++){
                    resBuyNumTable.put(String.valueOf(list.get(i).getResId()),0);
                    resIdList.add(list.get(i).getResId()+"");
                }
                for (ResBuyCategoryNum resBuyCategoryNum : resBuyCategoryNumList){
                    if (resIdList.contains(resBuyCategoryNum.getResId())){
                        int num = resBuyNumTable.get(resBuyCategoryNum.getResId()) + resBuyCategoryNum.getBuyNum();
                        resBuyNumTable.put(resBuyCategoryNum.getResId(),num);
                    }
                }
                //得到resBuyNumTable中的keyList
                List<String> keyList = new ArrayList<>();
                Iterator<String> itr = resBuyNumTable.keySet().iterator();
                while (itr.hasNext()){
                    String str = itr.next();
                    keyList.add(str);
                }
                //遍历设置homeRecResDetailList各项的buyNum
                for (int i=0;i<list.size();i++){
                    for (int j=0;j<keyList.size();j++){
                        if (list.get(i).getResId() == Integer.parseInt(keyList.get(j))){
                            list.get(i).setBuyNum(resBuyNumTable.get(keyList.get(j)));
                        }
                    }
                }

            }else {
                for (int i=0;i<list.size();i++){
                    list.get(i).setBuyNum(0);
                }
            }
            //刷新商家列表，显示红点数量
            adapter.notifyDataSetChanged();
        }
    }

}
