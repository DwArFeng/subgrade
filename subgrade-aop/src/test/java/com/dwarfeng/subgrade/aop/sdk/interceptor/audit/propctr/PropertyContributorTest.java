package com.dwarfeng.subgrade.aop.sdk.interceptor.audit.propctr;

import com.dwarfeng.subgrade.aop.sdk.interceptor.audit.AuditRecordContext;
import com.dwarfeng.subgrade.aop.sdk.interceptor.audit.ContributorAuditRecordAopManager;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static com.dwarfeng.subgrade.aop.sdk.interceptor.audit.AuditRecordContextTest.newContext;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 返回值与异常属性贡献器测试。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class PropertyContributorTest {

    @Test
    public void shouldContributeOnlyForMatchingOutcome() throws Exception {
        AuditRecordContext success = newContext(new Object[0], 42, null);
        AuditRecordContext failure = newContext(new Object[0], null, new IllegalStateException("failed"));
        ResultPropertyContributor resultContributor = new ResultPropertyContributor();
        ThrowablePropertyContributor throwableContributor = new ThrowablePropertyContributor();

        assertTrue(resultContributor.supports(success));
        assertFalse(resultContributor.supports(failure));
        assertFalse(throwableContributor.supports(success));
        assertTrue(throwableContributor.supports(failure));
        assertEquals(Map.of("success", true, "result", "42"), resultContributor.getProperties(success));
        assertEquals(Map.of("success", false, "exceptionType", IllegalStateException.class.getName(),
                "exceptionMessage", "failed"), throwableContributor.getProperties(failure));
    }

    @Test
    public void shouldCombineOutcomeContributorsWithoutKeyConflicts() throws Exception {
        ContributorAuditRecordAopManager manager = new ContributorAuditRecordAopManager(List.of(
                new ResultPropertyContributor(), new ThrowablePropertyContributor()
        ));

        assertEquals(Map.of("success", true, "result", "null"),
                manager.getProperties(newContext(new Object[0], null, null)));
        assertEquals(Map.of("success", false, "exceptionType", IllegalStateException.class.getName(),
                        "exceptionMessage", "null"),
                manager.getProperties(newContext(new Object[0], null, new IllegalStateException())));
    }
}
