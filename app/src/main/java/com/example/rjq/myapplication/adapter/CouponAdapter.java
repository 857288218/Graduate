package com.example.rjq.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rjq.myapplication.R;
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
    private String resName;
    private double allMoney;

    public CouponAdapter(Context context, String resName,double allMoney,List<CouponBean> list){
        this.mContext = context;
        this.list = list;
        this.resName = resName;
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
        holder.couponName.setText(resName+"红包");
        holder.couponData.setText(list.get(position).getDeadline());
        holder.minUse.setText("满"+list.get(position).getMiniPrice()+"元可用");
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
