package com.example.rjq.myapplication.Bean;

/**
 * Created by Administrator on 2017/7/25 0025.
 */

public class GoodsListItemBean {

    int img;
    String goodsDescribe;
    String goodsPrice;

    public void setImg(int img) {
        this.img = img;
    }

    public void setGoodsDescribe(String goodsDescribe) {
        this.goodsDescribe = goodsDescribe;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public int getImg() {

        return img;
    }

    public String getGoodsDescribe() {
        return goodsDescribe;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }
}
