package com.dwarfeng.subgrade.aop.sdk.interceptor.audit.propctr;

import com.dwarfeng.subgrade.aop.sdk.interceptor.audit.AuditRecordContext;
import com.dwarfeng.subgrade.aop.sdk.interceptor.audit.DefaultAuditRecordAopManager;
import com.dwarfeng.subgrade.aop.sdk.interceptor.audit.PropertyContributor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 返回值属性贡献器。
 *
 * <p>
 * 该贡献器只处理成功完成的方法调用，记录成功标记以及返回值的字符串表示。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class ResultPropertyContributor implements PropertyContributor {

    @Override
    public boolean supports(AuditRecordContext context) {
        return context.isSuccess();
    }

    @Override
    public Map<String, Object> getProperties(AuditRecordContext context) {
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put(DefaultAuditRecordAopManager.PROPERTY_SUCCESS, true);
        properties.put(DefaultAuditRecordAopManager.PROPERTY_RESULT, String.valueOf(context.result()));
        return properties;
    }
}
