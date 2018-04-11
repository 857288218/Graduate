package com.example.rjq.myapplication.activity;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.bean.UserBean;
import com.example.rjq.myapplication.util.HttpUtil;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AlterPhoneActivity extends BaseActivity {
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.classify_title)
    TextView title;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.get_identify_code)
    TextView getIdentify;
    @BindView(R.id.identify_code)
    TextView identifyCode;
    @BindView(R.id.alter_identify_code)
    EditText alterIdentifyCode;
    @BindView(R.id.identify_btn)
    Button identifyBtn;
    @BindView(R.id.confirm_btn)
    Button confirmBtn;
    @BindView(R.id.new_phone_code_ll)
    AutoLinearLayout newPhoneLl;
    @BindView(R.id.new_phone)
    EditText newPhoneEt;
    @BindView(R.id.new_phone_get_identify_code)
    TextView newPhoneGetIdentifyCode;
    @BindView(R.id.new_phone_alter_identify_code)
    EditText newPhoneAlterIdentifyCode;
    @BindView(R.id.login_by_identify_code_ll)
    AutoLinearLayout identifyCodeLl;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private int a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_phone);
        setStateBarColor(R.color.my_fragment_status_bar_color);
    }

    @Override
    protected void initView() {
        super.initView();
        title.setText("换绑手机");
        phone.setText(getIntent().getStringExtra("phone"));
        backBtn.setOnClickListener(this);
        getIdentify.setOnClickListener(this);
        identifyBtn.setOnClickListener(this);
        newPhoneGetIdentifyCode.setOnClickListener(this);
        confirmBtn.setOnClickListener(this);
        alterIdentifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!alterIdentifyCode.getText().toString().equals("")) {
                    identifyBtn.setEnabled(true);
                    identifyBtn.setBackground(getResources().getDrawable(R.drawable.login_selected_bg));
                }else{
                    identifyBtn.setEnabled(false);
                    identifyBtn.setBackground(getResources().getDrawable(R.drawable.login_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        newPhoneEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!newPhoneEt.getText().toString().equals("") && !newPhoneAlterIdentifyCode.getText().toString().equals("")){
                    confirmBtn.setEnabled(true);
                    confirmBtn.setBackground(getResources().getDrawable(R.drawable.login_selected_bg));
                }else{
                    confirmBtn.setEnabled(false);
                    confirmBtn.setBackground(getResources().getDrawable(R.drawable.login_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (newPhoneEt.getText().toString().length() == 11 && newPhoneGetIdentifyCode.getText().toString().equals("获取验证码")) {
                    newPhoneGetIdentifyCode.setTextColor(getResources().getColor(R.color.bottom_tab_text_selected_color));
                    newPhoneGetIdentifyCode.setEnabled(true);
                } else {
                    newPhoneGetIdentifyCode.setEnabled(false);
                    newPhoneGetIdentifyCode.setTextColor(getResources().getColor(R.color.text_color_grey));
                }
            }
        });

        newPhoneAlterIdentifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!newPhoneEt.getText().toString().equals("") && !newPhoneAlterIdentifyCode.getText().toString().equals("")){
                    confirmBtn.setEnabled(true);
                    confirmBtn.setBackground(getResources().getDrawable(R.drawable.login_selected_bg));
                }else{
                    confirmBtn.setEnabled(false);
                    confirmBtn.setBackground(getResources().getDrawable(R.drawable.login_bg));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
            case R.id.get_identify_code:
                setCountDownTimer();
                break;
            case R.id.new_phone_get_identify_code:
                if (newPhoneEt.getText().toString().equals(DataSupport.findAll(UserBean.class).get(0).getUserPhone())){
                    Toast.makeText(this, "新手机号不能与老手机号相同!", Toast.LENGTH_SHORT).show();
                }else{
                    setCountDownTimer();
                }
                break;
            case R.id.identify_btn:
                if (Integer.parseInt(alterIdentifyCode.getText().toString()) == a){
                    newPhoneLl.setVisibility(View.VISIBLE);
                    confirmBtn.setVisibility(View.VISIBLE);
                    identifyCodeLl.setVisibility(View.GONE);
                    identifyBtn.setVisibility(View.GONE);
                    identifyCode.setVisibility(View.GONE);
                }else{
                    Toast.makeText(this, "验证码错误!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.confirm_btn:
                if (Integer.parseInt(newPhoneAlterIdentifyCode.getText().toString()) == a){
                    changePhone();
                }else{
                    Toast.makeText(this, "验证码错误!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void changePhone(){
        progressBar.setVisibility(View.VISIBLE);
        int userId = PreferenceManager.getDefaultSharedPreferences(this).getInt("user_id",-1);
        HashMap<String,String> hash = new HashMap<>();
        hash.put("user_id",String.valueOf(userId));
        hash.put("phone",newPhoneEt.getText().toString());
        HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH+HttpUtil.SAVE_USER_PHONE, hash, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        try{
                            JSONObject json = new JSONObject(responseText);
                            int status = json.getInt("status");
                            final String msg = json.getString("msg");
                            if (status == 1){
                                AlterPhoneActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AlterPhoneActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        DataSupport.deleteAll(UserBean.class);
                                        finish();
                                    }
                                });
                            }else{
                                new android.os.Handler(getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(AlterPhoneActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }catch (JSONException e){

                        }
                    }
                });

            }
        });

    }

    private void setCountDownTimer() {
        identifyCode.setVisibility(View.VISIBLE);
        a = (int) (Math.random() * (9999 - 1000 + 1)) + 1000;//产生1000-9999的随机数
        identifyCode.setText("验证码为:" + a);
        getIdentify.setEnabled(false);
        getIdentify.setTextColor(getResources().getColor(R.color.text_color_grey));
        new CountDownTimer(59000 + 50, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getIdentify.setText("已发送(" + millisUntilFinished / 1000 + "秒)");
            }

            @Override
            public void onFinish() {
                getIdentify.setTextColor(getResources().getColor(R.color.bottom_tab_text_selected_color));
                getIdentify.setText("获取验证码");
                getIdentify.setEnabled(true);
            }
        }.start();
    }
}
