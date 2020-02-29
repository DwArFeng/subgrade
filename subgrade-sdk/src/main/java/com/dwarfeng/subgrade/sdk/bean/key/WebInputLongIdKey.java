package com.dwarfeng.subgrade.sdk.bean.key;

import com.alibaba.fastjson.annotation.JSONField;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.bean.key.LongIdKey;

/**
 * 带有验证注解的网络输入型的 LongIdKey。
 *
 * @author DwArFeng
 * @since 0.3.0-beta
 */
public class WebInputLongIdKey implements Key {

    private static final long serialVersionUID = 9039742259922391556L;

    @JSONField(name = "long_id", ordinal = 1)
    private long longId;

    public WebInputLongIdKey() {
    }

    public WebInputLongIdKey(long id) {
        this.longId = id;
    }

    public long getLongId() {
        return longId;
    }

    public void setLongId(long longId) {
        this.longId = longId;
    }

    public LongIdKey toStackBean() {
        return new LongIdKey(longId);
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
        return (int) (longId ^ (longId >>> 32));
    }

    @Override
    public String toString() {
        return "WebInputLongIdKey{" +
                "longId=" + longId +
                '}';
    }
}
