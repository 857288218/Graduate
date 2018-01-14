package com.example.rjq.myapplication.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.rjq.myapplication.R;

import butterknife.ButterKnife;

/**
 * Created by Dell on 2018/1/8.
 */

public class CartFragment extends Fragment {

    Context mContext;
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.common_status_bar_color));
        }

        if (rootView == null){
            rootView = inflater.inflate(R.layout.shopcar_fragment,container,false);
            initData();
            initView();
        }

        return rootView;
    }

    private void initData(){
        ButterKnife.bind(this,rootView);
        mContext = getActivity();
    }

    private void initView(){

    }
}
