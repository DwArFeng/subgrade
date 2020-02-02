package com.dwarfeng.subgrade.sdk.interceptor.permission;

import java.lang.annotation.*;

/**
 * 权限需求注解。
 *
 * @author DwArFeng
 * @since 0.1.0-beta
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface PermissionRequired {

    /**
     * 定义方法执行所需要的权限数组。
     *
     * @return 方法执行所需要的权限数组。
     */
    String[] value();
}
