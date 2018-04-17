package com.example.rjq.myapplication.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rjq.myapplication.R;

/**
 * Created by rjq on 2018/1/29.
 */

public class ResDetailFragment extends Fragment {
    private View root;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (root == null){
            root = inflater.inflate(R.layout.resdetail_fragment,container,false);
        }
        return root;
    }
}
