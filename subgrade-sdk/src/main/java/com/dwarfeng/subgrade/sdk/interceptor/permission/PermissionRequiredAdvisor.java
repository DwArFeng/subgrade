package com.dwarfeng.subgrade.sdk.interceptor.permission;

import com.dwarfeng.subgrade.sdk.interceptor.AdvisorUtil;
import com.dwarfeng.subgrade.stack.handler.PermissionHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 权限需求增强。
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

    private final Logger LOGGER = LoggerFactory.getLogger(PermissionRequiredAdvisor.class);

    // 为了与旧代码兼容，使用字段注入，忽略相关警告。
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private PermissionHandler permissionHandler;

    // 为了与旧代码兼容，使用字段注入，忽略相关警告。
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private PermissionRequiredAopManager manager;

    @Around("@annotation(com.dwarfeng.subgrade.sdk.interceptor.permission.PermissionRequired)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 记录日志。
        LOGGER.debug("开始检查权限...");

        // 获取 PJP 中的用户信息。
        String userId = manager.getUserId(pjp);
        LOGGER.debug("manager.getUserId(pjp) = {}", userId);

        // 获取方法所需的执行权限。
        LOGGER.debug("扫描 @PermissionRequired 注解，获取方法执行所需的权限");
        PermissionRequired permissionRequired = AdvisorUtil.directMethodAnnotation(pjp, PermissionRequired.class);
        LOGGER.debug("所需的权限: ");
        for (String s : permissionRequired.value()) {
            LOGGER.debug("\t{}", s);
        }
        List<String> permissionList = Arrays.asList(permissionRequired.value());

        /*
         * 在一般情况下，hasPermission 的实现效率要远高于 getMissingPermissions 的实现效率，
         * 因此先调用 hasPermission 方法，如果 hasPermission 方法返回 false，则调用 getMissingPermissions 方法。
         */
        boolean hasAllPermission = permissionHandler.hasPermission(userId, permissionList);
        LOGGER.debug("permissionHandler.hasPermission(userId, permissionList) = {}", hasAllPermission);
        if (hasAllPermission) {
            LOGGER.debug("用户具有所需的权限，直接调度原始方法...");
            return pjp.proceed(pjp.getArgs());
        }

        // 代码执行到此处意味着用户缺失权限，调用 getMissingPermissions 方法获取用户缺失的权限。
        LOGGER.debug("查询用户缺失的权限...");
        List<String> missingPermissions = permissionHandler.getMissingPermissions(userId, permissionList);
        if (Objects.isNull(missingPermissions)) {
            missingPermissions = Collections.emptyList();
        }

        // 调用 manager 的相关调度方法。
        LOGGER.debug("用户缺失权限，调用 manager 的相关调度方法...");
        for (String s : missingPermissions) {
            LOGGER.debug("\t{}", s);
        }
        return manager.onMissingPermission(pjp, userId, missingPermissions);
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
}
