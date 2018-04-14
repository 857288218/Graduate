package com.example.rjq.myapplication.activity;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.adapter.CouponAdapter;
import com.example.rjq.myapplication.bean.CouponBean;
import com.example.rjq.myapplication.util.HttpUtil;
import com.example.rjq.myapplication.util.permission.PermissionFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class CouponActivity extends BaseActivity {
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.classify_title)
    TextView title;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private CouponAdapter adapter;
    private String resName;
    private List<CouponBean> list;
    private double allMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        setStateBarColor(R.color.my_fragment_status_bar_color);
    }

    @Override
    protected void initData() {
        super.initData();
        //假数据
        //        list = new ArrayList<>();
//        CouponBean bean1 = new CouponBean();
//        CouponBean bean2 = new CouponBean();
//        bean1.setDeadline("2018.4.10");bean1.setIscommon(1);bean1.setMiniPrice(35);bean1.setRedPaperId(1);bean1.setPrice(7);
//        bean2.setDeadline("2018.3.30");bean2.setIscommon(1);bean2.setMiniPrice(40);bean2.setRedPaperId(2);bean2.setPrice(10);
//        list.add(bean1);list.add(bean2);

        resName = getIntent().getStringExtra("res_name");
        allMoney = getIntent().getDoubleExtra("all_money",0);
        //得到用户还未使用的红包
        progressBar.setVisibility(View.VISIBLE);
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("shop_id",getIntent().getStringExtra("res_id"));
        hashMap.put("buyer_id", String.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getInt("user_id",-1)));
        HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH+HttpUtil.OBTAIN_USER_RED_PACKET_BY_SHOP, hashMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("CouponActivity",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                list = new Gson().fromJson((responseText),new TypeToken<List<CouponBean>>(){}.getType());
                adapter = new CouponAdapter(CouponActivity.this,resName,allMoney,list);
                adapter.setOnUseBtnClickListener(new CouponAdapter.OnUseBtnClickListener() {
                    @Override
                    public void useBtnClickListener(int position, CouponBean couponBean) {
                        setResult(RESULT_OK,new Intent().putExtra("coupon",couponBean));
                        finish();
                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(CouponActivity.this));
                        progressBar.setVisibility(View.GONE);
                    }
                });

            }
        });

    }

    @Override
    protected void initView() {
        super.initView();
        title.setText("红包");
        backBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
        }
    }
}
