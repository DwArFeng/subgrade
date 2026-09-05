package com.dwarfeng.subgrade.aop.sdk.interceptor.audit;

import java.util.Map;

/**
 * 审计记录增强管理器。
 *
 * <p>
 * 审计记录增强管理器根据被审计方法的调用上下文生成审计属性。
 * 管理器只负责属性生成，不负责执行代理方法，也不负责向具体的审计系统提交记录。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public interface AuditRecordAopManager {

    /**
     * 获取审计属性。
     *
     * @param context 审计记录上下文。
     * @return 审计属性组成的映射。
     * @throws Exception 属性生成过程中发生异常。
     */
    Map<String, Object> getProperties(AuditRecordContext context) throws Exception;
}
