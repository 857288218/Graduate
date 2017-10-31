package com.example.rjq.myapplication.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.fragment.OneFragment;
import com.example.rjq.myapplication.fragment.ThreeFragment;
import com.example.rjq.myapplication.fragment.TwoFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TextActivity extends AppCompatActivity implements View.OnClickListener{
    private long mExitTime = 0;
    private static final String TAG = "life";

    @BindView(R.id.radio_group_button)
    RadioGroup mRadioGroup;
    @BindView(R.id.radio_button_home)
    RadioButton mRadioButtonHome;

    private Fragment []mFragments;
    private int tabsNum = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        initData();
        initView();

    }

    private void initData(){
        ButterKnife.bind(this);
        mFragments = new Fragment [tabsNum];
        mFragments[0] = new OneFragment();
        mFragments[1] = new TwoFragment();
        mFragments[2] = new ThreeFragment();
    }

    private void initView(){
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment mFragment = null;
                switch (checkedId){
                    case R.id.radio_button_home:
                        mFragment = mFragments[0];
                        break;
                    case R.id.radio_button_discovery:
                        mFragment = mFragments[1];
                        break;
                    case R.id.radio_button_attention:
                        mFragment = mFragments[2];
                        break;
                }

                if(mFragments!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_container,mFragment).commit();
                }

            }
        });
        mRadioButtonHome.setChecked(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, "再按一次退出食堂订餐", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
