package com.example.rjq.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rjq.myapplication.R;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/7 0007.
 */

public class TwoFragmentAdapter extends RecyclerView.Adapter<TwoFragmentAdapter.ViewHolder> {
    private View view;
    private Context mContext;
    private List<String> list;

    public TwoFragmentAdapter(List<String> list) {
        this.list = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView text;

        public ViewHolder(View view) {
            super(view);
            text = (TextView) view.findViewById(R.id.two_item_tv);
        }
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        view = LayoutInflater.from(mContext).inflate(R.layout.two_fragmet_item,parent,false);

        return new ViewHolder(view);
    }
}
