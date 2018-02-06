package com.example.rjq.myapplication.bean;

import com.example.rjq.myapplication.activity.ResActivity;

import org.litepal.crud.DataSupport;

/**
 * Created by rjq on 2018/2/4.
 */

public class ResBuyItemNum extends DataSupport {
    private int id;
    private String resId;
    private String categoryId;
    private String goodId;
    private int buyNum;
    private String resName;
    private String itemName;
    private float itemPrice;
    private String itemImg;

    public static void add(String resId,String categoryId,String goodId,int buyNum,String itemName,float itemPrice,String itemImg,String resName){
        ResBuyItemNum resBuyItemNum = new ResBuyItemNum();
        resBuyItemNum.setResId(resId);
        resBuyItemNum.setCategoryId(categoryId);
        resBuyItemNum.setGoodId(goodId);
        resBuyItemNum.setBuyNum(buyNum);
        resBuyItemNum.setItemName(itemName);
        resBuyItemNum.setItemPrice(itemPrice);
        resBuyItemNum.setItemImg(itemImg);
        resBuyItemNum.setResName(resName);
        resBuyItemNum.save();

    }


    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
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

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }
}
