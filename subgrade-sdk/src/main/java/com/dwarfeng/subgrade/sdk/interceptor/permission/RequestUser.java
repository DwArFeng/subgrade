package com.dwarfeng.subgrade.sdk.interceptor.permission;

import java.lang.annotation.*;

/**
 * 用户注解。
 *
 * @author DwArFeng
 * @since 0.1.0-beta
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface RequestUser {
}
