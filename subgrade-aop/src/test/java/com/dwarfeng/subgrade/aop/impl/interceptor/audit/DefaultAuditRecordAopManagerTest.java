package com.dwarfeng.subgrade.aop.impl.interceptor.audit;

import com.dwarfeng.subgrade.aop.sdk.interceptor.audit.AuditRecordContext;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.dwarfeng.subgrade.aop.sdk.interceptor.audit.AuditRecordContextTest.newContext;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 默认审计记录增强管理器测试。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class DefaultAuditRecordAopManagerTest {

    @Test
    public void shouldBuildDefaultSuccessProperties() throws Exception {
        AuditRecordContext context = newContext(new Object[]{new int[]{1, 2}, null}, 42, null);

        assertEquals(Map.of("arguments", "[[1, 2], null]", "success", true, "result", "42"),
                new DefaultAuditRecordAopManager().getProperties(context));
    }

    @Test
    public void shouldStringifyNullResult() throws Exception {
        AuditRecordContext context = newContext(new Object[0], null, null);

        assertEquals(Map.of("arguments", "[]", "success", true, "result", "null"),
                new DefaultAuditRecordAopManager().getProperties(context));
    }

    @Test
    public void shouldBuildDefaultFailurePropertiesWithNullMessage() throws Exception {
        AuditRecordContext context = newContext(new Object[]{"input"}, null, new IllegalArgumentException());

        assertEquals(Map.of("arguments", "[input]", "success", false,
                        "exceptionType", IllegalArgumentException.class.getName(), "exceptionMessage", "null"),
                new DefaultAuditRecordAopManager().getProperties(context));
    }
}
