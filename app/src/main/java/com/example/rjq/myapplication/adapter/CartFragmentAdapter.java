package com.example.rjq.myapplication.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.activity.AccountActivity;
import com.example.rjq.myapplication.activity.ResActivity;
import com.example.rjq.myapplication.bean.ResBuyCategoryNum;
import com.example.rjq.myapplication.bean.ResBuyItemNum;
import com.example.rjq.myapplication.util.GlideUtil;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;
import com.zhy.autolayout.utils.AutoUtils;

import org.litepal.crud.DataSupport;
import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.example.rjq.myapplication.activity.ResActivity.RES_ID;

/**
 * Created by rjq on 2018/2/7.
 */

public class CartFragmentAdapter extends RecyclerView.Adapter<CartFragmentAdapter.ViewHolder> {
    private Context mContext;
    private List<ResBuyItemNum> resBuyItemNumList;
    private List<String> resIdList;
    private ItemDeleteBtnListener deleteBtnListener;

    public CartFragmentAdapter(Context context, List<ResBuyItemNum> resBuyItemNumList){
        this.mContext = context;
        this.resBuyItemNumList = resBuyItemNumList;
        resIdList = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        //该方法计算ItemCount返回的不是resBuyItemNumList总数而是店铺数量,所以对应的position也不是resBuyItemNumList中的position
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        DecimalFormat df = new DecimalFormat("#0.0");
        //该list为点击结算跳页时携带的数据,该resId的店铺的购买数据
        final List<ResBuyItemNum> list = new ArrayList<>();
        double sum = 0;
        double packageMoney = 0;
        double deliverPrice = 0;
        holder.resItemContainer.removeAllViews();
        for (ResBuyItemNum resBuyItemNum : resBuyItemNumList){
            if (resBuyItemNum.getResId().equals(resIdList.get(position))){
                holder.resItemContainer.addView(
                        initResBuyItem(resBuyItemNum.getItemImg(),resBuyItemNum.getItemName(),resBuyItemNum.getBuyNum(),
                                resBuyItemNum.getBuyNum()*Double.parseDouble(df.format(resBuyItemNum.getItemPrice())))
                );
                sum += resBuyItemNum.getBuyNum() * resBuyItemNum.getItemPrice();
                packageMoney += resBuyItemNum.getItemPackageMoney() * resBuyItemNum.getBuyNum();
                list.add(resBuyItemNum);
            }
        }
        if (packageMoney > 0){
            holder.packageMoneyContainer.setVisibility(View.VISIBLE);
            holder.divider.setVisibility(View.VISIBLE);
            holder.packageMoneyTv.setText("￥"+packageMoney);
        }else{
            holder.packageMoneyContainer.setVisibility(View.GONE);
            holder.divider.setVisibility(View.GONE);
        }
        sum += packageMoney;
        if (list.size() > 0){
            holder.resNameCartFragmentItem.setText(list.get(0).getResName());
            deliverPrice = list.get(0).getResDeliverMoney() - sum;
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
            holder.goToAccount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, AccountActivity.class);
                    intent.putExtra("res_id",Integer.parseInt(list.get(0).getResId()));
                    intent.putExtra("res_name",list.get(0).getResName());
                    mContext.startActivity(intent);
                }
            });
        }

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ResActivity.class);
                intent.putExtra(RES_ID,list.get(0).getResId());
                intent.putExtra("res_name",list.get(0).getResName());
                mContext.startActivity(intent);
            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteBtnListener != null){
                    deleteBtnListener.onItemDeleteBtnListener(holder.deleteBtn,position,list.get(0).getResId());
                }
            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView resNameCartFragmentItem;
        TextView resDeliverMoney;
        TextView resAllPrice;
        TextView goToAccount;
        TextView goToBuy;
        AutoLinearLayout resItemContainer;
        ImageView deleteBtn;
        TextView haicha;
        TextView qisong;
        TextView packageMoneyTv;
        AutoRelativeLayout packageMoneyContainer;
        View divider;
        View root;

        public ViewHolder(View root) {
            super(root);
            this.root = root;
            resNameCartFragmentItem = (TextView) root.findViewById(R.id.res_name_cart_fragment_item);
            resDeliverMoney = (TextView) root.findViewById(R.id.res_deliver_money);
            resAllPrice = (TextView) root.findViewById(R.id.res_all_price);
            goToAccount = (TextView) root.findViewById(R.id.go_to_account);
            goToBuy = (TextView) root.findViewById(R.id.go_to_buy);
            resItemContainer = (AutoLinearLayout) root.findViewById(R.id.res_item_container);
            haicha = (TextView) root.findViewById(R.id.haicha);
            qisong = (TextView) root.findViewById(R.id.qisong);
            deleteBtn = (ImageView) root.findViewById(R.id.delete_btn);
            packageMoneyTv = (TextView) root.findViewById(R.id.res_package_money_tv);
            packageMoneyContainer = (AutoRelativeLayout) root.findViewById(R.id.res_package_money);
            divider = root.findViewById(R.id.divider_two);
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

    //子项的垃圾桶按钮的监听接口
    public interface ItemDeleteBtnListener{
        void onItemDeleteBtnListener(ImageView btn,int position,String resId);
    }

    public void setItemDeleteBtnListener(ItemDeleteBtnListener deleteBtnListener) {
        this.deleteBtnListener = deleteBtnListener;
    }
}
