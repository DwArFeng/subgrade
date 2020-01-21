package com.dwarfeng.subgrade.stack.exception;

/**
 * 服务异常映射器。
 * <p> 将一般的异常映射为服务异常。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public interface ServiceExceptionMapper {

    /**
     * 将指定的异常映射为服务异常。
     *
     * @param e 指定的异常。
     * @return 服务异常。
     */
    ServiceException map(Exception e);
}
