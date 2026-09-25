package com.dwarfeng.subgrade.sdk.interceptor.audit;

import java.lang.annotation.*;

/**
 * 审计参数贡献注解列表。
 *
 * <p>
 * 该注解是 {@link AuditParamContribute} 的容器注解，通常不应直接使用。
 *
 * @author DwArFeng
 * @since 1.10.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface AuditParamContributeList {

    /**
     * 审计参数贡献注解组成的数组。
     *
     * @return 审计参数贡献注解组成的数组。
     */
    AuditParamContribute[] value();
}
