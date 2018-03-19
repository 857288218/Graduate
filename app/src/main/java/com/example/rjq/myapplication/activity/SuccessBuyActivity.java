package com.example.rjq.myapplication.activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.example.rjq.myapplication.R;

public class SuccessBuyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_buy);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            startActivity(new Intent(this,MainActivity.class));
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
