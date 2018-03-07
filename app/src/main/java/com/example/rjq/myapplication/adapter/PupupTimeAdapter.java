package com.example.rjq.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rjq.myapplication.R;

import java.util.List;

/**
 * Created by rjq on 2018/3/4.
 */

public class PupupTimeAdapter extends RecyclerView.Adapter<PupupTimeAdapter.ViewHolder> {
    private Context mContext;
    private List<String> list;
    private OnItemClickListener onItemClickListener;

    public PupupTimeAdapter(Context context, List<String> list){
        mContext = context;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.time.setText(list.get(position));
        if (onItemClickListener != null){
            holder.time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_time_item,parent,false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView time;

        public ViewHolder(View view){
            super(view);
            time = (TextView) view.findViewById(R.id.time);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
