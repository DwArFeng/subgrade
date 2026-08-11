package com.dwarfeng.subgrade.basic.stack.exception;

import com.dwarfeng.subgrade.base.sdk.i18n.MessageContext;
import com.dwarfeng.subgrade.base.sdk.i18n.Messages;
import com.dwarfeng.subgrade.base.stack.i18n.MessageCatalog;
import com.dwarfeng.subgrade.basic.sdk.exception.ServiceExceptionCodeSuppliers;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 服务异常国际化测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class ServiceExceptionI18nTest {

    private static final MessageCatalog TEST_CATALOG = MessageCatalog.of(
            ServiceExceptionI18nTest.class, "com.dwarfeng.subgrade.basic.stack.i18n.fallback"
    );

    @Test
    public void shouldCreateCodeUsingScopedLocale() {
        ServiceException english = MessageContext.call(
                Locale.ENGLISH, () -> new ServiceException(ServiceExceptionCodeSuppliers.PAGING_FAILED.get())
        );
        ServiceException chinese = MessageContext.call(
                Locale.SIMPLIFIED_CHINESE, () -> new ServiceException(ServiceExceptionCodeSuppliers.PAGING_FAILED.get())
        );
        String englishMessage = MessageContext.call(Locale.ENGLISH, english::getMessage);
        String chineseMessage = MessageContext.call(Locale.SIMPLIFIED_CHINESE, chinese::getMessage);

        assertEquals(130, english.getCode().getCode());
        assertEquals("Paging operation failed", english.getCode().getTip());
        assertEquals("Exception code=130 - Paging operation failed", englishMessage);
        assertEquals("分页操作失败", chinese.getCode().getTip());
        assertEquals("异常代码=130 - 分页操作失败", chineseMessage);
    }

    @Test
    public void shouldFallbackFormatAndDegradeMissingKeys() {
        String fallback = MessageContext.call(
                Locale.FRANCE, () -> Messages.resolve(TEST_CATALOG, "greeting", "Ada")
        );
        String missing = MessageContext.call(
                Locale.ENGLISH, () -> Messages.resolve(TEST_CATALOG, "missing")
        );

        assertEquals("Hello, Ada!", fallback);
        assertEquals("!missing!", missing);
    }

    @Test
    public void shouldApplyUpdatedOffsetToNewCodes() {
        int previousOffset = ServiceExceptionCodeSuppliers.getExceptionCodeOffset();
        try {
            ServiceExceptionCodeSuppliers.setExceptionCodeOffset(9000);
            assertEquals(9130, ServiceExceptionCodeSuppliers.PAGING_FAILED.get().getCode());
        } finally {
            ServiceExceptionCodeSuppliers.setExceptionCodeOffset(previousOffset);
        }
    }
}
