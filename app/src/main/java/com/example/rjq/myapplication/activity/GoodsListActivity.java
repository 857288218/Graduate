package com.example.rjq.myapplication.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.rjq.myapplication.Bean.GoodsListItemBean;
import com.example.rjq.myapplication.adapter.GoodsListItemAdapter;
import com.example.rjq.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class GoodsListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private GoodsListItemAdapter goodsListItemAdapter;
    private List<GoodsListItemBean> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        initData();
        initView();

    }

    private void initView(){
        mRecyclerView = (RecyclerView)findViewById(R.id.goods_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        goodsListItemAdapter = new GoodsListItemAdapter(mData);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(goodsListItemAdapter);
    }

    private void initData(){
        mData = new ArrayList<>();
        GoodsListItemBean bean = new GoodsListItemBean();
        bean.setGoodsDescribe("农家现挖红心红薯地瓜小香薯粉糯香甜5斤");
        bean.setGoodsPrice("9.9");
        bean.setImg(R.mipmap.fruitall);
        for (int i=0;i<10;i++){
            mData.add(bean);
        }

    }
}
