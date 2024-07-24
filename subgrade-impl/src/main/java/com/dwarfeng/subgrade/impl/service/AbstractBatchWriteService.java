package com.dwarfeng.subgrade.impl.service;

import com.dwarfeng.subgrade.sdk.exception.ServiceExceptionHelper;
import com.dwarfeng.subgrade.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.stack.exception.ServiceException;
import com.dwarfeng.subgrade.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.stack.log.LogLevel;
import com.dwarfeng.subgrade.stack.service.BatchWriteService;
import com.dwarfeng.subgrade.stack.service.EntireLookupService;
import com.dwarfeng.subgrade.stack.service.PresetLookupService;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * 批量写入服务的抽象实现。
 *
 * @author DwArFeng
 * @since 1.5.4
 */
public abstract class AbstractBatchWriteService<E extends Entity<?>> implements BatchWriteService<E> {

    @Nonnull
    protected ServiceExceptionMapper sem;

    @Nonnull
    protected LogLevel exceptionLogLevel;

    public AbstractBatchWriteService(@Nonnull ServiceExceptionMapper sem, @Nonnull LogLevel exceptionLogLevel) {
        this.sem = sem;
        this.exceptionLogLevel = exceptionLogLevel;
    }

    @Override
    public void write(E entity) throws ServiceException {
        try {
            doWrite(entity);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("写入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

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
     * @throws Exception 方法执行时发生的任何异常。
     * @see #write(Entity)
     */
    protected abstract void doWrite(E entity) throws Exception;

    @Override
    public void batchWrite(List<E> entities) throws ServiceException {
        try {
            doBatchWrite(entities);
        } catch (Exception e) {
            throw ServiceExceptionHelper.logParse("批量写入实体时发生异常", exceptionLogLevel, e, sem);
        }
    }

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
     * @throws Exception 方法执行时发生的任何异常。
     * @see #write(Entity)
     */
    protected abstract void doBatchWrite(List<E> entities) throws Exception;

    @Nonnull
    public ServiceExceptionMapper getSem() {
        return sem;
    }

    public void setSem(@Nonnull ServiceExceptionMapper sem) {
        this.sem = sem;
    }

    @Nonnull
    public LogLevel getExceptionLogLevel() {
        return exceptionLogLevel;
    }

    public void setExceptionLogLevel(@Nonnull LogLevel exceptionLogLevel) {
        this.exceptionLogLevel = exceptionLogLevel;
    }

    @Override
    public String toString() {
        return "AbstractBatchWriteService{" +
                "sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
