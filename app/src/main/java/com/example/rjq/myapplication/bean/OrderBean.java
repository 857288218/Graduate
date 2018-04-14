package com.example.rjq.myapplication.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rjq on 2018/2/14.
 */

public class OrderBean implements Serializable{
    private int id;
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("buyer_id")
    private int userId;
    @SerializedName("shop_id")
    private int resId;
    @SerializedName("shop_logo")
    private String resImg;
    @SerializedName("shop_name")
    private String resName;
    @SerializedName("order_time")
    private String orderTime;
    @SerializedName("pay_amount")
    private double orderPrice;
    @SerializedName("order_state")
    private int orderState;
    @SerializedName("order_description")
    private String orderDescription;
    @SerializedName("order_detail")
    private List<ResBuyItemNum> orderDetail;

    public OrderBean(int userId,int resId,String resImg,String resName,String orderTime,double orderPrice,String orderDescription){
        this.resId = resId;
        this.resImg = resImg;
        this.userId = userId;
        this.resName = resName;
        this.orderPrice = orderPrice;
        this.orderTime = orderTime;
        this.orderDescription = orderDescription;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getResId() {
        return resId;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getResImg() {
        return resImg;
    }

    public void setResImg(String resImg) {
        this.resImg = resImg;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderDescription() {
        return orderDescription;
    }

    public void setOrderDescription(String orderDescription) {
        this.orderDescription = orderDescription;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public List<ResBuyItemNum> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<ResBuyItemNum> orderDetail) {
        this.orderDetail = orderDetail;
    }
}
