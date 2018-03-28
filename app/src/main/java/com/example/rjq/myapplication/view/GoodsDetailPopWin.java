package com.example.rjq.myapplication.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.bean.ResBuyItemNum;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.List;

/**
 * Created by rjq on 2018/3/26.
 */

public class GoodsDetailPopWin extends PopupWindow{
    private Context mContext;
    private List<ResBuyItemNum> list;
    private double packageMoney;
    private AutoLinearLayout root;
    private int popupWidth;
    private int popupHeight;

    public GoodsDetailPopWin(Context context, List<ResBuyItemNum> list){
        this.mContext = context;
        this.list = list;
        packageMoney = 0;

        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setFocusable(true);

        root = (AutoLinearLayout) LayoutInflater.from(mContext).inflate(R.layout.popup_goods_detail,null);
        AutoLinearLayout itemLl = (AutoLinearLayout) root.findViewById(R.id.item_ll);
        TextView packageMoneyTv = (TextView) root.findViewById(R.id.good_package_money);
        RelativeLayout packageMoneyRl = (RelativeLayout) root.findViewById(R.id.package_money_rl);
        for (ResBuyItemNum resBuyItemNum : list){
            itemLl.addView(initGoodDetailItemView(resBuyItemNum.getItemName(),resBuyItemNum.getBuyNum(),resBuyItemNum.getItemPrice()*resBuyItemNum.getBuyNum()));
            packageMoney += resBuyItemNum.getItemPackageMoney();
        }

        if (packageMoney > 0){
            packageMoneyRl.setVisibility(View.VISIBLE);
            int price = (int) packageMoney;
            if (packageMoney > price){
                packageMoneyTv.setText(packageMoney + "");
            }else{
                packageMoneyTv.setText(price + "");
            }
        }else{
            packageMoneyRl.setVisibility(View.GONE);
        }

        //获取自身的长宽高
        root.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = root.getMeasuredHeight();
        popupWidth = root.getMeasuredWidth();

    }

     /**
     * 设置显示在v上方（以v的中心位置为开始位置）
     * @param v
     */
    public void showUp2(View v) {
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
        showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
    }


    private View initGoodDetailItemView(String goodNameText, int num, double goodPriceText){
        View view = LayoutInflater.from(mContext).inflate(R.layout.goods_detail_item,null);
        TextView goodName = (TextView) view.findViewById(R.id.good_name);
        TextView goodNum = (TextView) view.findViewById(R.id.good_num);
        TextView goodPrice = (TextView) view.findViewById(R.id.good_price);
        goodName.setText(goodNameText);
        goodNum.setText(num + "");
        int price = (int) goodPriceText;
        if (goodPriceText > price){
            goodPrice.setText(goodPriceText + "");
        }else{
            goodPrice.setText(price+"");
        }
        return view;
    }

}
