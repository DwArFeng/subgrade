package com.dwarfeng.subgrade.stack.service;

import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.ServiceException;

/**
 * 写入服务。
 *
 * <p>
 * 写入服务是用在大量新数据写入场景下的，如数据采集。
 *
 * <p>
 * 该服务只有一个方法 {@link WriteService#write(Entity)} <br>
 * 与 {@link CrudService#insert(Entity)} 相比，该方法应该做出专用优化，以维持高速写入时的效率。<br>
 * {@link CrudService#insert(Entity)} 方法被设计为能够应对各种情况，比如故意插入不合法或者已经存在的数据时会抛出异常；而
 * 该方法为了效率，会对数据进行一些约定，具体的约定规则请看方法的文档。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public interface WriteService<E extends Entity<?>> extends Service {

    /**
     * 写入实体。
     *
     * <p>
     * 为保证写入的效率，该方法默认待写入的实体是合法的，因此不会对实体进行合法性检查。
     *
     * <p>
     * 待写入的实体需要满足如下的约定：
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
     * @param entity 实体。
     * @throws ServiceException 服务异常。
     */
    void write(E entity) throws ServiceException;
}
