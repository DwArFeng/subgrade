package com.dwarfeng.subgrade.sdk.interceptor.audit;

import java.lang.annotation.*;

/**
 * 审计记录注解。
 *
 * <p>
 * 该注解用于标记需要在方法执行后记录审计信息的方法，每个方法对应唯一的审计类别。
 * 审计属性由系统中配置的 {@link AuditRecordAopManager} 生成。
 *
 * @author DwArFeng
 * @since 1.10.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AuditRecord {

    /**
     * 定义审计记录时需要的审计类别 ID。
     *
     * @return 审计记录时需要的审计类别 ID。
     */
    String value();
}
