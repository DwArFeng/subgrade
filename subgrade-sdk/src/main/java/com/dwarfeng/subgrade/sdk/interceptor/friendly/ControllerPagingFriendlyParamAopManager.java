package com.dwarfeng.subgrade.sdk.interceptor.friendly;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * 控制器分页友好性参数增强管理器。
 *
 * <p>
 * 用于在web后端项目中对分页请求进行友好化（后端的分页通常从0开始，而前端的分页通常从1开始）。
 *
 * <p>
 * 可以直接加在控制器类上，也可以加在方法上。
 * <br> 该增强被调用时，会寻找目标方法是否有名称为 {@link ControllerPagingFriendlyParamAopManager#pageParamName}
 * 的 int 类型参数，如果有，则将该参数的值减1。对于不满足条件的方法，则直接返回原始值。
 *
 * <p>
 * 例如：
 * <blockquote><pre>
 * &#64;GetMapping("your-path-here") //Controller 的路径映射
 * &#64;BehaviorAnalyse //行为、性能分析
 * &#64;LoginRequired //需要登录
 * &#64;Friendly(paramManger = "beanIdHere", optionalKey= "paging") //分页友好
 * public FastJsonResponseData<JSFixedFastJsonPagedData<YourEntityBean>> all(
 *         HttpServletRequest request,
 *         &#64;RequestParam("page") int page, &#64;RequestParam("rows") int rows) {
 *         ...
 * }
 * </pre></blockquote>
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class ControllerPagingFriendlyParamAopManager implements FriendlyParamAopManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerPagingFriendlyParamAopManager.class);

    private String pageParamName;

    @Override
    public Object[] processParam(ProceedingJoinPoint pjp, Object[] args) {
        try {
            //获取参数的所有名称。
            MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
            String[] parameterNames = methodSignature.getParameterNames();
            Class<?>[] parameterTypes = methodSignature.getParameterTypes();

            //定位参数名等于pageParamName的下标。
            int index = findPageParamName(parameterNames, parameterTypes);

            //如果找不到下标，则在日志中输出WARN级别日志并返回原始值。
            if (index < 0) {
                LOGGER.debug("此方法不符合控制器友好性分页的要求，将返回原始值");
                return args;
            }

            //指定的参数减一，并返回。
            args[index] = ((int) args[index]) - 1;
            return args;
        } catch (Exception e) {
            LOGGER.warn("友好化过程发生异常，将返回参数本身，异常信息如下:", e);
            return args;
        }
    }

    public String getPageParamName() {
        return pageParamName;
    }

    public void setPageParamName(String pageParamName) {
        this.pageParamName = pageParamName;
    }

    private int findPageParamName(String[] parameterNames, Class<?>[] parameterTypes) {
        for (int i = 0; i < parameterNames.length; i++) {
            if (Objects.equals(pageParamName, parameterNames[i]) && (parameterTypes[i].isAssignableFrom(int.class)
                    || parameterTypes[i].isAssignableFrom(Integer.class))) {
                return i;
            }
        }
        return -1;
    }
}
