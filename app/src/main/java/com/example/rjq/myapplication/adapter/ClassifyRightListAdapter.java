package com.example.rjq.myapplication.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rjq.myapplication.R;

/**
 * Created by Administrator on 2017/8/3 0003.
 */

public class ClassifyRightListAdapter extends BaseAdapter {

    private  String[][] allData;
    private  Context context;
    private  int selectIndex;

    public ClassifyRightListAdapter(String[][] allData, Context context, int selectIndex) {
        this.allData=allData;
        this.context=context;
        this.selectIndex=selectIndex;
    }

    @Override
    public int getCount() {
        return allData[selectIndex].length;
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
        if(convertView==null){
            convertView=View.inflate(context, R.layout.classify_fragment_right_item_list,null);
            vh=new ViewHolder();
            vh.tv= (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(vh);
        }else {
            vh= (ViewHolder) convertView.getTag();
        }

        vh.tv.setText(allData[selectIndex][position]);

        return convertView;
    }

    public void setIndex(int index){
        selectIndex=index;
    }

    class ViewHolder{
        TextView tv;
    }

}
