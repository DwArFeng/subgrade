package com.dwarfeng.subgrade.sdk.interceptor.audit;

import java.lang.annotation.*;

/**
 * 审计参数贡献注解。
 *
 * <p>
 * 该注解用于将指定的入口参数交由对应的 {@link Contributor} 解析为审计属性。
 *
 * @author DwArFeng
 * @since 1.10.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Repeatable(AuditParamContributeList.class)
public @interface AuditParamContribute {

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
