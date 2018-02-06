package com.example.rjq.myapplication.util;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by rjq on 2018/1/21.
 */

public class HttpUtil {
    public static final String home_path = "http://";

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

    public static void sendOkHttpGetRequest(String address, Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void sendOkHttpPostRequest(String address, HashMap<String,String> hashMap, Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        Set set = hashMap.keySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            builder.add((String)iterator.next(),hashMap.get(iterator.next()));
        }

        Request request = new Request.Builder()
                .url(address)
                .post(builder.build())
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void upLoadImg(String address,String imgUrl, Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        File f = new File(imgUrl);
        builder.addFormDataPart("img", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));

        //添加其它信息
//        builder.addFormDataPart("time",takePicTime);
//        builder.addFormDataPart("mapX", SharedInfoUtils.getLongitude());
//        builder.addFormDataPart("mapY",SharedInfoUtils.getLatitude());
//        builder.addFormDataPart("name",SharedInfoUtils.getUserName());
        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url(address)//地址
                .post(requestBody)//添加请求体
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void upLoadImgs(String address, List<String> imgUrls, Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (int i = 0; i <imgUrls.size() ; i++) {
            File f = new File(imgUrls.get(i));
            if (f!=null) {
                builder.addFormDataPart("img", f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
            }
        }

        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url(address)//地址
                .post(requestBody)//添加请求体
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
