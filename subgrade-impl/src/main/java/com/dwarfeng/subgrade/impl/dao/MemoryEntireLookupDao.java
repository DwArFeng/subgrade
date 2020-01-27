package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.EntireLookupDao;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 通过内存实现的 EntireLookupDao。
 * <p>该类只提供最基本的方法实现，没有添加同步锁或其它的安全性一致性保证，请通过代理的方式在代理类中添加。</p>
 * <p>该类的查询效率较低，不应该应用在大数据量的场景中。</p>
 * <p>该类可以通过 MapResourceBridge 对内存映射进行外部资源桥接。
 * 在数据访问层启动前读取资源数据，在结束之后保存数据，即可实现内存数据的持久化。</p>
 *
 * @author DwArFeng
 * @see com.dwarfeng.subgrade.sdk.io.MapResourceBridge
 * @since 0.0.3-beta
 */
public class MemoryEntireLookupDao<K extends Key, E extends Entity<K>> implements EntireLookupDao<E> {

    private Map<K, E> memory;

    public MemoryEntireLookupDao(@NonNull Map<K, E> memory) {
        this.memory = memory;
    }

    @Override
    public List<E> lookup() {
        return new ArrayList<>(memory.values());
    }

    @Override
    public List<E> lookup(PagingInfo pagingInfo) {
        int beginIndex = pagingInfo.getPage() * pagingInfo.getRows();
        int endIndex = beginIndex + pagingInfo.getRows() - 1;
        return new ArrayList<>(memory.values()).subList(beginIndex, endIndex);
    }

    @Override
    public int lookupCount() {
        return memory.size();
    }

    public Map<K, E> getMemory() {
        return memory;
    }

    public void setMemory(@NonNull Map<K, E> memory) {
        this.memory = memory;
    }
}
