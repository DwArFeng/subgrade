package com.dwarfeng.subgrade.data.impl.service;

import com.dwarfeng.subgrade.basic.stack.bean.entity.Entity;
import com.dwarfeng.subgrade.basic.stack.bean.key.Key;
import com.dwarfeng.subgrade.basic.stack.exception.ServiceExceptionMapper;
import com.dwarfeng.subgrade.basic.stack.generation.KeyGenerator;
import com.dwarfeng.subgrade.basic.stack.log.LogLevel;
import com.dwarfeng.subgrade.data.sdk.service.AbstractWriteService;
import com.dwarfeng.subgrade.data.stack.dao.WriteDao;

import org.jetbrains.annotations.NotNull;
import java.util.Objects;

/**
 * 仅通过数据访问层实现的写入服务。
 *
 * <p>
 * 该类只提供最基本的方法实现，没有添加任何事务，请通过代理的方式在代理类中添加事务。
 *
 * @author DwArFeng
 * @since 1.1.0
 */
public class DaoOnlyWriteService<K extends Key, E extends Entity<K>> extends AbstractWriteService<E> {

    @NotNull
    private WriteDao<E> dao;

    @NotNull
    private KeyGenerator<K> keyGenerator;

    /**
     * 构造器方法。
     *
     * @param sem               服务异常映射器。
     * @param exceptionLogLevel 异常的日志级别。
     * @param dao               数据访问层。
     * @param keyGenerator      主键生成器。
     * @since 1.5.4
     */
    public DaoOnlyWriteService(
            @NotNull ServiceExceptionMapper sem,
            @NotNull LogLevel exceptionLogLevel,
            @NotNull WriteDao<E> dao,
            @NotNull KeyGenerator<K> keyGenerator
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
        this.keyGenerator = keyGenerator;
    }

    /**
     * 构造器方法。
     *
     * <p>
     * 由于在 1.5.4 后，该类的继承关系发生了变化，因此该构造器方法已经被废弃。<br>
     * 请使用 {@link #DaoOnlyWriteService(ServiceExceptionMapper, LogLevel, WriteDao, KeyGenerator)}。<br>
     * 新的构造器调整了参数顺序，使其更符合新的继承形式对应的参数顺序。
     *
     * @param dao               数据访问层。
     * @param keyGenerator      主键生成器。
     * @param sem               服务异常映射器。
     * @param exceptionLogLevel 异常的日志级别。
     * @see #DaoOnlyWriteService(ServiceExceptionMapper, LogLevel, WriteDao, KeyGenerator)
     * @deprecated 使用 {@link #DaoOnlyWriteService(ServiceExceptionMapper, LogLevel, WriteDao, KeyGenerator)} 代替。
     */
    @Deprecated
    public DaoOnlyWriteService(
            @NotNull WriteDao<E> dao,
            @NotNull KeyGenerator<K> keyGenerator,
            @NotNull ServiceExceptionMapper sem,
            @NotNull LogLevel exceptionLogLevel
    ) {
        super(sem, exceptionLogLevel);
        this.dao = dao;
        this.keyGenerator = keyGenerator;
    }

    @Override
    protected void doWrite(E entity) throws Exception {
        if (Objects.isNull(entity.getKey())) {
            entity.setKey(keyGenerator.generate());
        }
        dao.write(entity);
    }

    @NotNull
    public WriteDao<E> getDao() {
        return dao;
    }

    public void setDao(@NotNull WriteDao<E> dao) {
        this.dao = dao;
    }

    @NotNull
    public KeyGenerator<K> getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(@NotNull KeyGenerator<K> keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    @Override
    public String toString() {
        return "DaoOnlyWriteService{" +
                "dao=" + dao +
                ", keyGenerator=" + keyGenerator +
                ", sem=" + sem +
                ", exceptionLogLevel=" + exceptionLogLevel +
                '}';
    }
}
