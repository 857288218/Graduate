package com.example.rjq.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rjq.myapplication.R;

import java.util.List;

/**
 * Created by rjq on 2018/2/11.
 */

public class MultipleOrderPopWinAdapter extends RecyclerView.Adapter<MultipleOrderPopWinAdapter.ViewHolder>{
    private Context mContext;
    private List<String> list;
    private int selectedPosition = 0;
    private OnMultipleOrderItemClickListener onMultipleOrderItemClickListener;

    public MultipleOrderPopWinAdapter(Context mContext, List<String> list, OnMultipleOrderItemClickListener onMultipleOrderItemClickListener){
        this.mContext = mContext;
        this.list = list;
        this.onMultipleOrderItemClickListener = onMultipleOrderItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.multiple_order_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.orderMode.setText(list.get(position));
        if (selectedPosition == position){
            holder.orderSelected.setVisibility(View.VISIBLE);
            holder.orderMode.setTextColor(mContext.getResources().getColor(R.color.bottom_tab_text_selected_color));
        }else{
            holder.orderSelected.setVisibility(View.GONE);
            holder.orderMode.setTextColor(mContext.getResources().getColor(R.color.color_666));
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onMultipleOrderItemClickListener != null){
                    onMultipleOrderItemClickListener.onMultipleOrderItemClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setSelectedPosition(int position){
        if (selectedPosition != position){
            selectedPosition = position;
            notifyDataSetChanged();
        }
    }

    public int getSelectedPosition(){
        return selectedPosition;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView orderMode;
        public final ImageView orderSelected;
        public final View root;

        public ViewHolder(View root) {
            super(root);
            orderMode = (TextView) root.findViewById(R.id.order_mode);
            orderSelected = (ImageView) root.findViewById(R.id.order_selected);
            this.root = root;
        }

    }

    public interface OnMultipleOrderItemClickListener{
        void onMultipleOrderItemClick(int position);
    }

    public void setOnMultipleOrderItemClickListener(OnMultipleOrderItemClickListener onMultipleOrderItemClickListener) {
        this.onMultipleOrderItemClickListener = onMultipleOrderItemClickListener;
    }
}
