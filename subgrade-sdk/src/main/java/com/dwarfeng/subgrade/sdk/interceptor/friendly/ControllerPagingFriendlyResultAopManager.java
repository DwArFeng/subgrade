package com.dwarfeng.subgrade.sdk.interceptor.friendly;

import com.dwarfeng.subgrade.sdk.bean.dto.FastJsonPagedData;
import com.dwarfeng.subgrade.sdk.bean.dto.JSFixedFastJsonPagedData;
import com.dwarfeng.subgrade.stack.bean.dto.PagedData;
import org.apache.commons.beanutils.PropertyUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * 控制器分页友好性结果增强管理器。
 *
 * <p>
 * 用于在web后端项目中对分页请求进行友好化（后端的分页通常从0开始，而前端的分页通常从1开始）。
 *
 * <p>
 * 可以直接加在控制器类上，也可以加在方法上。
 * <br> 该增强被调用时，会调用返回对象的getData方法（按照subgrade的规范，Controller中的方法返回类型一定是
 * {@link com.dwarfeng.subgrade.stack.bean.dto.ResponseData}）或其它等效bean形式，如果其值不是 null，且其值类型为
 * {@link com.dwarfeng.subgrade.stack.bean.dto.PagedData}，{@link com.dwarfeng.subgrade.sdk.bean.dto.FastJsonPagedData}，
 * {@link com.dwarfeng.subgrade.sdk.bean.dto.JSFixedFastJsonPagedData} 中的任何一个，则调用
 * {@link com.dwarfeng.subgrade.stack.bean.dto.PagedData#setCurrentPage(int)} 方法，将其值加一。
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
 * @since 1.0.2
 */
public class ControllerPagingFriendlyResultAopManager implements FriendlyResultAopManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerPagingFriendlyResultAopManager.class);

    @Override
    public Object processResult(ProceedingJoinPoint pjp, Object result) {
        try {
            Object data = PropertyUtils.getProperty(result, "data");
            if (!Objects.nonNull(data) || (!(data instanceof PagedData) && !(data instanceof FastJsonPagedData) &&
                    !(data instanceof JSFixedFastJsonPagedData))) {
                LOGGER.debug("此方法不符合控制器友好性分页的要求，将返回原始值");
                return result;
            }
            int currentPage = (int) PropertyUtils.getProperty(data, "currentPage");
            PropertyUtils.setProperty(data, "currentPage", currentPage + 1);
            return result;
        } catch (Exception e) {
            LOGGER.warn("友好化过程发生异常，将返回参数本身，异常信息如下:", e);
            return result;
        }
    }
}
