package com.example.rjq.myapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rjq on 2018/4/9.
 */

public class WelcomeBean implements Serializable{
    private int status;
    private List<ResDetailBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ResDetailBean> getData() {
        return data;
    }

    public void setData(List<ResDetailBean> data) {
        this.data = data;
    }
}
