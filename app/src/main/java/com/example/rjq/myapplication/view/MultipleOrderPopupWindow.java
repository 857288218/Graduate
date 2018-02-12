package com.example.rjq.myapplication.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.adapter.MultipleOrderPopWinAdapter;

import java.util.List;

/**
 * Created by rjq on 2018/2/11.
 */

public class MultipleOrderPopupWindow extends PopupWindow {
    private Context mContext;
    private List<String> orderModeList;
    private View view;
    private RecyclerView multipleOrderRecycler;
    private View multipleOrderShadow;
    private LinearLayoutManager linearLayoutManager;
    private MultipleOrderPopWinAdapter adapter;

    public MultipleOrderPopupWindow(Context mContext, List<String> orderModeList, MultipleOrderPopWinAdapter.OnMultipleOrderItemClickListener onMultipleOrderItemClickListener){
        this.mContext = mContext;
        this.orderModeList = orderModeList;
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setFocusable(true);

        view = LayoutInflater.from(mContext).inflate(R.layout.popup_multiple_order,null);
        multipleOrderRecycler = (RecyclerView) view.findViewById(R.id.multiple_order_recycler);
        multipleOrderShadow = view.findViewById(R.id.multiple_order_shadow);
        multipleOrderShadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultipleOrderPopupWindow.this.dismiss();
            }
        });
        linearLayoutManager = new LinearLayoutManager(mContext);
        multipleOrderRecycler.setLayoutManager(linearLayoutManager);
        adapter = new MultipleOrderPopWinAdapter(mContext,orderModeList,onMultipleOrderItemClickListener);
        multipleOrderRecycler.setAdapter(adapter);
        this.setContentView(view);
    }

    public int getSelectedPosition(){
        return adapter.getSelectedPosition();
    }

    public void setSelectedPosition(int position){
        adapter.setSelectedPosition(position);
    }

}
