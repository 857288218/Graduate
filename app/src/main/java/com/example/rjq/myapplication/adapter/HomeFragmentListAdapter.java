package com.example.rjq.myapplication.adapter;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.example.rjq.myapplication.Bean.HomeFragmentContentItemListBean;
import com.example.rjq.myapplication.Bean.HomeFragmentHeadItemListBean;
import com.example.rjq.myapplication.R;
import com.zhy.autolayout.AutoLinearLayout;

import java.util.List;

public class HomeFragmentListAdapter extends RecyclerView.Adapter<HomeFragmentListAdapter.ViewHolder> implements View.OnClickListener{
    public static final int ITEM_TYPE_HEADER = 0;
    public static final int ITEM_TYPE_CONTENT = 1;
    public static final int ITEM_TYPE_BOTTOM = 2;
    public static final int TTT = 5000;

    private int mHeaderCount=1;//头部View个数
    private int mBottomCount=1;//底部View个数

    private HomeFragmentHeadItemListBean headData;
    private List<HomeFragmentContentItemListBean> contentData;
    private Context mContext;
    private View view;

    HomeFragmentHeadInnerListAdapter homeFragmentHeadInnerListAdapter;

    private LinearLayoutManager innerLinearLayoutManager;

    private OnItemClickListener onBannerItemClickListener;
    private OnHeadItemClickListener onHeadItemClickListener;

    class ViewHolder extends RecyclerView.ViewHolder{
        RecyclerView innerRecyclerView;
        ConvenientBanner convenientBanner;
        AutoLinearLayout homeFragmentNewFood;

        TextView title;
        ImageView image;
        public ViewHolder(View view) {
            super(view);
            //初始化头部数据
            if (view.getTag().equals("0")){
                convenientBanner = (ConvenientBanner) view.findViewById(R.id.convenientBanner);
                homeFragmentNewFood = (AutoLinearLayout)view.findViewById(R.id.home_fragment_new_food);
                innerRecyclerView = (RecyclerView) view.findViewById(R.id.home_fragment_inner_recycler_view);
            }else if(view.getTag().equals("1")){
                title = (TextView) view.findViewById(R.id.content_tv);
                image = (ImageView) view.findViewById(R.id.content_iv);
            }

        }
    }

    //内容长度
    public int getContentItemCount(){
        return contentData.size();
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

    public HomeFragmentListAdapter(HomeFragmentHeadItemListBean headData,LinearLayoutManager innerLinearLayoutManager,List<HomeFragmentContentItemListBean> contentDta,OnItemClickListener onBannerItemClickListener,OnHeadItemClickListener onHeadItemClickListener) {
        this.headData = headData;
        this.contentData = contentDta;
        this.innerLinearLayoutManager = innerLinearLayoutManager;
        this.onBannerItemClickListener = onBannerItemClickListener;
        this.onHeadItemClickListener = onHeadItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        if (viewType == ITEM_TYPE_HEADER){
            view = LayoutInflater.from(mContext).inflate(R.layout.home_fragment_head_item_list,parent,false);
            view.setTag("0");
            ViewHolder viewHolder = new ViewHolder(view);

            viewHolder.homeFragmentNewFood.setOnClickListener(this);
            //设置每个pager的点击事件
            viewHolder.convenientBanner.setOnItemClickListener(onBannerItemClickListener);
            return viewHolder;
        }else {
            view = LayoutInflater.from(mContext).inflate(R.layout.home_fragment_content_item_list,parent,false);
            view.setTag("1");
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position == 0){
            homeFragmentHeadInnerListAdapter = new HomeFragmentHeadInnerListAdapter(headData.getInnerListItemTitle(),headData.getInnerListItemImg());
            holder.innerRecyclerView.setLayoutManager(innerLinearLayoutManager);
            holder.innerRecyclerView.setAdapter(homeFragmentHeadInnerListAdapter);

            holder.convenientBanner.setPointViewVisible(true)
                    //设置小点
                    .setPageIndicator(new int[]{R.mipmap.banner_point,R.mipmap.banner_point_select});
            //允许手动轮播
            holder.convenientBanner.setManualPageable(true);
            //设置自动轮播的时间
            holder.convenientBanner.startTurning(TTT);
            //设置点击事件
            //泛型为具体实现类ImageLoaderHolder
            holder.convenientBanner.setPages(new CBViewHolderCreator<NetImageLoadHolder>() {
                @Override
                public NetImageLoadHolder createHolder() {
                    return new NetImageLoadHolder();
                }
            },headData.getBannerImgs());

        }else if(position>0&&position<=getContentItemCount()+1){
            holder.title.setText(contentData.get(position-1).getTitle());
            Glide.with(mContext).load(contentData.get(position-1).getImage()).into(holder.image);
        }

    }

    //该方法返回的值为position的值
    @Override
    public int getItemCount() {
        return contentData.size() + mHeaderCount;

    }


    /**
     * Created by Administrator on 2016/11/28 0028.
     * Holder的实现类,泛型为实体类
     */
    public class NetImageLoadHolder implements Holder<String> {
        private ImageView image_lv;

        @Override
        public ImageView createView(Context context) {
            image_lv = new ImageView(context);
            image_lv.setScaleType(ImageView.ScaleType.FIT_XY);

            return image_lv;

        }
        @Override
        public void UpdateUI(Context context, int position, String data) {
            Glide.with(context).load(data).into(image_lv);
        }
    }

    public interface OnHeadItemClickListener{
        void onHeadItemClick(View view);
    }

    @Override
    public void onClick(View v) {
        onHeadItemClickListener.onHeadItemClick(v);
    }
}
