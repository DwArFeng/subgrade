package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.memory.filter.PresetEntityFilter;
import com.dwarfeng.subgrade.sdk.memory.io.MapResourceBridge;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.PresetLookupDao;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通过内存实现的 MemoryPresetLookupDao。
 * <p>该类只提供最基本的方法实现，没有添加同步锁或其它的安全性一致性保证，请通过代理的方式在代理类中添加。</p>
 * <p>该类的查询效率较低，不应该应用在大数据量的场景中。</p>
 * <p>该类可以通过 MapResourceBridge 对内存映射进行外部资源桥接。
 * 在数据访问层启动前读取资源数据，在结束之后保存数据，即可实现内存数据的持久化。</p>
 *
 * @author DwArFeng
 * @see MapResourceBridge
 * @since 0.0.3-beta
 */
public class MemoryPresetLookupDao<K extends Key, E extends Entity<K>> implements PresetLookupDao<E> {

    private Map<K, E> memory;
    private PresetEntityFilter<E> filter;

    public MemoryPresetLookupDao(
            @NonNull Map<K, E> memory,
            @NonNull PresetEntityFilter<E> filter) {
        this.memory = memory;
        this.filter = filter;
    }

    @Override
    public List<E> lookup(String preset, Object[] objs) {
        return memory.values().stream().filter(e -> filter.accept(e, preset, objs)).collect(Collectors.toList());
    }

    @Override
    public List<E> lookup(String preset, Object[] objs, PagingInfo pagingInfo) {
        int beginIndex = pagingInfo.getPage() * pagingInfo.getRows();
        int endIndex = beginIndex + pagingInfo.getRows() - 1;
        List<E> collect = memory.values().stream().filter(e -> filter.accept(e, preset, objs)).collect(Collectors.toList());
        return collect.subList(beginIndex, endIndex);
    }

    @Override
    public int lookupCount(String preset, Object[] objs) {
        return (int) memory.values().stream().filter(e -> filter.accept(e, preset, objs)).count();
    }

    public Map<K, E> getMemory() {
        return memory;
    }

    public void setMemory(@NonNull Map<K, E> memory) {
        this.memory = memory;
    }

    public PresetEntityFilter<E> getFilter() {
        return filter;
    }

    public void setFilter(@NonNull PresetEntityFilter<E> filter) {
        this.filter = filter;
    }
}
