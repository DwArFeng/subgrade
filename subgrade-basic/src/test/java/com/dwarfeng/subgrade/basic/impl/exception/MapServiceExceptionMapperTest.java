package com.dwarfeng.subgrade.basic.impl.exception;

import com.dwarfeng.subgrade.basic.sdk.exception.ServiceExceptionCodeSuppliers;
import com.dwarfeng.subgrade.basic.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 映射服务异常映射器测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class MapServiceExceptionMapperTest {

    @Test
    public void shouldResolveSuppliersForEachMapping() {
        AtomicInteger parentSequence = new AtomicInteger();
        AtomicInteger directSequence = new AtomicInteger();
        AtomicInteger defaultSequence = new AtomicInteger();
        Supplier<ServiceException.Code> parentSupplier =
                () -> new ServiceException.Code(parentSequence.incrementAndGet(), "parent");
        Supplier<ServiceException.Code> directSupplier =
                () -> new ServiceException.Code(directSequence.incrementAndGet(), "direct");
        Supplier<ServiceException.Code> defaultSupplier =
                () -> new ServiceException.Code(defaultSequence.incrementAndGet(), "default");
        Map<Class<? extends Exception>, Supplier<ServiceException.Code>> destination = new HashMap<>();
        destination.put(IOException.class, parentSupplier);
        destination.put(IllegalStateException.class, directSupplier);
        MapServiceExceptionMapper mapper = new MapServiceExceptionMapper(destination, defaultSupplier);

        ServiceException firstParent = mapper.map(new FileNotFoundException());
        ServiceException secondParent = mapper.map(new FileNotFoundException());
        ServiceException direct = mapper.map(new IllegalStateException());
        ServiceException firstDefault = mapper.map(new IllegalArgumentException());
        ServiceException secondDefault = mapper.map(new IllegalArgumentException());

        assertEquals(1, firstParent.getCode().getCode());
        assertEquals(2, secondParent.getCode().getCode());
        assertEquals(1, direct.getCode().getCode());
        assertEquals(1, firstDefault.getCode().getCode());
        assertEquals(2, secondDefault.getCode().getCode());
    }

    @Test
    public void shouldUseDefaultDestinationSuppliers() {
        Map<Class<? extends Exception>, Supplier<ServiceException.Code>> destination =
                ServiceExceptionHelper.putDefaultDestination(null);
        MapServiceExceptionMapper mapper = new MapServiceExceptionMapper(
                destination, ServiceExceptionCodeSuppliers.UNDEFINED
        );

        ServiceException mapped = mapper.map(new IOException());

        assertEquals(50, mapped.getCode().getCode());
    }
}
