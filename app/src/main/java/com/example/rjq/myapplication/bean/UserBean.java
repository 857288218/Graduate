package com.example.rjq.myapplication.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

/**
 * Created by rjq on 2018/2/22.
 */

public class UserBean extends DataSupport{
    //存到本地数据库中的id
    private int id;
    //用户id
    @SerializedName("user_id")
    private int userId;
    //用户名（昵称）
    @SerializedName("nickname")
    private String userName;
    //用户头像
    @SerializedName("user_image")
    private String userImg;
    @SerializedName("user_tel")
    //手机号(账号)
    private String userPhone;
    //密码
    @SerializedName("password")
    private String password;
    @SerializedName("sex")
    //性别
    private int userSex;
//    @SerializedName("user_money")
//    //钱包余额
//    private double userMoney;
//    @SerializedName("user_red_paper")
//    //红包个数
//    private int userRedPaper;
//    @SerializedName("gold_money")
//    //金币个数
//    private int goldMoney;


    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public int getUserSex() {
        return userSex;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }

//    public double getUserMoney() {
//        return userMoney;
//    }
//
//    public void setUserMoney(double userMoney) {
//        this.userMoney = userMoney;
//    }
//
//    public int getUserRedPaper() {
//        return userRedPaper;
//    }
//
//    public void setUserRedPaper(int userRedPaper) {
//        this.userRedPaper = userRedPaper;
//    }
//
//    public int getGoldMoney() {
//        return goldMoney;
//    }
//
//    public void setGoldMoney(int goldMoney) {
//        this.goldMoney = goldMoney;
//    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
