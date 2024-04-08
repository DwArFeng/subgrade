package com.dwarfeng.subgrade.sdk.interceptor.permission;

import com.dwarfeng.subgrade.sdk.interceptor.AdvisorUtil;
import com.dwarfeng.subgrade.stack.bean.key.StringIdKey;
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
import java.util.List;

/**
 * 权限需求增强。
 *
 * @author DwArFeng
 * @since 0.1.0-alpha
 */
@Component
@Aspect
// 设定一个特别高的值，保证此方法在行为分析之后立即执行，但不设置最高的值，因为有可能有其它的AOP在此方法之前执行。
// 不高于 LoginRequiredAdvisor 的优先级，因为权限检查应该在登录检查之后进行。
@Order(Ordered.HIGHEST_PRECEDENCE + 1100)
public class PermissionRequiredAdvisor {

    private final Logger LOGGER = LoggerFactory.getLogger(PermissionRequiredAdvisor.class);

    @Autowired
    private PermissionHandler permissionHandler;
    @Autowired
    private PermissionRequiredAopManager manager;

    @Around("@annotation(com.dwarfeng.subgrade.sdk.interceptor.permission.PermissionRequired)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 记录日志。
        LOGGER.debug("开始检查权限...");

        // 获取 PJP 中的用户信息。
        StringIdKey userKey = manager.getUserKey(pjp);
        LOGGER.debug("manager.getUserKey(pjp) = {}", userKey);

        //获取方法所需的执行权限。
        LOGGER.debug("扫描 @PermissionRequired 注解，获取方法执行所需的权限");
        PermissionRequired permissionRequired = AdvisorUtil.directMethodAnnotation(pjp, PermissionRequired.class);
        LOGGER.debug("所需的权限: ");
        for (String s : permissionRequired.value()) {
            LOGGER.debug("\t{}", s);
        }
        List<String> permissionList = Arrays.asList(permissionRequired.value());

        // 获取用户缺失的权限。
        LOGGER.debug("查询用户缺失的权限...");
        List<String> missingPermissions = permissionHandler.getMissingPermissions(userKey, permissionList);

        // 如果用户缺失权限，则调用 manager 的相关调度方法。
        if (!missingPermissions.isEmpty()) {
            LOGGER.debug("用户缺失权限，调用 manager 的相关调度方法...");
            for (String s : missingPermissions) {
                LOGGER.debug("\t{}", s);
            }
            return manager.onMissingPermission(pjp, userKey, missingPermissions);
        }

        // 原始方法的调度。
        return pjp.proceed(pjp.getArgs());
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
