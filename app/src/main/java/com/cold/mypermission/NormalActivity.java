package com.cold.mypermission;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

/**
 * name:NormalActivity
 * desc:正常权限动态获取
 * author:
 * date: 2017-08-15 11:30
 * remark:
 * 权限获取流程：
 * 1.检查权限
 * 2.如果没有权限，动态请求权限
 * 3.处理返回结果
 * 4.如果失败，提示用户自己设置
 */
public class NormalActivity extends AppCompatActivity {

    private String[] permissions = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE, // sd卡读取权限
            Manifest.permission.WRITE_EXTERNAL_STORAGE // sd卡写入权限
           };
    private final int PERMISSIONS_CODE = 1; // 权限请求代码
    private boolean isShowGuide = false; // 是否显示用户指引权限对话框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);
        checkPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isShowGuide) {
            checkPermissions();
            isShowGuide = false;
        }
    }

    /**
     * 检查权限
     * @param
     * @return void
     */
    private void checkPermissions() {
        boolean flag = checkPermissions(permissions);
        if(!flag) {
            requestPermission(permissions);
        }
    }

    /**
     * 1.检查权限
     * @param
     * @return 权限是否允许标志
     */
    private boolean checkPermissions(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 2.如果没有权限，动态请求权限
     * @param
     * @return void
     */
    private void requestPermission(String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_CODE);
    }

    /**
     * 3.处理返回结果
     * @param grantResults 返回对应权限请求数组，如果成功，则为PERMISSION_GRANTED，否则是PERMISSION_DENIED
     * @return void
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_CODE) {
            boolean grantedFlag = true;
            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    grantedFlag = false;
                    break;
                }
            }

            if (!grantedFlag) {
                guideUserSetting();
            }
        }
    }

    /**
     * 4.如果失败，提示用户自己设置
     * @param
     * @return void
     */
    private void guideUserSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("备份通讯录需要访问“外部存储器”，请到 “应用信息 -> 权限” 中授予！");
        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                isShowGuide = true;
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

}
