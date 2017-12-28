package com.example.rjq.myapplication.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.adapter.OneFragmentAdapter;
import com.example.rjq.myapplication.adapter.TwoFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rjq on 2017/10/28 0028.
 */

public class TwoFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "life";
    Context mContext;
    private View rootView;
//    @BindView(R.id.two_fragment_rv)
//    RecyclerView twoRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null){
            rootView = inflater.inflate(R.layout.two_fragment,container,false);
            initData();
            initView();
        }
        Log.d(TAG,"onCreateView two");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.common_status_bar_color));
        }
        return rootView;
    }

    private void initView() {
        List<String> imageUrl = new ArrayList<>();
        imageUrl.add("httpuZZyi08cjrv.jpg");
        imageUrl.add("ht8ct96.jpg");
        imageUrl.add("http:c74r.jpg");
        imageUrl.add("ht08cwf4.jpg");
        imageUrl.add("httf4.jpg");
        imageUrl.add("http:0ci08cwf4.jpg");
        imageUrl.add("http:/ci08cwf4.jpg");
        imageUrl.add("http:30ci08cwf4.jpg");
//        twoRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
//        twoRecyclerView.setAdapter(new TwoFragmentAdapter(imageUrl));
    }

    private void initData(){
        ButterKnife.bind(this,rootView);
        mContext = getActivity();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG,"onAttach two");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate two");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"onActivityCreated two");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart two");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume two");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause two");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop two");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroyView two");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy two");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"onDetach two");
    }
}
