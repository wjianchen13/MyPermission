package com.cold.mypermission;

/**
 * name:RequestPermissionListener
 * desc:请求权限监听
 * author:
 * date: 2017-08-17 15:00
 * remark:
 */
public interface RequestPermissionListener {

    void onResult(boolean isGranted);

}
