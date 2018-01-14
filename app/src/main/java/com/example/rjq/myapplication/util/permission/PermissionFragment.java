package com.example.rjq.myapplication.util.permission;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rjq on 2018/1/2.
 */

public class PermissionFragment extends Fragment {
    /**
     * 申请权限的requestCode
     */
    private static final int PERMISSIONS_REQUEST_CODE = 1;

    /**
     * 权限监听接口
     */
    private PermissionListener listener;
    public void setListener(PermissionListener listener) {
        this.listener = listener;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * 申请权限
     * @param permissions 需要申请的权限
     */
//    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissions(@NonNull String[] permissions) {
        List<String> requestPermissionList = new ArrayList<>();
        //找出所有未授权的权限
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionList.add(permission);
            }
        }
        if (requestPermissionList.isEmpty()) {
            //已经全部授权
            permissionAllGranted();
        } else {
            //申请授权
            requestPermissions(requestPermissionList.toArray(new String[requestPermissionList.size()]), PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * fragment回调处理权限的结果
     * @param requestCode 请求码 要等于申请时候的请求码
     * @param permissions 申请的权限
     * @param grantResults 对应权限的处理结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != PERMISSIONS_REQUEST_CODE) {
            return;
        }

        if (grantResults.length > 0) {
            List<String> deniedPermissionList = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissionList.add(permissions[i]);
                }
            }

            if (deniedPermissionList.isEmpty()) {
                //已经全部授权
                permissionAllGranted();
            } else {
                //勾选了对话框中”Don’t ask again”的选项, 返回false
                for (String deniedPermission : deniedPermissionList) {
                    boolean flag = shouldShowRequestPermissionRationale(deniedPermission);
                    if (!flag) {
                        //拒绝授权
                        permissionShouldShowRationale(deniedPermissionList);
                        return;
                    }
                }
                //拒绝授权
                permissionHasDenied(deniedPermissionList);

            }


        }

    }


    /**
     * 权限全部已经授权
     */
    private void permissionAllGranted() {
        if (listener != null) {
            listener.onGranted();
        }
    }

    /**
     * 有权限被拒绝
     *
     * @param deniedList 被拒绝的权限
     */
    private void permissionHasDenied(List<String> deniedList) {
        if (listener != null) {
            listener.onDenied(deniedList);
        }
    }

    /**
     * 权限被拒绝并且勾选了不在询问
     *
     * @param deniedList 勾选了不在询问的权限
     */
    private void permissionShouldShowRationale(List<String> deniedList) {
        if (listener != null) {
            listener.onShouldShowRationale(deniedList);
        }
    }

}
