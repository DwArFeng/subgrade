package com.dwarfeng.subgrade.sdk.interceptor.analyse;

import com.dwarfeng.subgrade.stack.log.LogLevel;

import java.lang.annotation.*;

/**
 * 行为分析注解。
 *
 * @author DwArFeng
 * @since 0.1.0-beta
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface BehaviorAnalyse {

    /**
     * 日志等级，默认为 <code>LogLevel.DEBUG</code>。
     *
     * @since 1.2.0
     */
    LogLevel logLevel() default LogLevel.DEBUG;
}
