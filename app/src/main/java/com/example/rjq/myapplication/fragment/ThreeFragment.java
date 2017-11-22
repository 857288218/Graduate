package com.example.rjq.myapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rjq.myapplication.R;

/**
 * Created by Administrator on 2017/10/28 0028.
 */

public class ThreeFragment extends Fragment {
    private static final String TAG = "life";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.three_fragment,container,false);
        Log.d(TAG,"three onCreateView");
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"three onCreate");
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"three onActivityCreated");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG,"three onAttach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"three onDestroy");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"three onPause");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"three onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"three onResume");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"three onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"three onDetach");
    }
}
