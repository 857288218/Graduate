package com.example.rjq.myapplication.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by rjq on 2018/3/26.
 */

public class DiscountBean implements Serializable{
    @SerializedName("shop_id")
    private int resId;
    @SerializedName("filled_value")
    private double filledVal;
    @SerializedName("reduce_value")
    private double reduceVal;

    public DiscountBean(double filledVal,double reduceVal){
        this.filledVal = filledVal;
        this.reduceVal = reduceVal;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public double getFilledVal() {
        return filledVal;
    }

    public void setFilledVal(double filledVal) {
        this.filledVal = filledVal;
    }

    public double getReduceVal() {
        return reduceVal;
    }

    public void setReduceVal(double reduceVal) {
        this.reduceVal = reduceVal;
    }
}
