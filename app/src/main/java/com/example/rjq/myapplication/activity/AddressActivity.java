package com.example.rjq.myapplication.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.adapter.AddressAdapter;
import com.example.rjq.myapplication.bean.AddressBean;
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
                    setResult(RESULT_OK,new Intent().putExtra("address",list.get(position)));
                    finish();
                }
            });
        }

        addressAapter.setOnItemLongClickListener(new AddressAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(final int position) {
                AlertDialog alertDialog = new AlertDialog.Builder(AddressActivity.this)
                        .setTitle("设置")
                        .setMessage("确定将该地址设置成默认收货地址？")
                        .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AddressBean addressBean = new AddressBean();
                                addressBean.setToDefault("selected");
                                addressBean.updateAll("user_id = ?",String.valueOf(userId));

                                //刷新adapter
                                for(AddressBean address : list){
                                    address.setSelected(0);
                                }
                                list.get(position).setSelected(1);
                                list.get(position).updateAll("user_id = ? and address = ? and phone = ?",String.valueOf(userId),list.get(position).getAddress()
                                                            ,list.get(position).getPhone());
                                addressAapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.cancel),null)
                        .create();
                alertDialog.show();
                //设置Dialog中的文字样式
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.bottom_tab_text_selected_color));
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.bottom_tab_text_selected_color));
                TextView tvMsg = (TextView) alertDialog.findViewById(android.R.id.message);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.setMargins(0,getResources().getDimensionPixelSize(R.dimen.dimen_10dp),0,0);
                tvMsg.setLayoutParams(lp);
                tvMsg.setTextColor(getResources().getColor(R.color.color_666));
            }
        });

        addressAapter.setOnItemDeleteListener(new AddressAdapter.OnItemDeleteListener() {
            @Override
            public void onItemDelete(final int position) {
                //删除远程数据库中的该项地址信息
                hashMap.put("receiver_address",list.get(position).getAddress());
                hashMap.put("user_id",String.valueOf(userId));
                hashMap.put("name",list.get(position).getName());
                hashMap.put("phone",list.get(position).getPhone());
                HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH+HttpUtil.DELETE_USER_ADDRESS, hashMap, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG,e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseText = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    JSONObject jsonObject = new JSONObject(responseText);
                                    Toast.makeText(AddressActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                    if (jsonObject.getInt("status") == 1){
                                        //删除本地数据库中的该项地址信息
                                        DataSupport.deleteAll(AddressBean.class,"user_id = ? and address = ? and name = ? and phone = ?",
                                                String.valueOf(userId),list.get(position).getAddress(),list.get(position).getName(),list.get(position).getPhone());
                                        //删除某一项地址
                                        list.remove(position);
                                        addressAapter.notifyItemRemoved(position);
                                        addressAapter.notifyItemRangeChanged(0,list.size());
                                    }
                                }catch(JSONException e){

                                }
                            }
                        });
                    }
                });
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("onSaveInstancefunction","yes");
    }
}
