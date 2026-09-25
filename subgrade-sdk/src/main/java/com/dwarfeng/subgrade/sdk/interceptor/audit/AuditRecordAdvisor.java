package com.dwarfeng.subgrade.sdk.interceptor.audit;

import com.dwarfeng.subgrade.sdk.SystemPropertyConstants;
import com.dwarfeng.subgrade.sdk.interceptor.AdvisorUtil;
import com.dwarfeng.subgrade.stack.handler.AuditRecordHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 审计记录增强。
 *
 * <p>
 * 该类处理方法上的 {@link AuditRecord} 注解。
 * 在代理方法执行完成后，该类使用 {@link AuditRecordAopManager} 生成审计属性，并交由 {@link AuditRecordHandler} 提交。
 *
 * <p>
 * 审计记录属于安全敏感能力。审计属性生成或提交失败时，相关异常会直接向调用方传播；
 * 如果业务方法同时抛出异常，审计异常具有更高优先级。
 *
 * @author DwArFeng
 * @since 1.10.0
 */
@Component
@Aspect
// 登录和权限检查完成后再进入审计增强，只记录真正进入业务方法的调用。
@Order(Ordered.HIGHEST_PRECEDENCE + 1200)
public class AuditRecordAdvisor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditRecordAdvisor.class);

    /**
     * 是否启用审计记录注解的 SPEL 表达式解析功能。
     */
    private static final boolean AUDIT_RECORD_USE_SPEL = Boolean.parseBoolean(
            System.getProperty(
                    SystemPropertyConstants.VALUE_AUDIT_RECORD_USE_SPEL,
                    Boolean.FALSE.toString()
            )
    );

    // 为了与旧代码兼容，使用字段注入，忽略相关警告。
    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Autowired
    private AuditRecordHandler auditRecordHandler;

    // 为了与旧代码兼容，使用字段注入，忽略相关警告。
    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Autowired
    private AuditRecordAopManager manager;

    /**
     * 可配置 bean 工厂。
     *
     * <p>
     * 仅当解析方式为 SPEL 时，需要该属性。
     */
    // 为了与旧代码兼容，使用字段注入，忽略相关警告。
    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Nullable
    @Autowired(required = false)
    private ConfigurableBeanFactory configurableBeanFactory;

    /**
     * SPEL 表达式解析器。
     */
    // 为了与旧代码兼容，使用字段注入，忽略相关警告。
    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Nullable
    @Autowired(required = false)
    private ExpressionParser expressionParser;

    /**
     * SPEL 解析上下文。
     */
    // 为了与旧代码兼容，使用字段注入，忽略相关警告。
    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Nullable
    @Autowired(required = false)
    private ParserContext parserContext;

    @Around("@annotation(com.dwarfeng.subgrade.sdk.interceptor.audit.AuditRecord)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 记录日志。
        LOGGER.debug("正在记录审计信息...");

        // 获取 PJP 中的注解信息。
        AuditRecord auditRecord = AdvisorUtil.directMethodAnnotation(pjp, AuditRecord.class);

        // 解析注解中的审计类别 ID。
        String categoryId = resolveCategoryId(auditRecord);
        LOGGER.debug("解析后的审计类别 ID: {}", categoryId);
        if (Objects.isNull(categoryId) || categoryId.trim().isEmpty()) {
            throw new IllegalStateException("审计类别 ID 不能为空");
        }

        // 执行代理方法，并记录返回结果和异常信息。
        Object result = null;
        Throwable throwable = null;
        try {
            result = pjp.proceed(pjp.getArgs());
        } catch (Throwable t) {
            throwable = t;
        }

        // 执行审计记录。
        AuditRecordContext context = new AuditRecordContext(pjp, result, throwable);
        Map<String, Object> properties = manager.resolveProperties(context);
        auditRecordHandler.record(categoryId, properties);

        // 如果 throwable 不为 null，则抛出异常。
        if (Objects.nonNull(throwable)) {
            throw throwable;
        }

        // 返回结果。
        return result;
    }

    /**
     * 解析审计类别 ID。
     *
     * <p>
     * 根据系统属性 {@link SystemPropertyConstants#VALUE_AUDIT_RECORD_USE_SPEL} 决定是否启用 SPEL 表达式解析。
     * 如果启用 SPEL 解析，则对审计类别 ID 进行 SPEL 表达式解析；否则直接返回原始审计类别 ID。
     *
     * @param auditRecord 审计记录注解。
     * @return 解析后的审计类别 ID。
     */
    private String resolveCategoryId(AuditRecord auditRecord) {
        if (AUDIT_RECORD_USE_SPEL) {
            LOGGER.debug("SPEL 解析功能已启用, 开始进行 SPEL 解析");
            return resolveCategoryIdAsSpel(auditRecord);
        } else {
            LOGGER.debug("SPEL 解析功能已禁用, 直接返回原始值");
            return resolveCategoryIdAsIs(auditRecord);
        }
    }

    private String resolveCategoryIdAsSpel(AuditRecord auditRecord) {
        // 确保可配置 bean 工厂不为 null。
        if (Objects.isNull(configurableBeanFactory)) {
            LOGGER.warn("可配置 bean 工厂未设置, 无法进行 SPEL 解析, 将抛出异常");
            LOGGER.warn(
                    "如果希望禁用 SPEL 解析功能, 请将系统属性 {} 设为 false",
                    SystemPropertyConstants.VALUE_AUDIT_RECORD_USE_SPEL
            );
            throw new IllegalStateException("可配置 bean 工厂未设置, 无法进行 SPEL 解析");
        }
        // 确保表达式解析器不为 null。
        if (Objects.isNull(expressionParser)) {
            LOGGER.warn("表达式解析器未设置, 无法进行 SPEL 解析, 将抛出异常");
            LOGGER.warn(
                    "如果希望禁用 SPEL 解析功能, 请将系统属性 {} 设为 false",
                    SystemPropertyConstants.VALUE_AUDIT_RECORD_USE_SPEL
            );
            throw new IllegalStateException("表达式解析器未设置, 无法进行 SPEL 解析");
        }
        // 确保解析上下文不为 null。
        if (Objects.isNull(parserContext)) {
            LOGGER.warn("解析上下文未设置, 无法进行 SPEL 解析, 将抛出异常");
            LOGGER.warn(
                    "如果希望禁用 SPEL 解析功能, 请将系统属性 {} 设为 false",
                    SystemPropertyConstants.VALUE_AUDIT_RECORD_USE_SPEL
            );
            throw new IllegalStateException("解析上下文未设置, 无法进行 SPEL 解析");
        }

        // 创建结果。
        String result;

        // 对审计类别 ID 进行 SPEL 解析。
        result = Optional.ofNullable(configurableBeanFactory.resolveEmbeddedValue(auditRecord.value()))
                .orElse(auditRecord.value());
        result = expressionParser.parseExpression(result, parserContext).getValue(String.class);

        // 返回结果。
        return result;
    }

    private String resolveCategoryIdAsIs(AuditRecord auditRecord) {
        return auditRecord.value();
    }

    public AuditRecordHandler getAuditRecordHandler() {
        return auditRecordHandler;
    }

    public void setAuditRecordHandler(AuditRecordHandler auditRecordHandler) {
        this.auditRecordHandler = auditRecordHandler;
    }

    public AuditRecordAopManager getManager() {
        return manager;
    }

    public void setManager(AuditRecordAopManager manager) {
        this.manager = manager;
    }

    @Nullable
    public ConfigurableBeanFactory getConfigurableBeanFactory() {
        return configurableBeanFactory;
    }

    public void setConfigurableBeanFactory(@Nullable ConfigurableBeanFactory configurableBeanFactory) {
        this.configurableBeanFactory = configurableBeanFactory;
    }

    @Nullable
    public ExpressionParser getExpressionParser() {
        return expressionParser;
    }

    public void setExpressionParser(@Nullable ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    @Nullable
    public ParserContext getParserContext() {
        return parserContext;
    }

    public void setParserContext(@Nullable ParserContext parserContext) {
        this.parserContext = parserContext;
    }

    @Override
    public String toString() {
        return "AuditRecordAdvisor{" +
                "auditRecordHandler=" + auditRecordHandler +
                ", manager=" + manager +
                ", configurableBeanFactory=" + configurableBeanFactory +
                ", expressionParser=" + expressionParser +
                ", parserContext=" + parserContext +
                '}';
    }
}
