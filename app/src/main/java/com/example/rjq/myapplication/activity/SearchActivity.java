package com.example.rjq.myapplication.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.adapter.ClassifyResActivityAdapter;
import com.example.rjq.myapplication.bean.ResBuyCategoryNum;
import com.example.rjq.myapplication.bean.ResDetailBean;
import com.example.rjq.myapplication.bean.SearchHistoricalBean;
import com.example.rjq.myapplication.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SearchActivity extends BaseActivity {
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.search_activity_hot_flowlayout)
    TagFlowLayout mHotFlowLayout;
    @BindView(R.id.search_activity_history_flowlayout)
    TagFlowLayout mHistoryFlowLayout;
    @BindView(R.id.search_et)
    EditText ETsearch;
    @BindView(R.id.activity_search_no_history)
    TextView TVnoHistory;
    @BindView(R.id.activity_search_clear_history)
    TextView TVclearHistory;
    @BindView(R.id.activity_search_hot_and_history_ll)
    AutoLinearLayout LLhotAndHistory;
    @BindView(R.id.search_btn)
    TextView searchBtn;
    @BindView(R.id.first_load)
    ProgressBar progressBar;
    @BindView(R.id.search_recycler)
    RecyclerView recyclerView;

    private TagAdapter<String> mHotFlowLayoutAdapter;
    private TagAdapter<SearchHistoricalBean> mHistoryFlowLayoutAdapter;
    private List<String> mHotData;
    private List<SearchHistoricalBean> mHistoryData;
    List<ResDetailBean> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setStateBarColor(R.color.colorPrimary);
    }

    @Override
    protected void initData() {
        super.initData();
        mHotData = new ArrayList<>();
        mHotData.add("麻辣烫");
        mHotData.add("小笼包");
        mHotData.add("重庆小面");
        mHotData.add("奶茶");
        mHotData.add("米线");
        mHotData.add("拉面");
        mHotData.add("水饺");
        mHotData.add("盖饭");
        mHotData.add("土豆粉");

        mHistoryData = DataSupport.order("time desc").find(SearchHistoricalBean.class);
    }

    @Override
    protected void initView() {
        super.initView();
        //让软键盘延时弹出，以更好的加载Activity
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(ETsearch, 0);
            }
        }, 300);

        if (mHistoryData.size() > 0){
            TVclearHistory.setVisibility(View.VISIBLE);
            TVnoHistory.setVisibility(View.GONE);
            mHistoryFlowLayout.setVisibility(View.VISIBLE);
        }else{
            TVclearHistory.setVisibility(View.GONE);
            TVnoHistory.setVisibility(View.VISIBLE);
            mHistoryFlowLayout.setVisibility(View.GONE);
        }
        initTagFlowLayout();
        //软键盘 搜索键 监听
        ETsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){

                    searchUiChange();
                    return true;
                }
                return false;
            }
        });
        TVclearHistory.setOnClickListener(this);
        searchBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.activity_search_clear_history:
                DataSupport.deleteAll(SearchHistoricalBean.class);
                mHistoryData.clear();
                TVnoHistory.setVisibility(View.VISIBLE);
                TVclearHistory.setVisibility(View.GONE);
                mHistoryFlowLayout.setVisibility(View.GONE);
                mHistoryFlowLayoutAdapter.notifyDataChanged();
                break;
            case R.id.search_btn:
                searchUiChange();
                break;
        }
    }

    private void initTagFlowLayout(){
        mHotFlowLayoutAdapter = new TagAdapter<String>(mHotData){
            @Override
            public View getView(com.zhy.view.flowlayout.FlowLayout parent, int position, String s)
            {
                //将tv.xml文件填充到标签内
                TextView tv = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.flow_layout_item,
                        mHotFlowLayout, false);
                //为标签设置对应的内容
                tv.setText(s);
                return tv;
            }
        };
        mHistoryFlowLayoutAdapter = new TagAdapter<SearchHistoricalBean>(mHistoryData) {
            @Override
            public View getView(FlowLayout parent, int position, SearchHistoricalBean searchHistoricalBean) {
                //将tv.xml文件填充到标签内
                TextView tv = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.flow_layout_item,
                        mHistoryFlowLayout, false);
                //为标签设置对应的内容
                tv.setText(searchHistoricalBean.getName());
                return tv;
            }
        };

        //为热门标签设置点击事件
        mHotFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, com.zhy.view.flowlayout.FlowLayout parent)
            {
                ETsearch.setText(mHotData.get(position));
                ETsearch.setSelection(ETsearch.getText().length());
                saveSearchHistoryBean(mHotData.get(position));
                mHistoryData = DataSupport.order("time desc").find(SearchHistoricalBean.class);
                initHistoryTag();
                search(mHotData.get(position));
                if (mHistoryFlowLayout.getVisibility() == View.GONE){
                    mHistoryFlowLayout.setVisibility(View.VISIBLE);
                    TVclearHistory.setVisibility(View.VISIBLE);
                    TVnoHistory.setVisibility(View.GONE);
                }
                return true;
            }
        });

        //为历史标签设置点击事件
        mHistoryFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener()
        {
            @Override
            public boolean onTagClick(View view, int position, com.zhy.view.flowlayout.FlowLayout parent)
            {
                ETsearch.setText(mHistoryData.get(position).getName());
                ETsearch.setSelection(ETsearch.getText().length());
                saveSearchHistoryBean(mHistoryData.get(position).getName());
                mHistoryData = DataSupport.order("time desc").find(SearchHistoricalBean.class);
                initHistoryTag();
                search(mHistoryData.get(position).getName());
                return true;
            }
        });

        mHotFlowLayout.setAdapter(mHotFlowLayoutAdapter);
        mHistoryFlowLayout.setAdapter(mHistoryFlowLayoutAdapter);
    }

    private void search(String content){
        //关闭软键盘
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        LLhotAndHistory.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("search_key",content);
        HttpUtil.sendOkHttpPostRequest("http://", hashMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("SearchActivity",e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                list = new Gson().fromJson(response.toString(), new TypeToken<List<ResDetailBean>>(){}.getType());
                notifyResBuyNum();
                final ClassifyResActivityAdapter adapter = new ClassifyResActivityAdapter(SearchActivity.this,list);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    private void notifyResBuyNum(){
        List<ResBuyCategoryNum> resBuyCategoryNumList = DataSupport.findAll(ResBuyCategoryNum.class);
        if (resBuyCategoryNumList.size() > 0){
            Hashtable<String,Integer> resBuyNumTable = new Hashtable<>();
            //将resBuyCategoryNumList中的添加到购物车的数量按resId设置给resBuyNumTable
            for (int i=0;i<list.size();i++){
                resBuyNumTable.put(String.valueOf(list.get(i).getResId()),0);
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
    }

    private void searchUiChange(){
        if (!TextUtils.isEmpty(ETsearch.getText().toString())){
            saveSearchHistoryBean(ETsearch.getText().toString());
            mHistoryData = DataSupport.order("time desc").find(SearchHistoricalBean.class);
            initHistoryTag();
            search(ETsearch.getText().toString());
            if (mHistoryFlowLayout.getVisibility() == View.GONE){
                mHistoryFlowLayout.setVisibility(View.VISIBLE);
                TVclearHistory.setVisibility(View.VISIBLE);
                TVnoHistory.setVisibility(View.GONE);
            }
        }
    }

    private void saveSearchHistoryBean(String name){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        SearchHistoricalBean searchHistoricalBean = new SearchHistoricalBean();
        searchHistoricalBean.setName(name);
        searchHistoricalBean.setTime(df.format(new Date()));
        List<SearchHistoricalBean> list = DataSupport.where("name = ?",name).find(SearchHistoricalBean.class);
        if (list.size()>0){
            searchHistoricalBean.updateAll("name=?",name);
        }else{
            searchHistoricalBean.save();
        }
    }

    private void initHistoryTag(){
        mHistoryFlowLayoutAdapter = new TagAdapter<SearchHistoricalBean>(mHistoryData) {
            @Override
            public View getView(FlowLayout parent, int position, SearchHistoricalBean searchHistoricalBean) {
                //将tv.xml文件填充到标签内
                TextView tv = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.flow_layout_item,
                        mHistoryFlowLayout, false);
                //为标签设置对应的内容
                tv.setText(searchHistoricalBean.getName());
                return tv;
            }
        };
        mHistoryFlowLayout.setAdapter(mHistoryFlowLayoutAdapter);
    }
}
