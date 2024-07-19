package com.dwarfeng.subgrade.stack.service;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.ServiceException;

import java.util.List;

/**
 * 批量写入服务。
 *
 * <p>
 * 写入服务 {@link WriteService} 的批量版本。
 *
 * @author DwArFeng
 * @see WriteService
 * @since 1.1.0
 */
public interface BatchWriteService<E extends Entity<?>> extends WriteService<E> {

    /**
     * 批量写入实体。
     *
     * <p>
     * 为保证写入的效率，该方法默认待写入的实体是合法的，因此不会对实体进行合法性检查。
     *
     * <p>
     * 对于批量操作的每一个实体，需要满足如下的约定：
     * <ul>
     *   <li>实体主键允许为 <code>null</code>。</li>
     *   <li>如果实体的主键不为 <code>null</code>，则实体必须确保之前不存在。</li>
     * </ul>
     *
     * <p>
     * 如果传入的实体不合法，该方法可能会抛出异常，其具体行为由实现决定。
     *
     * <p>
     * 需要注意的是，该方法写入数据后，不会返回数据的主键，即使数据的主键是 <code>null</code>，或者是在写入时被改变。<br>
     * 因此，在项目开发中，需要使用其它的服务，确保写入的实体是可以被查询到的，例如可以使用：
     * <ul>
     *   <li>{@link EntireLookupService}</li>
     *   <li>{@link PresetLookupService}</li>
     * </ul>
     *
     * @param entities 实体组成的列表
     * @throws ServiceException 服务异常。
     * @see WriteService#write(Entity)
     */
    void batchWrite(List<E> entities) throws ServiceException;
}
