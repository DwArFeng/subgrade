package com.dwarfeng.subgrade.sdk.interceptor.login;

import com.dwarfeng.subgrade.sdk.interceptor.AdvisorUtil;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;
import com.dwarfeng.subgrade.stack.handler.LoginHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 登录需求增强。
 *
 * @author DwArFeng
 * @since 0.1.1-alpha
 */
@Component
@Aspect
//设定一个特别高的值，保证此方法在行为分析之后立即执行，但不设置最高的值，因为有可能有其它的AOP在此方法之前执行。
@Order(Ordered.HIGHEST_PRECEDENCE + 1000)
public class LoginRequiredAdvisor {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginRequiredAdvisor.class);

    @Autowired
    private LoginHandler loginHandler;
    @Autowired
    private LoginRequiredAopManager manager;

    @Around("@annotation(com.dwarfeng.subgrade.sdk.interceptor.login.LoginRequired)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        // 记录日志。
        LOGGER.debug("正在验证是否登录...");

        // 获取PJP中的登录ID信息。
        LongIdKey loginId = manager.getLoginId(pjp);
        LOGGER.debug("manager.getLoginId(pjp) = " + loginId);

        // 判断是否登录。
        if (!loginHandler.isLogin(loginId)) {
            // 没有登录时进行的调度。
            LOGGER.debug("指定的ID " + loginId + " 没有登录");
            return manager.onNotLogin(pjp, loginId);
        }

        // 用户登录后进行的调度。
        LOGGER.debug("指定的ID " + loginId + " 登录了");
        if (isPostpone(pjp)) {
            // 如果注解中注明了要延长登录时间。
            LOGGER.info("指定的ID " + loginId + " 延长登录时间...");
            loginHandler.postpone(loginId);
        }

        //原始方法的调度。
        return pjp.proceed(pjp.getArgs());
    }

    private boolean isPostpone(ProceedingJoinPoint pjp) {
        //获取方法所需的执行登录。
        LOGGER.debug("扫描 @LoginRequired 注解，获取方法执行所需的登录");
        LoginRequired loginRequired = AdvisorUtil.directMethodAnnotation(pjp, LoginRequired.class);
        return loginRequired.postpone();
    }

    public LoginHandler getLoginHandler() {
        return loginHandler;
    }

    public void setLoginHandler(LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }

    public LoginRequiredAopManager getManager() {
        return manager;
    }

    public void setManager(LoginRequiredAopManager manager) {
        this.manager = manager;
    }
}
