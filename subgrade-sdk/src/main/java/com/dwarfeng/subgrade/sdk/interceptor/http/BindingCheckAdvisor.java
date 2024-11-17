package com.dwarfeng.subgrade.sdk.interceptor.http;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

/**
 * 方法行为分析增强。
 *
 * @author DwArFeng
 * @since 0.3.0-beta
 */
@Component
@Aspect
@SuppressWarnings("DefaultAnnotationParam")
@Order(Ordered.LOWEST_PRECEDENCE) //该增强最后执行。
public class BindingCheckAdvisor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BindingCheckAdvisor.class);

    // 为了与旧代码兼容，使用字段注入，忽略相关警告。
    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired
    private BindingCheckAopManager manager;

    @Around("@annotation(com.dwarfeng.subgrade.sdk.interceptor.http.BindingCheck)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        LOGGER.debug("执行BindingCheck...");
        for (Object arg : pjp.getArgs()) {
            if (arg instanceof BindingResult && ((BindingResult) arg).hasErrors()) {
                LOGGER.debug("执行BindingCheck...");
                return manager.onHasError(pjp, (BindingResult) arg);
            }
        }
        return pjp.proceed(pjp.getArgs());
    }

    public BindingCheckAopManager getManager() {
        return manager;
    }

    public void setManager(BindingCheckAopManager manager) {
        this.manager = manager;
    }
}
