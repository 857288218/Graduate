package com.example.rjq.myapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.activity.ResActivity;
import com.example.rjq.myapplication.bean.CouponBean;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by rjq on 2018/3/25.
 */

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.ViewHolder>{
    private Context mContext;
    private List<CouponBean> list;
    private OnUseBtnClickListener onUseBtnClickListener;
    private double allMoney;

    public CouponAdapter(Context context, double allMoney,List<CouponBean> list){
        this.mContext = context;
        this.list = list;
        this.allMoney = allMoney;
    }

    public interface OnUseBtnClickListener{
        void useBtnClickListener(int position,CouponBean couponBean);
    }

    public void setOnUseBtnClickListener(OnUseBtnClickListener onUseBtnClickListener){
        this.onUseBtnClickListener = onUseBtnClickListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        int price = (int)list.get(position).getPrice();
        if (list.get(position).getPrice() > price){
            holder.couponMoney.setText(list.get(position).getPrice()+"");
        }else{
            holder.couponMoney.setText(price+"");
        }
        holder.couponName.setText(list.get(position).getShopName()+"红包");
        holder.couponData.setText(list.get(position).getDeadline());
        holder.minUse.setText("满"+list.get(position).getMiniPrice()+"元可用");
        //从我的界面进入红包，查看拥有的全部店铺红包
        if (allMoney == 0){
            holder.use.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ResActivity.class);
                    intent.putExtra("res_id",list.get(position).getShopId()+"");
                    intent.putExtra("res_name",list.get(position).getShopName());
                    mContext.startActivity(intent);
                }
            });

        }
        //从相应店铺进入红包界面，查看该店铺红包
        else{
            holder.use.setVisibility(View.VISIBLE);
            if (allMoney>=list.get(position).getMiniPrice()){
                if (onUseBtnClickListener != null){
                    holder.use.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onUseBtnClickListener.useBtnClickListener(position,list.get(position));
                        }
                    });
                }
                holder.use.setBackground(mContext.getResources().getDrawable(R.drawable.red_ban_yuan));
            }else{
                holder.use.setBackground(mContext.getResources().getDrawable(R.drawable.grey_ban_yuany));
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.coupon_item,parent,false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView couponName;
        TextView couponMoney;
        TextView minUse;
        TextView couponData;
        TextView use;
        public ViewHolder (View root){
            super(root);
            couponName = (TextView) root.findViewById(R.id.coupon_name);
            couponMoney = (TextView)root.findViewById(R.id.coupon_money);
            minUse = (TextView) root.findViewById(R.id.min_use);
            couponData = (TextView) root.findViewById(R.id.coupon_date);
            use = (TextView) root.findViewById(R.id.use);
        }
    }
}
