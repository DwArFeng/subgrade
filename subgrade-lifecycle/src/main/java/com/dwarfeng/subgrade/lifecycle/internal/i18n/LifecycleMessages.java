package com.dwarfeng.subgrade.lifecycle.internal.i18n;

import com.dwarfeng.subgrade.base.sdk.i18n.Messages;
import com.dwarfeng.subgrade.base.stack.i18n.MessageCatalog;

import java.util.Locale;

/**
 * 模块私有消息入口。
 *
 * <p>
 * 该工具隐藏资源路径和 Subgrade 内部国际化协议，不属于公共 API。
 *
 * @author DwArFeng
 * @since 2.0.0
 */
public final class LifecycleMessages {

    /**
     * 使用当前语言环境解析消息。
     *
     * @param key  消息键。
     * @param args 格式化参数。
     * @return 解析后的消息。
     */
    public static String message(LifecycleMessageKey key, Object... args) {
        return Messages.resolve(key.catalog().messageCatalog(), key.key(), args);
    }

    /**
     * 使用指定语言环境解析消息。
     *
     * @param locale 语言环境。
     * @param key    消息键。
     * @param args   格式化参数。
     * @return 解析后的消息。
     */
    public static String message(Locale locale, LifecycleMessageKey key, Object... args) {
        return Messages.resolve(key.catalog().messageCatalog(), key.key(), locale, args);
    }

    private LifecycleMessages() {
        throw new AssertionError("No instances");
    }

    /**
     * Lifecycle 模块消息目录。
     */
    enum Catalog {

        SDK("com.dwarfeng.subgrade.lifecycle.sdk.i18n.messages"),
        IMPL("com.dwarfeng.subgrade.lifecycle.impl.i18n.messages");

        private final MessageCatalog messageCatalog;

        Catalog(String baseName) {
            this.messageCatalog = MessageCatalog.of(LifecycleMessages.class, baseName);
        }

        MessageCatalog messageCatalog() {
            return messageCatalog;
        }
    }
}
