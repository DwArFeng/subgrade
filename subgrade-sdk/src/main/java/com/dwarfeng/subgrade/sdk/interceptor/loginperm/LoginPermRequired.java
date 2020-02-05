package com.dwarfeng.subgrade.sdk.interceptor.loginperm;

import java.lang.annotation.*;

/**
 * 登录权限需求注解。
 *
 * @author DwArFeng
 * @since 0.1.2-beta
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface LoginPermRequired {

    /**
     * 定义方法执行所需要的权限数组。
     *
     * @return 方法执行所需要的权限数组。
     */
    String[] value();

    /**
     * 验证成功后是否延迟过期时间。
     *
     * @return 验证成功后是否延迟过期时间。
     */
    boolean postpone() default false;
}
