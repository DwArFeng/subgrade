package com.dwarfeng.subgrade.data.sdk.exception;

import com.dwarfeng.subgrade.base.sdk.i18n.MessageContext;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import com.dwarfeng.subgrade.data.stack.exception.EntityExistedException;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Data 模块异常代码测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class ServiceExceptionCodeSuppliersTest {

    @Test
    public void shouldUseDataRangeAndLocalizedMessages() {
        ServiceException english = MessageContext.call(
                Locale.ENGLISH, () -> new ServiceException(ServiceExceptionCodeSuppliers.CACHE_FAILED.get())
        );
        ServiceException chinese = MessageContext.call(
                Locale.SIMPLIFIED_CHINESE, () -> new ServiceException(ServiceExceptionCodeSuppliers.CACHE_FAILED.get())
        );
        String entityExceptionMessage = MessageContext.call(
                Locale.ENGLISH, () -> new EntityExistedException(42).getMessage()
        );

        assertEquals(1000, ServiceExceptionCodeSuppliers.getExceptionCodeOffset());
        assertEquals(1020, english.getCode().getCode());
        assertEquals("Cache operation failed", english.getCode().getTip());
        assertEquals("缓存操作失败", chinese.getCode().getTip());
        assertEquals("The entity for key 42 already exists", entityExceptionMessage);
    }

    @Test
    public void shouldApplyUpdatedOffsetToNewCodes() {
        int previousOffset = ServiceExceptionCodeSuppliers.getExceptionCodeOffset();
        try {
            ServiceExceptionCodeSuppliers.setExceptionCodeOffset(9000);
            assertEquals(9020, ServiceExceptionCodeSuppliers.CACHE_FAILED.get().getCode());
        } finally {
            ServiceExceptionCodeSuppliers.setExceptionCodeOffset(previousOffset);
        }
    }
}
