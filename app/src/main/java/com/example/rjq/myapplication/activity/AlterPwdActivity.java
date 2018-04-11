package com.example.rjq.myapplication.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.bean.UserBean;
import com.example.rjq.myapplication.util.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AlterPwdActivity extends BaseActivity {
    @BindView(R.id.back_btn)
    ImageButton bakBtn;
    @BindView(R.id.title_middle)
    TextView titleMiddle;
    @BindView(R.id.title_right)
    TextView titleRight;
    @BindView(R.id.activity_set_password_now_pwd_et)
    EditText nowPwdEt;
    @BindView(R.id.activity_set_password_new_pwd_et)
    EditText newPwdEt;
    @BindView(R.id.activity_set_password_repeat_new_pwd_et)
    EditText repeatNewPwdEt;
    @BindView(R.id.activity_set_password_submit_tv)
    TextView submitTv;
    @BindView(R.id.activity_set_password_get_identify_code)
    TextView getIdentifyCodeTv;
    @BindView(R.id.identify_code)
    TextView identifyCode;

    private int a = 1000000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_pwd_acticity);
        setStateBarColor(R.color.my_fragment_status_bar_color);
    }

    @Override
    protected void initView() {
        super.initView();
        titleMiddle.setText(getResources().getString(R.string.alter_pwd));
        titleRight.setVisibility(View.GONE);
        bakBtn.setOnClickListener(this);
        submitTv.setOnClickListener(this);
        getIdentifyCodeTv.setOnClickListener(this);
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
            case R.id.activity_set_password_submit_tv:
                setPwd();
                break;
            case R.id.activity_set_password_get_identify_code:
                setCountDownTimer();
                break;
        }
    }

    private void setPwd(){
        if (nowPwdEt.getText().toString().equals("") || newPwdEt.getText().toString().equals("") || repeatNewPwdEt.getText().toString().equals("")){
            Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
        }else{
            if (Integer.parseInt(nowPwdEt.getText().toString()) != a){
                Toast.makeText(this, "验证码不正确", Toast.LENGTH_SHORT).show();
            }else{
                if (!newPwdEt.getText().toString().equals(repeatNewPwdEt.getText().toString())){
                    Toast.makeText(this, "两次输入的新密码不一致", Toast.LENGTH_SHORT).show();
                }else{
                    List<UserBean> list = DataSupport.findAll(UserBean.class);
                    if (newPwdEt.getText().toString().equals(list.get(0).getPassword())){
                        Toast.makeText(this, "新密码不能与当前密码相同", Toast.LENGTH_SHORT).show();
                    }else{
                        if (newPwdEt.getText().toString().length()<6){
                            Toast.makeText(this, "新密码必须大于6位！", Toast.LENGTH_SHORT).show();
                        }else{
                            //存入远程数据库
                            HashMap<String,String> hashMap = new HashMap<>();
                            hashMap.put("user_phone",list.get(0).getUserPhone());
                            hashMap.put("user_id", String.valueOf(PreferenceManager.getDefaultSharedPreferences(AlterPwdActivity.this).getInt("user_id",-1)));
                            hashMap.put("password",newPwdEt.getText().toString());
                            HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH+HttpUtil.ALTER_USER_PWD, hashMap, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.d("AlterPwdActivity",e.toString());
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    final String s = response.body().string();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try{
                                                JSONObject object = new JSONObject(s);
                                                Toast.makeText(AlterPwdActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                                                DataSupport.deleteAll(UserBean.class);
                                                finish();
                                            }catch (JSONException e){
                                                Log.d("AlterPwdActivity",e.getMessage());
                                            }
                                        }
                                    });

                                }
                            });
                        }
                    }
                }
            }
        }
    }

    private void setCountDownTimer(){
        identifyCode.setVisibility(View.VISIBLE);
        a = (int)(Math.random()*(9999-1000+1))+1000;//产生1000-9999的随机数
        identifyCode.setText("验证码为:"+a);
        getIdentifyCodeTv.setEnabled(false);
        getIdentifyCodeTv.setTextColor(getResources().getColor(R.color.text_color_grey));
        new CountDownTimer(59000+50,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getIdentifyCodeTv.setText("已发送("+millisUntilFinished/1000+"秒)");
            }

            @Override
            public void onFinish() {
                getIdentifyCodeTv.setEnabled(true);
                getIdentifyCodeTv.setTextColor(getResources().getColor(R.color.bottom_tab_text_selected_color));
                getIdentifyCodeTv.setText("获取验证码");
            }
        }.start();
    }
}
