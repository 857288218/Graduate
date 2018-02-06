package com.example.rjq.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.eowise.recyclerview.stickyheaders.StickyHeadersAdapter;
import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.bean.GoodsListBean;

import java.util.List;

public class BigramHeaderAdapter implements StickyHeadersAdapter<BigramHeaderAdapter.ViewHolder> {

    private  Context mContext;
    private List<GoodsListBean.GoodsCategoryBean.GoodsItemBean> dataList;
    private List<GoodsListBean.GoodsCategoryBean> goodscatrgoryEntities;
    public BigramHeaderAdapter(Context context, List<GoodsListBean.GoodsCategoryBean.GoodsItemBean> items
            , List<GoodsListBean.GoodsCategoryBean> goodscatrgoryEntities) {
        this.mContext = context;
        this.dataList = items;
        this.goodscatrgoryEntities = goodscatrgoryEntities;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_goods_list, parent, false);
        return new BigramHeaderAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BigramHeaderAdapter.ViewHolder headerViewHolder, int position) {
        headerViewHolder.tvGoodsCategoryTitle.setText(goodscatrgoryEntities.get(dataList.get(position).getId()).getName());
        if (!TextUtils.isEmpty(goodscatrgoryEntities.get(dataList.get(position).getId()).getCategoryDescription())){
            headerViewHolder.tvGoodsCategoryDescription.setText(goodscatrgoryEntities.get(dataList.get(position).getId()).getCategoryDescription());
            headerViewHolder.tvGoodsCategoryDescription.setVisibility(View.VISIBLE);
        }else{
            headerViewHolder.tvGoodsCategoryDescription.setVisibility(View.GONE);
        }
    }

    @Override
    public long getHeaderId(int position) {
        return dataList.get(position).getId();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvGoodsCategoryTitle;
        TextView tvGoodsCategoryDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            tvGoodsCategoryTitle = (TextView) itemView.findViewById(R.id.goods_category_title);
            tvGoodsCategoryDescription = (TextView) itemView.findViewById(R.id.goods_category_description);
        }
    }
}
