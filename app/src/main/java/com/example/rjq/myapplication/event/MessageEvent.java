package com.example.rjq.myapplication.event;



import com.example.rjq.myapplication.bean.GoodsListBean;

import java.util.List;

/**
 * Created by dalong on 2016/12/27.
 */

public class MessageEvent {
    public int num;
    public double price;
    public List<GoodsListBean.GoodsCategoryBean.GoodsItemBean> goods;

    public MessageEvent(int totalNum, double price,List<GoodsListBean.GoodsCategoryBean.GoodsItemBean> goods) {
        this.num = totalNum;
        this.price = price;
        this.goods = goods;
    }
}
