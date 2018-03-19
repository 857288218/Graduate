package com.example.rjq.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rjq.myapplication.R;

import java.util.List;

/**
 * Created by rjq on 2018/3/11.
 */

public class PopupPayWayAdapter extends RecyclerView.Adapter<PopupPayWayAdapter.ViewHolder> {
    private Context mContext;
    private List<String> listTv;
    private List<Integer> listIv;
    private int selected;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public PopupPayWayAdapter(Context mContext, List<String> listTv, List<Integer> listIv){
        this.mContext = mContext;
        this.listTv = listTv;
        this.listIv = listIv;
    }

    public void setSelected(int position){
        this.selected = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return listTv.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_pay_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.iv.setImageResource(listIv.get(position));
        holder.tv.setText(listTv.get(position));
        if (position == selected){
            holder.selected.setVisibility(View.VISIBLE);
        }else{
            holder.selected.setVisibility(View.GONE);
        }
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        View root;
        ImageView iv;
        TextView tv;
        ImageButton selected;
        public ViewHolder(View view){
            super(view);
            root = view.findViewById(R.id.root);
            iv = (ImageView) view.findViewById(R.id.iv);
            tv = (TextView) view.findViewById(R.id.tv);
            selected = (ImageButton) view.findViewById(R.id.selected);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
