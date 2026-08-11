package com.dwarfeng.subgrade.basic.stack.exception;

import com.dwarfeng.subgrade.base.sdk.i18n.MessageContext;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * 服务异常序列化测试。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public class ServiceExceptionSerializationTest {

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
