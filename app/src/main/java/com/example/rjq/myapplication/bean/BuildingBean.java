package com.example.rjq.myapplication.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rjq on 2018/4/2.
 */

public class BuildingBean {
    @SerializedName("build_id")
    private int builderId;
    @SerializedName("build_name")
    private String builderName;

    public BuildingBean(String name){
        this.builderName = name;
    }

    public int getBuilderId() {
        return builderId;
    }

    public void setBuilderId(int builderId) {
        this.builderId = builderId;
    }

    public String getBuilderName() {
        return builderName;
    }

    public void setBuilderName(String builderName) {
        this.builderName = builderName;
    }
}
