package com.dwarfeng.subgrade.aop.sdk.interceptor.audit;

import com.dwarfeng.subgrade.aop.sdk.interceptor.AdvisorUtil;
import com.dwarfeng.subgrade.aop.stack.handler.AuditRecordHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * 审计记录增强。
 *
 * <p>
 * 该类处理方法上的 {@link AuditRecord} 注解。
 * 在代理方法执行完成后，该类根据注解指定的 Bean 名称选择 {@link AuditRecordAopManager}，
 * 生成审计属性并交由 {@link AuditRecordHandler} 提交。
 *
 * <p>
 * 审计记录属于外围能力。审计属性生成或提交失败时，该类记录异常日志，但不会改变代理方法的返回值，
 * 也不会覆盖代理方法抛出的原始异常。
 *
 * @author DwArFeng
 * @since 2.1.0
 */
@Component
@Aspect
// 登录和权限检查完成后再进入审计增强，只记录真正进入业务方法的调用。
@Order(Ordered.HIGHEST_PRECEDENCE + 1200)
public class AuditRecordAdvisor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditRecordAdvisor.class);

    // 为了与旧代码兼容，使用字段注入，忽略相关警告。
    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Autowired
    private AuditRecordHandler auditRecordHandler;

    // 为了与旧代码兼容，使用字段注入，忽略相关警告。
    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Autowired
    private Map<String, AuditRecordAopManager> managers;

    @Around("@annotation(com.dwarfeng.subgrade.aop.sdk.interceptor.audit.AuditRecord)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Method method = AdvisorUtil.directMethod(pjp);
        AuditRecord auditRecord = AdvisorUtil.directMethodAnnotation(pjp, AuditRecord.class);
        Object[] arguments = Arrays.copyOf(pjp.getArgs(), pjp.getArgs().length);

        Object result;
        try {
            result = pjp.proceed(pjp.getArgs());
        } catch (Throwable throwable) {
            record(auditRecord, new AuditRecordContext(pjp, method, arguments, null, throwable));
            throw throwable;
        }

        record(auditRecord, new AuditRecordContext(pjp, method, arguments, result, null));
        return result;
    }

    private void record(AuditRecord auditRecord, AuditRecordContext context) {
        String categoryId = auditRecord.categoryId();
        String managerName = auditRecord.manager();
        try {
            if (Objects.isNull(categoryId) || categoryId.trim().isEmpty()) {
                throw new IllegalStateException("审计类别 ID 不能为空");
            }
            AuditRecordAopManager manager = managers.get(managerName);
            if (Objects.isNull(manager)) {
                throw new IllegalStateException("未找到指定的审计记录增强管理器: " + managerName);
            }
            Map<String, Object> properties = manager.getProperties(context);
            auditRecordHandler.record(categoryId, properties);
        } catch (Exception exception) {
            LOGGER.warn(
                    "记录审计信息失败, method = {}, categoryId = {}, manager = {}",
                    context.method(), categoryId, managerName, exception
            );
        }
    }

    public AuditRecordHandler getAuditRecordHandler() {
        return auditRecordHandler;
    }

    public void setAuditRecordHandler(AuditRecordHandler auditRecordHandler) {
        this.auditRecordHandler = auditRecordHandler;
    }

    public Map<String, AuditRecordAopManager> getManagers() {
        return managers;
    }

    public void setManagers(Map<String, AuditRecordAopManager> managers) {
        this.managers = managers;
    }

    @Override
    public String toString() {
        return "AuditRecordAdvisor{" +
                "auditRecordHandler=" + auditRecordHandler +
                ", managers=" + managers +
                '}';
    }
}
