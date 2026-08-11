package com.dwarfeng.subgrade.aop.sdk.exception;

import com.dwarfeng.subgrade.base.sdk.i18n.MessageContext;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * AOP 模块异常代码测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class ServiceExceptionCodeSuppliersTest {

    @Test
    public void shouldUseAopRange() {
        ServiceException english = MessageContext.call(
                Locale.ENGLISH,
                () -> new ServiceException(ServiceExceptionCodeSuppliers.INTERCEPTION_FAILED.get())
        );
        ServiceException chinese = MessageContext.call(
                Locale.SIMPLIFIED_CHINESE,
                () -> new ServiceException(ServiceExceptionCodeSuppliers.INTERCEPTION_FAILED.get())
        );

        assertEquals(3010, english.getCode().getCode());
        assertEquals("Interception processing failed", english.getCode().getTip());
        assertEquals("拦截处理失败", chinese.getCode().getTip());
    }

    @Test
    public void shouldApplyUpdatedOffsetToNewCodes() {
        int previousOffset = ServiceExceptionCodeSuppliers.getExceptionCodeOffset();
        try {
            ServiceExceptionCodeSuppliers.setExceptionCodeOffset(9000);
            assertEquals(9010, ServiceExceptionCodeSuppliers.INTERCEPTION_FAILED.get().getCode());
        } finally {
            ServiceExceptionCodeSuppliers.setExceptionCodeOffset(previousOffset);
        }
    }
}
