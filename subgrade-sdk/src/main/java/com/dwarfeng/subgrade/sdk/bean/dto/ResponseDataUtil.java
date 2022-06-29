package com.dwarfeng.subgrade.sdk.bean.dto;

import com.dwarfeng.subgrade.stack.bean.dto.ResponseData;
import com.dwarfeng.subgrade.stack.bean.dto.ResponseData.Meta;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;

/**
 * 响应数据工具类。
 *
 * @author DwArFeng
 * @since 0.1.3-beta
 */
public final class ResponseDataUtil {

    private ResponseDataUtil() {
        throw new IllegalStateException("禁止外部实例化。");
    }

    /**
     * 生成一个含有指定数据且元数据为good的响应数据。
     *
     * @param data 指定的数据。
     * @param <T>  数据的类型。
     * @return 含有指定的数据且元数据为good的响应数据。
     */
    public static <T> ResponseData<T> good(T data) {
        return new ResponseData<>(data, new Meta(0, "good"));
    }

    /**
     * 生成一个指定数据类型且元数据对应服务异常的响应数据。
     *
     * @param clazz 响应数据的泛型类。
     * @param e     元数据对应的服务异常。
     * @param <T>   泛型的类。
     * @return 指定数据类型且元数据对应服务异常的响应数据。
     * @see #bad(ServiceException)
     * @deprecated 有多余的入口参数，已被 {@link #bad(ServiceException)} 代替。
     */
    public static <T> ResponseData<T> bad(
            @SuppressWarnings("unused") Class<? super T> clazz, ServiceException e
    ) {
        return bad(e);
    }

    /**
     * 生成一个指定数据类型且元数据对应任何异常的响应数据。
     *
     * @param clazz 响应数据的泛型类。
     * @param e     元数据对应的任何异常。
     * @param sem   服务异常映射器。
     * @param <T>   泛型的类。
     * @return 指定数据类型且元数据对应服务异常的响应数据。
     * @see #bad(Exception, ServiceExceptionMapper)
     * @deprecated 有多余的入口参数，已被 {@link #bad(Exception, ServiceExceptionMapper)} 代替。
     */
    public static <T> ResponseData<T> bad(
            @SuppressWarnings("unused") Class<? super T> clazz, Exception e, ServiceExceptionMapper sem
    ) {
        return bad(sem.map(e));
    }

    /**
     * 生成一个指定数据类型且元数据对应服务异常的响应数据。
     *
     * @param e   元数据对应的服务异常。
     * @param <T> 泛型的类。
     * @return 指定数据类型且元数据对应服务异常的响应数据。
     * @since 1.2.8
     */
    public static <T> ResponseData<T> bad(ServiceException e) {
        return new ResponseData<>(null, new Meta(e.getCode().getCode(), e.getCode().getTip()));
    }

    /**
     * 生成一个指定数据类型且元数据对应任何异常的响应数据。
     *
     * @param e   元数据对应的任何异常。
     * @param sem 服务异常映射器。
     * @param <T> 泛型的类。
     * @return 指定数据类型且元数据对应服务异常的响应数据。
     * @since 1.2.8
     */
    public static <T> ResponseData<T> bad(Exception e, ServiceExceptionMapper sem) {
        return bad(sem.map(e));
    }
}
