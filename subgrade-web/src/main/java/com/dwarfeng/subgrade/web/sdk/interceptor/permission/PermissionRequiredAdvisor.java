package com.dwarfeng.subgrade.web.sdk.interceptor.permission;

import com.dwarfeng.subgrade.aop.sdk.interceptor.AdvisorUtil;
import com.dwarfeng.subgrade.expression.stack.exception.ExpressionParseException;
import com.dwarfeng.subgrade.expression.stack.parser.ExpressionParser;
import com.dwarfeng.subgrade.web.internal.i18n.WebMessageKey;
import com.dwarfeng.subgrade.web.internal.i18n.WebMessages;
import com.dwarfeng.subgrade.web.sdk.configuration.SystemPropertyConstants;
import com.dwarfeng.subgrade.web.stack.handler.PermissionHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 权限需求增强。
 *
 * <p>
 * 该类是一个 AOP 增强类，用于处理方法上的 {@link PermissionRequired} 注解。
 * 该类将 {@link PermissionRequired} 注解中的 {@link PermissionRequired#value()}
 * 中定义的权限与当前用户的权限进行比较，如果用户缺失所需的权限，则调用 {@link PermissionRequiredAopManager} 的相关调度方法。
 *
 * <p>
 * 该类处理 {@link PermissionRequired} 注解时，基于部分系统属性的配置，
 * 可对注解中的 {@link PermissionRequired#value()} 应用不同的处理逻辑。<br>
 * 系统属性基于优先级进行判断，该类将按照优先级从高到低的顺序判断系统属性，如果高优先级的系统属性处于启用状态，
 * 则使用该系统属性对应的处理逻辑；如果高优先级的系统属性处于禁用状态，则继续判断下一个优先级的系统属性，
 * 直到找到一个启用状态的系统属性或者所有系统属性都处于禁用状态。<br>
 * 相关的系统属性以及优先级见下表：
 * <table>
 *     <tr>
 *         <th>优先级</th>
 *         <th>系统属性</th>
 *         <th>处理逻辑</th>
 *     </tr>
 *     <tr>
 *         <td>1</td>
 *         <td>{@link SystemPropertyConstants#VALUE_PERMISSION_REQUIRED_USE_EXPRESSION}</td>
 *         <td>
 *             使用配置的通用表达式解析器处理 {@link PermissionRequired#value()} 中的每个权限值，
 *             解析后的结果作为最终的权限值列表。
 *         </td>
 *     </tr>
 *     <tr>
 *         <td>2</td>
 *         <td>无（缺省）</td>
 *         <td>
 *             直接将 {@link PermissionRequired#value()} 中的权限值作为最终的权限值解析，
 *             解析后的结果作为最终的权限值列表。
 *         </td>
 *     </tr>
 * </table>
 *
 * @author DwArFeng
 * @since 1.7.0
 */
@Component
@Aspect
// 设定一个特别高的值，保证此方法在行为分析之后立即执行，但不设置最高的值，因为有可能有其它的 AOP 在此方法之前执行。
// 不高于 LoginRequiredAdvisor 的优先级，因为权限检查应该在登录检查之后进行。
@Order(Ordered.HIGHEST_PRECEDENCE + 1100)
public class PermissionRequiredAdvisor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionRequiredAdvisor.class);

    /**
     * 是否启用权限需求注解的表达式解析功能。
     *
     * @since 1.7.3
     */
    private static final boolean PERMISSION_REQUIRED_USE_EXPRESSION = Boolean.parseBoolean(
            System.getProperty(
                    SystemPropertyConstants.VALUE_PERMISSION_REQUIRED_USE_EXPRESSION,
                    Boolean.FALSE.toString()
            )
    );

    // 为了与旧代码兼容，使用字段注入，忽略相关警告。
    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Autowired
    private PermissionHandler permissionHandler;

    // 为了与旧代码兼容，使用字段注入，忽略相关警告。
    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Autowired
    private PermissionRequiredAopManager manager;

    /**
     * 通用表达式解析器。
     *
     * @since 1.7.3
     */
    // 为了与旧代码兼容，使用字段注入，忽略相关警告。
    @SuppressWarnings("SpringAutowiredFieldsWarningInspection")
    @Nullable
    @Autowired(required = false)
    private ExpressionParser expressionParser;

    @Around("@annotation(com.dwarfeng.subgrade.web.sdk.interceptor.permission.PermissionRequired)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 记录日志。
        LOGGER.debug("开始检查权限...");

        // 获取 PJP 中的用户信息。
        String userId = manager.getUserId(pjp);
        LOGGER.debug("manager.getUserId(pjp) = {}", userId);

        // 获取方法所需的执行权限。
        LOGGER.debug("扫描 @PermissionRequired 注解，获取方法执行所需的权限");
        PermissionRequired permissionRequired = AdvisorUtil.directMethodAnnotation(pjp, PermissionRequired.class);
        for (String s : permissionRequired.value()) {
            LOGGER.debug("\t{}", s);
        }
        // 解析权限值列表（可能包含表达式）。
        List<String> permissionList = resolvePermissions(permissionRequired.value());
        LOGGER.debug("所需的权限: ");
        for (int i = 0; i < permissionRequired.value().length; i++) {
            LOGGER.debug("\t{} -> {}", permissionRequired.value()[i], permissionList.get(i));
        }

        // 获取用户缺失的权限。
        LOGGER.debug("查询用户缺失的权限...");
        List<String> missingPermissions = permissionHandler.getMissingPermissions(userId, permissionList);

        // 如果用户缺失权限，则调用 manager 的相关调度方法。
        if (!missingPermissions.isEmpty()) {
            LOGGER.debug("用户缺失权限，调用 manager 的相关调度方法...");
            for (String s : missingPermissions) {
                LOGGER.debug("\t{}", s);
            }
            return manager.onMissingPermission(pjp, userId, missingPermissions);
        }

        // 原始方法的调度。
        return pjp.proceed(pjp.getArgs());
    }

    /**
     * 解析权限值列表。
     *
     * <p>
     * 根据系统属性 {@link SystemPropertyConstants#VALUE_PERMISSION_REQUIRED_USE_EXPRESSION} 决定是否启用表达式解析。
     * 如果启用表达式解析，则对每个权限值进行解析；否则直接返回原始权限值列表。
     *
     * @param permissionValues 原始权限值数组。
     * @return 解析后的权限值列表。
     * @since 1.7.3
     */
    private List<String> resolvePermissions(@NotNull String[] permissionValues) throws ExpressionParseException {
        if (PERMISSION_REQUIRED_USE_EXPRESSION) {
            LOGGER.debug("表达式解析功能已启用, 开始进行表达式解析");
            return resolvePermissionsAsExpression(permissionValues);
        } else {
            LOGGER.debug("表达式解析功能已禁用, 直接返回原始权限值列表");
            return resolvePermissionsAsIs(permissionValues);
        }
    }

    private List<String> resolvePermissionsAsIs(String[] permissionValues) {
        return Arrays.asList(permissionValues);
    }

    private List<String> resolvePermissionsAsExpression(String[] permissionValues) throws ExpressionParseException {
        // 确保表达式解析器不为 null。
        if (Objects.isNull(expressionParser)) {
            LOGGER.warn(WebMessages.message(WebMessageKey.LOG_EXPRESSION_PARSER_MISSING));
            LOGGER.warn(WebMessages.message(WebMessageKey.LOG_EXPRESSION_DISABLED_HINT,
                    SystemPropertyConstants.VALUE_PERMISSION_REQUIRED_USE_EXPRESSION
            ));
            throw new IllegalStateException(WebMessages.message(WebMessageKey.ERROR_EXPRESSION_PARSER_MISSING));
        }

        // 创建解析后的权限值列表。
        List<String> resolvedPermissions = new ArrayList<>();

        // 对每个 permissionValues 中的值进行表达式解析。
        for (String permissionValue : permissionValues) {
            permissionValue = expressionParser.parseExpression(permissionValue, null, String.class);
            // 将解析结果添加到结果列表中。
            resolvedPermissions.add(permissionValue);
        }

        // 返回结果。
        return resolvedPermissions;
    }

    public PermissionHandler getPermissionHandler() {
        return permissionHandler;
    }

    public void setPermissionHandler(PermissionHandler permissionHandler) {
        this.permissionHandler = permissionHandler;
    }

    public PermissionRequiredAopManager getManager() {
        return manager;
    }

    public void setManager(PermissionRequiredAopManager manager) {
        this.manager = manager;
    }

    @Nullable
    public ExpressionParser getExpressionParser() {
        return expressionParser;
    }

    public void setExpressionParser(@Nullable ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;
    }

    @Override
    public String toString() {
        return "PermissionRequiredAdvisor{" +
                "permissionHandler=" + permissionHandler +
                ", manager=" + manager +
                ", expressionParser=" + expressionParser +
                '}';
    }
}
