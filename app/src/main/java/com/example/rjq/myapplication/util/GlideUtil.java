package com.example.rjq.myapplication.util;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.rjq.myapplication.R;

/**
 * Created by rjq on 2017/12/20 0020.
 */

//Glide4.0 加载图片类
public class GlideUtil {

    public static final RequestOptions REQUEST_OPTIONS = new RequestOptions()
            .placeholder(R.drawable.no_banner)
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC) //硬盘缓存,内存缓存是自动开启的
            .dontAnimate();

    public static void load(Context context, Object res, ImageView imageView){
        Glide.with(context)
                .load(res)
                .into(imageView);
    }

    public static void load(Context context, Object res, ImageView imageView, RequestOptions requestOptions){
        Glide.with(context)
                .load(res)
                .apply(requestOptions)
                .into(imageView);
    }
}
