package com.dwarfeng.subgrade.sdk.interceptor.friendly;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 友好性增强注解。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD})
@Repeatable(value = FriendlyList.class)
public @interface Friendly {

    /**
     * 获取入口参数增强管理器。
     *
     * @return 入口参数管理器。
     */
    String paramManger() default "";

    /**
     * 获取返回值增强管理器。
     *
     * @return 返回值增强管理器。
     */
    String resultManger() default "";

    /**
     * 获取可选键。
     *
     * <p>
     * 可选键可以使用普通文本，也可以使用Spring的SpEL表达式。
     *
     * @return 可选键。
     */
    String optionalKey() default "";
}
