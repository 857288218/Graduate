package com.example.rjq.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.bean.ResBuyItemNum;
import com.example.rjq.myapplication.util.GlideUtil;
import com.zhy.autolayout.AutoLinearLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjq on 2018/2/7.
 */

public class CartFragmentAdapter extends RecyclerView.Adapter<CartFragmentAdapter.ViewHolder> {
    private Context mContext;
    private List<ResBuyItemNum> resBuyItemNumList;
    private List<String> resIdList;

    public CartFragmentAdapter(Context context, List<ResBuyItemNum> resBuyItemNumList){
        this.mContext = context;
        this.resBuyItemNumList = resBuyItemNumList;
        resIdList = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        for (ResBuyItemNum resBuyItemNum : resBuyItemNumList){
            if (!resIdList.contains(resBuyItemNum.getResId())){
                resIdList.add(resBuyItemNum.getResId());
            }
        }
        return resIdList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cart_fragment_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("#0.0");
        double sum = 0;
        double deliverPrice = 0;

        for (ResBuyItemNum resBuyItemNum : resBuyItemNumList){
            if (resBuyItemNum.getResId().equals(resIdList.get(position))){
                holder.resItemContainer.addView(
                        initResBuyItem(resBuyItemNum.getItemImg(),resBuyItemNum.getItemName(),resBuyItemNum.getBuyNum(),
                                resBuyItemNum.getBuyNum()*Double.parseDouble(df.format(resBuyItemNum.getItemPrice())))
                );
                holder.resNameCartFragmentItem.setText(resBuyItemNum.getResName());
                sum += resBuyItemNum.getBuyNum() * resBuyItemNum.getItemPrice();
                deliverPrice = resBuyItemNum.getResDeliverMoney() - sum;
            }
        }
        int sum1 = (int) sum;
        if (sum > sum1){
            holder.resAllPrice.setText("￥"+df.format(sum));
        }else{
            holder.resAllPrice.setText("￥"+sum1);
        }

        if (deliverPrice > 0){
            int dp = (int) deliverPrice;
            if (deliverPrice > dp){
                holder.resDeliverMoney.setText(df.format(deliverPrice));
            }else{
                holder.resDeliverMoney.setText(dp+"");
            }

            holder.resDeliverMoney.setVisibility(View.VISIBLE);
            holder.goToBuy.setVisibility(View.VISIBLE);
            holder.goToAccount.setVisibility(View.INVISIBLE);
            holder.haicha.setVisibility(View.VISIBLE);
            holder.qisong.setVisibility(View.VISIBLE);
        }else{
            holder.resDeliverMoney.setVisibility(View.GONE);
            holder.goToBuy.setVisibility(View.INVISIBLE);
            holder.goToAccount.setVisibility(View.VISIBLE);
            holder.haicha.setVisibility(View.GONE);
            holder.qisong.setVisibility(View.GONE);
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView resNameCartFragmentItem;
        TextView resDeliverMoney;
        TextView resAllPrice;
        TextView goToAccount;
        TextView goToBuy;
        AutoLinearLayout resItemContainer;
        TextView haicha;
        TextView qisong;

        public ViewHolder(View root) {
            super(root);
            resNameCartFragmentItem = (TextView) root.findViewById(R.id.res_name_cart_fragment_item);
            resDeliverMoney = (TextView) root.findViewById(R.id.res_deliver_money);
            resAllPrice = (TextView) root.findViewById(R.id.res_all_price);
            goToAccount = (TextView) root.findViewById(R.id.go_to_account);
            goToBuy = (TextView) root.findViewById(R.id.go_to_buy);
            resItemContainer = (AutoLinearLayout) root.findViewById(R.id.res_item_container);
            haicha = (TextView) root.findViewById(R.id.haicha);
            qisong = (TextView) root.findViewById(R.id.qisong);
        }

    }

    private View initResBuyItem(String imgUrl,String itemName,int itemNum,double itemPrice){
        View linearLayout = LayoutInflater.from(mContext).inflate(R.layout.cart_fragment_item_container,null);

        ImageView item_img_cart_fragment_item = (ImageView) linearLayout.findViewById(R.id.item_img_cart_fragment_item);
        GlideUtil.load(mContext,imgUrl,item_img_cart_fragment_item,GlideUtil.REQUEST_OPTIONS);

        TextView item_name_cart_fragment_item = (TextView) linearLayout.findViewById(R.id.item_name_cart_fragment_item);
        item_name_cart_fragment_item.setText(itemName);

        TextView item_num_cart_fragment_item = (TextView) linearLayout.findViewById(R.id.item_num_cart_fragment_item);
        String num = mContext.getResources().getString(R.string.res_item_num);
        num = String.format(num,itemNum);
        item_num_cart_fragment_item.setText(num);

        TextView res_item_price = (TextView) linearLayout.findViewById(R.id.res_item_price);
        res_item_price.setText("￥"+itemPrice);
        return linearLayout;
    }
}
