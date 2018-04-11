package com.example.rjq.myapplication.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.adapter.PopupAddressAdapter;
import com.example.rjq.myapplication.adapter.PopupPayWayAdapter;
import com.example.rjq.myapplication.bean.AddressBean;
import com.example.rjq.myapplication.bean.BuildingBean;
import com.example.rjq.myapplication.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddAddressActivity extends BaseActivity {
    @BindView(R.id.classify_title)
    TextView title;
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.name)
    EditText etName;
    @BindView(R.id.activity_add_address_submit_tv)
    TextView saveBtn;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_address)
    TextView etAddress;
    @BindView(R.id.et_num)
    EditText etNum;
    @BindView(R.id.ll_address)
    AutoRelativeLayout llAddress;
    @BindView(R.id.loading)
    ProgressBar progressBar;

    private String name;
    private String phone;
    private String address;
    private String num;
    private int sex = -1;
    private List<BuildingBean> addressList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        setStateBarColor(R.color.my_fragment_status_bar_color);
    }

    @Override
    protected void initView() {
        super.initView();
        //让软键盘延时弹出，以更好的加载Activity
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(etName, 0);
            }
        }, 300);
        title.setText("新增地址");
        backBtn.setOnClickListener(this);
        saveBtn.setOnClickListener(this);
        llAddress.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();
        //获取标志性建筑物
        HttpUtil.sendOkHttpGetRequest(HttpUtil.HOME_PATH+HttpUtil.OBTAIN_BUILDING, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("AddAddressActivity",e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try{
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    if (jsonObject.getInt("status") == 1){
                        addressList = new Gson().fromJson(jsonObject.getJSONArray("data").toString(),new TypeToken<List<BuildingBean>>(){}.getType());
                    }
                }catch (JSONException e){

                }

            }
        });
        //假数据
//        addressList.add(new BuildingBean("天津财经大学15号楼"));addressList.add(new BuildingBean("天津财经大学14号楼"));addressList.add(new BuildingBean("天津财经大学13号楼"));
//        addressList.add(new BuildingBean("天津财经大学12号楼"));addressList.add(new BuildingBean("天津财经大学11号楼"));addressList.add(new BuildingBean("园厅"));
//        addressList.add(new BuildingBean("韵达快递"));addressList.add(new BuildingBean("天津财经大学10号楼"));addressList.add(new BuildingBean("天津财经大学9号楼"));
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.activity_add_address_submit_tv:
                saveAddress();
                break;
            case R.id.ll_address:
                initAddressDialog();
                break;
        }
    }

    private void saveAddress(){
        name = etName.getText().toString();
        phone = etPhone.getText().toString();
        address = etAddress.getText().toString();
        num = etNum.getText().toString();
        if (radioGroup.getCheckedRadioButtonId() == R.id.man){
            sex = 1;
            if (!TextUtils.isEmpty(name)){
                name = name + "先生";
            }
        }else if (radioGroup.getCheckedRadioButtonId() == R.id.woman){
            sex = 0;
            if (!TextUtils.isEmpty(name)){
                name = name + "女士";
            }
        }
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address) || TextUtils.isEmpty(num) || sex == -1){
            Toast.makeText(this, "请填写完整信息!", Toast.LENGTH_SHORT).show();
        }else{
//            progressBar.setVisibility(View.VISIBLE);
            //将地址信息添加到本地数据库
//            final AddressBean addressBean = new AddressBean();
//            addressBean.setUser_id(PreferenceManager.getDefaultSharedPreferences(this).getInt("user_id",-1));
//            addressBean.setName(name);
//            addressBean.setPhone(phone);
//            addressBean.setAddress(address+" "+num);
//            addressBean.save();
            //将地址添加到远程数据库
            progressBar.setVisibility(View.VISIBLE);
            HashMap<String,String> hash = new HashMap<>();
            hash.put("user_id",String.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getInt("user_id",-1)));
            hash.put("build_name",address);
            hash.put("name",name);
            hash.put("phone",phone);
            hash.put("addr_detail",num);
            HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH+HttpUtil.ADD_USER_ADDRESS, hash, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(AddAddressActivity.this, "收货地址添加失败，请重新添加", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String responseText = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            try{
                                JSONObject object = new JSONObject(responseText);
                                if (object.getInt("status") == 1){
                                    Toast.makeText(AddAddressActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                                    AddressBean addressBean = new AddressBean();
                                    addressBean.setUser_id(PreferenceManager.getDefaultSharedPreferences(AddAddressActivity.this).getInt("user_id",-1));
                                    addressBean.setName(name);
                                    addressBean.setPhone(phone);
                                    addressBean.setAddress(address+" "+num);
                                    addressBean.save();
                                    Intent intent = new Intent();
                                    intent.putExtra("new_address",addressBean);
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }else{
                                    Toast.makeText(AddAddressActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                                }
                            }catch(JSONException e){

                            }
                        }
                    });
                }
            });
//            Intent intent = new Intent();
//            intent.putExtra("new_address",addressBean);
//            setResult(RESULT_OK,intent);
//            finish();
//            Toast.makeText(this, "新收货地址添加成功", Toast.LENGTH_SHORT).show();
        }
    }

    private void initAddressDialog(){
        final Dialog addressDialog = new Dialog(this,R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View view = LayoutInflater.from(this).inflate(R.layout.popup_address_from_bottom, null);
        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycler.setLayoutManager(linearLayoutManager);
        final PopupAddressAdapter adapter = new PopupAddressAdapter(this,addressList);
        adapter.setOnItemClickListener(new PopupAddressAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                progressBar.setVisibility(View.VISIBLE);
                addressDialog.dismiss();
                adapter.setSelected(position);
                new android.os.Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        etAddress.setText(addressList.get(position).getBuilderName());
                        progressBar.setVisibility(View.GONE);
                    }
                },500);

            }
        });
        recycler.setAdapter(adapter);

        //将布局设置给Dialog
        addressDialog.setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = addressDialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
        //设置dialog宽度满屏
        WindowManager m = dialogWindow.getWindowManager();
        Display d = m.getDefaultDisplay();
        lp.width = d.getWidth();
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        addressDialog.show();
    }
}
