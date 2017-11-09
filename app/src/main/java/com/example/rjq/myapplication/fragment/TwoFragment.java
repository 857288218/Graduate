package com.example.rjq.myapplication.fragment;

import android.app.Activity;
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

/**
 * Created by Administrator on 2017/10/28 0028.
 */

public class TwoFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "life";
    private View rootView;
    private ImageView twoIV;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.two_fragment,container,false);
            initView();
        }
        return rootView;
    }

    private void initView(){
        twoIV = (ImageView) rootView.findViewById(R.id.two);
        twoIV.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_rotate));
        twoIV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.two:
                twoIV.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.pop_exit_anim));
                twoIV.setVisibility(View.INVISIBLE);
                break;
        }
    }

}
