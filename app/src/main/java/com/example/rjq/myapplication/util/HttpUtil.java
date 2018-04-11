package com.example.rjq.myapplication.util;


import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
    //如果可以在界面中设置服务器地址，则把final去掉 ，在登陆界面getconfig中给HOME_PATH赋值
    public static final String HOME_PATH = "http://192.168.30.84/restaurant/index.php";

    //首页API
    public static final String OBTAIN_RECOMMEND_SHOP = "/HomePage/obtainShopByRecommend";
    public static final String OBTAIN_SHOP_BY_LABEL = "/HomePage/obtainShopByLabel";
    public static final String OBTAIN_SHOP_ACCOUNT = "/HomePage/obtainShopDiscount";
    public static final String OBTAIN_SHOP_GOODS = "/HomePage/obtainShopGoods";
    public static final String OBTAIN_SHOP_BY_ID = "/HomePage/obtainShopById";

    //用户信息API
    public static final String UPLOAD_IMG_API = "/UserInfo/upLoadImgs";
    public static final String SAVE_USER_NAME = "/UserInfo/alterUserName";
    public static final String SAVE_USER_SEX = "/UserInfo/alterUserSex";
    public static final String SAVE_USER_PHONE = "/UserInfo/alterUserPhone";
    public static final String LOGIN_BY_PWD = "/UserInfo/checkByTelAndPwd";
    public static final String LOGIN_BY_CODE = "/UserInfo/checkByTelAndCode";
    public static final String REGISTER = "/UserInfo/register";
    public static final String OBTAIN_BUILDING = "/UserInfo/obtainBuilding";
    public static final String ADD_USER_ADDRESS = "/UserInfo/addUserAddress";
    public static final String DELETE_USER_ADDRESS = "/UserInfo/deleteUserAddress";
    public static final String ALTER_USER_PWD = "/UserInfo/alterUserPwd";

    //订单信息API
    public static final String SAVE_ORDER = "/Order/saveToOrder";
    public static final String GET_ORDER = "/Order/getOrder";
    public static final String REDUCE_BUYER_LUCKEY_MONEY = "/Order/reduceBuyerLuckyMoney";
    public static final String GET_BUYER_AVAILABLE_LUCKEY_MONEY = "/Order/getBuyerAvailableLuckyMoney";



    private static final MediaType MEDIA_TYPE_IMAGE = MediaType.parse("image/*");

    public static void sendOkHttpGetRequest(String address, Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(8, TimeUnit.SECONDS).build();
        Request request = new Request.Builder()
                .url(address)
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    //第三个方法可以代替第二个方法
    public static void sendOkHttpPostRequest(String address, HashMap<String,String> hashMap, Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(8,TimeUnit.SECONDS).build();
        FormBody.Builder builder = new FormBody.Builder();
        Set set = hashMap.keySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            String key = (String)iterator.next();
            builder.add(key,hashMap.get(key));
        }
        //传递json字符串,后台用$_POST['json']获取,然后用json_decode()解析成数组或对象;
//        builder.add("json",new Gson().toJson());

        Request request = new Request.Builder()
                .url(address)
                .post(builder.build())
                .build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void upLoadImgsRequest(String address, HashMap<String,String> hashMap,List<String> imgUrls, Callback callback){
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(8,TimeUnit.SECONDS).build();
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (imgUrls!=null){
            for (int i = 0; i <imgUrls.size() ; i++) {
                File f = new File(imgUrls.get(i));
                if (f != null) {
                    builder.addFormDataPart("img"+i, f.getName(), RequestBody.create(MEDIA_TYPE_IMAGE, f));
                }
            }
        }
//builder.addFormDataPart("json",json字符串); 也可以这样传递json字符串,一般json字符串是用new Gson().toJson(obj)把对象转换成的，也可以解析list，这样就解析成jsonArray字符串了
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
