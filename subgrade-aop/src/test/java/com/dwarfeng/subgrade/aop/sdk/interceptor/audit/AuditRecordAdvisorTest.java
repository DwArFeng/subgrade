package com.dwarfeng.subgrade.aop.sdk.interceptor.audit;

import com.dwarfeng.subgrade.aop.impl.interceptor.audit.DefaultAuditRecordAopManager;
import com.dwarfeng.subgrade.aop.stack.handler.AuditRecordHandler;
import com.dwarfeng.subgrade.basic.stack.exception.HandlerException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 审计记录增强集成测试。
 *
 * <p>
 * 使用 Spring 容器创建真实代理，验证注解匹配、字段注入、管理器选择以及业务与审计异常的隔离。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
public class AuditRecordAdvisorTest {

    private AnnotationConfigApplicationContext applicationContext;
    private BusinessService target;
    private BusinessService service;
    private final List<AuditRecordContext> contexts = new ArrayList<>();
    private final List<String> categories = new ArrayList<>();
    private final List<Map<String, Object>> records = new ArrayList<>();
    private Exception managerFailure;
    private HandlerException handlerFailure;
    private int recordAttempts;

    @BeforeEach
    public void setUp() {
        applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(ProxyConfiguration.class, AuditRecordAdvisor.class);
        target = new BusinessService();
        applicationContext.registerBean("businessService", BusinessService.class, () -> target);
        applicationContext.registerBean(
                "selectedManager",
                AuditRecordAopManager.class, () -> context -> {
                    contexts.add(context);
                    if (managerFailure != null) {
                        throw managerFailure;
                    }
                    return new DefaultAuditRecordAopManager().getProperties(context);
                }
        );
        applicationContext.registerBean("unusedManager", AuditRecordAopManager.class, () -> context -> {
            throw new AssertionError("Wrong manager selected");
        });
        applicationContext.registerBean(
                "auditRecordHandler",
                AuditRecordHandler.class, () -> (category, properties) -> {
                    recordAttempts++;
                    if (handlerFailure != null) {
                        throw handlerFailure;
                    }
                    categories.add(category);
                    records.add(properties);
                }
        );
        applicationContext.refresh();
        service = applicationContext.getBean(BusinessService.class);
    }

    @AfterEach
    public void tearDown() {
        if (applicationContext != null) {
            applicationContext.close();
        }
    }

    @Test
    public void shouldInjectDependenciesAndRecordAfterSuccessfulCall() throws Exception {
        Object result = new Object();

        assertTrue(AopUtils.isAopProxy(service));
        assertSame(result, service.success(result));
        assertEquals(1, target.invocations);
        assertEquals(List.of("business.success"), categories);
        assertEquals(1, recordAttempts);
        assertEquals(1, contexts.size());
        AuditRecordContext context = contexts.getFirst();
        assertSame(result, context.result());
        assertArrayEquals(new Object[]{result}, context.arguments());
        assertEquals(BusinessService.class.getMethod("success", Object.class), context.method());
        assertSame(target, context.proceedingJoinPoint().getTarget());
        assertTrue(context.isSuccess());
        assertEquals(true, records.getFirst().get("success"));
        AuditRecordAdvisor advisor = applicationContext.getBean(AuditRecordAdvisor.class);
        assertSame(applicationContext.getBean(AuditRecordHandler.class), advisor.getAuditRecordHandler());
        assertEquals(2, advisor.getManagers().size());
        assertSame(applicationContext.getBean("selectedManager"), advisor.getManagers().get("selectedManager"));
        assertEquals(Ordered.HIGHEST_PRECEDENCE + 1200, AuditRecordAdvisor.class.getAnnotation(Order.class).value());
    }

    @Test
    public void shouldRecordFailureWithoutReplacingBusinessException() {
        IllegalArgumentException failure = new IllegalArgumentException("business failed");

        assertSame(failure, assertThrows(IllegalArgumentException.class, () -> service.failure(failure)));
        assertEquals(1, target.invocations);
        assertEquals(List.of("business.failure"), categories);
        assertSame(failure, contexts.getFirst().throwable());
        assertFalse(contexts.getFirst().isSuccess());
        assertEquals("business failed", records.getFirst().get("exceptionMessage"));
    }

