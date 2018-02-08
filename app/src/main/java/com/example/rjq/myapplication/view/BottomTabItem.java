package com.example.rjq.myapplication.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rjq.myapplication.R;

/**
 * Created by rjq on 2017/11/4 0004.
 */

public class BottomTabItem {
    private Context context;
    private String tag;
    private Class fragmentClass;
    private Drawable imageRes;
    private String name;
    private View view;

    public BottomTabItem(Context context, String tag, Class fragmentClass, Drawable imageRes, String name) {
        this.context = context;
        this.tag = tag;
        this.fragmentClass = fragmentClass;
        this.imageRes = imageRes;
        this.name = name;

        view = LayoutInflater.from(context).inflate(R.layout.bottom_tab_view,null);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_tab);
        TextView tv = (TextView) view.findViewById(R.id.tv_tab);
        iv.setBackground(imageRes);
        tv.setText(name);

    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setFragmentClass(Class fragmentClass) {
        this.fragmentClass = fragmentClass;
    }

    public void setImageRes(Drawable imageRes) {
        this.imageRes = imageRes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class getFragmentClass() {
        return fragmentClass;
    }

    public String getTag() {
        return tag;
    }

    public Drawable getImageRes() {
        return imageRes;
    }

    public String getName() {
        return name;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }
}
