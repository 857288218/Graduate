package com.example.rjq.myapplication.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by rjq on 2018/2/4.
 */

public class ResBuyItemNum extends DataSupport implements Serializable{
    private int id;
    private String resId;
    private String resName;
    //多少钱起送
    private int resDeliverMoney;
    //配送费
    private int resExtraMoney;
    //包装费
    @SerializedName("gs_pack_money")
    private double itemPackageMoney;
    private String categoryId;
    @SerializedName("gs_id")
    private String goodId;
    @SerializedName("quantity")
    private int buyNum;
    @SerializedName("gs_name")
    private String itemName;
    @SerializedName("gs_newprice")
    private double itemPrice;
    @SerializedName("gs_pic")
    private String itemImg;

    public static void add(String resId,String categoryId,String goodId,int buyNum,String itemName,double itemPrice,String itemImg,String resName,
                           int resDeliverMoney,int resExtraMoney,double goodPackageMoney){
        ResBuyItemNum resBuyItemNum = new ResBuyItemNum();
        resBuyItemNum.setResId(resId);
        resBuyItemNum.setCategoryId(categoryId);
        resBuyItemNum.setGoodId(goodId);
        resBuyItemNum.setBuyNum(buyNum);
        resBuyItemNum.setItemName(itemName);
        resBuyItemNum.setItemPrice(itemPrice);
        resBuyItemNum.setItemImg(itemImg);
        resBuyItemNum.setResName(resName);
        resBuyItemNum.setResDeliverMoney(resDeliverMoney);
        resBuyItemNum.setResExtraMoney(resExtraMoney);
        resBuyItemNum.setItemPackageMoney(goodPackageMoney);
        resBuyItemNum.save();

    }

    public int getResExtraMoney() {
        return resExtraMoney;
    }

    public void setResExtraMoney(int resExtraMoney) {
        this.resExtraMoney = resExtraMoney;
    }

    public int getResDeliverMoney() {
        return resDeliverMoney;
    }

    public void setResDeliverMoney(int resDeliverMoney) {
        this.resDeliverMoney = resDeliverMoney;
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

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemImg() {
        return itemImg;
    }

    public void setItemImg(String itemImg) {
        this.itemImg = itemImg;
    }

    public double getItemPackageMoney() {
        return itemPackageMoney;
    }

    public void setItemPackageMoney(double itemPackageMoney) {
        this.itemPackageMoney = itemPackageMoney;
    }
}
