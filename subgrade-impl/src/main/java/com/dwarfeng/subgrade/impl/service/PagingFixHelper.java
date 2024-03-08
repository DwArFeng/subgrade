package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.SystemPropertyConstants;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.exception.PagingException;
import com.dwarfeng.subgrade.stack.service.EntireLookupService;
import com.dwarfeng.subgrade.stack.service.PresetLookupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 分页修正帮助类。
 *
 * @author DwArFeng
 * @since 1.5.0
 */
public final class PagingFixHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(PagingFixHelper.class);

    private static final boolean USE_STRICT_PAGING = Boolean.parseBoolean(
            System.getProperty(SystemPropertyConstants.USE_STRICT_PAGING, Boolean.FALSE.toString())
    );

    private static final Boolean LOG_PAGING_WARNING = Boolean.parseBoolean(
            System.getProperty(SystemPropertyConstants.LOG_PAGING_WARNING, Boolean.TRUE.toString())
    );

    /**
     * 基于系统属性修正分页信息或抛出异常。
     *
     * <p>
     * 1.5.0 版本对 {@link EntireLookupService} 和 {@link PresetLookupService} 的分页查询方法做了新特性的支持。
     * 您可以通过使用此方法来修正分页信息，或者抛出异常。使用此方法可以方便地适配 1.5.0 版本要求的新特性。<br>
     * 以下是使用此方法的示例代码：
     * <blockquote>
     * <pre>
     * {@literal @Override}
     * public PagedData<E> lookup(PagingInfo pagingInfo) throws ServiceException {
     *     try {
     *         <b>// 使用 mayFixPagingInfo 修正分页信息。</b>
     *         <b>pagingInfo = PagingFixHelper.mayFixPagingInfo(pagingInfo);</b>
     *         // 使用修正后的 pagingInfo 查询数据，得出的结果即可适配 1.5.0 版本的新特性。
     *         return lookupImpl(pagingInfo);
     *     } catch (Exception e) {
     *         // 处理异常。
     *         throw new ServiceException();
     *     }
     * }
     * </pre>
     * </blockquote>
     *
     * @param pagingInfo 分页信息。
     * @return 修正后的分页信息。
     * @throws PagingException 分页异常。
     * @see EntireLookupService#lookup(PagingInfo)
     * @see EntireLookupService#lookupAsList(PagingInfo)
     * @see PresetLookupService#lookup(String, Object[], PagingInfo)
     * @see PresetLookupService#lookupAsList(String, Object[], PagingInfo)
     */
    public static PagingInfo mayFixPagingInfo(PagingInfo pagingInfo) throws PagingException {
        // 展开参数。
        int page = pagingInfo.getPage();
        int rows = pagingInfo.getRows();

        // 定义修正标记。
        boolean fixedFlag = false;

        // 对 page 进行校验及修正。
        if (page < 0) {
            // 如果使用严格分页，则抛出异常。
            if (USE_STRICT_PAGING) {
                throw new PagingException(pagingInfo);
            }
            // 如果记录分页警告，则记录警告。
            if (LOG_PAGING_WARNING) {
                String message = "subgrade: 分页信息的页数为 " + page + ", 将其修正为 0";
                LOGGER.warn(message);
                message = "subgrade: 分页信息的页数为 " + page + ", 将其修正为 0, 异常信息如下: ";
                LOGGER.debug(message, new PagingException(pagingInfo));
            }
            // 修正 page。
            page = 0;
            fixedFlag = true;
        }

        // 对 rows 进行校验及修正。
        if (rows < 0) {
            // 如果使用严格分页，则抛出异常。
            if (USE_STRICT_PAGING) {
                throw new PagingException(pagingInfo);
            }
            // 如果记录分页警告，则记录警告。
            if (LOG_PAGING_WARNING) {
                String message = "subgrade: 分页信息的每页行数为 " + rows + ", 将其修正为 " + Integer.MAX_VALUE;
                LOGGER.warn(message);
                message = "subgrade: 分页信息的每页行数为 " + rows + ", 将其修正为 " + Integer.MAX_VALUE +
                        ", 异常信息如下: ";
                LOGGER.debug(message, new PagingException(pagingInfo));
            }
            // 修正 rows。
            rows = Integer.MAX_VALUE;
            fixedFlag = true;
        }

        // 根据修正标记，返回修正后的分页信息。
        return fixedFlag ? new PagingInfo(page, rows) : pagingInfo;
    }

    private PagingFixHelper() {
        throw new IllegalStateException("禁止实例化");
    }
}
