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
    public static final String HOME_PATH = "http://192.168.30.84/restaurant/index.php";
    public static final String HOME_DATA_API = "/HomePage/obtain_res_by_label";
    public static final String UPLOAD_IMG_API = "/UserInfo/upLoadImgs";

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");

    public static void sendOkHttpGetRequest(String address, Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    //第三个方法可以代替第二个方法
    public static void sendOkHttpPostRequest(String address, HashMap<String,String> hashMap, Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder builder = new FormBody.Builder();
        Set set = hashMap.keySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            String key = (String)iterator.next();
            builder.add(key,hashMap.get(key));
        }

        Request request = new Request.Builder()
                .url(address)
                .post(builder.build())
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void upLoadImgsRequest(String address, HashMap<String,String> hashMap,List<String> imgUrls, Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (imgUrls!=null){
            for (int i = 0; i <imgUrls.size() ; i++) {
                File f = new File(imgUrls.get(i));
                if (f != null) {
                    builder.addFormDataPart("img"+i, f.getName(), RequestBody.create(MEDIA_TYPE_PNG, f));
                }
            }
        }

        Set set = hashMap.keySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            String key = (String)iterator.next();
            builder.addFormDataPart(key,hashMap.get(key));
        }

        //构建请求
        Request request = new Request.Builder()
                .url(address)//地址
                .post(builder.build())//添加请求体
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
