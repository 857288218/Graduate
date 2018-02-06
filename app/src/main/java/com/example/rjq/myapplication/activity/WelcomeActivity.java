package com.example.rjq.myapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.bean.HomeDataBean;
import com.example.rjq.myapplication.util.HttpUtil;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WelcomeActivity extends AppCompatActivity {
    private HomeDataBean homeDataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //请求首页数据
//        HttpUtil.sendOkHttpGetRequest(HttpUtil.home_path + "", new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("WelcomeActivity",e.toString());
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                Gson gson = new Gson();
//                homeDataBean = gson.fromJson(response.body().string(),HomeDataBean.class);
//                Intent intent = new Intent(WelcomeActivity.this,MainActivity.class);
//                intent.putExtra("home_data",homeDataBean);
//                startActivity(intent);
//                finish();
//            }
//        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                finish();
            }
        }, 1000);
    }
}
