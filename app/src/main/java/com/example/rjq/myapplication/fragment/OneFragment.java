package com.example.rjq.myapplication.fragment;

import android.content.Context;
import android.content.pm.ActivityInfo;
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

public class OneFragment extends Fragment implements View.OnClickListener{
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
        Log.d("two","onCreateView one");
        return rootView;
    }

    private void initView(){
        oneIV = (ImageView) rootView.findViewById(R.id.one);
        oneTV = (TextView) rootView.findViewById(R.id.one2);
        oneTV.setText(list.get(0));
        oneIV.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_scale));
        oneIV.setOnClickListener(this);
    }

    private void initData(){
        list = new ArrayList<>();
        list.add("rjq");
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.one:
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
                break;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("two","onAttach one");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("two","onCreate one");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("two","onActivityCreated one");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("two","onStart one");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("two","onResume one");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("two","onPause one");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("two","onStop one");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("two","onDestroyView one");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("two","onDestroy one");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("two","onDetach one");
    }


}
