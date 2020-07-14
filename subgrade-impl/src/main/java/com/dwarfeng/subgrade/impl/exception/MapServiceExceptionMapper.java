package com.dwarfeng.subgrade.impl.exception;

import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 通过映射实现的服务异常映射器。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 */
public class MapServiceExceptionMapper implements ServiceExceptionMapper {

    private Map<Class<? extends Exception>, ServiceException.Code> destination;
    private ServiceException.Code defaultCode;
    private final Map<Class<? extends Exception>, ServiceException.Code> router = new HashMap<>();

    public MapServiceExceptionMapper(Map<Class<? extends Exception>, ServiceException.Code> destination, ServiceException.Code defaultCode) {
        this.destination = destination;
        this.defaultCode = defaultCode;
    }

    @Override
    public ServiceException map(Exception e) {
        //如果入口参数是 null，则返回 null。
        if (Objects.isNull(e)) {
            return null;
        }
        //如果异常本身属于 ServiceException 则试图获取其错误代码。如果没有错误代码，按照普通异常处理。
        if (e instanceof ServiceException && Objects.nonNull(((ServiceException) e).getCode())) {
            return new ServiceException(((ServiceException) e).getCode());
        }

        //如果 destination 中有关于异常的直接定义，则返回直接定义。
        if (destination.containsKey(e.getClass())) {
            return new ServiceException(destination.get(e.getClass()));
        }

        //如果 destination 中没有关于异常的直接定义，则进行父类寻找，并且使用 router 缓存结果。
        if (router.containsKey(e.getClass())) {
            return new ServiceException(router.get(e.getClass()));
        }
        for (Map.Entry<Class<? extends Exception>, ServiceException.Code> entry : destination.entrySet()) {
            if (entry.getKey().isAssignableFrom(e.getClass())) {
                router.put(e.getClass(), entry.getValue());
                return new ServiceException(entry.getValue());
            }
        }
        // 如果父类寻找也找不到匹配项，则返回默认的异常代码。
        router.put(e.getClass(), defaultCode);
        return new ServiceException(defaultCode);
    }

    public Map<Class<? extends Exception>, ServiceException.Code> getDestination() {
        return destination;
    }

    public void setDestination(Map<Class<? extends Exception>, ServiceException.Code> destination) {
        this.destination = destination;
    }

    public ServiceException.Code getDefaultCode() {
        return defaultCode;
    }

    public void setDefaultCode(ServiceException.Code defaultCode) {
        this.defaultCode = defaultCode;
    }
}