    @Test
    public void shouldRecordNullAndVoidReturnsAsSuccess() {
        assertNull(service.success(null));
        service.noResult();

        assertEquals(2, target.invocations);
        assertEquals(2, recordAttempts);
        assertEquals(List.of("business.success", "business.void"), categories);
        for (Map<String, Object> properties : records) {
            assertEquals(true, properties.get("success"));
            assertEquals("null", properties.get("result"));
        }
    }

    @Test
    public void shouldLeaveUnannotatedMethodUnrecorded() {
        assertEquals("result", service.unannotated("result"));

        assertEquals(1, target.invocations);
        assertEquals(0, recordAttempts);
        assertTrue(contexts.isEmpty());
    }

    @Test
    public void shouldIsolateManagerExceptionForBothOutcomes() {
        managerFailure = new Exception("property generation failed");
        IllegalArgumentException failure = new IllegalArgumentException("business failed");

        assertEquals("result", service.success("result"));
        assertSame(failure, assertThrows(IllegalArgumentException.class, () -> service.failure(failure)));
        assertEquals(2, target.invocations);
        assertEquals(2, contexts.size());
        assertEquals(0, recordAttempts);
    }

    @Test
    public void shouldIsolateHandlerExceptionForBothOutcomes() {
        handlerFailure = new HandlerException("audit submission failed");
        IllegalArgumentException failure = new IllegalArgumentException("business failed");

        assertEquals("result", service.success("result"));
        assertSame(failure, assertThrows(IllegalArgumentException.class, () -> service.failure(failure)));
        assertEquals(2, target.invocations);
        assertEquals(2, recordAttempts);
        assertTrue(records.isEmpty());
    }

    @Test
    public void shouldIgnoreInvalidCategoryAndMissingManagerAfterBusinessCall() {
        assertEquals("blank", service.blankCategory("blank"));
        assertEquals("missing", service.missingManager("missing"));

        assertEquals(2, target.invocations);
        assertTrue(contexts.isEmpty());
        assertEquals(0, recordAttempts);
    }

    @Test
    public void shouldPreserveBusinessExceptionWhenManagerIsMissing() {
        IllegalArgumentException failure = new IllegalArgumentException("business failed");

        assertSame(failure, assertThrows(IllegalArgumentException.class, () -> service.missingManagerFailure(failure)));
        assertEquals(1, target.invocations);
        assertTrue(contexts.isEmpty());
        assertEquals(0, recordAttempts);
    }

    /**
     * 为测试业务对象启用基于类的 Spring 代理。
     */
    @Configuration(proxyBeanMethods = false)
    @EnableAspectJAutoProxy(proxyTargetClass = true)
    public static class ProxyConfiguration {
    }

    /**
     * 提供不同调用结果与注解配置的测试业务对象。
     */
    public static class BusinessService {

        private int invocations;

        @AuditRecord(categoryId = "business.success", manager = "selectedManager")
        public Object success(Object result) {
            invocations++;
            return result;
        }

        @AuditRecord(categoryId = "business.failure", manager = "selectedManager")
        public void failure(RuntimeException failure) {
            invocations++;
            throw failure;
        }

        @AuditRecord(categoryId = "business.void", manager = "selectedManager")
        public void noResult() {
            invocations++;
        }

        public Object unannotated(Object result) {
            invocations++;
            return result;
        }

        @AuditRecord(categoryId = " ", manager = "selectedManager")
        public Object blankCategory(Object result) {
            invocations++;
            return result;
        }

        @AuditRecord(categoryId = "business.missing", manager = "missingManager")
        public Object missingManager(Object result) {
            invocations++;
            return result;
        }

        @AuditRecord(categoryId = "business.missing", manager = "missingManager")
        public void missingManagerFailure(RuntimeException failure) {
            invocations++;
            throw failure;
        }
    }
}
