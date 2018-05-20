package com.example.rjq.myapplication.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.activity.LoginActivity;
import com.example.rjq.myapplication.adapter.CartFragmentAdapter;
import com.example.rjq.myapplication.bean.ResBuyCategoryNum;
import com.example.rjq.myapplication.bean.ResBuyItemNum;
import com.example.rjq.myapplication.bean.UserBean;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rjq on 2018/1/8.
 */

public class CartFragment extends Fragment implements CartFragmentAdapter.ItemDeleteBtnListener{

    Context mContext;
    private View rootView;
    @BindView(R.id.cart_fragment_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.cart_empty)
    ImageView cartEmptyIv;
    @BindView(R.id.common_bar_title)
    TextView title;
    CartFragmentAdapter cartFragmentAdapter;
    List<ResBuyItemNum> resBuyItemNumList;
    @BindView(R.id.login_btn)
    Button loginBtn;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.my_fragment_status_bar_color));
        }
        if (rootView == null){
            rootView = inflater.inflate(R.layout.shopcar_fragment,container,false);
            initData();
        }
        return rootView;
    }

    private void initData(){
        ButterKnife.bind(this,rootView);
        mContext = getActivity();
        title.setText(getResources().getString(R.string.cart));
    }

    private void initView(){
        if (DataSupport.findAll(UserBean.class).size() > 0){
            //本地数据库查询所有ResBuyItemNum数据
            resBuyItemNumList = DataSupport.findAll(ResBuyItemNum.class);
            loginBtn.setVisibility(View.GONE);
            if (resBuyItemNumList.size()>0){
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                recyclerView.setLayoutManager(linearLayoutManager);
                cartFragmentAdapter = new CartFragmentAdapter(mContext,resBuyItemNumList);
                cartFragmentAdapter.setItemDeleteBtnListener(this);
                recyclerView.setAdapter(cartFragmentAdapter);
                recyclerView.setVisibility(View.VISIBLE);
                cartEmptyIv.setVisibility(View.GONE);
            }else{
                recyclerView.setVisibility(View.GONE);
                cartEmptyIv.setVisibility(View.VISIBLE);
            }
        }else{
            recyclerView.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
            cartEmptyIv.setVisibility(View.GONE);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }

    //购物车每个子项垃圾桶图标的监听方法
    @Override
    public void onItemDeleteBtnListener(ImageView btn, int position, final String resId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(mContext.getResources().getString(R.string.delete_res));
        alertDialog.setMessage(mContext.getResources().getString(R.string.delete_res_goods));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,mContext.getResources().getString(R.string.delete), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DataSupport.deleteAll(ResBuyItemNum.class,"resId = ?",resId);
                DataSupport.deleteAll(ResBuyCategoryNum.class,"resId = ?",resId);
                resBuyItemNumList = DataSupport.findAll(ResBuyItemNum.class);
                if (resBuyItemNumList.size() > 0){
                    //注意不能使用resBuyItemNumList.remove(position);cartFragmentAdapter.notifyItemRemoved(position);notifyItemRangeChanged(0,resBuyItemNumList.size());
                    // 删除，因为position不是resBuyItemNumList计算出来的，现在暂时使用重新设置cartFragmentAdapter方法刷新数据
                    cartFragmentAdapter = new CartFragmentAdapter(mContext,resBuyItemNumList);
                    cartFragmentAdapter.setItemDeleteBtnListener(CartFragment.this);
                    recyclerView.setAdapter(cartFragmentAdapter);
                }else{
                    recyclerView.setVisibility(View.GONE);
                    cartEmptyIv.setVisibility(View.VISIBLE);
                }
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE,mContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
        //设置Dialog中的文字样式
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.bottom_tab_text_selected_color));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.bottom_tab_text_selected_color));
        TextView tvMsg = (TextView) alertDialog.findViewById(android.R.id.message);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,mContext.getResources().getDimensionPixelSize(R.dimen.dimen_10dp),0,0);
        tvMsg.setLayoutParams(lp);
        tvMsg.setTextColor(mContext.getResources().getColor(R.color.color_666));
    }
}
