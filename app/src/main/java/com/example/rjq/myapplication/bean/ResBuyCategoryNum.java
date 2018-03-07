package com.example.rjq.myapplication.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by rjq on 2018/2/4.
 */

public class ResBuyCategoryNum extends DataSupport implements Serializable{
    private int id;
    private String resId; //商店id
    private String categoryId; //范畴id
    private int buyNum;

    public static void add(String resId, String categoryId, int buyNum){
        ResBuyCategoryNum resBuyCategoryNum = new ResBuyCategoryNum();
        resBuyCategoryNum.setBuyNum(buyNum);
        resBuyCategoryNum.setResId(resId);
        resBuyCategoryNum.setCategoryId(categoryId);
        resBuyCategoryNum.save();
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }
}
