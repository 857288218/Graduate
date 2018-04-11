package com.example.rjq.myapplication.activity;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.bean.ResDetailBean;
import com.example.rjq.myapplication.bean.WelcomeBean;
import com.example.rjq.myapplication.view.BottomTabItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    private long mExitTime = 0;
    private static final String TAG = "MainActivity";
    private List<ResDetailBean> list;

    @BindView(android.R.id.tabhost)
    FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @Override
    protected void initView() {
        super.initView();
        initBottomTabHost();
    }

    private void initBottomTabHost(){
        //绑定tabContent
        tabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        List<BottomTabItem> bottomTabItemList = getBottomTabItemList(this);
        for (BottomTabItem item : bottomTabItemList){
            TabHost.TabSpec tabSpec = tabHost
                    .newTabSpec(item.getTag())
                    .setIndicator(item.getView());
            //添加tab和关联对应的fragment
            tabHost.addTab(tabSpec, item.getFragmentClass(), null);
        }

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

            }
        });
    }

    public List<BottomTabItem> getBottomTabItemList(Context context) {
        List<BottomTabItem> list = new ArrayList<>();
        TypedArray tab_item_tags = context.getResources().obtainTypedArray(R.array.bottom_tab_item_tags);
        String[] tab_item_fragments = context.getResources().getStringArray(R.array.bottom_tab_item_fragment);
        TypedArray tab_item_names = context.getResources().obtainTypedArray(R.array.bottom_tab_item_names);
        TypedArray tab_item_drawables = context.getResources().obtainTypedArray(R.array.bottom_tab_item_drawables);
        for (int i =0;i<tab_item_tags.length();i++) {
            BottomTabItem tabItem  = getBottomTabItem(tab_item_tags.getResourceId(i, -1), tab_item_fragments[i], tab_item_names.getResourceId(i, -1), tab_item_drawables.getResourceId(i, -1));
            list.add(tabItem);
        }
        return list;
    }

    private BottomTabItem getBottomTabItem(int client_tab_item_id, String client_tab_item_launcher, int client_tab_item_name, int client_tab_item_drawable) {
        String  tag = getResources().getString(client_tab_item_id);
        String fragment = client_tab_item_launcher;
        String name = getResources().getString(client_tab_item_name);
        Drawable icon = getResources().getDrawable(client_tab_item_drawable);

        Class<?> c = null;
        try {
            if ((fragment != null) && !("".equals(fragment))) {
                c = Class.forName(fragment);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        BottomTabItem tabItem = new BottomTabItem(this, tag, c, icon, name);
        return tabItem;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, getResources().getString(R.string.app_exit_notice), Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

}
