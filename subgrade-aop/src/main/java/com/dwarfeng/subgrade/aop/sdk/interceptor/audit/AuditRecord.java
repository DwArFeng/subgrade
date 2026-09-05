package com.dwarfeng.subgrade.aop.sdk.interceptor.audit;

import java.lang.annotation.*;

/**
 * 审计记录注解。
 *
 * <p>
 * 该注解用于标记需要在方法执行后记录审计信息的方法。
 * 每个方法对应唯一的审计类别，并通过指定名称的 {@link AuditRecordAopManager} 生成审计属性。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuditRecord {

    /**
     * 审计类别 ID。
     *
     * @return 审计类别 ID。
     */
    String categoryId();

    /**
     * 审计记录增强管理器的 Bean 名称。
     *
     * @return 审计记录增强管理器的 Bean 名称。
     */
    String manager();
}
