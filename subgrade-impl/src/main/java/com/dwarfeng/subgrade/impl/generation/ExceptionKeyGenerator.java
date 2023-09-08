package com.dwarfeng.subgrade.impl.generation;

import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.exception.GenerateException;
import com.dwarfeng.subgrade.stack.generation.KeyGenerator;

/**
 * 异常主键生成器。
 *
 * <p>
 * 该生成器无法返回主键，每次调用都抛出异常。用于在不允许抓取主键的场景下做占位生成器。
 *
 * @author DwArFeng
 * @since 1.4.5
 */
public class ExceptionKeyGenerator<K extends Key> implements KeyGenerator<K> {

    @Override
    public K generate() throws GenerateException {
        throw new GenerateException();
    }

    @Override
    public String toString() {
        return "ExceptionKeyGenerator{}";
    }
}
