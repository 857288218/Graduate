package com.example.rjq.myapplication.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.rjq.myapplication.R;
import com.example.rjq.myapplication.util.GlideUtil;
import com.example.rjq.myapplication.view.CommonPopupWindow;
import com.example.rjq.myapplication.view.RoundAngleImageView;
import com.example.rjq.myapplication.util.FileStorage;
import com.example.rjq.myapplication.util.lubanimage.Luban;
import com.example.rjq.myapplication.util.lubanimage.OnCompressListener;
import com.zhy.autolayout.AutoLinearLayout;
import com.zhy.autolayout.AutoRelativeLayout;

import java.io.File;
/**
 * Created by rjq on 2017/10/28 0028.
 */
public class ThreeFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = "life";
    private View rootView;
    private RoundAngleImageView myHeadPhotoIV;
    private AutoRelativeLayout myHeadPhotoRL;

    private static final int REQUEST_PICK_IMAGE = 1; //相册选取
    private static final int REQUEST_CAPTURE = 2;  //拍照
    private static final int REQUEST_PICTURE_CUT = 3;  //剪裁图片
    private static final int REQUEST_PERMISSION = 4;  //权限请求
    private Uri imageUri;//原图保存地址
    private Uri outputUri;//剪切的地址
    private File lubanFile;
    private String imagePath;

    private Dialog dialog;
    private CommonPopupWindow commonPopupWindow;
    private TextView tv3;
    private TextView tv1;
    private TextView tv2;

    private TextView threetv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView three");
        if (rootView == null){
            rootView = inflater.inflate(R.layout.three_fragment,container,false);
            initView();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.my_fragment_status_bar_color));
        }
        return rootView;
    }

    private void initView(){
        myHeadPhotoIV = (RoundAngleImageView) rootView.findViewById(R.id.fragment_main_my_head_photo_iv);
        myHeadPhotoRL = (AutoRelativeLayout) rootView.findViewById(R.id.fragment_main_my_head_photo_rl);
        myHeadPhotoRL.setOnClickListener(this);
        threetv = (TextView) rootView.findViewById(R.id.three_tv);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        lp.setMargins((int)getActivity().getResources().getDimensionPixelSize(R.dimen.dimen_10dp),0,0,0);
//        threetv.setLayoutParams(lp);
        Log.d("dimen",getActivity().getResources().getDimension(R.dimen.dimen_171dp)+"");
        Log.d("dimen",getActivity().getResources().getDimension(R.dimen.space_171px)+"");
        Log.d("dimen",getActivity().getResources().getDimensionPixelSize(R.dimen.dimen_171dp)+"");
        Log.d("dimen",getActivity().getResources().getDimensionPixelSize(R.dimen.space_171px)+"");
        Log.d("dimen",getActivity().getResources().getDimensionPixelOffset(R.dimen.dimen_171dp)+"");
        Log.d("dimen",getActivity().getResources().getDimensionPixelOffset(R.dimen.space_171px)+"");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_main_my_head_photo_rl:
                initPicPopWindow();
                break;
            case R.id.tx_3:
                //commonPopupWindow.dismiss();
                dialog.dismiss();
                break;
            case R.id.tx_1:
                //commonPopupWindow.dismiss();
                dialog.dismiss();
                openCamera();
                break;
            case R.id.tx_2:
                //commonPopupWindow.dismiss();
                dialog.dismiss();
                //检查权限(6.0以上做权限判断)
                if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_PERMISSION);
                }else{
                    selectFromAlbum();
                }
                break;
        }
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
        File file = new FileStorage().createIconFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(getActivity(), "com.bugull.cameratakedemo.fileprovider", file);//通过FileProvider创建一个content类型的Uri
        } else {
            imageUri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
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
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    /**
     * 裁剪
     */
    private void cropPhoto() {
        outputUri = null;
        File file = new FileStorage().createCropFile();
        outputUri = Uri.fromFile(file);//缩略图保存地址
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_PICTURE_CUT);
    }

    /**
     * 权限判断弹出窗口后选择完的回调方法
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults){
        switch(requestCode){
            case REQUEST_PERMISSION:
                if (grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    selectFromAlbum();
                }else{
                    Toast.makeText(getActivity(),"you denied the permission",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        imagePath = null;
        if (data != null) {
            imageUri = data.getData();
            if (DocumentsContract.isDocumentUri(getActivity(), imageUri)) {
                //如果是document类型的uri,则通过document id处理
                String docId = DocumentsContract.getDocumentId(imageUri);
                if ("com.android.providers.media.documents".equals(imageUri.getAuthority())) {
                    String id = docId.split(":")[1];//解析出数字格式的id
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.downloads.documents".equals(imageUri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                    imagePath = getImagePath(contentUri, null);
                }
            } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
                //如果是content类型的Uri，则使用普通方式处理
                imagePath = getImagePath(imageUri, null);
            } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
                //如果是file类型的Uri,直接获取图片路径即可
                imagePath = imageUri.getPath();
            }

            cropPhoto();
        }
    }

    private void handleImageBeforeKitKat(Intent intent) {
        imageUri = intent.getData();
        imagePath = getImagePath(imageUri, null);
        cropPhoto();
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection老获取真实的图片路径
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
                if (Build.VERSION.SDK_INT >= 19) {
                    handleImageOnKitKat(data);
                } else {
                    handleImageBeforeKitKat(data);
                }
                break;
            case REQUEST_CAPTURE://拍照
                if (resultCode == Activity.RESULT_OK) {
                    cropPhoto();
                }
                break;
            case REQUEST_PICTURE_CUT://裁剪完成
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.default_photo)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate();
                GlideUtil.load(getActivity(),outputUri,myHeadPhotoIV,requestOptions);
                luBanCompress();
                break;
        }
    }

    private void luBanCompress() {
        final File file = new File(FileStorage.getRealFilePath(getActivity(), outputUri));
        Luban.with(getContext())
                .load(file)                     //传人要压缩的图片
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI
                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        lubanFile = file;
//                        Map<String, RequestBody> map = mesgPackMap();
//                        if (map != null) {
//                            postHeadPhoto(map);
//                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过去出现问题时调用
                        Toast.makeText(getActivity(), "压缩失败", Toast.LENGTH_SHORT).show();
                    }
                }).launch();    //启动压缩
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG,"onAttach three");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate three");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG,"onActivityCreated three");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"onStart three");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume three");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"onPause three");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"onStop three");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"onDestroyView three");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy three");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"onDetach three");
    }


}
