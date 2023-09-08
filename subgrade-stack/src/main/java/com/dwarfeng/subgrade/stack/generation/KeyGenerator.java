package com.dwarfeng.subgrade.stack.generation;

import com.dwarfeng.subgrade.stack.bean.key.Key;

/**
 * 主键生成器。
 *
 * <p>
 * 主键抓取器用于在无主键实体插入时向其分配一个主键。
 *
 * <p>
 * 主键生成器要求生成的主键要么为 <code>null</code>，要么为全局唯一的对象。<br>
 * 当生成的主键为 <code>null</code> 时，表示使用数据访问层的主键生成机制。
 *
 * @author DwArFeng
 * @since 1.4.5
 */
public interface KeyGenerator<K extends Key> extends Generator<K> {
}
