package com.example.rjq.myapplication.Bean;

/**
 * Created by Administrator on 2017/8/2 0002.
 */

public class HomeFragmentContentItemListBean {
    private String title;
    private String Image;

    public HomeFragmentContentItemListBean(){}

    public HomeFragmentContentItemListBean(String title,String image){
        this.title = title;
        this.Image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return Image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        Image = image;
    }
}
