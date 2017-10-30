package com.example.rjq.myapplication.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rjq.myapplication.adapter.ClassifyLeftListAdapter;
import com.example.rjq.myapplication.adapter.ClassifyRightListAdapter;
import com.example.rjq.myapplication.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

public class ClassifyFragment extends Fragment {
    private View classifyFragment;
    private Context mContext;
    private Activity activity;
    private int selectIndex=0;
    private TabLayout tabLayout;
    private SmartRefreshLayout refreshLayout;

    private static final String[] mMenus = { "常用分类", "服饰内衣", "鞋靴", "手机",
            "家用电器", "数码", "电脑办公", "个护化妆"};
    private String[] strs1={"常用分类1","常用分类2","常用分类3","常用分类4","常用分类5","常用分类6","常用分类7","常用分类8","常用分类9","常用分类10"};
    private String[] strs2={"服饰内衣1","服饰内衣2","服饰内衣3","服饰内衣4","服饰内衣5","服饰内衣6","服饰内衣7","服饰内衣8","服饰内衣9","服饰内衣10","服饰内衣11","服饰内衣12","服饰内衣13","服饰内衣14","服饰内衣15","服饰内衣16"};
    private String[] strs3={"鞋靴1","鞋靴2","鞋靴3","鞋靴4","鞋靴5","鞋靴6"};
    private String[] strs4={"手机1","手机2","手机3","手机4"};
    private String[] strs5={"家用电器1","家用电器2","家用电器3","家用电器4","家用电器5","家用电器6","家用电器7","家用电器8"};
    private String[][] allData={strs1,strs2,strs3,strs4,strs5,strs1,strs2,strs3};
    private ListView leftListView,rightListView;
    private ClassifyLeftListAdapter leftAdapter;
    private ClassifyRightListAdapter rightAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext();
        activity = getActivity();
        classifyFragment = inflater.inflate(R.layout.classify_fragment, container, false);
        initView();
        return classifyFragment;
    }

    private void initView() {
        leftListView = (ListView) classifyFragment.findViewById(R.id.list_left);
        rightListView = (ListView) classifyFragment.findViewById(R.id.list_right);

        refreshLayout = (SmartRefreshLayout) classifyFragment.findViewById(R.id.classify_fragment_refresh);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore(2000);
            }
        });

        tabLayout = (TabLayout)  classifyFragment.findViewById(R.id.classify_fragment_tab);
        tabLayout.addTab(tabLayout.newTab().setText("新鲜蔬菜"));
        tabLayout.addTab(tabLayout.newTab().setText("肉禽类"));
        tabLayout.addTab(tabLayout.newTab().setText("预制菜"));
        tabLayout.addTab(tabLayout.newTab().setText("水产冻货物"));
        tabLayout.addTab(tabLayout.newTab().setText("蛋类"));
        tabLayout.addTab(tabLayout.newTab().setText("米面粮油"));


        leftAdapter = new ClassifyLeftListAdapter(mMenus,mContext,selectIndex);
        rightAdapter = new ClassifyRightListAdapter(allData,mContext,selectIndex);
        leftListView.setAdapter(leftAdapter);
        rightListView.setAdapter(rightAdapter);

        leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectIndex = position;
                //把下标传过去，然后刷新adapter
                leftAdapter.setIndex(position);
                leftAdapter.notifyDataSetChanged();
                //当点击某个item的时候让这个item自动滑动到listview的顶部(下面item够多，如果点击的是最后一个就不能到达顶部了)
                leftListView.smoothScrollToPositionFromTop(position,0);
                
                rightAdapter.setIndex(position);
                rightListView.setAdapter(rightAdapter);
            }
        });

        rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, allData[selectIndex][position]+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
