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

public class OneFragment extends Fragment {
    private static final String TAG = "life";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one_fragment,container,false);
        Log.d(TAG,"one onCreateView");
        return view;
    }


}
