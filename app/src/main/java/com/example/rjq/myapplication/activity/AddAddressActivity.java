package com.example.rjq.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.bean.AddressBean;
import com.example.rjq.myapplication.util.HttpUtil;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
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
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.et_num)
    EditText etNum;

    private String name;
    private String phone;
    private String address;
    private String num;
    private int sex = -1;

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
    }

    @Override
    protected void initData() {
        super.initData();


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
        }
    }

    private void saveAddress(){
        name = etName.getText().toString();
        phone = etPhone.getText().toString();
        address = etAddress.getText().toString();
        num = etNum.getText().toString();
        if (radioGroup.getCheckedRadioButtonId() == R.id.man){
            sex = 1;
        }else if (radioGroup.getCheckedRadioButtonId() == R.id.woman){
            sex = 0;
        }
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address) || TextUtils.isEmpty(num) || sex == -1){
            Toast.makeText(this, "请填写完整信息!", Toast.LENGTH_SHORT).show();
        }else{
            //将地址信息添加到本地数据库
            AddressBean addressBean = new AddressBean();
            addressBean.setUser_id(PreferenceManager.getDefaultSharedPreferences(this).getInt("user_id",-1));
            addressBean.setName(name);
            addressBean.setPhone(phone);
            addressBean.setAddress(address+" "+num);
            addressBean.save();
            //将地址添加到远程数据库
//            HashMap<String,String> hash = new HashMap<>();
//            hash.put("user_id",String.valueOf(PreferenceManager.getDefaultSharedPreferences(this).getInt("user_id",-1)));
//            HttpUtil.sendOkHttpPostRequest("http://", hash, new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//
//                }
//            });
            Intent intent = new Intent();
            intent.putExtra("new_address",addressBean);
            setResult(RESULT_OK,intent);
            finish();
            Toast.makeText(this, "新收货地址添加成功", Toast.LENGTH_SHORT).show();
        }
    }
}
