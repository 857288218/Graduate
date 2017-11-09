package com.example.rjq.myapplication.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjq on 2017/10/28 0028.
 */

public class OneFragment extends Fragment {
    private static final String TAG = "life";
    private View rootView;
    private ImageView oneIV;
    private TextView oneTV;

    private List<String> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.one_fragment,container,false);
            initData();
            initView();
        }
        return rootView;
    }

    private void initView(){
        oneIV = (ImageView) rootView.findViewById(R.id.one);
        oneTV = (TextView) rootView.findViewById(R.id.one2);
        oneTV.setText(list.get(0));
        oneIV.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_scale));
    }

    private void initData(){
        list = new ArrayList<>();
        list.add("rjq");
    }


}
