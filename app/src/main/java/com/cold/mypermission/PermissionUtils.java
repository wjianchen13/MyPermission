package com.cold.mypermission;

import android.content.Context;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * name:PermissionUtils
 * desc: 权限操作类
 * author:
 * date: 2017-08-22 19:30
 * remark:
 */
@Aspect
public class PermissionUtils {

    private static Context context;

    public static void init(Context context) {
        PermissionUtils.context = context.getApplicationContext();
    }

    @Pointcut("execution(@com.cold.mypermission.NeedPermission * *(..)) && @annotation(needPermission)")
    public void checkPermission(NeedPermission needPermission) {

    }

    @Around("checkPermission(needPermission)")
    public Object onCheckPermission(final ProceedingJoinPoint joinPoint, NeedPermission needPermission) {
        System.out.println("--------------------> needPermission == null: " + (needPermission == null));
        if (needPermission == null) {
            try {
                Object object = joinPoint.proceed();
                return object;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        String[] permissions = needPermission.permissions();

        PermissionActivity.gotoPermissionActivity(context, permissions, new RequestPermissionListener() {
            @Override
            public void onResult(boolean granted) {
                if (granted) {
                    try {
                        joinPoint.proceed();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
            }
        });
        return null;
    }

}
