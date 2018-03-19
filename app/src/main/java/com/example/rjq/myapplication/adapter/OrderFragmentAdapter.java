package com.example.rjq.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.activity.ResActivity;
import com.example.rjq.myapplication.bean.OrderBean;
import com.example.rjq.myapplication.util.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjq on 2018/2/13.
 */

public class OrderFragmentAdapter extends RecyclerView.Adapter<OrderFragmentAdapter.ViewHolder>{

    private Context mContext;
    private List<OrderBean> orderList;

    public OrderFragmentAdapter(Context mContext, List<OrderBean> orderList){
        this.mContext = mContext;
        this.orderList = orderList;
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_fragment_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        GlideUtil.load(mContext,orderList.get(position).getResImg(),holder.order_fragment_item_res_img,GlideUtil.REQUEST_OPTIONS);
        holder.order_fragment_item_res_name.setText(orderList.get(position).getResName());
        holder.order_fragment_item_res_buy_time.setText(orderList.get(position).getOrderTime());
        holder.order_fragment_item_res_item_price.setText("￥"+orderList.get(position).getOrderPrice());
        holder.order_fragment_item_res_item_name.setText(orderList.get(position).getOrderDescription());

        holder.order_fragment_item_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ResActivity.class);
                intent.putExtra(ResActivity.RES_ID,String.valueOf(orderList.get(position).getResId()));
                mContext.startActivity(intent);
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView order_fragment_item_res_img;
        TextView order_fragment_item_res_name;
        TextView order_fragment_item_res_buy_time;
        TextView order_fragment_item_res_item_name;
        TextView order_fragment_item_res_item_price;
        TextView order_fragment_item_buy;

        public ViewHolder(View root){
            super(root);
            order_fragment_item_res_img = (ImageView) root.findViewById(R.id.order_fragment_item_res_img);
            order_fragment_item_res_name = (TextView) root.findViewById(R.id.order_fragment_item_res_name);
            order_fragment_item_res_buy_time = (TextView) root.findViewById(R.id.order_fragment_item_res_buy_time);
            order_fragment_item_res_item_name = (TextView) root.findViewById(R.id.order_fragment_item_res_item_name);
            order_fragment_item_res_item_price = (TextView) root.findViewById(R.id.order_fragment_item_res_item_price);
            order_fragment_item_buy = (TextView) root.findViewById(R.id.order_fragment_item_buy);
        }
    }
}
