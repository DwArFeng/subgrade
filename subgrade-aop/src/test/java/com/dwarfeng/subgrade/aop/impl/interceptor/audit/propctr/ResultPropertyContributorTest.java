package com.dwarfeng.subgrade.aop.impl.interceptor.audit.propctr;

import com.dwarfeng.subgrade.aop.sdk.interceptor.audit.AuditRecordContext;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.dwarfeng.subgrade.aop.sdk.interceptor.audit.AuditRecordContextTest.newContext;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 返回值属性贡献器测试。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class ResultPropertyContributorTest {

    @Test
    public void shouldContributeOnlyForMatchingOutcome() throws Exception {
        AuditRecordContext success = newContext(new Object[0], 42, null);
        AuditRecordContext failure = newContext(new Object[0], null, new IllegalStateException("failed"));
        ResultPropertyContributor resultContributor = new ResultPropertyContributor();

        assertTrue(resultContributor.supports(success));
        assertFalse(resultContributor.supports(failure));
        assertEquals(Map.of("success", true, "result", "42"), resultContributor.getProperties(success));
    }
}
