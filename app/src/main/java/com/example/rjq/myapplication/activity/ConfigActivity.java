package com.example.rjq.myapplication.activity;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.util.HttpUtil;

import butterknife.BindView;

import static com.example.rjq.myapplication.util.HttpUtil.HOME_PATH;

public class ConfigActivity extends BaseActivity {
    @BindView(R.id.et_host)
    EditText host;
    @BindView(R.id.confirm_btn)
    Button confirmBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);


    }

    @Override
    protected void initView() {
        super.initView();
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (host.getText().toString().length() > 7 && host.getText().toString().substring(0,7).equals("http://")){
                    PreferenceManager.getDefaultSharedPreferences(ConfigActivity.this).edit().putString(HttpUtil.SERVER_HOST,host.getText().toString()).commit();
                    HOME_PATH = host.getText().toString()+"/restaurant/index.php";
                    finish();
                }else{
                    Toast.makeText(ConfigActivity.this, "请输入正确的服务器地址，“http://”开头", Toast.LENGTH_SHORT).show();
                }

            }
        });
        host.setText(PreferenceManager.getDefaultSharedPreferences(ConfigActivity.this).getString(HttpUtil.SERVER_HOST,""));
    }
}
