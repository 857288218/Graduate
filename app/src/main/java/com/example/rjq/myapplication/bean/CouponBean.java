package com.example.rjq.myapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rjq on 2018/3/25.
 */

public class CouponBean implements Serializable{
    @SerializedName("shop_name")
    private String shopName;
    @SerializedName("shop_id")
    private int shopId;
    @SerializedName("red_packet_id")
    private int redPaperId;
    //消费门槛
    @SerializedName("mini_consume")
    private double miniPrice;
    //金额
    @SerializedName("amount")
    private double price;
    //是否通用，1通用，0不通用
    @SerializedName("iscommon")
    private int iscommon;
    //截至时间
    @SerializedName("deadline")
    private String deadline;

    public double getMiniPrice() {
        return miniPrice;
    }

    public void setMiniPrice(double miniPrice) {
        this.miniPrice = miniPrice;
    }


    public int getIscommon() {
        return iscommon;
    }

    public void setIscommon(int iscommon) {
        this.iscommon = iscommon;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRedPaperId() {
        return redPaperId;
    }

    public void setRedPaperId(int redPaperId) {
        this.redPaperId = redPaperId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }
}
