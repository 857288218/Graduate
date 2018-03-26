package com.example.rjq.myapplication.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.bean.AddressBean;

import java.util.List;

/**
 * Created by rjq on 2018/3/5.
 */

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder>{
    private Context mContext;
    private List<AddressBean> list;
    private OnItemClickListener onItemClickListener;
    private OnItemDeleteListener onItemDeleteListener;
    private OnItemLongClickListener onItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemDeleteListener(OnItemDeleteListener onItemDeleteListener){
        this.onItemDeleteListener = onItemDeleteListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public AddressAdapter(Context context, List<AddressBean> list){
        mContext = context;
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.address_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.address.setText(list.get(position).getAddress());
        holder.name.setText(list.get(position).getName());
        holder.phone.setText(list.get(position).getPhone());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemDeleteListener != null){
                    onItemDeleteListener.onItemDelete(position);
                }
            }
        });

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(position);
                }
            }
        });

        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null){
                    onItemLongClickListener.onItemLongClick(position);
                    return true;
                }
                return false;
            }
        });

        if (list.get(position).getSelected() == 1){
            holder.view.setBackgroundColor(mContext.getResources().getColor(R.color.grey_background));
        }else{
            holder.view.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView address;
        TextView name;
        TextView phone;
        ImageButton delete;
        public ViewHolder(View root){
            super(root);
            view = root;
            address = (TextView) root.findViewById(R.id.tv_address);
            name = (TextView) root.findViewById(R.id.tv_name);
            phone = (TextView) root.findViewById(R.id.tv_phone);
            delete = (ImageButton) root.findViewById(R.id.address_delete);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public interface OnItemDeleteListener{
        void onItemDelete(int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(int position);
    }

}
