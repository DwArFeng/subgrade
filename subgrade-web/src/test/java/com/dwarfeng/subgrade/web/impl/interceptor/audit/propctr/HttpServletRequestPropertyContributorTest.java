package com.dwarfeng.subgrade.web.impl.interceptor.audit.propctr;

import com.dwarfeng.subgrade.aop.sdk.interceptor.audit.AuditRecordContext;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * HTTP 请求属性贡献器测试。
 *
 * <p>
 * 通过 Jakarta Servlet 接口代理验证请求提取，并限制允许读取的请求属性。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class HttpServletRequestPropertyContributorTest {

    @Test
    public void shouldReadOnlyStableRequestProperties() throws Exception {
        List<String> accessedMethods = new ArrayList<>();
        HttpServletRequest request = request(Map.of(
                "getMethod", "POST", "getRequestURI", "/audit/sample", "getRemoteAddr", "127.0.0.1"
        ), accessedMethods);
        AuditRecordContext context = context(null, "argument", request);
        HttpServletRequestPropertyContributor contributor = new HttpServletRequestPropertyContributor();

        assertTrue(contributor.supports(context));
        assertEquals(Map.of("requestMethod", "POST", "requestUri", "/audit/sample", "remoteAddress", "127.0.0.1"),
                contributor.getProperties(context));
        assertEquals(List.of("getMethod", "getRequestURI", "getRemoteAddr"), accessedMethods);
    }

    @Test
    public void shouldNotSupportCallsWithoutRequest() throws Exception {
        HttpServletRequestPropertyContributor contributor = new HttpServletRequestPropertyContributor();

        assertFalse(contributor.supports(context()));
        assertFalse(contributor.supports(context(null, "argument")));
    }

    @Test
    public void shouldOmitNullRequestProperties() throws Exception {
        HttpServletRequest request = request(Map.of("getMethod", "GET"), new ArrayList<>());

        assertEquals(Map.of("requestMethod", "GET"),
                new HttpServletRequestPropertyContributor().getProperties(context(request)));
    }

    @Test
    public void shouldUseFirstRequestArgument() throws Exception {
        HttpServletRequest first = request(Map.of("getMethod", "GET"), new ArrayList<>());
        List<String> secondAccesses = new ArrayList<>();
        HttpServletRequest second = request(Map.of("getMethod", "POST"), secondAccesses);

        assertEquals(Map.of("requestMethod", "GET"),
                new HttpServletRequestPropertyContributor().getProperties(context(first, second)));
        assertTrue(secondAccesses.isEmpty());
    }

    @SuppressWarnings("SuspiciousInvocationHandlerImplementation")
    private HttpServletRequest request(Map<String, String> values, List<String> accessedMethods) {
        return (HttpServletRequest) Proxy.newProxyInstance(
                HttpServletRequest.class.getClassLoader(), new Class<?>[]{HttpServletRequest.class},
                (_, method, _) -> {
                    String name = method.getName();
                    if (!List.of("getMethod", "getRequestURI", "getRemoteAddr").contains(name)) {
                        throw new AssertionError("Unexpected request access: " + name);
                    }
                    accessedMethods.add(name);
                    return values.get(name);
                }
        );
    }

    private AuditRecordContext context(Object... arguments) throws NoSuchMethodException {
        ProceedingJoinPoint joinPoint = (ProceedingJoinPoint) Proxy.newProxyInstance(
                ProceedingJoinPoint.class.getClassLoader(), new Class<?>[]{ProceedingJoinPoint.class},
                (_, method, _) -> {
                    throw new AssertionError("Unexpected join point access: " + method.getName());
                }
        );
        return new AuditRecordContext(joinPoint, Object.class.getMethod("toString"), arguments, null, null);
    }
}
