package com.dwarfeng.subgrade.sdk.interceptor.audit;

import java.lang.annotation.*;

/**
 * 审计调用贡献注解。
 *
 * <p>
 * 该注解用于生成与整次方法调用相关的审计属性。
 * 对应的 {@link Contributor} 没有特定源对象，应通过 {@link AuditRecordContext} 获取调用信息。
 *
 * @author DwArFeng
 * @since 1.10.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Repeatable(AuditInvocationContributeList.class)
public @interface AuditInvocationContribute {

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
