package com.dwarfeng.subgrade.sdk.interceptor.audit.propctr;

import com.dwarfeng.subgrade.sdk.interceptor.audit.AuditRecordContext;
import com.dwarfeng.subgrade.sdk.interceptor.audit.PropertyContributor;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * HTTP 请求属性贡献器。
 *
 * <p>
 * 该贡献器从入口参数中查找 {@link HttpServletRequest}，并记录稳定的请求方法、请求 URI 和远程地址。
 * 为避免泄露凭证或产生过大的审计属性，该贡献器不记录请求头、请求体、Token 和 Session。
 *
 * @author DwArFeng
 * @since 1.9.0
 */
public class HttpServletRequestPropertyContributor implements PropertyContributor {

    public static final String PROPERTY_REQUEST_METHOD = "requestMethod";
    public static final String PROPERTY_REQUEST_URI = "requestUri";
    public static final String PROPERTY_REMOTE_ADDRESS = "remoteAddress";

    @Override
    public boolean supports(AuditRecordContext context) {
        return findRequest(context) != null;
    }

    @Override
    public Map<String, Object> getProperties(AuditRecordContext context) {
        HttpServletRequest request = Objects.requireNonNull(findRequest(context));
        Map<String, Object> properties = new LinkedHashMap<>();
        putIfNonNull(properties, PROPERTY_REQUEST_METHOD, request.getMethod());
        putIfNonNull(properties, PROPERTY_REQUEST_URI, request.getRequestURI());
        putIfNonNull(properties, PROPERTY_REMOTE_ADDRESS, request.getRemoteAddr());
        return properties;
    }

    private HttpServletRequest findRequest(AuditRecordContext context) {
        for (Object argument : context.getArguments()) {
            if (argument instanceof HttpServletRequest) {
                return (HttpServletRequest) argument;
            }
        }
        return null;
    }

    private void putIfNonNull(Map<String, Object> properties, String key, Object value) {
        if (Objects.nonNull(value)) {
            properties.put(key, value);
        }
    }
}
