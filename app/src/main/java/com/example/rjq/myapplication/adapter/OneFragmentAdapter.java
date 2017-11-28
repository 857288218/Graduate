package com.example.rjq.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.util.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.List;

/**
 * Created by Administrator on 2017/11/28 0028.
 */

public class OneFragmentAdapter extends RecyclerView.Adapter<OneFragmentAdapter.ViewHolder> {

    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_BOTTOM = 2;

    private int mHeaderCount=1;//头部View个数
    private int mBottomCount=0;//底部View个数

    private List<String> mList;
    private List<Integer> imageLocal;
    private Context mContext;
    private View view;

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img;
        TextView text;
        Banner banner;
        public ViewHolder(View view) {
            super(view);
            if (view.getTag().equals("0")){
                //初始化头部数据
                banner = (Banner) view.findViewById(R.id.banner);
            }else{
                //初始化内容数据
                img = (ImageView)view.findViewById(R.id.one_content_item_iv);
                text = (TextView)view.findViewById(R.id.one_content_item_tv);
            }

        }
    }

    //内容长度
    public int getContentItemCount(){
        return mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getContentItemCount();
        if (mHeaderCount != 0 && position < mHeaderCount) {
            //头部View
            return ITEM_TYPE_HEADER;
        } else if (mBottomCount != 0 && position >= (mHeaderCount + dataItemCount)) {
            //底部View
            return ITEM_TYPE_BOTTOM;
        } else {
            //内容View
            return ITEM_TYPE_CONTENT;
        }
    }

    public OneFragmentAdapter(List<String> mList,List<Integer> imageLocal) {
        this.mList = mList;
        this.imageLocal = imageLocal;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }

        if (viewType == ITEM_TYPE_HEADER){
            view = LayoutInflater.from(mContext).inflate(R.layout.one_fragemnt_head_item,parent,false);
            //头部标志
            view.setTag("0");
            return new ViewHolder(view);
        }else if(viewType == ITEM_TYPE_CONTENT){
            view = LayoutInflater.from(mContext).inflate(R.layout.one_fragment_content_item,parent,false);
            //内容标志
            view.setTag("1");
            return new ViewHolder(view);
        }else{
            //尾部标志
            view.setTag("2");
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0){
            //设置样式,默认为:Banner.NOT_INDICATOR(不显示指示器和标题)
            holder.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            //设置轮播样式（没有标题默认为右边,有标题时默认左边）
            holder.banner.setIndicatorGravity(BannerConfig.CENTER);
            //设置图片加载器
            holder.banner.setImageLoader(new GlideImageLoader());
            //设置图片集合
            holder.banner.setImages(imageLocal);
            //设置点击事件，下标是从0开始
            holder.banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    Toast.makeText(mContext, "您点击了："+position, Toast.LENGTH_SHORT).show();
                }
            });
            holder.banner.start();
        }else if(position>0&&position<=getContentItemCount()+1){

        }else{

        }

    }

    //该方法返回的值为position的值
    @Override
    public int getItemCount() {
        return mList.size() + mHeaderCount + mBottomCount;
    }

}
