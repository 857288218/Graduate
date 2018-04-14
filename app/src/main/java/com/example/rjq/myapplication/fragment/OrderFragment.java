package com.example.rjq.myapplication.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.activity.LoginActivity;
import com.example.rjq.myapplication.adapter.OrderFragmentAdapter;
import com.example.rjq.myapplication.bean.OrderBean;
import com.example.rjq.myapplication.bean.ResBuyItemNum;
import com.example.rjq.myapplication.bean.UserBean;
import com.example.rjq.myapplication.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.rjq.myapplication.fragment.ThreeFragment.REQUEST_LOGIN;

/**
 * Created by rjq on 2018/2/7.
 */

public class OrderFragment extends Fragment {
    private String TAG = "OrderFragment";
    private View root;
    private Context mContext;
    @BindView(R.id.common_bar_title)
    TextView title;
    @BindView(R.id.order_fragment_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.first_load)
    ProgressBar progressBar;
    @BindView(R.id.list_empty)
    ImageView emptyImg;
    @BindView(R.id.login_btn)
    Button loginBtn;

    OrderFragmentAdapter adapter;
    List<OrderBean> orderList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.my_fragment_status_bar_color));
        }

        if (root == null){
            root = inflater.inflate(R.layout.order_fragment,container,false);
            initData();
            initView();
        }
        return root;
    }

    private void initData(){
        ButterKnife.bind(this,root);
        mContext = getActivity();
        title.setText(getResources().getString(R.string.list));
    }

    private void initView(){
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivityForResult(intent,REQUEST_LOGIN);
            }
        });
        if (DataSupport.findAll(UserBean.class).size() > 0){
            loginBtn.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(linearLayoutManager);
            requestListData();
        }else{
            recyclerView.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (DataSupport.findAll(UserBean.class).size() == 0){
            loginBtn.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            emptyImg.setVisibility(View.GONE);
        }else{
            initView();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode){
//            case REQUEST_LOGIN:
//                if (resultCode == Activity.RESULT_OK){
//                    initView();
//                }
//                break;
//        }
    }

    //请求订单数据
    private void requestListData(){
//        orderList = new ArrayList<>();
//        OrderBean orderBean = new OrderBean(1001,2,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg","瓦罐汤","2018-01-06 17:05",20.5,
//                "宫保鸡丁等3件商品");
//        OrderBean orderBean2 = new OrderBean(1001,4,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg","杭州小笼包","2018-02-06 18:07",10.5,
//                "小笼包2笼");
//        OrderBean orderBean3 = new OrderBean(1001,5,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg","北李妈妈菜","2018-02-12 20:05",120,
//                "鱼香肉丝等13件商品");
//        OrderBean orderBean4 = new OrderBean(1001,1,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic2b16zuj30ci08cwf4.jpg","杨国福麻辣烫","2018-02-12 12:08",18.6,
//                "麻辣烫一份");
//        OrderBean orderBean5 = new OrderBean(1001,8,"http://ww4.sinaimg.cn/large/006uZZy8jw1faic1xjab4j30ci08cjrv.jpg","重庆小面","2018-02-014 12:35",7,
//                "排骨面");
//        orderList.add(orderBean);orderList.add(orderBean2);orderList.add(orderBean3);orderList.add(orderBean4);orderList.add(orderBean5);
//        adapter = new OrderFragmentAdapter(mContext,orderList);
//        recyclerView.setAdapter(adapter);

        //请求数据
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("buyer_id",String.valueOf(PreferenceManager.getDefaultSharedPreferences(mContext).getInt("user_id",-1)));
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        emptyImg.setVisibility(View.GONE);
        HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH+HttpUtil.GET_ORDER, hashMap, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG,e.toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        emptyImg.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, "网络错误，请检查网络设置！", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String jsonString = response.body().string();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        try{
                            JSONObject jsonObject = new JSONObject(jsonString);
                            int status = jsonObject.getInt("status");
                            if (status != 0){
                                orderList = new Gson().fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<List<OrderBean>>(){}.getType());
//                                Log.d("OrderFragment",orderList.get(0).getOrderDetail().get(0).getItemName()+" "+);
                                if (orderList.size() == 0){
                                    recyclerView.setVisibility(View.GONE);
                                    emptyImg.setVisibility(View.VISIBLE);
                                }else{
                                    adapter = new OrderFragmentAdapter(mContext,orderList);
                                    recyclerView.setAdapter(adapter);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                            }else{
                                emptyImg.setVisibility(View.VISIBLE);
                                Toast.makeText(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e){
                            emptyImg.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });
    }

}
