package com.dwarfeng.subgrade.sdk.interceptor.friendly;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 友好性列表注解。
 * <p>
 * 在AOP的使用过程中，不需要显式使用该注解，而是仅仅需要使用多个{@link Friendly}注解即可。
 * <br>
 * 例如
 * <blockquote><pre>
 * public class XXX {
 *
 *     // 使用多个&#64;Friendly，而不是使用&#64;FriendlyList
 *     &#64;Friendly(...)
 *     &#64;Friendly(...)
 *     &#64;Friendly(...)
 *     public ReturnType friendlyMethod(...) {
 *         ...
 *     }
 * }
 * </pre></blockquote>
 *
 * @author DwArFeng
 * @since 1.0.2
 */
@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface FriendlyList {

    /**
     * 获取注解中的所有友好注解。
     *
     * @return 所有的友好注解组成的数组。
     */
    Friendly[] value();
}
