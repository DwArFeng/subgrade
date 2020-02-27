package com.dwarfeng.subgrade.sdk.interceptor.http;

import java.lang.annotation.*;

/**
 * 行为分析注解。
 *
 * @author DwArFeng
 * @since 0.3.0-beta
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface BindingCheck {

}
