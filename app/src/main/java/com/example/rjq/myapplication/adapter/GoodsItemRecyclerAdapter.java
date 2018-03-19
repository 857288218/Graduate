package com.example.rjq.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.activity.ResActivity;
import com.example.rjq.myapplication.bean.GoodsListBean;
import com.example.rjq.myapplication.bean.ResBuyCategoryNum;
import com.example.rjq.myapplication.bean.ResBuyItemNum;
import com.example.rjq.myapplication.event.GoodsListEvent;
import com.example.rjq.myapplication.event.MessageEvent;
import com.example.rjq.myapplication.fragment.GoodsFragment;
import com.example.rjq.myapplication.util.GlideUtil;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import static com.example.rjq.myapplication.util.GlideUtil.REQUEST_OPTIONS;

/**
 * Created by rjq on 2018/1/28.
 */

public class GoodsItemRecyclerAdapter extends RecyclerView.Adapter<GoodsItemRecyclerAdapter.ViewHolder> {
    private List<GoodsListBean.GoodsCategoryBean.GoodsItemBean> dataList;
    private Context mContext;
    private int[] goodsNum;       //各个商品的购买数量,goodsNum中的数据也要存入本地数据库中,下次进入该商店界面加载每个物品数量
    private int buyNum;           //buyNum和totalPrice要存到本地数据库中，作为购物车中的信息
    private double totalPrice;
    private int[] mSectionIndices;
    private int[]  mGoodsCategoryBuyNums;
    private TextView shopCart;
    private ImageView buyImg;
    private List<GoodsListBean.GoodsCategoryBean> goodscatrgoryEntities;
    private String[] mSectionLetters;
    private List<GoodsListBean.GoodsCategoryBean.GoodsItemBean> selectGoods=new ArrayList<>();
    public GoodsItemRecyclerAdapter(Context context, List<GoodsListBean.GoodsCategoryBean.GoodsItemBean> items
            , List<GoodsListBean.GoodsCategoryBean> goodsCategoryList) {
        this.mContext = context;
        this.dataList = items;
        this.goodscatrgoryEntities = goodsCategoryList;
        initGoodsNum();
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
        mGoodsCategoryBuyNums = getBuyNums();
        setHasStableIds(true);
    }

    public void setShopCart(TextView shopCart) {
        this.shopCart = shopCart;
    }

    /**
     * 初始化各个商品item的购买数量,和购买总数和总价钱
     */
    private void initGoodsNum() {
        int leng = dataList.size();
        goodsNum = new int[leng];
        for (int i = 0; i < leng; i++) {
            goodsNum[i] = dataList.get(i).getBuyNum();
            if (goodsNum[i] > 0){
                buyNum += goodsNum[i];
                totalPrice = totalPrice + goodsNum[i] * dataList.get(i).getPrice() + goodsNum[i] * dataList.get(i).getGoodPackageMoney();
            }
        }
    }

    /**
     * 存放每个category的购买数量
     * @return
     */
    public int[] getBuyNums() {
        int[] letters = new int[goodscatrgoryEntities.size()];
        for (int i = 0; i < goodscatrgoryEntities.size(); i++) {
            letters[i] = goodscatrgoryEntities.get(i).getBuyNum();
        }
        return letters;
    }

    /**
     * 开始动画
     * @param view
     */
    private void startAnim(View view) {
        buyImg = new ImageView(mContext);
        buyImg.setBackgroundResource(R.mipmap.icon_goods_add_item);// 设置buyImg的图片
        int[] startLocation = new int[2];// 一个整型数组，用来存储按钮的在屏幕的X、Y坐标
        view.getLocationInWindow(startLocation);// 这是获取购买按钮的在屏幕的X、Y坐标（这也是动画开始的坐标）
        ((ResActivity)mContext).setAnim(buyImg, startLocation);// 开始执行动画
    }

