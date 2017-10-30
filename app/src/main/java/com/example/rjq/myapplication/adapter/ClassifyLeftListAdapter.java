package com.example.rjq.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rjq.myapplication.R;

/**
 * Created by Administrator on 2017/8/3 0003.
 */

public class ClassifyLeftListAdapter extends BaseAdapter {
    private final String[] mMenus;
    private final Context context;
    private int selectIndex;

    public ClassifyLeftListAdapter(String[] mMenus, Context context, int selectIndex){
        this.mMenus=mMenus;
        this.context=context;
        this.selectIndex=selectIndex;
    }
    @Override
    public int getCount() {
        return mMenus.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.classify_fragment_left_item_list,null);
            vh = new ViewHolder();
            vh.tv = (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(vh);
        }else {
            vh= (ViewHolder) convertView.getTag();
        }

        if(position == selectIndex){
            vh.tv.setBackgroundColor(context.getResources().getColor(R.color.white));
            vh.tv.setTextColor(context.getResources().getColor(R.color.text_select_color));
        }else {
            vh.tv.setBackgroundColor(context.getResources().getColor(R.color.grey_background_f3));
            vh.tv.setTextColor(context.getResources().getColor(R.color.text_color_grey));
        }

        vh.tv.setText(mMenus[position]);
        return convertView;
    }

    public void setIndex(int index){
        selectIndex = index;
    }

    class ViewHolder{
        TextView tv;
    }

}
