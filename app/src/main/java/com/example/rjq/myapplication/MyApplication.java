package com.example.rjq.myapplication;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.litepal.LitePal;

public class MyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Log.d("Application create", context.toString());
        LitePal.initialize(context);
    }

    public static Context getContext(){
        return context;
    }
}
