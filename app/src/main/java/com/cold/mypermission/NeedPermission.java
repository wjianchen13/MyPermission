package com.cold.mypermission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * name:NeedPermission
 * desc:
 * author:
 * date: 2017-08-22 19:30
 * remark:
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NeedPermission {

    String[] permissions() default "";
}
