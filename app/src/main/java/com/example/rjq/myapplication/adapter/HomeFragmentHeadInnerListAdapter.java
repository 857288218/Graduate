package com.example.rjq.myapplication.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.rjq.myapplication.R;

import java.util.List;

/**
 * Created by Administrator on 2017/7/21 0021.
 */

public class HomeFragmentHeadInnerListAdapter  extends RecyclerView.Adapter<HomeFragmentHeadInnerListAdapter.ViewHolder>{

    private List<String> listTitle;
    private List<String> listImg;
    private Context mContext;

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        ImageView titleImg;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.inner_item_title_tv);
            titleImg = (ImageView) itemView.findViewById(R.id.inner_item_title_iv);
        }
    }

    public HomeFragmentHeadInnerListAdapter(List<String> listTitle,List<String> listImg) {
        super();
        this.listImg = listImg;
        this.listTitle = listTitle;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(listTitle.get(position));
        if (position < listImg.size())
        Glide.with(mContext).load(listImg.get(position)).into(holder.titleImg);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.home_fragment_head_inner_item_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return listTitle.size();
    }




}
