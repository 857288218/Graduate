package com.example.rjq.myapplication.activity;

import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.rjq.myapplication.R;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar mToolbar;
    private static final String TAG = "life";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initListener();
        Log.d(TAG,"t onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"t onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"t onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"t onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"t onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"t onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"t onDestroy");
    }

    private void initView(){
        mToolbar = (Toolbar)findViewById(R.id.toolBar);
    }

    private void initListener(){

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }
}