    /**
     * 判断商品是否有添加到购物车中,初始化红点状态
     * @param i  条目下标
     * @param vh ViewHolder
     */
    private void isSelected(int i, ViewHolder vh) {
        if (i == 0) {
            vh.tvGoodsSelectNum.setVisibility(View.GONE);
            vh.ivGoodsMinus.setVisibility(View.GONE);
        } else {
            vh.tvGoodsSelectNum.setVisibility(View.VISIBLE);
            vh.tvGoodsSelectNum.setText(i + "");
            vh.ivGoodsMinus.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 存放每个分组的第一条的ID
     *
     * @return
     */
    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<>();
        int lastFirstPoi = -1;
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getCategoryId() != lastFirstPoi) {
                lastFirstPoi = dataList.get(i).getCategoryId();
                sectionIndices.add(i);
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }
    /**
     * 填充每一个分组标题要展现的数据
     *
     * @return
     */
    private String[] getSectionLetters() {
        String[] letters = new String[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            letters[i] = goodscatrgoryEntities.get(i).getName();
        }
        return letters;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.goods_list_item, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return dataList.get(position).hashCode();
    }

    public void clear() {
        mSectionIndices = new int[0];
        mSectionLetters = new String[0];
        notifyDataSetChanged();
    }

    public void restore() {
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final int resId = ((ResActivity)mContext).getResDetailBean().getResId();
        final String resName = ((ResActivity)mContext).getResDetailBean().getResName();
        final int resDeliverMoney = ((ResActivity)mContext).getResDetailBean().getResDeliverMoney();
        final int resExtraMoney = ((ResActivity)mContext).getResDetailBean().getResExtraMoney();
        //设置名
        holder.goodName.setText(dataList.get(position).getName());
        //设置说明描述
        if (!TextUtils.isEmpty(dataList.get(position).getIntroduce())){
            holder.tvGoodsDescription.setVisibility(View.VISIBLE);
            holder.tvGoodsDescription.setText(dataList.get(position).getIntroduce());
        }else{
            holder.tvGoodsDescription.setVisibility(View.GONE);
        }
        //设置价格
        int price = (int)dataList.get(position).getPrice();
        if (dataList.get(position).getPrice() > price){
            holder.tvGoodsPrice.setText("¥"+dataList.get(position).getPrice());
        }else{
            holder.tvGoodsPrice.setText("¥"+price);
        }

        //设置月销量好评率
        holder.tvGoodsMonthOrder.setText("月销量"+dataList.get(position).getMonthOrder()+"  好评率"+dataList.get(position).getGoodComment()+"%");
        //设置图片
        GlideUtil.load(mContext,dataList.get(position).getGoodsImgUrl(),holder.ivGoodsImage,REQUEST_OPTIONS);

        //通过判别对应位置的数量是否大于0来显示隐藏红点数量
        isSelected(goodsNum[position], holder);
        changeShopCart();

        //加号按钮点击
        holder.ivGoodsAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsNum[position]++;
                selectGoods.add(dataList.get(position));
                mGoodsCategoryBuyNums[dataList.get(position).getId()]++;
                buyNum++;
                totalPrice = totalPrice + dataList.get(position).getPrice() + dataList.get(position).getGoodPackageMoney();
                if (goodsNum[position]<=1) {
                    holder.ivGoodsMinus.setAnimation(getShowAnimation());
                    holder.tvGoodsSelectNum.setAnimation(getShowAnimation());
                    holder.ivGoodsMinus.setVisibility(View.VISIBLE);
                    holder.tvGoodsSelectNum.setVisibility(View.VISIBLE);
                }
                startAnim(holder.ivGoodsAdd);
                changeShopCart();
                if(mOnShopCartGoodsChangeListener != null)
                    mOnShopCartGoodsChangeListener.onNumChange();
                isSelected(goodsNum[position], holder);

                //将状态保存到本地数据库
                if (goodsNum[position] == 1){
                    List<ResBuyCategoryNum> resBuyCategoryNumList = DataSupport.where("resId = ? and categoryId = ?",String.valueOf(resId),
                            String.valueOf(dataList.get(position).getCategoryId())).find(ResBuyCategoryNum.class);
                    if (resBuyCategoryNumList.size() == 0){
                        ResBuyCategoryNum.add(String.valueOf(resId),String.valueOf(dataList.get(position).getCategoryId()),mGoodsCategoryBuyNums[dataList.get(position).getId()]);
                    }else{
                        ResBuyCategoryNum resBuyCategoryNum = new ResBuyCategoryNum();
                        resBuyCategoryNum.setBuyNum(mGoodsCategoryBuyNums[dataList.get(position).getId()]);
                        resBuyCategoryNum.updateAll("resId = ? and categoryId = ?", String.valueOf(resId),
                                String.valueOf(dataList.get(position).getCategoryId()));
                    }

                    ResBuyItemNum.add(String.valueOf(resId),String.valueOf(dataList.get(position).getCategoryId()),String.valueOf(dataList.get(position).getGoodId()),
                            goodsNum[position],dataList.get(position).getName(),dataList.get(position).getPrice(),dataList.get(position).getGoodsImgUrl(),
                            resName,resDeliverMoney,resExtraMoney,dataList.get(position).getGoodPackageMoney());

                }else if (goodsNum[position] > 1){
                    ResBuyItemNum resBuyItemNum = new ResBuyItemNum();
                    resBuyItemNum.setBuyNum(goodsNum[position]);
                    resBuyItemNum.updateAll("resId = ? and categoryId = ? and goodId = ?", String.valueOf(resId),
                            String.valueOf(dataList.get(position).getCategoryId()),String.valueOf(dataList.get(position).getGoodId()));

                    ResBuyCategoryNum resBuyCategoryNum = new ResBuyCategoryNum();
                    resBuyCategoryNum.setBuyNum(mGoodsCategoryBuyNums[dataList.get(position).getId()]);
                    resBuyCategoryNum.updateAll("resId = ? and categoryId = ?", String.valueOf(resId),
                            String.valueOf(dataList.get(position).getCategoryId()));

                }

            }
        });
        //减号按钮点击
        holder.ivGoodsMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (goodsNum[position] > 0) {
                    goodsNum[position]--;
                    selectGoods.remove(dataList.get(position));
                    mGoodsCategoryBuyNums[dataList.get(position).getId()]--;
                    isSelected(goodsNum[position], holder);
                    buyNum --;
                    totalPrice = totalPrice - dataList.get(position).getPrice() - dataList.get(position).getGoodPackageMoney();
                    if (goodsNum[position] <=0) {
                        holder.ivGoodsMinus.setAnimation(getHiddenAnimation());
                        holder.tvGoodsSelectNum.setAnimation(getHiddenAnimation());
                        holder.ivGoodsMinus.setVisibility(View.GONE);
                        holder.tvGoodsSelectNum.setVisibility(View.GONE);
                    }
                    changeShopCart();
                    if(mOnShopCartGoodsChangeListener != null)
                        mOnShopCartGoodsChangeListener.onNumChange();

                    //将状态保存到数据库
                    if (mGoodsCategoryBuyNums[dataList.get(position).getId()] == 0){
                        DataSupport.deleteAll(ResBuyCategoryNum.class,"resId = ? and categoryId = ?",
                                String.valueOf(resId),String.valueOf(dataList.get(position).getCategoryId()));
                    }else{
                        ResBuyCategoryNum resBuyCategoryNum = new ResBuyCategoryNum();
                        resBuyCategoryNum.setBuyNum(mGoodsCategoryBuyNums[dataList.get(position).getId()]);
                        resBuyCategoryNum.updateAll("resId = ? and categoryId = ?", String.valueOf(resId),String.valueOf(dataList.get(position).getCategoryId()));
                    }

                    if (goodsNum[position] == 0){
                        DataSupport.deleteAll(ResBuyItemNum.class,"resId = ? and categoryId = ? and goodId = ?",
                                String.valueOf(resId),String.valueOf(dataList.get(position).getCategoryId()), String.valueOf(dataList.get(position).getGoodId()));

                    }else if (goodsNum[position] > 0){
                        ResBuyItemNum resBuyItemNum = new ResBuyItemNum();
                        resBuyItemNum.setBuyNum(goodsNum[position]);
                        resBuyItemNum.updateAll("resId = ? and categoryId = ? and goodId = ?", String.valueOf(resId),
                                String.valueOf(dataList.get(position).getCategoryId()),String.valueOf(dataList.get(position).getGoodId()));
                    }

                }

            }
        });
    }

    /**
     * 修改购物车状态
     */
    private void changeShopCart() {
        EventBus.getDefault().post(new MessageEvent(buyNum,totalPrice,selectGoods));
        EventBus.getDefault().post(new GoodsListEvent(mGoodsCategoryBuyNums));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
    /**
     * 显示减号的动画
     * @return
     */
    private Animation getShowAnimation(){
        AnimationSet set = new AnimationSet(true);
        //旋转动画
        RotateAnimation rotate = new RotateAnimation(0,720,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,2f
                ,TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(0,1);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }

    /**
     * 隐藏减号的动画
     * @return
     */
    private Animation getHiddenAnimation(){
        AnimationSet set = new AnimationSet(true);
        RotateAnimation rotate = new RotateAnimation(0,720,RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF,0.5f);
        set.addAnimation(rotate);
        TranslateAnimation translate = new TranslateAnimation(
                TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,4f
                ,TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,0);
        set.addAnimation(translate);
        AlphaAnimation alpha = new AlphaAnimation(1,0);
        set.addAnimation(alpha);
        set.setDuration(500);
        return set;
    }


    private OnShopCartGoodsChangeListener mOnShopCartGoodsChangeListener = null;

    public void setOnShopCartGoodsChangeListener(OnShopCartGoodsChangeListener e){
        mOnShopCartGoodsChangeListener = e;
    }

    public interface OnShopCartGoodsChangeListener {
        void onNumChange();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public final ImageView ivGoodsImage;
        public final TextView goodName;
        public final TextView tvGoodsDescription;
        public final LinearLayout goodsInfo;
        public final TextView tvGoodsPrice;
        public final TextView tvGoodsMonthOrder;
        public final TextView ivGoodsMinus;
        public final TextView tvGoodsSelectNum;
        public final TextView ivGoodsAdd;
        public final View root;

        public ViewHolder(View root) {
            super(root);
            ivGoodsImage = (ImageView) root.findViewById(R.id.good_img);
            goodName = (TextView) root.findViewById(R.id.good_name);
            tvGoodsDescription = (TextView) root.findViewById(R.id.good_description);
            goodsInfo = (LinearLayout) root.findViewById(R.id.good_info);
            tvGoodsPrice = (TextView) root.findViewById(R.id.good_price);
            tvGoodsMonthOrder = (TextView) root.findViewById(R.id.good_month_order);
            ivGoodsMinus = (TextView) root.findViewById(R.id.good_reduce);
            tvGoodsSelectNum = (TextView) root.findViewById(R.id.good_selected_num);
            ivGoodsAdd = (TextView) root.findViewById(R.id.good_add);
            this.root = root;
        }
    }

}
