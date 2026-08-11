package com.dwarfeng.subgrade.expression.sdk.exception;

import com.dwarfeng.subgrade.base.sdk.i18n.MessageContext;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceException;
import com.dwarfeng.subgrade.expression.stack.exception.ExpressionParseException;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Expression 模块异常代码测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class ServiceExceptionCodeSuppliersTest {

    @Test
    public void shouldUseExpressionRangeAndLocalizedMessages() {
        ServiceException english = MessageContext.call(
                Locale.ENGLISH, () -> new ServiceException(ServiceExceptionCodeSuppliers.PARSE_FAILED.get())
        );
        ServiceException chinese = MessageContext.call(
                Locale.SIMPLIFIED_CHINESE, () -> new ServiceException(ServiceExceptionCodeSuppliers.PARSE_FAILED.get())
        );
        String parseExceptionMessage = MessageContext.call(
                Locale.SIMPLIFIED_CHINESE, () -> new ExpressionParseException("#{name}").getMessage()
        );

        assertEquals(2010, english.getCode().getCode());
        assertEquals("Expression parsing failed", english.getCode().getTip());
        assertEquals("表达式解析失败", chinese.getCode().getTip());
        assertEquals("无法解析表达式：#{name}", parseExceptionMessage);
    }

    @Test
    public void shouldApplyUpdatedOffsetToNewCodes() {
        int previousOffset = ServiceExceptionCodeSuppliers.getExceptionCodeOffset();
        try {
            ServiceExceptionCodeSuppliers.setExceptionCodeOffset(9000);
            assertEquals(9010, ServiceExceptionCodeSuppliers.PARSE_FAILED.get().getCode());
        } finally {
            ServiceExceptionCodeSuppliers.setExceptionCodeOffset(previousOffset);
        }
    }
}
