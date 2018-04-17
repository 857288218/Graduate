package com.example.rjq.myapplication.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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

    private OrderFragmentAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private List<OrderBean> orderList;


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
        orderList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(mContext);
        adapter = new OrderFragmentAdapter(getActivity(),orderList);
        adapter.setOnItemBtnClickListener(new OrderFragmentAdapter.OnItemBtnClickListener() {
            @Override
            public void onItemBtnClick(int position, int state) {
                switch (state){
                    //订单取消
                    case 2:
                        progressBar.setVisibility(View.VISIBLE);
                        HashMap<String,String> hashMap = new HashMap<>();
                        hashMap.put("order_id",orderList.get(position).getOrderId());
                        hashMap.put("buyer_id",PreferenceManager.getDefaultSharedPreferences(mContext).getInt("user_id",-1)+"");
                        HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH+HttpUtil.ORDER_CANCEL, hashMap, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(mContext, "网络错误，请检查网络!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String responseText = response.body().string();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try{
                                            JSONObject jsonObject = new JSONObject(responseText);
                                            if (jsonObject.getInt("state") == 1){
                                                orderList.clear();
                                                orderList.addAll((List)new Gson().fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<List<OrderBean>>(){}.getType()));
                                                adapter.notifyDataSetChanged();
                                                Toast.makeText(mContext, "订单已取消!", Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(mContext, "失败!", Toast.LENGTH_SHORT).show();
                                            }
                                        }catch(JSONException e){
                                            Toast.makeText(mContext, "失败!", Toast.LENGTH_SHORT).show();
                                        }
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                            }
                        });
                        break;
                }
            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
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
            recyclerView.setLayoutManager(linearLayoutManager);
            requestListData();
        }else{
            recyclerView.setVisibility(View.GONE);
            emptyImg.setVisibility(View.GONE);
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
            loginBtn.setVisibility(View.GONE);
            requestListData();
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
                                orderList.clear();
                                orderList.addAll((List)new Gson().fromJson(jsonObject.getJSONArray("data").toString(), new TypeToken<List<OrderBean>>(){}.getType()));
                                if (orderList.size() == 0){
                                    recyclerView.setVisibility(View.GONE);
                                    emptyImg.setVisibility(View.VISIBLE);
                                }else{
                                    adapter.notifyDataSetChanged();
                                    recyclerView.setVisibility(View.VISIBLE);
                                    //置顶RecyclerView
                                    linearLayoutManager.scrollToPositionWithOffset(0,0);
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
