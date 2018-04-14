package com.example.rjq.myapplication.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rjq on 2018/1/18.
 */

public class ResDetailBean extends DataSupport implements Serializable{
    //存到本地数据库中都要设置id
    private int id;
    //用户在该商店里加入购物车商品的数量，不是从server端获取的，是本地数据库中查到后，代码设置的
    private int buyNum;

    @SerializedName("shop_id")
    private int resId;
    @SerializedName("shop_logo")
    private String resImg;
    @SerializedName("shop_pic")
    private String shopPic;
    @SerializedName("shop_name")
    private String resName;
    //星级
    @SerializedName("eval_decription")
    private float resStar;
    //月售多少订单,应该是在订单表中查询的订单数
    @SerializedName("order_num")
    private int resOrderNum;
    //多少钱起送
    @SerializedName("ship_money")
    private int resDeliverMoney;
    //配送费
    @SerializedName("deliver_money")
    private int resExtraMoney;
    //包装费
    @SerializedName("pack_expense")
    private double packExpense;
    //地址
    @SerializedName("shop_addr")
    private String resAddress;
    //配送时间
    @SerializedName("avg_delitime")
    private int resDeliverTime;
    //商家描述
    @SerializedName("shop_intro")
    private String resDescription;
    //减价
    @SerializedName("discount_list")
    private List<DiscountBean> discountList;

    public ResDetailBean(){}

    public ResDetailBean(int resId, String resImg, String resName, float resStar, int resOrderNum,
                                int resDeliverMoney, int resExtraMoney, String resAddress, int resDeliverTime,
                                List<DiscountBean> discountList) {
        this.resId = resId;
        this.resImg = resImg;
        this.resName = resName;
        this.resStar = resStar;
        this.resOrderNum = resOrderNum;
        this.resDeliverMoney = resDeliverMoney;
        this.resExtraMoney = resExtraMoney;
        this.resAddress = resAddress;
        this.resDeliverTime = resDeliverTime;
        this.discountList = discountList;
    }

    public int getBuyNum() {
        return buyNum;
    }

    public void setBuyNum(int buyNum) {
        this.buyNum = buyNum;
    }

    public void setResStar(float resStar) {
        this.resStar = resStar;
    }

    public String getResDescription() {
        return resDescription;
    }

    public void setResDescription(String resDescription) {
        this.resDescription = resDescription;
    }

    public int getResId() {
        return resId;
    }

    public String getResImg() {
        return resImg;
    }

    public String getResName() {
        return resName;
    }

    public float getResStar() {
        return resStar;
    }


    public int getResOrderNum() {
        return resOrderNum;
    }

    public int getResDeliverMoney() {
        return resDeliverMoney;
    }

    public int getResExtraMoney() {
        return resExtraMoney;
    }

    public String getResAddress() {
        return resAddress;
    }

    public int getResDeliverTime() {
        return resDeliverTime;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public void setResImg(String resImg) {
        this.resImg = resImg;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public void setResStar(int resStar) {
        this.resStar = resStar;
    }

    public void setResOrderNum(int resOrderNum) {
        this.resOrderNum = resOrderNum;
    }

    public void setResDeliverMoney(int resDeliverMoney) {
        this.resDeliverMoney = resDeliverMoney;
    }

    public void setResExtraMoney(int resExtraMoney) {
        this.resExtraMoney = resExtraMoney;
    }

    public void setResAddress(String resAddress) {
        this.resAddress = resAddress;
    }

    public void setResDeliverTime(int resDeliverTime) {
        this.resDeliverTime = resDeliverTime;
    }

    public List<DiscountBean> getDiscountList() {
        return discountList;
    }

    public void setDiscountList(List<DiscountBean> discountList) {
        this.discountList = discountList;
    }

    public double getPackExpense() {
        return packExpense;
    }

    public void setPackExpense(double packExpense) {
        this.packExpense = packExpense;
    }

    public String getShopPic() {
        return shopPic;
    }

    public void setShopPic(String shopPic) {
        this.shopPic = shopPic;
    }
}

