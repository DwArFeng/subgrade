package com.dwarfeng.subgrade.impl.dao;

import com.dwarfeng.subgrade.sdk.memory.io.MapResourceBridge;
import com.dwarfeng.subgrade.stack.bean.dto.PagingInfo;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.bean.key.Key;
import com.dwarfeng.subgrade.stack.dao.EntireLookupDao;
import com.dwarfeng.subgrade.stack.exception.DaoException;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * 通过内存实现的 EntireLookupDao。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加同步锁或其它的安全性一致性保证，请通过代理的方式在代理类中添加。
 *
 * <p>
 * 该类的查询效率较低，不应该应用在大数据量的场景中。
 *
 * <p>
 * 该类可以通过 MapResourceBridge 对内存映射进行外部资源桥接。
 * 在数据访问层启动前读取资源数据，在结束之后保存数据，即可实现内存数据的持久化。<br>
 * 如果该数据访问层单独使用，可以利用 {@link #fillData(MapResourceBridge)} 初始化内存。<br>
 * 如果该数据访问层和其它数据访问层组合使用，则需要和其它数据访问层共享 memory，
 * 此时宜在外部持有 memory 的引用，并在外部初始化。
 *
 * @author DwArFeng
 * @see MapResourceBridge
 * @since 0.0.3-beta
 */
public class MemoryEntireLookupDao<K extends Key, E extends Entity<K>> implements EntireLookupDao<E> {

    private Map<K, E> memory;

    public MemoryEntireLookupDao() {
        this(new LinkedHashMap<>());
    }

    public MemoryEntireLookupDao(@Nonnull Map<K, E> memory) {
        this.memory = memory;
    }

    @Override
    public List<E> lookup() {
        return new ArrayList<>(memory.values());
    }

    @Override
    public List<E> lookup(PagingInfo pagingInfo) {
        // 展开参数。
        int page = pagingInfo.getPage();
        int rows = pagingInfo.getRows();
        // 每页行数大于 0 时，按照正常的逻辑查询数据。
        if (rows > 0) {
            return lookupWithPositiveRows(page, rows);
        }
        // 否则返回空列表。
        else {
            return Collections.emptyList();
        }
    }

    @SuppressWarnings("DuplicatedCode")
    @Nonnull
    private List<E> lookupWithPositiveRows(int page, int rows) {
        int beginIndex = page * rows;
        int endIndex = beginIndex + rows;
        List<E> list = new ArrayList<>(memory.values());

        // 修正 beginIndex 和 endIndex，使得 list.subList 方法不抛出 IndexOutOfBoundsException 异常。
        int size = list.size();
        beginIndex = Math.max(beginIndex, 0);
        beginIndex = Math.min(beginIndex, size);
        endIndex = Math.max(endIndex, beginIndex);
        endIndex = Math.min(endIndex, size);

        return list.subList(beginIndex, endIndex);
    }

    @Override
    public int lookupCount() {
        return memory.size();
    }

    /**
     * 填充数据。
     *
     * @param mrb 映射资源桥。
     * @throws DaoException 数据访问层异常。
     */
    public void fillData(@Nonnull MapResourceBridge<K, E> mrb) throws DaoException {
        try {
            mrb.fillMap(this.memory);
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

    public Map<K, E> getMemory() {
        return memory;
    }

    public void setMemory(@Nonnull Map<K, E> memory) {
        this.memory = memory;
    }
}
