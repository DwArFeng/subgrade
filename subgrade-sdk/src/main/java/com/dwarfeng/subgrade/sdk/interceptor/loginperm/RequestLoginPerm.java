package com.dwarfeng.subgrade.sdk.interceptor.loginperm;

import java.lang.annotation.*;

/**
 * 登录权限注解。
 *
 * @author DwArFeng
 * @since 0.1.2-beta
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface RequestLoginPerm {
}
