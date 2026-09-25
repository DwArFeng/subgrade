package com.dwarfeng.subgrade.sdk.interceptor.audit;

import java.lang.annotation.*;

/**
 * 审计返回值贡献注解列表。
 *
 * <p>
 * 该注解是 {@link AuditResultContribute} 的容器注解，通常不应直接使用。
 *
 * @author DwArFeng
 * @since 1.10.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuditResultContributeList {

    /**
     * 审计返回值贡献注解组成的数组。
     *
     * @return 审计返回值贡献注解组成的数组。
     */
    AuditResultContribute[] value();
}
