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

    @SerializedName("res_id")
    private int resId;
    @SerializedName("res_img")
    private String resImg;
    @SerializedName("res_name")
    private String resName;
    //星级
    @SerializedName("res_star")
    private float resStar;
    //月售多少订单
    @SerializedName("res_order_num")
    private int resOrderNum;
    //多少钱起送
    @SerializedName("res_launch_money")
    private int resDeliverMoney;
    //配送费
    @SerializedName("res_deliver_money")
    private int resExtraMoney;
    //地址
    @SerializedName("res_address")
    private String resAddress;
    //配送时间
    @SerializedName("res_deliver_time")
    private int resDeliverTime;
    //商家描述
    @SerializedName("res_description")
    private String resDescription;
    //满减
    @SerializedName("res_reduce_activity")
    private String resReduce;
    //特殊活动
    @SerializedName("res_special_activity")
    private String resSpecial;
    //新用户下单减
    @SerializedName("res_new_activity")
    private String resNew;
    //赠
    @SerializedName("res_gift_activity")
    private String resGive;

    public ResDetailBean(){}

    public ResDetailBean(int resId, String resImg, String resName, float resStar, int resOrderNum,
                                int resDeliverMoney, int resExtraMoney, String resAddress, int resDeliverTime,
                                String resReduce, String resSpecial, String resNew, String resGive) {
        this.resId = resId;
        this.resImg = resImg;
        this.resName = resName;
        this.resStar = resStar;
        this.resOrderNum = resOrderNum;
        this.resDeliverMoney = resDeliverMoney;
        this.resExtraMoney = resExtraMoney;
        this.resAddress = resAddress;
        this.resDeliverTime = resDeliverTime;
        this.resReduce = resReduce;
        this.resSpecial = resSpecial;
        this.resNew = resNew;
        this.resGive = resGive;
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

    public String getResReduce() {
        return resReduce;
    }

    public String getResSpecial() {
        return resSpecial;
    }

    public String getResNew() {
        return resNew;
    }

    public String getResGive() {
        return resGive;
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

    public void setResReduce(String resReduce) {
        this.resReduce = resReduce;
    }

    public void setResSpecial(String resSpecial) {
        this.resSpecial = resSpecial;
    }

    public void setResNew(String resNew) {
        this.resNew = resNew;
    }

    public void setResGive(String resGive) {
        this.resGive = resGive;
    }
}

