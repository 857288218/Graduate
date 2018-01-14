package com.example.rjq.myapplication.util.permission;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.rjq.myapplication.R;

import java.util.List;

/**
 * Created by rjq on 2018/1/2.
 */

public class PermissionUtil {
    private static final String TAG = "PermissionsUtil";

    private PermissionFragment fragment;

    public PermissionUtil(@NonNull FragmentActivity activity) {
        fragment = getPermissionsFragment(activity);
    }

    private PermissionFragment getPermissionsFragment(FragmentActivity activity) {
        PermissionFragment fragment = (PermissionFragment) activity.getSupportFragmentManager().findFragmentByTag(TAG);
        boolean isNewInstance = fragment == null;
        if (isNewInstance) {
            fragment = new PermissionFragment();
            FragmentManager fragmentManager = activity.getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .add(fragment, TAG)
                    .commit();
//            fragmentManager.executePendingTransactions();
        }

        return fragment;
    }

    /**
     * 外部调用申请权限
     * @param permissions 申请的权限
     * @param listener 监听权限接口
     */
    public void requestPermissions(String[] permissions, PermissionListener listener) {
        fragment.setListener(listener);
        fragment.requestPermissions(permissions);

    }

    public static String deniedPermissionToMsg(List<String> deniedPermission){
        StringBuffer sb = new StringBuffer();
        if (deniedPermission.contains(Manifest.permission.CAMERA)){
            sb.append(" 相机 ");
        }
        if (deniedPermission.contains(Manifest.permission.READ_CONTACTS)){
            sb.append(" 读取联系人 ");
        }
        if (deniedPermission.contains(Manifest.permission.WRITE_CONTACTS)){
            sb.append(" 修改联系人 ");
        }
        if (deniedPermission.contains(Manifest.permission.RECORD_AUDIO)){
            sb.append(" 麦克风 ");
        }
        if (deniedPermission.contains(Manifest.permission.ACCESS_FINE_LOCATION)||deniedPermission.contains(Manifest.permission.ACCESS_COARSE_LOCATION)){
            sb.append(" 位置信息 ");
        }
        if (deniedPermission.contains(Manifest.permission.CALL_PHONE)){
            sb.append(" 打电话 ");
        }
        if (deniedPermission.contains(Manifest.permission.BODY_SENSORS)){
            sb.append(" 传感器 ");
        }
        if (deniedPermission.contains(Manifest.permission.READ_EXTERNAL_STORAGE)||deniedPermission.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            sb.append(" 读写存储卡 ");
        }
        if (deniedPermission.contains(Manifest.permission.SEND_SMS)||deniedPermission.contains(Manifest.permission.RECEIVE_SMS)||
                deniedPermission.contains(Manifest.permission.READ_SMS)||deniedPermission.contains(Manifest.permission.READ_EXTERNAL_STORAGE)){
            sb.append(" 短信 ");
        }
        return sb.toString();
    }

    public static void showDialog(final Context context,String msg){

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("权限申请")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent =  new Intent(Settings.ACTION_SETTINGS);
//                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
//                                        intent.setData(uri);
                        context.startActivity(intent);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
        dialog.show();

//        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//        params.width = context.getResources().getDimensionPixelSize(R.dimen.dimen_171dp);
//        params.height = context.getResources().getDimensionPixelSize(R.dimen.dimen_171dp);
//        dialog.getWindow().setAttributes(params);

        dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.text_select_color));
        dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getResources().getColor(R.color.bottom_tab_text_normal_color));
        TextView tvMsg = (TextView) dialog.findViewById(android.R.id.message);
        tvMsg.setTextColor(context.getResources().getColor(R.color.bottom_tab_text_normal_color));

    }
}
