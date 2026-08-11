package com.dwarfeng.subgrade.lifecycle.sdk.exception;

import com.dwarfeng.subgrade.base.sdk.i18n.MessageContext;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Lifecycle 模块异常代码测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class ServiceExceptionCodeSuppliersTest {

    @Test
    public void shouldUseLifecycleRange() {
        ServiceException english = MessageContext.call(
                Locale.ENGLISH,
                () -> new ServiceException(ServiceExceptionCodeSuppliers.OPERATION_FAILED.get())
        );
        ServiceException chinese = MessageContext.call(
                Locale.SIMPLIFIED_CHINESE,
                () -> new ServiceException(ServiceExceptionCodeSuppliers.OPERATION_FAILED.get())
        );

        assertEquals(5010, english.getCode().getCode());
        assertEquals("Lifecycle operation failed", english.getCode().getTip());
        assertEquals("生命周期操作失败", chinese.getCode().getTip());
    }

    @Test
    public void shouldApplyUpdatedOffsetToNewCodes() {
        int previousOffset = ServiceExceptionCodeSuppliers.getExceptionCodeOffset();
        try {
            ServiceExceptionCodeSuppliers.setExceptionCodeOffset(9000);
            assertEquals(9010, ServiceExceptionCodeSuppliers.OPERATION_FAILED.get().getCode());
        } finally {
            ServiceExceptionCodeSuppliers.setExceptionCodeOffset(previousOffset);
        }
    }
}
