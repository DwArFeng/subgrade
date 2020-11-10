package com.dwarfeng.subgrade.sdk.interceptor.analyse;

import java.lang.annotation.*;

/**
 * 跳过记录。
 *
 * <p>
 * 有时候，待分析方法中的参数或返回结果过大，输出在日志中会占用大量的空间，我们只希望分析该方法的功能，而不希望
 * 将这个巨大的参数/返回结果记录在日志中。
 *
 * <p>
 * 在方法中加入这个注解，方法的返回结果将不会输出在日志中。<br>
 * 在参数中加入这个注解，具体参数将不会输出在日志中。
 *
 * @author DwArFeng
 * @since 1.2.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Documented
public @interface SkipRecord {
}
