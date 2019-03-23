package com.example.rjq.myapplication.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.bean.ResDetailBean;
import com.example.rjq.myapplication.bean.WelcomeBean;
import com.example.rjq.myapplication.fragment.OneFragment;
import com.example.rjq.myapplication.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WelcomeActivity extends AppCompatActivity {
    private List<ResDetailBean> homeRecResDetailList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        intent.putExtra("data","");
        startActivity(intent);
        finish();
//        HttpUtil.sendOkHttpGetRequest(HttpUtil.HOME_PATH+HttpUtil.OBTAIN_RECOMMEND_SHOP, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.d("oneFragment",e.getMessage());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(WelcomeActivity.this, "网络连接错误，请输入正确的服务器地址!", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(WelcomeActivity.this, ConfigActivity.class);
//                        startActivity(intent);
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                //这行代码也要放在子线程中执行，不能放在主线程中，response.body()还会进行网络请求
//                String responseText = response.body().string();
//                Log.d("WelcomeActivity",responseText.toString());
//                //请求首页数据
//                try{
//                    final WelcomeBean welcomeBean = new Gson().fromJson(responseText,WelcomeBean.class);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//                            intent.putExtra("data",welcomeBean);
//                            startActivity(intent);
//                            finish();
//                        }
//                    });
//                }catch(Exception e){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(WelcomeActivity.this, "网络连接错误，请输入正确的服务器地址!", Toast.LENGTH_SHORT).show();
//                            startActivity(new Intent(WelcomeActivity.this,ConfigActivity.class));
//                        }
//                    });
//
//                }
//
//            }
//        });
    }
}
