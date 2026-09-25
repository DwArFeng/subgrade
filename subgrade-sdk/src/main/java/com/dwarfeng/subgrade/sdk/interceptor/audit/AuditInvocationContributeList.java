package com.dwarfeng.subgrade.sdk.interceptor.audit;

import java.lang.annotation.*;

/**
 * 审计调用贡献注解列表。
 *
 * <p>
 * 该注解是 {@link AuditInvocationContribute} 的容器注解，通常不应直接使用。
 *
 * @author DwArFeng
 * @since 1.10.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuditInvocationContributeList {

    /**
     * 审计调用贡献注解组成的数组。
     *
     * @return 审计调用贡献注解组成的数组。
     */
    AuditInvocationContribute[] value();
}
