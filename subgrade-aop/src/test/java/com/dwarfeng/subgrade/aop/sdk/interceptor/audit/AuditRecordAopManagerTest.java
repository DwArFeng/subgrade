package com.dwarfeng.subgrade.aop.sdk.interceptor.audit;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.dwarfeng.subgrade.aop.sdk.interceptor.audit.AuditRecordContextTest.newContext;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 审计属性管理器测试。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class AuditRecordAopManagerTest {

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

    @Test
    public void shouldMergeContributionsInConfiguredOrder() throws Exception {
        List<String> invocationOrder = new ArrayList<>();
        PropertyContributor first = orderedContributor("first", invocationOrder);
        PropertyContributor second = orderedContributor("second", invocationOrder);
        ContributorAuditRecordAopManager manager = new ContributorAuditRecordAopManager(List.of(first, second));

        Map<String, Object> properties = manager.getProperties(newContext(new Object[0], null, null));

        assertEquals(List.of("first", "second"), invocationOrder);
        assertEquals(List.of("first", "second"), List.copyOf(properties.keySet()));
        assertEquals(Map.of("first", "first", "second", "second"), properties);
    }

    @Test
    public void shouldSkipUnsupportedNullMapAndNullValues() throws Exception {
        PropertyContributor unsupported = new PropertyContributor() {
            @Override
            public boolean supports(AuditRecordContext context) {
                return false;
            }

            @Override
            public Map<String, Object> getProperties(AuditRecordContext context) {
                throw new AssertionError("Unsupported contributor must not be invoked");
            }
        };
        Map<String, Object> nullableValues = new LinkedHashMap<>();
        nullableValues.put("ignored", null);
        nullableValues.put("kept", "value");
        ContributorAuditRecordAopManager manager = new ContributorAuditRecordAopManager(List.of(
                unsupported, constantContributor(null), constantContributor(nullableValues)
        ));

        assertEquals(Map.of("kept", "value"), manager.getProperties(newContext(new Object[0], null, null)));
    }

    @Test
    public void shouldRejectNullKeyAndDuplicateKey() throws Exception {
        AuditRecordContext context = newContext(new Object[0], null, null);
        Map<String, Object> nullKey = new LinkedHashMap<>();
        nullKey.put(null, "value");
        ContributorAuditRecordAopManager invalid = new ContributorAuditRecordAopManager(
                List.of(constantContributor(nullKey)));
        ContributorAuditRecordAopManager duplicate = new ContributorAuditRecordAopManager(List.of(
                constantContributor(Map.of("same", "first")), constantContributor(Map.of("same", "second"))
        ));

        assertThrows(IllegalStateException.class, () -> invalid.getProperties(context));
        assertThrows(IllegalStateException.class, () -> duplicate.getProperties(context));
    }

    @Test
    public void shouldCopyContributorConfigurationAndSupportEmptyList() throws Exception {
        AuditRecordContext context = newContext(new Object[0], null, null);
        ContributorAuditRecordAopManager manager = new ContributorAuditRecordAopManager();
        assertEquals(Map.of(), manager.getProperties(context));

        List<PropertyContributor> contributors = new ArrayList<>();
        contributors.add(constantContributor(Map.of("kept", "value")));
        manager.setContributors(contributors);
        contributors.clear();

        assertEquals(Map.of("kept", "value"), manager.getProperties(context));
        assertThrows(UnsupportedOperationException.class, () -> manager.getContributors().clear());
    }

    @Test
    public void shouldPropagateContributorExceptionToAdvisor() throws Exception {
        Exception failure = new Exception("contributor failed");
        PropertyContributor failing = new PropertyContributor() {
            @Override
            public boolean supports(AuditRecordContext context) {
                return true;
            }

            @Override
            public Map<String, Object> getProperties(AuditRecordContext context) throws Exception {
                throw failure;
            }
        };
        ContributorAuditRecordAopManager manager = new ContributorAuditRecordAopManager(List.of(failing));
        AuditRecordContext context = newContext(new Object[0], null, null);

        assertSame(failure, assertThrows(Exception.class, () -> manager.getProperties(context)));
    }

    private PropertyContributor constantContributor(Map<String, Object> properties) {
        return new PropertyContributor() {
            @Override
            public boolean supports(AuditRecordContext context) {
                return true;
            }

            @Override
            public Map<String, Object> getProperties(AuditRecordContext context) {
                return properties;
            }
        };
    }

    private PropertyContributor orderedContributor(String name, List<String> invocationOrder) {
        return new PropertyContributor() {
            @Override
            public boolean supports(AuditRecordContext context) {
                return true;
            }

            @Override
            public Map<String, Object> getProperties(AuditRecordContext context) {
                invocationOrder.add(name);
                return Map.of(name, name);
            }
        };
    }
}
