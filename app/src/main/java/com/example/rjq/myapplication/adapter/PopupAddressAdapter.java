package com.example.rjq.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.bean.BuildingBean;

import java.util.List;

/**
 * Created by rjq on 2018/3/14.
 */

public class PopupAddressAdapter extends RecyclerView.Adapter<PopupAddressAdapter.ViewHolder>{
    private Context mContext;
    private List<BuildingBean> list;
    private int selected = -1;
    private OnItemClickListener onItemClickListener;

    public void setSelected(int position){
        this.selected = position;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public PopupAddressAdapter(Context context,List<BuildingBean> list){
        this.mContext = context;
        this.list = list;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tv.setText(list.get(position).getBuilderName());
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_address_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        View root;
        TextView tv;
        ImageButton selected;

        public ViewHolder(View root){
            super(root);
            this.root = root;
            this.tv = (TextView) root.findViewById(R.id.tv);
            this.selected = (ImageButton) root.findViewById(R.id.selected);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
}
