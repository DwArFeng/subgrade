package com.dwarfeng.subgrade.web.sdk.bean.key;

import com.alibaba.fastjson2.annotation.JSONField;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;
import com.dwarfeng.subgrade.basic.stack.bean.key.LongIdKey;

import java.io.Serial;
import java.util.Objects;

/**
 * 带有验证注解的网络输入型的 LongIdKey。
 *
 * @author DwArFeng
 * @since 0.3.0-beta
 */
public class WebInputLongIdKey implements Key {

    @Serial
    private static final long serialVersionUID = -3356098743236106737L;
    @JSONField(name = "long_id")
    private long longId;

    public WebInputLongIdKey() {
    }

    public WebInputLongIdKey(long id) {
        this.longId = id;
    }

    /**
     * WebInputLongIdKey 转 LongIdKey。
     *
     * @param webInputLongIdKey WebInputLongIdKey。
     * @return LongIdKey。
     */
    public static LongIdKey toStackBean(WebInputLongIdKey webInputLongIdKey) {
        if (Objects.isNull(webInputLongIdKey)) {
            return null;
        }
        return new LongIdKey(webInputLongIdKey.getLongId());
    }

    public long getLongId() {
        return longId;
    }

    public void setLongId(long longId) {
        this.longId = longId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebInputLongIdKey that = (WebInputLongIdKey) o;

        return longId == that.longId;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(longId);
    }

    @Override
    public String toString() {
        return "WebInputLongIdKey{" +
                "longId=" + longId +
                '}';
    }
}
