package com.example.rjq.myapplication.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.bean.AddressBean;
import com.example.rjq.myapplication.bean.UserBean;
import com.example.rjq.myapplication.fragment.ThreeFragment;
import com.example.rjq.myapplication.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.zhy.autolayout.AutoLinearLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.login_user_name)
    EditText userName;
    @BindView(R.id.login_phone)
    EditText loginPhoneEt;
    @BindView(R.id.login_password)
    EditText password;
    @BindView(R.id.login_identify_code)
    EditText loginIdentifyCodeEt;
    @BindView(R.id.login_clear_iv)
    ImageView clearIv;
    @BindView(R.id.login_clear_iv2)
    ImageView clearIv2;
    @BindView(R.id.login_eye_iv)
    ImageView eyeIv;
    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.login_by_identify_code)
    TextView loginByIdentifyCodeTv;
    @BindView(R.id.login_by_password)
    TextView loginByPassword;
    @BindView(R.id.login_by_password_ll)
    AutoLinearLayout loginByPasswordLl;
    @BindView(R.id.login_by_identify_code_ll)
    AutoLinearLayout loginByIdentifyCodeLl;
    @BindView(R.id.login_get_identify_code)
    TextView loginGetIdentifyCodeTv;
    @BindView(R.id.login_not)
    TextView loginNotTv;
    @BindView(R.id.title_middle)
    TextView titleMiddle;
    @BindView(R.id.title_right)
    TextView titleRight;
    @BindView(R.id.identify_code)
    TextView identifyCode;
    @BindView(R.id.first_load)
    ProgressBar progressBar;

    private LoginTextWatcher loginTextWatcher;
    private int a = 1000000000;
    List<AddressBean> addressBeanList;
    private LocalBroadcastManager localBroadcastManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setStateBarColor(R.color.my_fragment_status_bar_color);
    }

    @Override
    protected void initData() {
        super.initData();
        loginTextWatcher = new LoginTextWatcher();
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void initView() {
        super.initView();
        //让软键盘延时弹出，以更好的加载Activity
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(userName, 0);
            }
        }, 300);
        titleMiddle.setText(getResources().getString(R.string.login));
        titleRight.setText(getResources().getString(R.string.register));

        loginBtn.setOnClickListener(this);
        clearIv.setOnClickListener(this);
        clearIv2.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        eyeIv.setOnClickListener(this);
        titleRight.setOnClickListener(this);
        loginByIdentifyCodeTv.setOnClickListener(this);
        loginNotTv.setOnClickListener(this);
        loginByPassword.setOnClickListener(this);
        loginGetIdentifyCodeTv.setOnClickListener(this);
        loginNotTv.setOnClickListener(this);
        userName.addTextChangedListener(loginTextWatcher);
        password.addTextChangedListener(loginTextWatcher);
        loginPhoneEt.addTextChangedListener(loginTextWatcher);
        loginIdentifyCodeEt.addTextChangedListener(loginTextWatcher);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.login_btn:
                login();
                break;
            case R.id.back_btn:
                finish();
                break;
            case R.id.login_clear_iv:
                clearEditText(userName);
                break;
            case R.id.login_clear_iv2:
                clearEditText(loginPhoneEt);
                break;
            case R.id.login_eye_iv:
                if (password.getTransformationMethod() == HideReturnsTransformationMethod.getInstance()) {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                password.setSelection(password.getText().length());
                break;
            case R.id.title_right:
                //启动注册页
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.login_by_identify_code:
                loginWay();
                break;
            case R.id.login_by_password:
                loginWay();
                break;
            case R.id.login_get_identify_code:
                setCountDownTimer();
                break;
            case R.id.login_not:
                //忘记密码
                AlertDialog builder = new AlertDialog.Builder(this)
                        .setTitle("修改登陆密码流程")
                        .setMessage("通过手机验证码登陆->进入我的界面->修改登录密码")
                        .setPositiveButton(R.string.yes, null)
                        .create();
                builder.show();

                break;

        }
    }

    private void login() {
        HashMap<String, String> hash = new HashMap<>();
        //密码登陆
        if (loginByPasswordLl.getVisibility() == View.VISIBLE) {
            if (userName.getText().toString().equals("get conf ig")){
                startActivity(new Intent(this,ConfigActivity.class));
            }else{
                progressBar.setVisibility(View.VISIBLE);
                hash.put("user_tel",userName.getText().toString().replace(" ", ""));
                hash.put("password",password.getText().toString());
                //用户名密码校验(实现)
                HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH+HttpUtil.LOGIN_BY_PWD, hash, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d("LoginActivity",e.toString());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "登陆失败，请检查网络!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
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
                                    final JSONObject jsonObject = new JSONObject(responseText);
                                    int state = jsonObject.getInt("status");
                                    String msg = jsonObject.getString("msg");
                                    if (state == 1){
                                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        //保存用户信息
                                        UserBean userBean = new Gson().fromJson(jsonObject.getJSONObject("user_info").toString(),UserBean.class);
                                        userBean.save();
                                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                                        editor.putInt("user_id", userBean.getUserId());
                                        editor.commit();
//                                        new Thread(new Runnable() {
//                                            @Override
//                                            public void run() {
//                                                Intent intent = new Intent("com.rjq");
//                                                sendBroadcast(intent);
//                                                for (int i=0;i<8000;i++)
//                                                    Log.d("tid", Process.myTid()+"");
//                                            }
//                                        }).start();
//                                        for (int i=0;i<8000;i++)
//                                            Log.d("tid",android.os.Process.myTid()+"");
                                        try{
                                            addressBeanList = new Gson().fromJson(jsonObject.getJSONArray("address").toString(),new TypeToken<List<AddressBean>>(){}.getType());
                                            //将地址添加到本地数据库
                                            DataSupport.deleteAll(AddressBean.class);
                                            for (AddressBean addressBean : addressBeanList){
                                                addressBean.save();
                                            }
                                        }catch(JSONException e){
                                            Toast.makeText(LoginActivity.this, "收货地址获取失败！", Toast.LENGTH_SHORT).show();
                                        }
                                        setResult(RESULT_OK);
                                        finish();

                                    }else{
                                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                }catch (JSONException e){
                                    Log.d("LoginActivity",e.getMessage());
                                    Toast.makeText(LoginActivity.this, "登陆失败，请检查网络!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
            }
        } else {
            //手机验证码登陆
            if (Integer.parseInt(loginIdentifyCodeEt.getText().toString()) == a) {
                progressBar.setVisibility(View.VISIBLE);
                hash.put("user_tel",loginPhoneEt.getText().toString().replace(" ",""));
                HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH+HttpUtil.LOGIN_BY_CODE, hash, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, "登陆失败，请检查网络!", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
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
                                    final JSONObject jsonObject = new JSONObject(responseText);
                                    Integer state = (Integer) jsonObject.get("status");
                                    String msg = (String) jsonObject.get("msg");
                                    if (state == 1){
                                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        UserBean userBean = new Gson().fromJson(jsonObject.getJSONObject("user_info").toString(),UserBean.class);
                                        userBean.save();
                                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                                        editor.putInt("user_id", userBean.getUserId());
                                        editor.commit();

                                        try{
                                            addressBeanList = new Gson().fromJson(jsonObject.getJSONArray("address").toString(),new TypeToken<List<AddressBean>>(){}.getType());
                                            //将地址添加到本地数据库
                                            DataSupport.deleteAll(AddressBean.class);
                                            for (AddressBean addressBean : addressBeanList){
                                                addressBean.save();
                                            }
                                        }catch(JSONException e){
                                            Toast.makeText(LoginActivity.this, "收货地址获取失败！", Toast.LENGTH_SHORT).show();
                                        }
                                        setResult(RESULT_OK);
                                        finish();
                                    }else{
                                        Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
                                    }
                                }catch (JSONException e){
                                    Log.d("LoginActivity",e.toString());
                                    Toast.makeText(LoginActivity.this, "登陆失败，请检查网络!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
            } else {
                Toast.makeText(this, "验证码不正确", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class LoginTextWatcher implements TextWatcher {
        EditText editText;
        int lastContentLength = 0;
        boolean isDelete = false;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //隐藏显示登陆按钮
            if (loginByPasswordLl.getVisibility() == View.VISIBLE) {
                if (!(userName.getText().toString().equals("") || password.getText().toString().equals(""))) {
                    loginBtn.setEnabled(true);
                    loginBtn.setBackground(getResources().getDrawable(R.drawable.login_selected_bg));
                }
            } else {
                if (!(loginPhoneEt.getText().toString().equals("") || loginIdentifyCodeEt.getText().toString().equals(""))) {
                    loginBtn.setEnabled(true);
                    loginBtn.setBackground(getResources().getDrawable(R.drawable.login_selected_bg));
                }
            }

            /**
             *
             *手机号分隔处理
             *
             */
            if (loginByPasswordLl.getVisibility() == View.VISIBLE) {
                editText = userName;
            } else {
                editText = loginPhoneEt;
            }
            StringBuffer sb = new StringBuffer(editText.getText().toString());
            //是否为输入状态
            isDelete = editText.getText().toString().length() > lastContentLength ? false : true;

            //输入是第4，第9位，这时需要插入空格
            if (!isDelete && (editText.getText().toString().length() == 4 || editText.getText().toString().length() == 9)) {
                if (editText.getText().toString().length() == 4) {
                    sb.insert(3, " ");
                } else {
                    sb.insert(8, " ");
                }
                setContent(sb);
            }

            //删除的位置到4，9时，剔除空格
            if (isDelete && (editText.getText().toString().length() == 4 || editText.getText().toString().length() == 9)) {
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
            editText.setText(sb.toString());
            //移动光标到最后面
            editText.setSelection(sb.length());
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().equals("")) {
                loginBtn.setEnabled(false);
                loginBtn.setBackground(getResources().getDrawable(R.drawable.login_bg));
            }
            if (loginByIdentifyCodeLl.getVisibility() == View.VISIBLE) {
                if (loginPhoneEt.getText().toString().length() == 13 && loginGetIdentifyCodeTv.getText().toString().equals("获取验证码")) {
                    loginGetIdentifyCodeTv.setTextColor(getResources().getColor(R.color.bottom_tab_text_selected_color));
                    loginGetIdentifyCodeTv.setEnabled(true);
                } else {
                    loginGetIdentifyCodeTv.setEnabled(false);
                    loginGetIdentifyCodeTv.setTextColor(getResources().getColor(R.color.text_color_grey));
                }
            }
        }
    }

    private void loginWay() {
        if (loginByPasswordLl.getVisibility() == View.GONE) {
            identifyCode.setVisibility(View.GONE);
            loginByPasswordLl.setVisibility(View.VISIBLE);
            loginByIdentifyCodeLl.setVisibility(View.GONE);
            loginByIdentifyCodeTv.setVisibility(View.VISIBLE);
            loginNotTv.setVisibility(View.VISIBLE);
            loginByPassword.setVisibility(View.GONE);
            password.setText("");
        } else {
            if (loginPhoneEt.getText().toString().length() == 13) {
                loginGetIdentifyCodeTv.setEnabled(true);
                loginGetIdentifyCodeTv.setTextColor(getResources().getColor(R.color.bottom_tab_text_selected_color));
            }
            loginByIdentifyCodeLl.setVisibility(View.VISIBLE);
            loginByPasswordLl.setVisibility(View.GONE);
            loginByIdentifyCodeTv.setVisibility(View.GONE);
            loginNotTv.setVisibility(View.GONE);
            loginByPassword.setVisibility(View.VISIBLE);
            loginIdentifyCodeEt.setText("");
        }

    }

    private void clearEditText(EditText et) {
        et.setText("");
        et.setFocusable(true);
        et.setFocusableInTouchMode(true);
        et.requestFocus();
    }

    private void setCountDownTimer() {
        identifyCode.setVisibility(View.VISIBLE);
        a = (int) (Math.random() * (9999 - 1000 + 1)) + 1000;//产生1000-9999的随机数
        identifyCode.setText("验证码为:" + a);
        loginGetIdentifyCodeTv.setEnabled(false);
        loginGetIdentifyCodeTv.setTextColor(getResources().getColor(R.color.text_color_grey));
        new CountDownTimer(59000 + 50, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                loginGetIdentifyCodeTv.setText("已发送(" + millisUntilFinished / 1000 + "秒)");
            }

            @Override
            public void onFinish() {
                if (loginPhoneEt.getText().toString().length() == 13) {
                    loginGetIdentifyCodeTv.setEnabled(true);
                    loginGetIdentifyCodeTv.setTextColor(getResources().getColor(R.color.bottom_tab_text_selected_color));
                }
                loginGetIdentifyCodeTv.setText("获取验证码");
            }
        }.start();
    }


}
