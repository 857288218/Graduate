package com.example.rjq.myapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.bean.UserBean;
import com.example.rjq.myapplication.util.HttpUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity {
    @BindView(R.id.title_right)
    TextView titleRight;
    @BindView(R.id.title_middle)
    TextView titleMiddle;
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.register_user_name)
    EditText registerUserName;
    @BindView(R.id.register_password)
    EditText registerPassword;
    @BindView(R.id.identify_code)
    EditText identifyCode;
    @BindView(R.id.register_btn)
    Button registerBtn;
    @BindView(R.id.get_identify_code)
    TextView getIdentifyCodeTv;
    @BindView(R.id.register_eye_iv)
    ImageView eyeIv;
    @BindView(R.id.identify_code_num)
    TextView identifyCodeNum;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private int a = 10000;

    private RegisterTextWatcher registerTextWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setStateBarColor(R.color.my_fragment_status_bar_color);
    }

    @Override
    protected void initData() {
        super.initData();
        registerTextWatcher = new RegisterTextWatcher();
    }

    @Override
    protected void initView() {
        super.initView();
        registerBtn.setEnabled(false);
        getIdentifyCodeTv.setEnabled(false);
        titleRight.setText(getResources().getString(R.string.login));
        titleMiddle.setText(getResources().getString(R.string.register));

        backBtn.setOnClickListener(this);
        getIdentifyCodeTv.setOnClickListener(RegisterActivity.this);
        registerBtn.setOnClickListener(this);
        eyeIv.setOnClickListener(this);
        registerUserName.addTextChangedListener(registerTextWatcher);
        registerPassword.addTextChangedListener(registerTextWatcher);
        identifyCode.addTextChangedListener(registerTextWatcher);
        titleRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch(v.getId()){
            case R.id.back_btn:
                finish();
                break;
            case R.id.get_identify_code:
                setCountDownTimer();
                break;
            case R.id.register_btn:
                register();
                break;
            case R.id.register_eye_iv:
                if (registerPassword.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()){
                    registerPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else{
                    registerPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                registerPassword.setSelection(registerPassword.getText().length());
                break;
            case R.id.title_right:
                finish();
                break;

        }
    }

    private void register(){
        String account = registerUserName.getText().toString().replaceAll(" ","");
        String pwd = registerPassword.getText().toString();
        if (Integer.parseInt(identifyCode.getText().toString()) != a){
            Toast.makeText(this, "验证码错误!", Toast.LENGTH_SHORT).show();
        }else{
            //注册
            progressBar.setVisibility(View.VISIBLE);
            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("account",account);
            hashMap.put("password",pwd);
            HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH+HttpUtil.REGISTER, hashMap, new Callback() {
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
                                JSONObject jsonObject = new JSONObject(responseText);
                                Toast.makeText(RegisterActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                            }catch (JSONException e){
                                Log.d("RegisterActivity",e.toString());
                            }

                        }
                    });
                }
            });
        }
    }

    class RegisterTextWatcher implements TextWatcher {
        int lastContentLength = 0;
        boolean isDelete = false;
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (!(registerUserName.getText().toString().equals("")||registerPassword.getText().toString().equals("")||identifyCode.getText().toString()
                    .equals(""))) {
                registerBtn.setEnabled(true);
                registerBtn.setBackground(getResources().getDrawable(R.drawable.login_selected_bg));
            }

            /**
             *
             *手机号分隔处理
             *
             */
            StringBuffer sb = new StringBuffer(registerUserName.getText().toString());
            //是否为输入状态
            isDelete = registerUserName.getText().toString().length() > lastContentLength ? false : true;

            //输入是第4，第9位，这时需要插入空格
            if(!isDelete&& (registerUserName.getText().toString().length() == 4||registerUserName.getText().toString().length() == 9)){
                if(registerUserName.getText().toString().length() == 4) {
                    sb.insert(3, " ");
                }else {
                    sb.insert(8, " ");
                }
                setContent(sb);
            }

            //删除的位置到4，9时，剔除空格
            if (isDelete && (registerUserName.getText().toString().length() == 4 || registerUserName.getText().toString().length() == 9)) {
                sb.deleteCharAt(sb.length() - 1);
                setContent(sb);
            }
            lastContentLength = sb.length();

        }

        /**
         * 添加或删除空格EditText的设置
         *
         * @param sb
         */
        private void setContent(StringBuffer sb) {
            registerUserName.setText(sb.toString());
            //移动光标到最后面
            registerUserName.setSelection(sb.length());
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().equals("")){
                registerBtn.setEnabled(false);
                registerBtn.setBackground(getResources().getDrawable(R.drawable.login_bg));
            }
            if (registerUserName.getText().toString().length() == 13 && getIdentifyCodeTv.getText().toString().equals("获取验证码")){
                getIdentifyCodeTv.setTextColor(getResources().getColor(R.color.bottom_tab_text_selected_color));
                getIdentifyCodeTv.setEnabled(true);
            }else{
                getIdentifyCodeTv.setEnabled(false);
                getIdentifyCodeTv.setTextColor(getResources().getColor(R.color.text_color_grey));
            }
        }
    }

    private void setCountDownTimer(){
        identifyCodeNum.setVisibility(View.VISIBLE);
        a = (int)(Math.random()*(9999-1000+1))+1000;//产生1000-9999的随机数
        identifyCodeNum.setText("验证码为:"+a);
        getIdentifyCodeTv.setEnabled(false);
        getIdentifyCodeTv.setTextColor(getResources().getColor(R.color.text_color_grey));
        new CountDownTimer(59000+50,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getIdentifyCodeTv.setText("已发送("+millisUntilFinished/1000+"秒)");
            }

            @Override
            public void onFinish() {
                if (registerUserName.getText().toString().length() == 13){
                    getIdentifyCodeTv.setEnabled(true);
                    getIdentifyCodeTv.setTextColor(getResources().getColor(R.color.bottom_tab_text_selected_color));
                }
                getIdentifyCodeTv.setText("获取验证码");
            }
        }.start();
    }

}
