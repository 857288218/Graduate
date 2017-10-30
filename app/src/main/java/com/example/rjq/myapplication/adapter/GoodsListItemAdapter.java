package com.example.rjq.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.rjq.myapplication.Bean.GoodsListItemBean;
import com.example.rjq.myapplication.R;

import java.util.List;

/**
 * Created by Administrator on 2017/7/25 0025.
 */

public class GoodsListItemAdapter extends RecyclerView.Adapter<GoodsListItemAdapter.ViewHolder> {

    private Context mContext;
    private List<GoodsListItemBean> mData;

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView goodImg;
        TextView goodsDescribe;
        TextView goodsPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            goodImg = (ImageView) itemView.findViewById(R.id.goods_list_item_img);
            goodsDescribe = (TextView) itemView.findViewById(R.id.goods_list_item_describe_tv);
            goodsPrice = (TextView) itemView.findViewById(R.id.goods_list_item_price_tv);
        }
    }

    public GoodsListItemAdapter(List<GoodsListItemBean> mData) {
        super();
        this.mData = mData;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GoodsListItemBean bean = mData.get(position);
        Glide.with(mContext).load(bean.getImg()).into(holder.goodImg);
        holder.goodsDescribe.setText(bean.getGoodsDescribe());
        holder.goodsPrice.setText(bean.getGoodsPrice()+"元");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.goods_list_activity_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

}
