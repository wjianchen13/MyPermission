package com.cold.mypermission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * name:PermissionsManager
 * desc:动态权限管理类
 * author:
 * date: 2017-08-15 11:30
 * remark:
 */
public final class PermissionsManager {

    private static final PermissionsManager INSTANCE = new PermissionsManager();
    private Set<String> mPermissions = new HashSet<>(1);
    private RequestPermissionResultListener listener;

    public static PermissionsManager getInstance() {
        return INSTANCE;
    }

    private PermissionsManager() {}

    /**
     * 检查并获取权限
     * @param activity
     * @param permissions 权限集合
     * @param permissionResultListener 请求权限返回监听
     * @return void
     */
    public synchronized void checkAndRequestPermissions(Activity activity, String[] permissions, RequestPermissionResultListener permissionResultListener) {
        if (activity == null) {
            return;
        }
        this.listener = permissionResultListener;
        Collections.addAll(mPermissions, permissions);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    onResult(permission, PackageManager.PERMISSION_DENIED);
                } else {
                    onResult(permission, PackageManager.PERMISSION_GRANTED);
                }
            }
        } else {
            List<String> permList = new ArrayList<>(1);
            for (String permission : permissions) {
                boolean result;
                result = ActivityCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
                if (!result) {
                    permList.add(permission);
                } else {
                    onResult(permission, PackageManager.PERMISSION_GRANTED);
                }
            }
            if (!permList.isEmpty()) {
                String[] permsToRequest = permList.toArray(new String[permList.size()]);
                ActivityCompat.requestPermissions(activity, permsToRequest, 1);
            }
        }
    }

    /**
     * 检查并获取权限
     * @param permissions 所有权限
     * @param grantResults 返回结果数组
     * @return void
     */
    public synchronized void requestPermissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults) {
        int size = permissions.length;
        if (grantResults.length < size) {
            size = grantResults.length;
        }
        for (int n = 0; n < size; n++) {
            onResult(permissions[n], grantResults[n]);
        }
    }

    /**
     * 检查并获取权限
     * @param permission 权限
     * @param result
     * @return void
     */
    public synchronized boolean onResult(@NonNull String permission, int result) {
        if (result == PackageManager.PERMISSION_GRANTED) {
            mPermissions.remove(permission);
            if (mPermissions.isEmpty()) {
                listener.onGranted();
                return true;
            }
        } else {
            listener.onDenied(permission);
            return true;
        }
        return false;
    }
}
