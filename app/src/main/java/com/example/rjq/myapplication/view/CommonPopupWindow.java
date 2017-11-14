package com.example.rjq.myapplication.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by rjq on 2017/11/7 0007.
 */

public class CommonPopupWindow {
    private PopupWindow mPopupWindow;
    private View contentview;
    private Context mContext;
    public CommonPopupWindow(Builder builder) {
        mContext = builder.context;
        contentview = LayoutInflater.from(mContext).inflate(builder.contentviewid,null);
        mPopupWindow = new PopupWindow(contentview,builder.width,builder.height);

        mPopupWindow.setAnimationStyle(builder.animstyle);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setOutsideTouchable(builder.outsidecancel);
        mPopupWindow.setFocusable(builder.fouse);
        setBackgroundAlpha(builder.alpha);
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener listener){
        if (mPopupWindow != null){
            mPopupWindow.setOnDismissListener(listener);
        }
    }

    /**
     * popup 消失
     */
    public void dismiss(){
        if (mPopupWindow != null){
            mPopupWindow.dismiss();
        }
    }

    /**
     * 根据id获取view
     * @param viewid
     * @return
     */
    public View getItemView(int viewid){
        if (mPopupWindow != null){
            return this.contentview.findViewById(viewid);
        }
        return null;
    }

    /**
     * 根据父布局，显示位置
     * @param rootviewid
     * @param gravity
     * @param x
     * @param y
     * @return
     */
    public CommonPopupWindow showAtLocation(int rootviewid,int gravity,int x,int y){
        if (mPopupWindow != null){
            View rootview = LayoutInflater.from(mContext).inflate(rootviewid,null);
            mPopupWindow.showAtLocation(rootview,gravity,x,y);
        }
        return this;
    }

    /**
     * 根据id获取view ，并显示在该view的指定位置
     * @param targetviewId
     * @param gravity
     * @param offx
     * @param offy
     * @return
     */
    public CommonPopupWindow showAsDropDown(int targetviewId,int gravity,int offx,int offy){
        if (mPopupWindow != null){
            View targetview = LayoutInflater.from(mContext).inflate(targetviewId,null);
            mPopupWindow.showAsDropDown(targetview,gravity,offx,offy);
        }
        return this;
    }

    /**
     * 显示在 targetview 的不同位置
     * @param targetview
     * @param gravity
     * @param offx
     * @param offy
     * @return
     */
    public CommonPopupWindow showAsDropDown(View targetview,int gravity,int offx,int offy){
        if (mPopupWindow != null){
            mPopupWindow.showAsDropDown(targetview,gravity,offx,offy);
        }
        return this;
    }

    /**
     * 根据id设置焦点监听
     * @param viewid
     * @param listener
     */
    public void setOnFocusListener(int viewid,View.OnFocusChangeListener listener){
        View view = getItemView(viewid);
        view.setOnFocusChangeListener(listener);
    }

    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity)mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        ((Activity)mContext).getWindow().setAttributes(lp);
    }


    /**
     * builder 类
     */
    public static class Builder{
        private int contentviewid;
        private int width;
        private int height;
        private boolean fouse = false;
        private boolean outsidecancel = false;
        private int animstyle;
        private Context context;
        private float alpha = 1.0f;

        public Builder(Context context){
            this.context = context;
        }

        public Builder setContentView(int contentviewid){
            this.contentviewid = contentviewid;
            return this;
        }

        public Builder setwidth(int width){
            this.width = width;
            return this;
        }
        public Builder setheight(int height){
            this.height = height;
            return this;
        }

        public Builder setFouse(boolean fouse){
            this.fouse = fouse;
            return this;
        }

        public Builder setOutSideCancel(boolean outsidecancel){
            this.outsidecancel = outsidecancel;
            return this;
        }

        public Builder setAnimationStyle(int animstyle){
            this.animstyle = animstyle;
            return this;
        }

        public Builder setBackgroundAlpha(float alpha){
            this.alpha = alpha;
            return this;
        }

        public CommonPopupWindow builder(){
            return new CommonPopupWindow(this);
        }
    }
}