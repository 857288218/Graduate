package com.example.rjq.myapplication.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by rjq on 2018/3/5.
 */

public class AddressBean extends DataSupport implements Serializable{
    private int id;
    @SerializedName("buyer_id")
    private int user_id;
    @SerializedName("receiver_address")
    private String address;
    @SerializedName("receiver_name")
    private String name;
    @SerializedName("receiver_tel")
    private String phone;
    private int selected;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }
}
