package com.example.rjq.myapplication.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.rjq.myapplication.R;

import butterknife.ButterKnife;

/**
 * Created by rjq on 2017/11/29 0029.
 */

public class BaseActivity extends AppCompatActivity {
    private boolean handleStatusBarColor = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (handleStatusBarColor){
            handleStateBar();
        }

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initData();
        initView();
    }

    protected void initView(){

    }

    protected void initData(){
        ButterKnife.bind(this);
    }

    private void handleStateBar(){
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.bottom_tab_text_normal_color));
            getWindow().setStatusBarColor(getResources().getColor(R.color.bottom_tab_text_normal_color));
        }
    }

    protected void setHandleStatusBarColor(boolean handleStatusBarColor){
        this.handleStatusBarColor = handleStatusBarColor;
    }
}
