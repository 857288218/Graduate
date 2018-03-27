package com.example.rjq.myapplication.util;

import com.example.rjq.myapplication.bean.GoodsListBean;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by rjq on 2017/12/27.
 */

public class DataUtils {

    private static GoodsListBean goodsListBean;

    public static GoodsListBean getGoodsListBean(){
        goodsListBean = new GoodsListBean();
        GoodsListBean.GoodsCategoryBean.GoodsItemBean goodsItemBean = new GoodsListBean.GoodsCategoryBean.GoodsItemBean(111,"苹果",10.5,"新摘的大红田苹果，欢迎品尝",
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1701109266,1081476103&fm=116&gp=0.jpg",50,90,0);
        GoodsListBean.GoodsCategoryBean.GoodsItemBean goodsItemBean2 = new GoodsListBean.GoodsCategoryBean.GoodsItemBean(112,"香蕉",12,"香蕉好吃啊，又大很甜！",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=4150969577,207675143&fm=116&gp=0.jpg",12,89,1);
        GoodsListBean.GoodsCategoryBean.GoodsItemBean goodsItemBean3 = new GoodsListBean.GoodsCategoryBean.GoodsItemBean(113,"橘子",13.9,"橘子非常好吃啊，很甜！",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1241578183,1527853673&fm=116&gp=0.jpg",9,98,2);
        GoodsListBean.GoodsCategoryBean.GoodsItemBean goodsItemBean4 = new GoodsListBean.GoodsCategoryBean.GoodsItemBean(114,"榴莲",17,"榴莲闻着臭，吃着香",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1241578183,1527853673&fm=116&gp=0.jpg",30,99,3);
        GoodsListBean.GoodsCategoryBean.GoodsItemBean goodsItemBean5 = new GoodsListBean.GoodsCategoryBean.GoodsItemBean(115,"桃子",8.4,"大毛桃",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2983725700,2237376083&fm=116&gp=0.jpg",10,100,4);
        GoodsListBean.GoodsCategoryBean.GoodsItemBean goodsItemBean6 = new GoodsListBean.GoodsCategoryBean.GoodsItemBean(116,"橙子",15,"皮薄,可甜了",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2549573239,2448800271&fm=111&gp=0.jpg",40,96,5);
        GoodsListBean.GoodsCategoryBean.GoodsItemBean goodsItemBean7 = new GoodsListBean.GoodsCategoryBean.GoodsItemBean(117,"梨",4,"大鸭梨，水多",
                "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=1966863908,2578898835&fm=116&gp=0.jpg",30,98,6);
        List<GoodsListBean.GoodsCategoryBean.GoodsItemBean> goodsItemList = new ArrayList<>();
        goodsItemList.add(goodsItemBean);goodsItemList.add(goodsItemBean2);goodsItemList.add(goodsItemBean3);goodsItemList.add(goodsItemBean4);
        goodsItemList.add(goodsItemBean5);goodsItemList.add(goodsItemBean6);goodsItemList.add(goodsItemBean7);

        GoodsListBean.GoodsCategoryBean goodsCategoryBean = new GoodsListBean.GoodsCategoryBean(11,"水果","新鲜水果,欢迎选购",goodsItemList);

        GoodsListBean.GoodsCategoryBean.GoodsItemBean goodsItemBean1 = new GoodsListBean.GoodsCategoryBean.GoodsItemBean(121,"菠菜",10.8,"含铁丰富",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1090875894,2830536606&fm=116&gp=0.jpg",50,100,0);
        GoodsListBean.GoodsCategoryBean.GoodsItemBean goodsItemBean12 = new GoodsListBean.GoodsCategoryBean.GoodsItemBean(122,"芥菜",12,"野菜包饺子好吃呢",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3257122934,2954156923&fm=116&gp=0.jpg",12,98,1);
        GoodsListBean.GoodsCategoryBean.GoodsItemBean goodsItemBean13 = new GoodsListBean.GoodsCategoryBean.GoodsItemBean(123,"生菜",13.5,"卷饼必须品",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3706196441,2863505692&fm=116&gp=0.jpg",9,99,2);
        GoodsListBean.GoodsCategoryBean.GoodsItemBean goodsItemBean14 = new GoodsListBean.GoodsCategoryBean.GoodsItemBean(124,"空心菜",17,"适合炒着吃",
                "https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=1884106436,247345386&fm=116&gp=0.jpg",30,97,3);
        GoodsListBean.GoodsCategoryBean.GoodsItemBean goodsItemBean15 = new GoodsListBean.GoodsCategoryBean.GoodsItemBean(125,"芹菜",8,"芹菜好吃降血压",
                "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3881529425,3695854192&fm=116&gp=0.jpg",10,96,4);
        GoodsListBean.GoodsCategoryBean.GoodsItemBean goodsItemBean16 = new GoodsListBean.GoodsCategoryBean.GoodsItemBean(126,"冬瓜",15,"皮薄,肉厚",
                "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2549573239,2448800271&fm=111&gp=0.jpg",40,95,5);
        GoodsListBean.GoodsCategoryBean.GoodsItemBean goodsItemBean17 = new GoodsListBean.GoodsCategoryBean.GoodsItemBean(127,"白菜",4,"大白菜很普遍",
                "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=2051825384,1743284996&fm=116&gp=0.jpg",30,94,6);
        List<GoodsListBean.GoodsCategoryBean.GoodsItemBean> goodsItemList2 = new ArrayList<>();

        goodsItemList2.add(goodsItemBean1);goodsItemList2.add(goodsItemBean12);goodsItemList2.add(goodsItemBean13);goodsItemList2.add(goodsItemBean14);
        goodsItemList2.add(goodsItemBean15);goodsItemList2.add(goodsItemBean16);goodsItemList2.add(goodsItemBean17);
        GoodsListBean.GoodsCategoryBean goodsCategoryBean2 = new GoodsListBean.GoodsCategoryBean(12,"蔬菜","新鲜蔬菜,欢迎选购",goodsItemList2);

        List<GoodsListBean.GoodsCategoryBean> categoryBeanList = new ArrayList<>();
        categoryBeanList.add(goodsCategoryBean);categoryBeanList.add(goodsCategoryBean2);
        goodsListBean.setData(categoryBeanList);
        return goodsListBean;
    }


}
