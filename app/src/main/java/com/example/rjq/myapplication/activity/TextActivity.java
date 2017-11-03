package com.example.rjq.myapplication.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
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
import com.example.rjq.myapplication.adapter.FragAdapter;
import com.example.rjq.myapplication.fragment.OneFragment;
import com.example.rjq.myapplication.fragment.ThreeFragment;
import com.example.rjq.myapplication.fragment.TwoFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TextActivity extends AppCompatActivity implements View.OnClickListener{
    private long mExitTime = 0;
    private static final String TAG = "life";

    @BindView(R.id.radio_group_button)
    RadioGroup mRadioGroup;
    @BindView(R.id.radio_button_home)
    RadioButton mRadioButtonHome;
    @BindView(R.id.radio_button_order)
    RadioButton mRadioBtnOrder;
    @BindView(R.id.radio_button_my)
    RadioButton mRadioBtnMy;
    @BindView(R.id.fragment_vp)
    ViewPager fragmentVp;

    private List<Fragment> mFragments;
    FragAdapter fragAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        initData();
        initView();

    }

    private void initData(){
        ButterKnife.bind(this);
        mFragments = new ArrayList<Fragment>();
        mFragments.add(new OneFragment());
        mFragments.add(new TwoFragment());
        mFragments.add(new ThreeFragment());
        fragAdapter = new FragAdapter(getSupportFragmentManager(), mFragments);
    }

    private void initView(){
        initBottomTab();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

        }
    }

    private void initBottomTab(){
        fragmentVp.setAdapter(fragAdapter);
        fragmentVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                        mRadioButtonHome.setChecked(true);
                        break;
                    case 1:
                        mRadioBtnOrder.setChecked(true);
                        break;
                    case 2:
                        mRadioBtnMy.setChecked(true);
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Fragment mFragment = null;
                switch (checkedId){
                    case R.id.radio_button_home:
                        mFragment = mFragments.get(0);
                        fragmentVp.setCurrentItem(0);
                        break;
                    case R.id.radio_button_order:
                        mFragment = mFragments.get(1);
                        fragmentVp.setCurrentItem(1);
                        break;
                    case R.id.radio_button_my:
                        mFragment = mFragments.get(2);
                        fragmentVp.setCurrentItem(2);
                        break;
                }

//                if(mFragments!=null){
//                    getSupportFragmentManager().beginTransaction().replace();
//                }

            }
        });
        mRadioButtonHome.setChecked(true);
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

}
