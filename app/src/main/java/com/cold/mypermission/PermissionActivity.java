package com.cold.mypermission;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

/**
 * name:PermissionActivity
 * desc:获取权限Activity
 * author:
 * date: 2017-08-22 19:30
 * remark:
 */
public class PermissionActivity extends FragmentActivity implements RequestPermissionResultListener{

    public static final String EXTRA_PERMISSIONS = "permissions";
    String[] permissions;

    private static RequestPermissionListener sOnPermissionRequestFinishedListener;

    /**
     * 启动permission activity
     * @param
     * @return
     */
    public static void gotoPermissionActivity(Context context, String[] permissions, RequestPermissionListener sOnPermissionRequestFinishedListener) {
        PermissionActivity.sOnPermissionRequestFinishedListener = sOnPermissionRequestFinishedListener;
        Intent intent = new Intent(context, PermissionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(EXTRA_PERMISSIONS, permissions);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        Bundle bundle = getIntent().getExtras();
        permissions = bundle.getStringArray(EXTRA_PERMISSIONS);
        checkAndRequestPermissions();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        permissionDenied();
    }

    /**
     * 权限拒绝
     * @param
     * @return void
     */
    private void permissionDenied() {
        if (sOnPermissionRequestFinishedListener != null) {
            sOnPermissionRequestFinishedListener.onResult(false);
            finish();
            overridePendingTransition(0, 0);
        }
    }

    /**
     * 检查是否有权限，如果没有就获取
     * @param
     * @return void
     */
    private void checkAndRequestPermissions() {
        PermissionsManager.getInstance().checkAndRequestPermissions(this, permissions, this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionsManager.getInstance().requestPermissionsResult(permissions, grantResults);
    }

    @Override
    public void onGranted() {
        permissionGranted();
    }

    /**
     * 权限允许
     * @param
     * @return void
     */
    private void permissionGranted() {
        if (sOnPermissionRequestFinishedListener != null) {
            sOnPermissionRequestFinishedListener.onResult(true);
            finish();
            overridePendingTransition(0, 0);
        }
    }

    @Override
    public void onDenied(String permission) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("备份通讯录需要访问 “短信接收” 和 “短信发送”权限，请到 “应用信息 -> 权限” 中授予！");
            builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    gotoSetting();
                    PermissionActivity.this.finish();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PermissionActivity.this.finish();
                }
            });
            builder.show();
    }

    /**
     * 跳转到设置界面
     * @param
     * @return void
     */
    private void gotoSetting() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
