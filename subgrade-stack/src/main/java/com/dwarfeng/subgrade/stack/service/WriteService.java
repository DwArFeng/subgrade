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
     * 写入指定的元素。
     * <p>
     * 写入的元素应该遵守如下的规则：
     * <pre>
     * 1. 元素的主键允许为 null。
     * 2. 主键不为 null 的元素必须确保之前不存在。
     * </pre>
     *
     * @param element 指定的元素。
     * @throws ServiceException 服务异常。
     */
    void write(E element) throws ServiceException;
}
