package com.example.rjq.myapplication.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.adapter.CartFragmentAdapter;
import com.example.rjq.myapplication.bean.ResBuyItemNum;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rjq on 2018/1/8.
 */

public class CartFragment extends Fragment {

    Context mContext;
    private View rootView;
    @BindView(R.id.cart_fragment_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.cart_empty)
    ImageView cartEmptyIv;
    CartFragmentAdapter cartFragmentAdapter;

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
        }
        initView();
        return rootView;
    }

    private void initData(){
        ButterKnife.bind(this,rootView);
        mContext = getActivity();
    }

    private void initView(){
        //本地数据库查询所有ResBuyItemNum数据
        List<ResBuyItemNum> resBuyItemNumList = DataSupport.findAll(ResBuyItemNum.class);
        if (resBuyItemNumList.size()>0){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(linearLayoutManager);
            cartFragmentAdapter = new CartFragmentAdapter(mContext,resBuyItemNumList);
            recyclerView.setAdapter(cartFragmentAdapter);
            recyclerView.setVisibility(View.VISIBLE);
            cartEmptyIv.setVisibility(View.GONE);
        }else{
            recyclerView.setVisibility(View.GONE);
            cartEmptyIv.setVisibility(View.VISIBLE);
        }


    }
}
