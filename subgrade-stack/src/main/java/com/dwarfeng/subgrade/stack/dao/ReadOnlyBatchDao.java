package com.dwarfeng.subgrade.stack.dao;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;

/**
 * 只读批量数据访问层。
 *
 * @author DwArFeng
 * @since 0.0.1-alpha
 * @deprecated 该接口由于命名规范性问题已经被 {@link BatchReadOnlyDao} 替代。
 */
@Deprecated
public interface ReadOnlyBatchDao<K extends Key, E extends Entity<K>> extends BatchReadOnlyDao<K, E> {
}
