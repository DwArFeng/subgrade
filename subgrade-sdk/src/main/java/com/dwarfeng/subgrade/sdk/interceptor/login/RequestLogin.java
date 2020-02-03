package com.dwarfeng.subgrade.sdk.interceptor.login;

import java.lang.annotation.*;

/**
 * 登录注解。
 *
 * @author DwArFeng
 * @since 0.1.1-beta
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface RequestLogin {
}
