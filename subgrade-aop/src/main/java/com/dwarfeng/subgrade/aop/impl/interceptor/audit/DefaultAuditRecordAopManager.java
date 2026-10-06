package com.dwarfeng.subgrade.aop.impl.interceptor.audit;

import com.dwarfeng.subgrade.aop.sdk.interceptor.audit.AuditRecordAopManager;
import com.dwarfeng.subgrade.aop.sdk.interceptor.audit.AuditRecordContext;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 默认的审计记录增强管理器。
 *
 * <p>
 * 该管理器使用固定的属性键记录入口参数和方法执行结果。所有非布尔属性均转换为字符串，
 * 适用于审计属性定义简单、无需精细拆分业务字段的场景。
 *
 * @author DwArFeng
 * @since 2.1.1
 */
public class DefaultAuditRecordAopManager implements AuditRecordAopManager {

    public static final String BEAN_NAME = "defaultAuditRecordAopManager";

    public static final String PROPERTY_ARGUMENTS = "arguments";
    public static final String PROPERTY_RESULT = "result";
    public static final String PROPERTY_EXCEPTION_TYPE = "exceptionType";
    public static final String PROPERTY_EXCEPTION_MESSAGE = "exceptionMessage";
    public static final String PROPERTY_SUCCESS = "success";

    @Override
    public Map<String, Object> getProperties(AuditRecordContext context) {
        Map<String, Object> properties = new LinkedHashMap<>();
        properties.put(PROPERTY_ARGUMENTS, Arrays.deepToString(context.arguments()));
        properties.put(PROPERTY_SUCCESS, context.isSuccess());
        if (context.isSuccess()) {
            properties.put(PROPERTY_RESULT, String.valueOf(context.result()));
        } else {
            Throwable throwable = context.throwable();
            assert throwable != null;
            properties.put(PROPERTY_EXCEPTION_TYPE, throwable.getClass().getName());
            properties.put(PROPERTY_EXCEPTION_MESSAGE, String.valueOf(throwable.getMessage()));
        }
        return properties;
    }
}
