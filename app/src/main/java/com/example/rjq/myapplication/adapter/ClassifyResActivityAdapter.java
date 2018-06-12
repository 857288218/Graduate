package com.example.rjq.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.activity.ClassifyResActivity;
import com.example.rjq.myapplication.activity.ResActivity;
import com.example.rjq.myapplication.bean.DiscountBean;
import com.example.rjq.myapplication.bean.ResDetailBean;
import com.example.rjq.myapplication.util.GlideUtil;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.List;

import static com.example.rjq.myapplication.fragment.OneFragment.RES_DETAIL;

/**
 * Created by rjq on 2018/2/11.
 */

public class ClassifyResActivityAdapter extends RecyclerView.Adapter<ClassifyResActivityAdapter.ViewHolder> {

    private Context mContext;
    private List<ResDetailBean> homeRecResDetailBeanList;

    public ClassifyResActivityAdapter(Context mContext, List<ResDetailBean> homeRecResDetailBeanList){
        this.mContext = mContext;
        this.homeRecResDetailBeanList = homeRecResDetailBeanList;
    }

    @Override
    public int getItemCount() {
        return homeRecResDetailBeanList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.one_fragment_content_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.one_fragment_item_reduce_container.setVisibility(View.GONE);

        //设置添加到购物车的数量，红点显示
        if (homeRecResDetailBeanList.get(position).getBuyNum() > 0){
            holder.one_content_item_buy_num.setText(homeRecResDetailBeanList.get(position).getBuyNum()+"");
            holder.one_content_item_buy_num.setVisibility(View.VISIBLE);
        }else{
            holder.one_content_item_buy_num.setVisibility(View.GONE);
        }

        //设置img
        GlideUtil.load(mContext,homeRecResDetailBeanList.get(position).getResImg(),holder.one_content_item_iv,GlideUtil.REQUEST_OPTIONS);
        //店名
        holder.one_fragment_content_item_name.setText(homeRecResDetailBeanList.get(position).getResName());
        //评分
        holder.one_fragment_star.setRating(homeRecResDetailBeanList.get(position).getResStar());
        holder.one_fragment_score.setText(homeRecResDetailBeanList.get(position).getResStar()+"");
        //月售订单
        String orderNum = mContext.getResources().getString(R.string.res_month_sell_order);
        orderNum = String.format(orderNum,homeRecResDetailBeanList.get(position).getResOrderNum());
        holder.one_fragment_order_num.setText(orderNum);

        //起送
        String deliverMoney = mContext.getResources().getString(R.string.res_deliver_money);
        deliverMoney = String.format(deliverMoney,homeRecResDetailBeanList.get(position).getResDeliverMoney());
        holder.one_fragment_deliver.setText(deliverMoney);

        //配送费
        String extraMoney = mContext.getResources().getString(R.string.res_extra_money);
        extraMoney = String.format(extraMoney,homeRecResDetailBeanList.get(position).getResExtraMoney());
        holder.one_fragment_extra.setText(extraMoney);

        holder.one_fragment_address.setText(homeRecResDetailBeanList.get(position).getResAddress());
        //配送时间
        String deliverTime = mContext.getResources().getString(R.string.res_deliver_time);
        deliverTime = String.format(deliverTime,homeRecResDetailBeanList.get(position).getResDeliverTime());
        holder.one_fragment_deliver_time.setText(deliverTime);

        if (homeRecResDetailBeanList.get(position).getDiscountList()!= null && homeRecResDetailBeanList.get(position).getDiscountList().size()>0){
            holder.one_fragment_item_reduce_container.setVisibility(View.VISIBLE);
            holder.divider.setVisibility(View.VISIBLE);
            StringBuffer sb = new StringBuffer();

            for (DiscountBean discountBean : homeRecResDetailBeanList.get(position).getDiscountList()){
                int fillPrice = (int) discountBean.getFilledVal();
                int reducePrice = (int) discountBean.getReduceVal();
                if (discountBean.getFilledVal()>fillPrice){
                    sb.append("满"+discountBean.getFilledVal());
                }else{
                    sb.append("满"+fillPrice);
                }
                if (discountBean.getReduceVal() > reducePrice){
                    sb.append("减"+discountBean.getReduceVal()+",");
                }else{
                    sb.append("减"+reducePrice+",");
                }
            }

            holder.one_fragment_item_reduce.setText(sb.toString().substring(0,sb.length()-1));
        }else{
            holder.divider.setVisibility(View.GONE);
            holder.one_fragment_item_reduce_container.setVisibility(View.GONE);
        }

        //设置每个item的点击事件
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ResActivity.class);
                intent.putExtra(RES_DETAIL,homeRecResDetailBeanList.get(position));
                mContext.startActivity(intent);
            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View root;
        public ImageView one_content_item_iv;
        public TextView one_content_item_buy_num;
        public TextView one_fragment_content_item_name;
        public RatingBar one_fragment_star;
        public TextView one_fragment_score;
        public TextView one_fragment_deliver;
        public TextView one_fragment_order_num;
        public TextView one_fragment_extra;
        public TextView one_fragment_address;
        public TextView one_fragment_deliver_time;
        public AutoLinearLayout one_fragment_item_reduce_container;
        public TextView one_fragment_item_reduce;
        public View divider;
        public ViewHolder(View root) {
            super(root);
            this.root = root;
            divider = root.findViewById(R.id.divider);
            one_fragment_star = (RatingBar) root.findViewById(R.id.one_fragment_star);
            one_fragment_item_reduce_container = (AutoLinearLayout) root.findViewById(R.id.one_fragment_item_reduce_container);
            one_content_item_iv = (ImageView) root.findViewById(R.id.one_content_item_iv);
            one_content_item_buy_num = (TextView) root.findViewById(R.id.one_content_item_buy_num);
            one_fragment_content_item_name = (TextView) root.findViewById(R.id.one_fragment_content_item_name);
            one_fragment_score = (TextView) root.findViewById(R.id.one_fragment_score);
            one_fragment_order_num = (TextView) root.findViewById(R.id.one_fragment_order_num);
            one_fragment_extra = (TextView) root.findViewById(R.id.one_fragment_extra);
            one_fragment_address = (TextView) root.findViewById(R.id.one_fragment_address);
            one_fragment_deliver_time = (TextView) root.findViewById(R.id.one_fragment_deliver_time);
            one_fragment_item_reduce = (TextView) root.findViewById(R.id.one_fragment_item_reduce);
            one_fragment_deliver = (TextView) root.findViewById(R.id.one_fragment_deliver);

        }

    }
}
