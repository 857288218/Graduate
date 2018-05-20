package com.example.rjq.myapplication.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Handler;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.R;

public class OtherProcessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_process);
        Log.d("process test","other process :"+android.os.Process.myPid());
        Log.d("process test","other process current thread:"+android.os.Process.myTid());
        final TextView tv = (TextView) findViewById(R.id.test);
        Button btn = (Button) findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Log.d("process test","other process main thread:"+android.os.Process.myTid());
//                                tv.setText("asas");
//                            }
//                        });
                        new Handler(getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("process test","other process main thread:"+android.os.Process.myTid());
                                // 获取activity任务栈
                                ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                                ActivityManager.RunningTaskInfo info = manager.getRunningTasks(1).get(0);
                                tv.setText(info.baseActivity.getClassName());
                            }
                        });
                    }
                }).start();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("process test","other process son thread:"+ Process.myTid());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("process test","other process main thread:"+android.os.Process.myTid());
                    }
                });
            }
        }).start();

    }
}
