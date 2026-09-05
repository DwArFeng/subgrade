package com.dwarfeng.subgrade.aop.stack.handler;

import com.dwarfeng.subgrade.basic.stack.exception.HandlerException;
import com.dwarfeng.subgrade.basic.stack.handler.Handler;

import java.util.Map;

/**
 * 审计记录处理器。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public interface AuditRecordHandler extends Handler {

    /**
     * 记录审计信息。
     *
     * @param categoryId 审计类别 ID。
     * @param properties 审计属性组成的映射。
     * @throws HandlerException 处理器异常。
     */
    void record(String categoryId, Map<String, Object> properties) throws HandlerException;
}
