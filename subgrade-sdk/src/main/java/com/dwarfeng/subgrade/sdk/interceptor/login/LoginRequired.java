package com.dwarfeng.subgrade.sdk.interceptor.login;

import java.lang.annotation.*;

/**
 * 登录需求注解。
 *
 * @author DwArFeng
 * @since 0.1.1-beta
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface LoginRequired {

    /**
     * 验证成功后是否延迟过期时间。
     *
     * @return 验证成功后是否延迟过期时间。
     */
    boolean postpone() default false;
}
