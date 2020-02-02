package com.dwarfeng.subgrade.sdk.interceptor.analyse;

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

}
