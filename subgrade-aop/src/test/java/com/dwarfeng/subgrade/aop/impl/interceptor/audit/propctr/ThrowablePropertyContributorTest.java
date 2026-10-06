package com.dwarfeng.subgrade.aop.impl.interceptor.audit.propctr;

import com.dwarfeng.subgrade.aop.sdk.interceptor.audit.AuditRecordContext;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.dwarfeng.subgrade.aop.sdk.interceptor.audit.AuditRecordContextTest.newContext;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 异常属性贡献器测试。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class ThrowablePropertyContributorTest {

    @Test
    public void shouldContributeOnlyForMatchingOutcome() throws Exception {
        AuditRecordContext success = newContext(new Object[0], 42, null);
        AuditRecordContext failure = newContext(new Object[0], null, new IllegalStateException("failed"));
        ThrowablePropertyContributor throwableContributor = new ThrowablePropertyContributor();

        assertFalse(throwableContributor.supports(success));
        assertTrue(throwableContributor.supports(failure));
        assertEquals(Map.of("success", false, "exceptionType", IllegalStateException.class.getName(),
                "exceptionMessage", "failed"), throwableContributor.getProperties(failure));
    }
}
