package com.dwarfeng.subgrade.basic.stack.exception;

import com.dwarfeng.subgrade.base.sdk.i18n.MessageContext;
import com.dwarfeng.subgrade.basic.sdk.exception.ServiceExceptionCodeSuppliers;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 服务异常测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class ServiceExceptionTest {

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
    public void shouldPreserveCodeAndMessageAfterSerialization() throws IOException, ClassNotFoundException {
        ServiceException origin = new ServiceException(new ServiceException.Code(42, "serialization tip"));

        ServiceException restored;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(origin);
            objectOutputStream.flush();

            try (ObjectInputStream objectInputStream = new ObjectInputStream(
                    new ByteArrayInputStream(outputStream.toByteArray())
            )) {
                restored = (ServiceException) objectInputStream.readObject();
            }
        }

        assertEquals(42, restored.getCode().getCode());
        assertEquals("serialization tip", restored.getCode().getTip());
        assertEquals(
                "Exception code=42 - serialization tip",
                MessageContext.call(Locale.ENGLISH, restored::getMessage)
        );
    }
}
