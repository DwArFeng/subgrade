package com.dwarfeng.subgrade.web.sdk.exception;

import com.dwarfeng.subgrade.base.sdk.i18n.MessageContext;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import com.dwarfeng.subgrade.web.stack.exception.LoginFailedException;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Web 模块异常代码测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class ServiceExceptionCodeSuppliersTest {

    @Test
    public void shouldUseWebRangeAndLocalizedMessages() {
        ServiceException english = MessageContext.call(
                Locale.ENGLISH,
                () -> new ServiceException(ServiceExceptionCodeSuppliers.PERMISSION_DENIED.get())
        );
        ServiceException chinese = MessageContext.call(
                Locale.SIMPLIFIED_CHINESE,
                () -> new ServiceException(ServiceExceptionCodeSuppliers.PERMISSION_DENIED.get())
        );
        String loginFailedExceptionMessage = MessageContext.call(
                Locale.SIMPLIFIED_CHINESE,
                () -> new LoginFailedException("tester").getMessage()
        );

        assertEquals(4080, english.getCode().getCode());
        assertEquals("Permission denied", english.getCode().getTip());
        assertEquals("权限被拒绝", chinese.getCode().getTip());
        assertEquals("身份尚未登录：tester", loginFailedExceptionMessage);
    }

    @Test
    public void shouldApplyUpdatedOffsetToNewCodes() {
        int previousOffset = ServiceExceptionCodeSuppliers.getExceptionCodeOffset();
        try {
            ServiceExceptionCodeSuppliers.setExceptionCodeOffset(9000);
            assertEquals(9090, ServiceExceptionCodeSuppliers.LOGIN_FAILED.get().getCode());
        } finally {
            ServiceExceptionCodeSuppliers.setExceptionCodeOffset(previousOffset);
        }
    }
}
