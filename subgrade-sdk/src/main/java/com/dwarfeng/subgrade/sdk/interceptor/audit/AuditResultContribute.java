package com.dwarfeng.subgrade.sdk.interceptor.audit;

import java.lang.annotation.*;

/**
 * 审计返回值贡献注解。
 *
 * <p>
 * 该注解在方法正常返回后，将返回值交由对应的 {@link Contributor} 解析为审计属性。
 *
 * @author DwArFeng
 * @since 1.10.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(AuditResultContributeList.class)
public @interface AuditResultContribute {

    /**
     * Contributor Bean 名称。
     *
     * @return Contributor Bean 名称。
     */
    String contributor();

    /**
     * 属性键前缀。
     *
     * @return 属性键前缀。
     */
    String prefix() default "";
}
