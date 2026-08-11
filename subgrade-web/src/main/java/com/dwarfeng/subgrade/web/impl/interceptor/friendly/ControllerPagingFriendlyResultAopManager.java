package com.dwarfeng.subgrade.web.impl.interceptor.friendly;

import com.dwarfeng.subgrade.aop.sdk.interceptor.friendly.FriendlyResultAopManager;
import com.dwarfeng.subgrade.basic.stack.bean.dto.PagedData;
import com.dwarfeng.subgrade.web.internal.i18n.WebMessageKey;
import com.dwarfeng.subgrade.web.internal.i18n.WebMessages;
import com.dwarfeng.subgrade.web.sdk.bean.dto.FastJsonPagedData;
import com.dwarfeng.subgrade.web.sdk.bean.dto.JSFixedFastJsonPagedData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Objects;

/**
 * 控制器分页友好性结果增强管理器。
 *
 * <p>
 * 用于在 web 后端项目中对分页请求进行友好化（后端的分页通常从 0 开始，而前端的分页通常从 1 开始）。
 *
 * <p>
 * 可以直接加在控制器类上，也可以加在方法上。<br>
 * 该增强被调用时，会调用返回对象的 getData 方法（按照 subgrade 的规范，Controller 中的方法返回类型一定是
 * {@link com.dwarfeng.subgrade.web.stack.response.ResponseData}）或其它等效 bean 形式，如果其值不是 null，且其值类型为
 * {@link com.dwarfeng.subgrade.basic.stack.bean.dto.PagedData}，
 * {@link com.dwarfeng.subgrade.web.sdk.bean.dto.FastJsonPagedData}，
 * {@link com.dwarfeng.subgrade.web.sdk.bean.dto.JSFixedFastJsonPagedData} 中的任何一个，则调用
 * {@link com.dwarfeng.subgrade.basic.stack.bean.dto.PagedData#setCurrentPage(int)} 方法，将其值加一。
 *
 * <p>
 * 例如：
 * <blockquote><pre>
 * &#64;GetMapping("your-path-here") //Controller 的路径映射
 * &#64;BehaviorAnalyse //行为、性能分析
 * &#64;LoginRequired //需要登录
 * &#64;Friendly(paramManger = "beanIdHere", optionalKey= "paging") //分页友好
 * public FastJsonResponseData&lt;JSFixedFastJsonPagedData&lt;YourEntityBean&gt;&gt; all(
 *         HttpServletRequest request,
 *         &#64;RequestParam("page") int page, &#64;RequestParam("rows") int rows) {
 *         ...
 * }
 * </pre></blockquote>
 *
 * @author DwArFeng
 * @since 1.0.2
 */
public class ControllerPagingFriendlyResultAopManager implements FriendlyResultAopManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerPagingFriendlyResultAopManager.class);

    @Override
    public Object processResult(ProceedingJoinPoint pjp, Object result) {
        try {
            BeanWrapper resultWrapper = new BeanWrapperImpl(result);
            Object data = resultWrapper.getPropertyValue("data");
            if (!Objects.nonNull(data) || (!(data instanceof PagedData) && !(data instanceof FastJsonPagedData) &&
                    !(data instanceof JSFixedFastJsonPagedData))) {
                LOGGER.debug("此方法不符合控制器友好性分页的要求，将返回原始值");
                return result;
            }
            BeanWrapper dataWrapper = new BeanWrapperImpl(data);
            int currentPage = (int) Objects.requireNonNull(dataWrapper.getPropertyValue("currentPage"));
            dataWrapper.setPropertyValue("currentPage", currentPage + 1);
            return result;
        } catch (Exception e) {
            LOGGER.warn(WebMessages.message(WebMessageKey.LOG_FRIENDLY_FAILED), e);
            return result;
        }
    }
}
