package com.example.rjq.myapplication.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.rjq.myapplication.activity.AddressActivity;
import com.example.rjq.myapplication.activity.AlterPhoneActivity;
import com.example.rjq.myapplication.activity.AlterPwdActivity;
import com.example.rjq.myapplication.activity.BaseActivity;
import com.example.rjq.myapplication.activity.CouponActivity;
import com.example.rjq.myapplication.activity.LoginActivity;
import com.example.rjq.myapplication.activity.MainActivity;
import com.example.rjq.myapplication.bean.AddressBean;
import com.example.rjq.myapplication.bean.CouponBean;
import com.example.rjq.myapplication.bean.OrderBean;
import com.example.rjq.myapplication.bean.UserBean;
import com.example.rjq.myapplication.util.HttpUtil;
import com.example.rjq.myapplication.util.permission.PermissionListener;
import com.example.rjq.myapplication.util.permission.PermissionUtil;
import com.example.rjq.myapplication.view.RoundAngleImageView;
import com.example.rjq.myapplication.view.CommonPopupWindow;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.util.GlideUtil;
import com.example.rjq.myapplication.util.FileStorage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by rjq on 2017/10/28 0028.
 */
public class ThreeFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "ThreeFragment";
    private static final int REQUEST_ALTER_PHONE = 3001;
    private View rootView;

    private static final int REQUEST_PICK_IMAGE = 1; //相册选取
    private static final int REQUEST_CAPTURE = 2;  //拍照
    private static final int REQUEST_PICTURE_CUT = 3;  //剪裁图片
    private static final int REQUEST_PERMISSION = 4;  //权限请求
    public static final int REQUEST_LOGIN = 5;      //登录
    private Uri imageUri;//原图保存地址
    private Uri outputUri;//剪切的地址
    private String imagePath;

    private Dialog dialog;
    private TextView tv3;
    private TextView tv1;
    private TextView tv2;
    PermissionUtil permissionUtil;

    @BindView(R.id.common_bar_title)
    TextView title;
    @BindView(R.id.user_name)
    AutoRelativeLayout userName;
    @BindView(R.id.user_phone)
    AutoRelativeLayout userPhone;
    @BindView(R.id.user_sex)
    AutoRelativeLayout userSex;
    @BindView(R.id.goods_address)
    AutoRelativeLayout goodsAddress;
    @BindView(R.id.my_collect)
    AutoRelativeLayout myCollect;
    @BindView(R.id.user_password)
    AutoRelativeLayout userPassword;
    @BindView(R.id.buy_password)
    AutoRelativeLayout buyPwd;
    @BindView(R.id.log_out)
    AutoRelativeLayout logOut;
    @BindView(R.id.my_head_img)
    CircleImageView headImg;
    @BindView(R.id.user_img_container)
    AutoRelativeLayout userImgContainer;
    @BindView(R.id.all_money)
    AutoLinearLayout allMoney;
    @BindView(R.id.user_set_up)
    AutoLinearLayout userSetUp;
    @BindView(R.id.fragment_my_wallet_money_tv)
    TextView walletMoney;
    @BindView(R.id.fragment_my_red_paper)
    TextView redPaperNum;
    @BindView(R.id.fragment_my_coupon_ll)
    AutoLinearLayout redPaperLl;
    @BindView(R.id.gold_money_num)
    TextView goldMoney;
    @BindView(R.id.user_name_text)
    TextView userNameText;
    @BindView(R.id.user_phone_text)
    TextView userPhoneText;
    @BindView(R.id.user_sex_text)
    TextView userSexText;
    @BindView(R.id.login)
    RelativeLayout login;
    @BindView(R.id.sv)
    ScrollView sv;
    @BindView(R.id.ll)
    AutoLinearLayout ll;
    @BindView(R.id.user_address_text)
    TextView userAddressText;
    @BindView(R.id.loading)
    ProgressBar progressBar;
    @BindView(R.id.alter_user_pwd)
    TextView alterUserPwdTv;

    private ArrayList<CouponBean> couponBeanList;
    private int redPacketNum;
    private boolean isSetUserInfo;
    private BroadcastReceiver receiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                receiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Log.d("tid", "onReceive"+android.os.Process.myTid());
                        //可以弹出土司，说明该广播接收器即时是在子线程中注册的，但是onReceive方法也是运行在主线程中
                    }
                };
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("com.rjq");
                getActivity().registerReceiver(receiver,intentFilter);
            }
        }).start();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.my_fragment_status_bar_color));
        }
        if (rootView == null){
            rootView = inflater.inflate(R.layout.three_fragment,container,false);
            initData();
            initView();
        }
        return rootView;
    }

    private void initData(){
        ButterKnife.bind(this,rootView);
    }

    private void initView(){
        permissionUtil = new PermissionUtil(getActivity());
        title.setText(getResources().getString(R.string.my));
        userName.setOnClickListener(this);
        userPhone.setOnClickListener(this);
        userSex.setOnClickListener(this);
        goodsAddress.setOnClickListener(this);
        myCollect.setOnClickListener(this);
        userPassword.setOnClickListener(this);
        buyPwd.setOnClickListener(this);
        headImg.setOnClickListener(this);
        logOut.setOnClickListener(this);
        redPaperLl.setOnClickListener(this);
        setUserInfo();
    }

    private void setUserInfo(){
        List<UserBean> userList = DataSupport.findAll(UserBean.class);
        if (userList.size() > 0){
            login.setVisibility(View.GONE);
            logOut.setVisibility(View.VISIBLE);
            UserBean userBean = userList.get(0);
            if (new File(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(userList.get(0).getUserId()+"","")).exists()){
                GlideUtil.load(getActivity(),PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(userList.get(0).getUserId()+"",""),headImg,GlideUtil.REQUEST_OPTIONS);
            }else{
                GlideUtil.load(getActivity(),userBean.getUserImg(),headImg,GlideUtil.REQUEST_OPTIONS);
            }
            userNameText.setText(userBean.getUserName());
            if (userBean.getUserSex() == 0){
                userSexText.setText("女");
            }else{
                userSexText.setText("男");
            }
            String phoneNumber = userBean.getUserPhone().substring(0, 3) + "****" + userBean.getUserPhone().substring(7, userBean.getUserPhone().length());
            userPhoneText.setText(phoneNumber);
            alterUserPwdTv.setText("修改");
            ll.setClickable(false);
            userName.setClickable(true);
            userPhone.setClickable(true);
            userSex.setClickable(true);
            goodsAddress.setClickable(true);
            myCollect.setClickable(true);
            userPassword.setClickable(true);
            buyPwd.setClickable(true);
            headImg.setClickable(true);
            redPaperLl.setClickable(true);
            userName.setBackground(getResources().getDrawable(R.drawable.one_fragment_content_item_bg));
            userPhone.setBackground(getResources().getDrawable(R.drawable.one_fragment_content_item_bg));
            userSex.setBackground(getResources().getDrawable(R.drawable.one_fragment_content_item_bg));
            goodsAddress.setBackground(getResources().getDrawable(R.drawable.one_fragment_content_item_bg));
            myCollect.setBackground(getResources().getDrawable(R.drawable.one_fragment_content_item_bg));
            userPassword.setBackground(getResources().getDrawable(R.drawable.one_fragment_content_item_bg));
            buyPwd.setBackground(getResources().getDrawable(R.drawable.one_fragment_content_item_bg));

        }else{
            alterUserPwdTv.setText("");
            login.setVisibility(View.VISIBLE);
            walletMoney.setText(0+"");
            redPaperNum.setText(0+"");
            goldMoney.setText(0+"");
            userNameText.setText("");
            userPhoneText.setText("");
            userSexText.setText("");
            userAddressText.setText("");
            GlideUtil.load(getActivity(),R.mipmap.default_img_header,headImg,GlideUtil.REQUEST_OPTIONS);
            logOut.setVisibility(View.GONE);
            userName.setClickable(false);
            userPhone.setClickable(false);
            userSex.setClickable(false);
            goodsAddress.setClickable(false);
            myCollect.setClickable(false);
            userPassword.setClickable(false);
            buyPwd.setClickable(false);
            headImg.setClickable(false);
            redPaperLl.setClickable(false);
            userName.setBackground(getResources().getDrawable(R.color.white));
            userPhone.setBackground(getResources().getDrawable(R.color.white));
            userSex.setBackground(getResources().getDrawable(R.color.white));
            goodsAddress.setBackground(getResources().getDrawable(R.color.white));
            myCollect.setBackground(getResources().getDrawable(R.color.white));
            userPassword.setBackground(getResources().getDrawable(R.color.white));
            buyPwd.setBackground(getResources().getDrawable(R.color.white));
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("user_id",-1) != -1 && !isSetUserInfo){
            setUserInfo();

            HashMap<String,String> hashMap = new HashMap<>();
            hashMap.put("buyer_id",PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("user_id",-1)+"");
            HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH + HttpUtil.OBTAIN_USER_RED_PACKET, hashMap, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseText = response.body().string();
                    couponBeanList = new Gson().fromJson(responseText,new TypeToken<List<CouponBean>>(){}.getType());
                    redPacketNum = couponBeanList.size();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            redPaperNum.setText(redPacketNum+"");
                            isSetUserInfo = true;
                        }
                    });
                }
            });
        }
        //设置默认收货地址
        List<AddressBean> addressList = DataSupport.where("user_id = ? and selected = ?",
                String.valueOf(PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("user_id",-1)),"1").find(AddressBean.class);
        if (addressList.size()>0){
            userAddressText.setText(addressList.get(0).getAddress());
        }else{
            userAddressText.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.user_name:
                alterUserNameDialog();
                break;
            case R.id.user_phone:
                Intent intentPhone = new Intent(getActivity(),AlterPhoneActivity.class);
                intentPhone.putExtra("phone",userPhoneText.getText().toString());
                startActivity(intentPhone);
                break;
            case R.id.user_sex:
                alterUserSexDialog();
                break;
            case R.id.goods_address:
                Intent addressIntent = new Intent(getActivity(), AddressActivity.class);
                addressIntent.putExtra("threefragment",true);
                startActivity(addressIntent);
                break;
            case R.id.my_collect:
                break;
            case R.id.user_password:
                Intent intent = new Intent(getActivity(), AlterPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.buy_password:
                break;
            case R.id.my_head_img:
                initPicPopWindow();
                break;
            case R.id.fragment_my_coupon_ll:
                Intent intentRedPaper = new Intent(getActivity(), CouponActivity.class);
                intentRedPaper.putExtra("coupon_list",couponBeanList);
                startActivity(intentRedPaper);
                break;
            case R.id.log_out:
                DataSupport.deleteAll(UserBean.class);
                setUserInfo();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                editor.putInt("user_id",-1);
                editor.commit();
                isSetUserInfo = false;
                break;
            case R.id.tx_1:
                dialog.dismiss();
                permissionUtil.requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                    @Override
                    public void onGranted() {
                        openCamera();
                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        String msg = PermissionUtil.deniedPermissionToMsg(deniedPermission);
                        PermissionUtil.showDialog(getActivity(),"在设置-应用-食堂订餐-权限中开启 或 安全管家-应用管理-敏行中开启"+msg+"权限，以正常使用相机、录像等功能");
                    }

                    @Override
                    public void onShouldShowRationale(List<String> deniedPermission) {
                        String msg = PermissionUtil.deniedPermissionToMsg(deniedPermission);
                        PermissionUtil.showDialog(getActivity(),"在设置-应用-食堂订餐-权限中开启 或 安全管家-应用管理-食堂订餐中开启"+msg+"权限，以正常使用相机、录像等功能");
                    }
                });
                break;
            case R.id.tx_2:
                dialog.dismiss();
                permissionUtil.requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                    @Override
                    public void onGranted() {
                        selectFromAlbum();
                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {

                    }

                    @Override
                    public void onShouldShowRationale(List<String> deniedPermission) {
                        String msg = PermissionUtil.deniedPermissionToMsg(deniedPermission);
                        PermissionUtil.showDialog(getActivity(),"在设置-应用-食堂订餐-权限中开启 或 安全管家-应用管理-食堂订餐中开启"+msg+"权限,以正常使用相册");
                    }
                });
                break;
            case R.id.tx_3:
                dialog.dismiss();
                break;
        }
    }

    private void alterUserNameDialog(){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.alter_edit,null);
        final EditText edit = (EditText) view.findViewById(R.id.edit);
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).setTitle(getResources().getString(R.string.user_name))
                .setView(view)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        final String input = edit.getText().toString();
                        if (!TextUtils.isEmpty(input)) {
//                            userNameText.setText(input);
//                            Toast.makeText(getActivity(), "用户名修改成功", Toast.LENGTH_SHORT).show();
                            final List<UserBean> list = DataSupport.findAll(UserBean.class);
                            if (list.size()>0){
                                UserBean userBean = list.get(0);
//                                userBean.setUserName(input);
//                                userBean.save();
//                                将更改保存到远程数据库
                                progressBar.setVisibility(View.VISIBLE);
                                HashMap<String,String> hash = new HashMap<>();
                                hash.put("user_id",String.valueOf(userBean.getUserId()));
                                hash.put("user_name",input);
                                HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH+HttpUtil.SAVE_USER_NAME, hash, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Log.d("ThreeFragment",e.toString());
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String responseText  = response.body().string();
                                        try{
                                            JSONObject jsonObject = new JSONObject(responseText);
                                            final String msg = jsonObject.getString("msg");
                                            final int status = jsonObject.getInt("status");
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                                    progressBar.setVisibility(View.GONE);
                                                    if (status != 0){
                                                        userNameText.setText(input);
                                                        //将更改保存到本地数据库
                                                        UserBean userBean = list.get(0);
                                                        userBean.setUserName(input);
                                                        userBean.save();
                                                    }
                                                }
                                            });
                                        }catch(JSONException e){
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressBar.setVisibility(View.GONE);
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                        }
                        else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.user_name_not_empty), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), null)
                .create();
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.show();
    }

    private void alterUserSexDialog(){
        final View viewSex = LayoutInflater.from(getActivity()).inflate(R.layout.alter_sex,null);
        final RadioGroup selected = (RadioGroup) viewSex.findViewById(R.id.radio_group);
        RadioButton man = (RadioButton) viewSex.findViewById(R.id.man);
        RadioButton woman = (RadioButton) viewSex.findViewById(R.id.woman);
        if (userSexText.getText().toString().equals("男")){
            man.setChecked(true);
        }else{
            woman.setChecked(true);
        }
        new AlertDialog.Builder(getActivity())
                .setTitle(getResources().getString(R.string.user_sex))
                .setView(viewSex)
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RadioButton rb = (RadioButton) viewSex.findViewById(selected.getCheckedRadioButtonId());
                        final String sex = rb.getText().toString();
//                        userSexText.setText(sex);
                        //将修改保存到本地数据库
                        final List<UserBean> list = DataSupport.findAll(UserBean.class);
//                        if (list.size()>0){
//                            UserBean userBean = list.get(0);
//                            if (sex.equals("男")){
//                                userBean.setUserSex(1);
//                            }else{
//                                userBean.setUserSex(0);
//                            }
//                            userBean.save();
//                        }
//                        Toast.makeText(getActivity(), "用户性别修改成功", Toast.LENGTH_SHORT).show();
                        //将更改保存到远程数据库
                        progressBar.setVisibility(View.VISIBLE);
                        HashMap<String,String> hash = new HashMap<>();
                        hash.put("user_id",String.valueOf(list.get(0).getUserId()));
                        if (sex.equals("男")){
                            hash.put("user_sex",1+"");
                        }else{
                            hash.put("user_sex",0+"");
                        }
                        HttpUtil.sendOkHttpPostRequest(HttpUtil.HOME_PATH+HttpUtil.SAVE_USER_SEX, hash, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("ThreeFragment",e.toString());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseText = response.body().string();
                                try{
                                    JSONObject jsonObject = new JSONObject(responseText);
                                    final String msg = jsonObject.getString("msg");
                                    final int status = jsonObject.getInt("status");
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.GONE);
                                            if (status != 0){
                                                userSexText.setText(sex);
                                                //保存到本地数据库
                                                UserBean userBean = list.get(0);
                                                if (sex.equals("男")){
                                                    userBean.setUserSex(1);
                                                }else{
                                                    userBean.setUserSex(0);
                                                }
                                                userBean.save();
                                            }
                                        }
                                    });
                                }catch(JSONException e){

                                }
                            }
                        });
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), null)
                .show();
    }

    private void initPicPopWindow(){
        dialog = new Dialog(getActivity(),R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.popup_slide_from_bottom, null);
        tv1 = (TextView)view.findViewById(R.id.tx_1);
        tv2 = (TextView)view.findViewById(R.id.tx_2);
        tv3 = (TextView)view.findViewById(R.id.tx_3);
        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        //将布局设置给Dialog
        dialog.setContentView(view);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 0;//设置Dialog距离底部的距离
        //设置dialog宽度满屏
        WindowManager m = dialogWindow.getWindowManager();
        Display d = m.getDefaultDisplay();
        lp.width = d.getWidth();
        //将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框
    }

    /**
     * 打开系统相机
     */
    private void openCamera() {
        File file = new FileStorage().createIconFile(); //用到了sd卡权限,运行时权限处理
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(getActivity(), "com.example.rjq.myapplication.fileprovider", file);//通过FileProvider创建一个content类型的Uri
        } else {
            imageUri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    /**
     * 从相册选择
     */
    private void selectFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");//打开指定URI目录下的照片
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    /**
     * 裁剪
     */
    private void cropPhoto() {
        outputUri = null;
        File file = new FileStorage().createCropFile(); //用到了sd卡权限，运行时权限处理
        outputUri = Uri.fromFile(file);
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(imageUri, "image/*");  //打开指定URI目录下的照片
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);//将裁剪完的照片保存到指定URI
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_PICTURE_CUT);
    }

    @TargetApi(19)
    private String handleImgUri2String(Uri uri){
        String path = "";
        if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
            //如果是document类型的uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                path = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                path = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            path = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            path = uri.getPath();
        }
        return path;
    }

    private String handleImgUri2StringBeforeKitKat(Uri uri){
        String path;
        path = getImagePath(uri, null);
        return path;
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_IMAGE://从相册选择
                if (resultCode == Activity.RESULT_OK){
                    if (data != null) {
                        imageUri = data.getData();
                        cropPhoto();
                    }
                }
                break;
            case REQUEST_CAPTURE://拍照
                if (resultCode == Activity.RESULT_OK) {
                    cropPhoto();
                }
                break;
            case REQUEST_PICTURE_CUT://裁剪完成
                if (resultCode == Activity.RESULT_OK){
                    if (Build.VERSION.SDK_INT >= 19) {
                        imagePath = handleImgUri2String(outputUri);
                    } else {
                        imagePath = handleImgUri2StringBeforeKitKat(outputUri);
                    }

                    //将图片保存到MySql
                    progressBar.setVisibility(View.VISIBLE);
                    List<String> files = new ArrayList<>();
                    files.add(imagePath);
                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("user_id",String.valueOf(PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("user_id",-1)));
                    if (DataSupport.findAll(UserBean.class).get(0).getUserImg() != null){
                        hashMap.put("user_img",DataSupport.findAll(UserBean.class).get(0).getUserImg());
                    }
                    HttpUtil.upLoadImgsRequest(HttpUtil.HOME_PATH + HttpUtil.UPLOAD_IMG_API, hashMap ,files, new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d(TAG,e.getMessage());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getContext(), "网络错误，请检查网络状态!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            //将更改的头像保存到本地用户中
                            final String responseText = response.body().string();
                            try{
                                final JSONObject jsonObject = new JSONObject(responseText);
                                //将新上传的图片的服务器路径保存到用户信息中
                                List<UserBean> list = DataSupport.findAll(UserBean.class);
                                final UserBean userBean = list.get(0);
                                userBean.setUserImg((String)jsonObject.get("url"));
                                userBean.save();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                    //保存本地图片头像的路径
                                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString(userBean.getUserId()+"",imagePath).commit();
                                    GlideUtil.load(getActivity(),imagePath,headImg,GlideUtil.REQUEST_OPTIONS);
                                    progressBar.setVisibility(View.GONE);
                                    }
                                });
                            }catch (JSONException e){
                                Log.d(TAG,e.getMessage());
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), "上传头像失败!", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                    });

                }
                break;
        }
    }


}
