package com.dayou.crm.annotation;

import java.lang.annotation.*;

/**
 * @Description: 定义方法需要的对应资源的权限码
 * @author: dayou
 * @create: 2022-03-15 14:41
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiredPermission {

    //权限码
    String code() default "";


}
