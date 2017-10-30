package com.example.rjq.myapplication.Bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/20 0020.
 */

public class HomeFragmentHeadItemListBean {

    private List<String> innerListItemTitle;
    private List<String> innerListItemImg;
    private List<String> bannerImgs;

    public HomeFragmentHeadItemListBean(List<String> innerListItemTitle,List<String> innerListItemImg,List<String> bannerImgs){
        this.innerListItemTitle = innerListItemTitle;
        this.innerListItemImg = innerListItemImg;
        this.bannerImgs = bannerImgs;
    }
    public HomeFragmentHeadItemListBean(){}

    public List<String> getInnerListItemTitle() {
        return innerListItemTitle;
    }

    public List<String> getInnerListItemImg() {
        return innerListItemImg;
    }

    public void setInnerListItemTitle(List<String> innerListItemTitle) {
        this.innerListItemTitle = innerListItemTitle;
    }

    public void setInnerListItemImg(List<String> innerListItemImg) {
        this.innerListItemImg = innerListItemImg;
    }

    public List<String> getBannerImgs() {
        return bannerImgs;
    }

    public void setBannerImgs(List<String> bannerImgs) {
        this.bannerImgs = bannerImgs;
    }
}
