package com.example.rjq.myapplication.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.MyApplication;
import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.util.Sington;

public class OtherProcessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Application create", this.toString());
        Log.d("packname", MyApplication.getContext().getPackageName());
        setContentView(R.layout.activity_other_process);
        Log.d("process test","other process :"+android.os.Process.myPid());
        Log.d("process test","other process current thread:"+android.os.Process.myTid());
        Log.d("single", Sington.singles.toString());
        //已经开启了该进程主线程中的looper，不同于主进程中主线程的looper;进程间内存不共享，每个进程都有自己的内存
        Log.d("otherProcessMainLooper", Looper.getMainLooper().toString());
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
//                        Context context = MyApplication.getContext();
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
                Intent intent = new Intent(OtherProcessActivity.this, ConfigActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //杀掉该进程
//        Process.killProcess(android.os.Process.myPid());
    }
}
