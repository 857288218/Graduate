package com.example.rjq.myapplication.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.adapter.AddressAdapter;
import com.example.rjq.myapplication.bean.AddressBean;
import com.example.rjq.myapplication.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddressActivity extends BaseActivity {
    public static final String SELECTED_ADDRESS = "selected_address";
    public static final int ADD_ADDRESS = 5001;
    private static final String TAG = "AddressActivity";

    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.add_address)
    TextView addAddress;
    @BindView(R.id.address_recycler)
    RecyclerView recyclerView;

    private AddressAdapter addressAapter;
    private List<AddressBean> list;
    private int userId;
    private HashMap<String,String> hashMap;
    private Boolean isFromThreeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adress);
        setStateBarColor(R.color.my_fragment_status_bar_color);
    }

    @Override
    protected void initData() {
        super.initData();
        isFromThreeFragment = getIntent().getBooleanExtra("threefragment",false);
        userId = PreferenceManager.getDefaultSharedPreferences(this).getInt("user_id",-1);
        hashMap = new HashMap<>();
        hashMap.put("user_id",String.valueOf(userId));
        list = DataSupport.where("user_id = ?", String.valueOf(userId)).find(AddressBean.class);
    }

    @Override
    protected void initView() {
        super.initView();
        backBtn.setOnClickListener(this);
        addAddress.setOnClickListener(this);
        addAddress.setVisibility(View.VISIBLE);

        addressAapter = new AddressAdapter(this,list);
        if (!isFromThreeFragment){
            addressAapter.setOnItemClickListener(new AddressAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    //设置收货地址
                    AddressBean addressBean = new AddressBean();
                    addressBean.setToDefault("selected");
                    addressBean.updateAll("user_id = ?",String.valueOf(userId));

                    list.get(position).setSelected(1);
                    list.get(position).updateAll("user_id = ? and address = ?",String.valueOf(userId),list.get(position).getAddress());
                    finish();
                }
            });
        }
        addressAapter.setOnItemDeleteListener(new AddressAdapter.OnItemDeleteListener() {
            @Override
            public void onItemDelete(int position) {
                //删除本地数据库中的该项地址信息
                DataSupport.deleteAll(AddressBean.class,"user_id = ? and address = ?",String.valueOf(userId),list.get(position).getAddress());
                //删除某一项地址
                list.remove(position);
                addressAapter.notifyItemRemoved(position);
                addressAapter.notifyItemRangeChanged(0,list.size());
                //删除远程数据库中的该项地址信息
//                hashMap.put("receiver_address",list.get(position).getAddress());
//                HttpUtil.sendOkHttpPostRequest("http://", hashMap, new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Log.d(TAG,e.toString());
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        Log.d(TAG,response.body().toString());
//                    }
//                });
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(addressAapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.add_address:
                Intent intent = new Intent(this,AddAddressActivity.class);
                startActivityForResult(intent,ADD_ADDRESS);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case ADD_ADDRESS:
                if (resultCode == RESULT_OK){
                    list.add((AddressBean) data.getSerializableExtra("new_address"));
                    addressAapter.notifyDataSetChanged();
                }
                break;
        }
    }
}
