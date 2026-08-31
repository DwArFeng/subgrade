package com.dwarfeng.subgrade.sdk.interceptor.audit.propctr;

import com.dwarfeng.subgrade.sdk.interceptor.audit.AuditRecordContext;
import com.dwarfeng.subgrade.sdk.interceptor.audit.DefaultAuditRecordAopManager;
import com.dwarfeng.subgrade.sdk.interceptor.audit.PropertyContributor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 异常属性贡献器。
 *
 * <p>
 * 该贡献器只处理异常结束的方法调用，记录失败标记、异常类型和异常消息。
 *
 * @author DwArFeng
 * @since 1.9.0
 */
public class ThrowablePropertyContributor implements PropertyContributor {

    @Override
    public boolean supports(AuditRecordContext context) {
        return !context.isSuccess();
    }

    @Override
    public Map<String, Object> getProperties(AuditRecordContext context) {
        Throwable throwable = Objects.requireNonNull(context.getThrowable());
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put(DefaultAuditRecordAopManager.PROPERTY_SUCCESS, false);
        properties.put(DefaultAuditRecordAopManager.PROPERTY_EXCEPTION_TYPE, throwable.getClass().getName());
        properties.put(
                DefaultAuditRecordAopManager.PROPERTY_EXCEPTION_MESSAGE,
                String.valueOf(throwable.getMessage())
        );
        return properties;
    }
}
